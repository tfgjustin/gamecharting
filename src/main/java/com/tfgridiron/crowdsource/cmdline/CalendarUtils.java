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
  public static final String BOWL_WEEK = "Bowls";
  public static final String SEASON_WEEK_TAG = "Week";
  // Historically the first day of bowl games is no earlier than December 15th.
  private static final int BOWL_START_MONTH = Calendar.DECEMBER;
  private static final int BOWL_START_DAY_OF_MONTH = 15;
  // Historically the first week in which there are games is week 35. So subtract 34 from the week
  // number to get the week of the season.
  private static final int SEASON_START_WEEK_OFFSET = 34;
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

  public static String gameDateToWeek(String gameDate) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(DATE_FORMATTER.parse(gameDate));
    Integer month = cal.get(Calendar.MONTH);
    if (month < Calendar.AUGUST) {
      return BOWL_WEEK;
    }
    Integer day = cal.get(Calendar.DAY_OF_MONTH);
    if (month == BOWL_START_MONTH && day >= BOWL_START_DAY_OF_MONTH) {
      return BOWL_WEEK;
    }
    // It's not in bowl season, so it must be the regular season.
    Integer weekNum = cal.get(Calendar.WEEK_OF_YEAR) - SEASON_START_WEEK_OFFSET;
    return String.format("%s %02d", SEASON_WEEK_TAG, weekNum);
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
