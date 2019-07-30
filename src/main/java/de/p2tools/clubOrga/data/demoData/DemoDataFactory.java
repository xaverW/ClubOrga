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


package de.p2tools.clubOrga.data.demoData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DemoDataFactory {
    private static Random random = new Random();
    public static final int maxYear = 10;


    private DemoDataFactory() {
    }

    public static Calendar getRandomCalender() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1 * random.nextInt(10));
        cal.add(Calendar.MONTH, -1 * random.nextInt(12));
        cal.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(30));

        return cal;
    }

    public static ArrayList<Integer> getYearList() {
        ArrayList<Integer> yearList = new ArrayList<>(15);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1 * maxYear);
        int year = cal.get(Calendar.YEAR);

        for (int i = 1; i <= maxYear; ++i) {
            year += i;
            yearList.add(year);
        }

        return yearList;
    }

    public static LocalDateTime getRandomDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1 * random.nextInt(maxYear));
        cal.add(Calendar.MONTH, -1 * random.nextInt(12));
        cal.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(30));

        LocalDateTime dateTime = LocalDateTime.ofInstant(cal.toInstant(),
                ZoneId.systemDefault());

        return dateTime;
    }

    public static LocalDateTime getRandomDate(int year) {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1 * random.nextInt(12));
        cal.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(30));
        cal.set(Calendar.YEAR, year);

        LocalDateTime dateTime = LocalDateTime.ofInstant(cal.toInstant(),
                ZoneId.systemDefault());

        return dateTime;
    }
}
