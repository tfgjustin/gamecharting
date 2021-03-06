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
import java.util.Date;
import java.util.Locale;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class CalendarUtils {
  public static final String BOWL_WEEK = "Bowls";
  public static final String PRE_SEASON_WEEK = "PreSeason";
  public static final String SEASON_WEEK_TAG = "Week";
  // Historically the first day of bowl games is no earlier than December 15th.
  private static final int BOWL_START_MONTH = Calendar.DECEMBER;
  private static final int BOWL_START_DAY_OF_MONTH = 15;
  
  private static final int SEASON_START_MONTH_CUTOFF = Calendar.FEBRUARY;
  private static final int WEEK_START_CUTOFF = Calendar.TUESDAY;
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");

  public static int getSeasonStartWeekOffset(int year){
      // The first week of the season, since at least 2008, is based around the closest
      // thursday to Sept 1.  Which can be figured out based on, the day of Sept 1.
      // The offsets below yield results consistent with ESPN from 2008-2015, and 
      // with educated guesses about the first week of 2016-2018, and are verified
      // in the Unit testing.  The rule is not consistent with all time, since in 2004
      // and earlier, pre-season games (kickoff classics) played a week earlier in August,
      // were deemed week 1 by ESPN
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      switch (cal.get(Calendar.DAY_OF_WEEK)){
          case Calendar.SUNDAY : return 34;
          case Calendar.MONDAY : return 34;
          case Calendar.TUESDAY : return 35;
          case Calendar.WEDNESDAY : return 35;
          case Calendar.THURSDAY : return 35;
          case Calendar.FRIDAY : return 34;
          case Calendar.SATURDAY : return 34;
          default : return 34;    
      }
  }
  
  public static String gameDateToSeason(String gameDate) throws ParseException {
      
    Calendar cal = Calendar.getInstance();
    cal.setTime(parseDate(gameDate));
    Integer year = cal.get(Calendar.YEAR);
    Integer month = cal.get(Calendar.MONTH);
    if (month < SEASON_START_MONTH_CUTOFF) {
      // If it's earlier than august, this is from the previous year
      year -= 1;
    }
    return year.toString();
  }

  public static String gameDateToWeek(String gameDate) throws ParseException {
      
    Calendar cal = Calendar.getInstance();
    cal.setTime(parseDate(gameDate));
    Integer month = cal.get(Calendar.MONTH);
    if (month < SEASON_START_MONTH_CUTOFF) {
        // there are no bowl games Feb or later
      return BOWL_WEEK;
    }
    Integer day = cal.get(Calendar.DAY_OF_MONTH);
    if (month == BOWL_START_MONTH && day >= BOWL_START_DAY_OF_MONTH) {
      return BOWL_WEEK;
    }
    // It's not in bowl season, so lets figure out which week of the season.
    Integer year = cal.get(Calendar.YEAR);
    Integer weekNum = cal.get(Calendar.WEEK_OF_YEAR) - getSeasonStartWeekOffset(year);
    if (cal.get(Calendar.DAY_OF_WEEK) < WEEK_START_CUTOFF){
        weekNum -= 1;
    }
    if (weekNum < 1){
        // if for some reason, the game is scheduled before SEASON_START_WEEK_OFFSET,
        // then we'll call it the pre-season.  
        return PRE_SEASON_WEEK;
    }
    else{
        return String.format("%s %02d", SEASON_WEEK_TAG, weekNum);
    }
  }

  public static boolean isFutureGame(String gameDate) throws ParseException {
      
    Calendar gameCal = Calendar.getInstance();
    gameCal.setTime(parseDate(gameDate));
    Calendar currCal = Calendar.getInstance();
    if (gameCal.after(currCal)) {
      return true;
    }
    return false;
  }

  public static boolean isValidGameDate(String gameDate) {
    try {
      parseDate(gameDate);
    } catch (ParseException e) {
      //System.err.println("Invalid game date: " + gameDate);
      return false;
    }
    return true;
  }
  
  private static Date parseDate(String gameDate) throws ParseException{
      if (gameDate == null){
          throw new ParseException("gameDate cannot be null.",0);
      }
      if (gameDate.length() != 8){
          throw new ParseException(gameDate +" must be of length 8, not "+gameDate.length(),
                  gameDate.length()<8?gameDate.length():gameDate.length()-8);
      }
      
      return DATE_FORMATTER.parse(gameDate);
  }
  
}  
