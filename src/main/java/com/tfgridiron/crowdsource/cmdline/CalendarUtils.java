/*
 * Copyright (c) Justin Moore.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tfgridiron.crowdsource.cmdline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class CalendarUtils {
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");

  public static String gameDateToSeason(String gameDate) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(DATE_FORMATTER.parse(gameDate));
    Integer year = cal.get(Calendar.YEAR);
    Integer month = cal.get(Calendar.MONTH);
    if (month < Calendar.AUGUST) {
      // If it's earlier than august, this is from the previous year
      year -= 1;
    }
    return year.toString();
  }

  public static boolean isFutureGame(String gameDate) throws ParseException {
    Calendar gameCal = Calendar.getInstance();
    gameCal.setTime(DATE_FORMATTER.parse(gameDate));
    Calendar currCal = Calendar.getInstance();
    if (gameCal.after(currCal)) {
      return true;
    }
    return false;
  }

  public static boolean isValidGameDate(String gameDate) {
    try {
      DATE_FORMATTER.parse(gameDate);
    } catch (ParseException e) {
      System.err.println("Invalid game date: " + gameDate);
      return false;
    }
    return true;
  }
}
