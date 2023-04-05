/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.cluborga.data.financeData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.feeData.FeeFactory;
import de.p2tools.p2lib.configfile.config.Config;

public class TransactionFactory {
    /**
     * copy TransactionData FROM to the TransactionData TO
     *
     * @param dataFrom
     */
    private static TransactionData copyTransactionData(ClubConfig clubConfig, boolean copyFeeData, boolean copyMemberDate,
                                                       TransactionData dataFrom, TransactionData dataTo) {
        if (dataFrom == null) {
            return null;
        }
        if (dataTo == null) {
            dataTo = new TransactionData();
        }

        Config[] configs = dataFrom.getConfigsArr();
        Config[] configsCopy = dataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }

        dataTo.setFinanceCategoryData(dataFrom.getFinanceCategoryData());
        if (copyFeeData) {
            dataTo.setFeeData(FeeFactory.copyFeeData(clubConfig, copyMemberDate, dataFrom.getFeeData(), dataTo.getFeeData()));
        }

        return dataTo;
    }


    /**
     * copy TransactionDataList FROM to the TransactionDataList TO
     *
     * @param dataListFrom
     * @param dataListTo
     */
    public static TransactionDataList copyTransactionDataList(ClubConfig clubConfig,
                                                              TransactionDataList dataListFrom, TransactionDataList dataListTo) {
        if (dataListFrom == null) {
            return null;
        }
        if (dataListTo == null) {
            dataListTo = new TransactionDataList();
        }

        for (int i = 0; i < dataListFrom.getSize(); ++i) {
            TransactionData trFrom = dataListFrom.get(i);
            TransactionData trTo = null;
            if (dataListTo.size() > i) {
                trTo = dataListTo.remove(i);
            }
            trTo = copyTransactionData(clubConfig, true, true, trFrom, trTo);
            dataListTo.add(i, trTo);

//            boolean foundFee = false;
//            boolean foundMember = false;
//            int ii;
//            for (ii = 0; ii < i; ++ii) {
//                if (dataListFrom.get(ii).getFeeData().equals(dataListFrom.get(i).getFeeData())) {
//                    // wenn die gleiche FeeData in verschiedenen Trans vorkommen
//                    foundFee = true;
//                    break;
//                } else if (dataListFrom.get(ii).getFeeData().getMemberData().equals(dataListFrom.get(i).getFeeData().getMemberData())) {
//                    // oder MemberData
//                    foundMember = true;
//                    break;
//                }
//            }
//
//            trTo = copyTransactionData(clubConfig, foundFee, foundMember, trFrom, trTo);
//            dataListTo.add(i, trTo);
//            if (foundFee) {
//                trTo.setFeeData(dataListTo.get(i).getFeeData());
//            }
//            if (foundMember) {
//                trTo.getFeeData().setMemberData(dataListTo.get(i).getFeeData().getMemberData());
//            }
        }

        return dataListTo;
    }
}
