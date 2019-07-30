/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.clubOrga.controller.sepa;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFactory;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PDateFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Sepa {

    private XMLStreamWriter writer;
    private OutputStreamWriter out = null;
    private final File sepaFile;
    private final Date executeDate;
    private static final String KODIERUNG_UTF = "UTF-8";
    private final List<FeeData> feeDataList;

    private final ClubConfig clubConfig;
    private final FinanceAccountData financeAccountData;

    /**
     * @param feeDataList
     * @param sepaFile
     * @param executeDate
     */
    public Sepa(ClubConfig clubConfig, FinanceAccountData financeAccountData, List<FeeData> feeDataList,
                Path sepaFile, String executeDate) {
        this.clubConfig = clubConfig;
        this.financeAccountData = financeAccountData;
        this.feeDataList = feeDataList;
        this.sepaFile = sepaFile.toFile();
        this.executeDate = new PDate(executeDate);
    }

    public boolean write() {
        boolean ret;
        try {
            startXml();
            write_GrpHdr();
            write_PmtInf();

            // Buchungen
            for (FeeData beitrag : feeDataList) {
                MemberData datenMitglied = beitrag.getMemberData();
                write_DrctDbtTxInf(beitrag, datenMitglied);
            }
            newLine();
            write_PmtInf_End();
            endXml();
            ret = true;
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            ret = false;
        }
        return ret;
    }

    public String getSum() {
        // Die Gesamtsumme aller einzelnen Eurobeträge, die in der Nachricht enthalten sind, wird vom System angegeben.
        String sum = FeeFactory.getSumFee(feeDataList) + "";
        sum = normBetrag(sum);
        return sum;
    }

    public String normBetrag(String betrag) {
        if (betrag.length() > 2) {
            betrag = betrag.substring(0, betrag.length() - 2) + "." + betrag.substring(betrag.length() - 2);
        } else if (betrag.length() > 1) {
            betrag = "0." + betrag;
        } else {
            betrag = "0.0" + betrag;
        }
        return betrag;
    }

    private void startXml() throws IOException, XMLStreamException {
        out = new OutputStreamWriter(new FileOutputStream(sepaFile), KODIERUNG_UTF);

        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        writer = outFactory.createXMLStreamWriter(out);
        writer.writeStartDocument(KODIERUNG_UTF, "1.0");
        newLine();

        writer.writeStartElement("Document");
        writer.writeAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pain.008.002.02");
        writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeAttribute("xsi:schemaLocation", "urn:iso:std:iso:20022:tech:xsd:pain.008.002.02 pain.008.002.02.xsd");
        newLine();

        writer.writeStartElement("CstmrDrctDbtInitn");
        newLine();
    }

    private void write_GrpHdr() throws XMLStreamException {
        // <GrpHdr>
        //   <MsgId>DTCV201501071454321</MsgId>
        //   <CreDtTm>2015-01-07T13:53:18Z</CreDtTm>
        //   <NbOfTxs>1</NbOfTxs>
        //   <CtrlSum>25.00</CtrlSum>
        //   <InitgPty>
        //     <Nm>KJG PROJECT E.V.</Nm>
        //   </InitgPty>
        // </GrpHdr>
        newLine();
        newLine();

        startElement("GrpHdr", true);
        element("MsgId", getMsgId());
        tab();
        element("CreDtTm", genDate());
        tab();
        element("NbOfTxs", getCount());
        tab();
        element("CtrlSum", getSum());
        tab();
        element("InitgPty", "Nm", clubConfig.clubData.getName());

        endElement(true);
    }

    private void write_PmtInf() throws XMLStreamException {
        // <PmtInf>
        //   <PmtInfId>DTCV201501071454321</PmtInfId>
        //     <PmtMtd>DD</PmtMtd>
        //     <NbOfTxs>1</NbOfTxs>
        //     <CtrlSum>25.00</CtrlSum>
        //     <PmtTpInf>
        //       <SvcLvl>
        //         <Cd>SEPA</Cd>
        //       </SvcLvl>
        //       <LclInstrm>
        //         <Cd>CORE</Cd>
        //       </LclInstrm>
        //       <SeqTp>OOFF</SeqTp>
        //     </PmtTpInf>
        //     <ReqdColltnDt>2015-01-14</ReqdColltnDt>
        //     <Cdtr>
        //       <Nm>KJG PROJECT E.V.</Nm>
        //     </Cdtr>
        //     <CdtrAcct>
        //       <Id>
        //         <IBAN>DE85752500000200443158</IBAN>
        //       </Id>
        //     </CdtrAcct>
        //     <CdtrAgt>
        //       <FinInstnId>
        //         <BIC>BYLADEM1ABG</BIC>
        //       </FinInstnId>
        //     </CdtrAgt>
        //     <ChrgBr>SLEV</ChrgBr>

        newLine();
        newLine();
        startElement("PmtInf", true);

        tab();
        element("PmtInfId", getMsgId());
        tab();
        element("PmtMtd", "DD");
        tab();
        element("NbOfTxs", getCount());
        tab();
        element("CtrlSum", getSum());
        newLine();

        tab();
        startElement("PmtTpInf", true);
        tab(2);
        element("SvcLvl", "Cd", "SEPA");
        tab(2);
        element("LclInstrm", "Cd", "CORE");
        tab(2);
        element("SeqTp", "OOFF");
        tab();
        endElement(true);
        newLine();

        tab();
        element("ReqdColltnDt", genExDate());
        tab();
        element("Cdtr", "Nm", clubConfig.clubData.getName());
        tab();
        element("CdtrAcct", "Id", "IBAN", financeAccountData.getIban());
        tab();
        element("CdtrAgt", "FinInstnId", "BIC", financeAccountData.getBic());
        tab();
        element("ChrgBr", "SLEV");

        newLine();
    }

    private void write_PmtInf_End() throws XMLStreamException {
        endElement(true);
        newLine();
    }

    private void write_DrctDbtTxInf(FeeData beitrag, MemberData mitglied) throws XMLStreamException {
        // <DrctDbtTxInf>
        //   <PmtId>
        //     <EndToEndId>NOTPROVIDED</EndToEndId>
        //   </PmtId>
        //   <InstdAmt Ccy="EUR">30.00</InstdAmt>
        //   <DrctDbtTx>
        //     <MndtRltdInf>
        //       <MndtId>12345</MndtId>
        //       <DtOfSgntr>2015-01-07</DtOfSgntr>
        //       <AmdmntInd>false</AmdmntInd>
        //     </MndtRltdInf>
        //     <CdtrSchmeId>
        //       <Id>
        //         <PrvtId>
        //           <Othr>
        //             <Id>DE10ZZZ00000905381</Id>
        //             <SchmeNm>
        //               <Prtry>SEPA</Prtry>
        //             </SchmeNm>
        //           </Othr>
        //         </PrvtId>
        //       </Id>
        //     </CdtrSchmeId>
        //   </DrctDbtTx>
        //   <DbtrAgt>
        //     <FinInstnId>
        //       <BIC>GENODEF1M05</BIC>
        //     </FinInstnId>
        //   </DbtrAgt>
        //   <Dbtr>
        //     <Nm>PETER BUBLITZ</Nm>
        //   </Dbtr>
        //   <DbtrAcct>
        //     <Id>
        //       <IBAN>DE02750903000001167774</IBAN>
        //     </Id>
        //   </DbtrAcct>
        //   <RmtInf>
        //     <Ustrd>BEITRAG 2014</Ustrd>
        //   </RmtInf>
        // </DrctDbtTxInf>

        newLine();
        newLine();
        startElement("DrctDbtTxInf", true);

        tab();
        element("PmtId", "EndToEndId", "NOTPROVIDED");
        tab();
        elementA("InstdAmt", "Ccy", "EUR", normBetrag(SepaFactory.parseBetragCent(beitrag.getBetrag() + "") + ""));

        tab();
        writer.writeStartElement("DrctDbtTx");
        newLine();
        tab(2);
        writer.writeStartElement("MndtRltdInf");
        newLine();
        tab(3);
        element("MndtId", beitrag.getMitgliedNr() + "");
        tab(3);
        element("DtOfSgntr", getSepaStartDate(mitglied));
        tab(3);
        element("AmdmntInd", "false");
        tab(2);
        endElement(true); // MndtRltdInf

        tab(2);
        writer.writeStartElement("CdtrSchmeId");
        writer.writeStartElement("Id");
        writer.writeStartElement("PrvtId");
        newLine();
        tab(3);

        writer.writeStartElement("Othr");
        newLine();
        tab(4);
        element("Id", clubConfig.clubData.getGlaeubigerId());
        tab(4);
        element("SchmeNm", "Prtry", "SEPA");
        tab(3);
        endElement(true); // Othr

        tab(2);
        endElement();
        endElement();
        endElement(true); // CdtrSchmeId

        tab();
        endElement(true); // DrctDbtTx

        tab();
        element("DbtrAgt", "FinInstnId", "BIC", mitglied.getBic());
        tab();
        element("Dbtr", "Nm", mitglied.getVorname() + " " + mitglied.getNachname());
        tab();
        element("DbtrAcct", "Id", "IBAN", mitglied.getIban());
        tab();
        element("RmtInf", "Ustrd", "BEITRAG " + beitrag.getJahr());

        endElement(true); // DrctDbtTxInf
        newLine();
    }

    private String getMsgId() {
        // Das System generiert eine interne ID, die aus der Unternehmensnummer, Bankkontonummer und fortlaufenden Nummer (74/01) besteht. 	Maximal 35 Zeichen
        String mId = financeAccountData.getBic();
        if (mId.length() > 20) {
            mId = mId.substring(0, 15);
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String genD = sdf.format(new Date());
        mId = mId + "00000" + genD;

        if (mId.length() > 35) {
            mId = mId.substring(0, 35);
        }
        return mId;
    }

    private String genDate() {
        // Das System generiert einen Datums- und Zeitstempel zum Zeitpunkt der Formatierung der Nachricht. 	Das Format lautet JJJJ-MM-TTThh:mm:ss
        // <CreDtTm>2015-01-07T13:53:18Z</CreDtTm>
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String genD = sdf.format(new Date());
        return genD;
    }

    private String genExDate() {
        // Fälligkeitsdatum
        // <DtOfSgntr>2015-01-07</DtOfSgntr>
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String genD = sdf.format(executeDate);
        return genD;
    }

    private String getSepaStartDate(MemberData mitglied) {
        // Datum ab dem SEPA gillt
        // <DtOfSgntr>2015-01-07</DtOfSgntr>
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String genD;
        try {
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(mitglied.getSepaBeginn().toString());
            genD = sdf.format(date);
        } catch (Exception ex) {
            mitglied.getSepaBeginn().setPDateNow(); // todo??
            genD = sdf.format(new Date());
        }
        return genD;
    }

    private String getCount() {
        // Eine Gesamtzahl von Transaktionsblöcken der Überweisung in der Nachricht wird vom System angegeben.
        // Beispiel: Eine Nachricht enthält einen Gruppen-Header, einen Zahlungsinformationsblock und 4 Überweisungsinformationsblöcke. In diesem Fall wird die Anzahl der Transaktionen auf "4" gesetzt.
        // Maximal 15 numerische Zeichen
        return feeDataList.size() + "";
    }

    private String getNm(String name) {
        // Das System druckt den Empfängernamen des Unternehmens, das die Lieferantenrechnung bezahlt. Das System ruft diese Informationen aus der Tabelle "Adresse nach Datum" (F0116) ab. 	Maximal 70 Zeichen
        if (name.length() > 70) {
            name = name.substring(0, 65);
        }
        return name;
    }

    private void element(String element, String wert) throws XMLStreamException {
        startElement(element);
        wert(wert);
        endElement();
        newLine();
    }

    private void elementA(String element, String attribute, String attWert, String wert) throws XMLStreamException {
        startElement(element);
        writer.writeAttribute(attribute, attWert);
        wert(wert);
        endElement();
        newLine();
    }

    private void element(String element1, String element2, String wert) throws XMLStreamException {
        startElement(element1);
        startElement(element2);
        wert(wert);
        endElement();
        endElement();
        newLine();
    }

    private void element(String element1, String element2, String element3, String wert) throws XMLStreamException {
        startElement(element1);
        startElement(element2);
        startElement(element3);
        wert(wert);
        endElement();
        endElement();
        endElement();
        newLine();
    }

    private void tab() throws XMLStreamException {
        tab(1);
    }

    private void tab(int anz) throws XMLStreamException {
        for (int i = 0; i < anz; ++i) {
            writer.writeCharacters("\t");
        }
    }

    private void wert(String w) throws XMLStreamException {
        writer.writeCharacters(w);
    }

    private void startElement(String element) throws XMLStreamException {
        startElement(element, false);
    }

    private void startElement(String element, boolean nl) throws XMLStreamException {
        writer.writeStartElement(element);
        if (nl) {
            newLine();
        }
    }

    private void endElement() throws XMLStreamException {
        endElement(false);
    }

    private void endElement(boolean nl) throws XMLStreamException {
        writer.writeEndElement();
        if (nl) {
            newLine();
        }
    }

    private void newLine() throws XMLStreamException {
        writer.writeCharacters("\n"); //neue Zeile
    }

    private void endXml() throws Exception {
        endElement(true);
        endElement(true);
        writer.writeEndDocument();
        writer.flush();
        writer.close();
        out.close();
    }

    /**
     * @param file
     * @return
     */
    public boolean getZettel(Path file) {

        LinkedList<String> zettel = new LinkedList<>();
        String zeile;
        boolean ret;

        zettel.add("");
        zettel.add("");
        zettel.add("  Buchungsliste · Lastschriften " + PDateFactory.getTodayStr());
        zettel.add("");
        zettel.add("  ---------------------------------------------------------------------------");
        for (FeeData b : feeDataList) {
            zeile = getStringL(b);
            zettel.add("");
            zettel.add(zeile);
        }
        zettel.add("");
        zettel.add("  ---------------------------------------------------------------------------");
        zettel.add("");
        zettel.add("  _____________________________________________________________");
        zettel.add("");
        zettel.add("  - Eine Diskette ..................... : Nr. 1");
        zettel.add("  - Erstellungsdatum .................. : " + PDateFactory.getTodayStr());
        zettel.add("");
        zettel.add("  - Anzahl der Datensätze C ........... : " + getCount());
        zettel.add("  - Summe Euro der Datensätze C ....... : " + verlaengernBetrag(String.valueOf(getSum()), 0));
        zettel.add("");
        zettel.add("");
        zettel.add("  - Name des Auftraggebers............. : " + clubConfig.clubData.getName());
        zettel.add("  - BIC des Auftraggebers..... : " + financeAccountData.getBic());
        zettel.add("  - IBAN des Auftraggebers...... : " + financeAccountData.getIban());

        try {
            OutputStreamWriter strWriter;
            strWriter = new OutputStreamWriter(new FileOutputStream(file.toFile()));
            ListIterator<String> zettelIterator = zettel.listIterator(0);
            while (zettelIterator.hasNext()) {
                strWriter.write(zettelIterator.next() + "\n");
            }
            strWriter.close();
            ret = true;
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    private String verlaengernBetrag(String s, int n) {
        s = s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2);
        while (s.length() < n) {
            s = " " + s;
        }
        return s;
    }

    private String getStringL(FeeData beitrag) {

        String ret = "";
        int laenge = 25;

        ConfigExtra[] configs = beitrag.getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            if (i >= 11) {
                break;
            }

            if (i != 0 && i % 3 == 0) {
                ret += "\n";
            }
            String s = configs[i].getName() + ": " + configs[i].getActValueString();
            if (s.length() > laenge) {
                s = s.substring(0, laenge);
            }
            while (s.length() < laenge) {
                s += " ";
            }
            ret += s + "    ";
        }
        return ret;
    }

}
