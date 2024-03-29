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


package de.p2tools.cluborga.data.feeData.paymentType;

public class PaymentTypeFactory {
    public static int PAYMENT_TYPE_SIZE = PAYMENT_TYPE.values().length;

    public enum PAYMENT_TYPE {

        PAYMENT_BAR(0, 1, "Bar", "Barzahlung"),
        PAYMENT_UEBERWEISUNG(1, 2, "Überweisung", "Überweisung"),
        PAYMENT_BANKEINZUG(2, 3, "Einzug", "Bankeinzug");

        private final int id;
        private final int shownNo;
        private final String name;
        private final String description;

        PAYMENT_TYPE(final int id, final int shownNo, final String name, final String description) {
            this.id = id;
            this.shownNo = shownNo;
            this.name = name;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public int getShownNo() {
            return shownNo;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private PaymentTypeFactory() {
    }

    public static PAYMENT_TYPE getPaymentType(long id) {
        if (id >= PAYMENT_TYPE.values().length) {
            return null;
        }

        return PAYMENT_TYPE.values()[(int) id];
    }
}
