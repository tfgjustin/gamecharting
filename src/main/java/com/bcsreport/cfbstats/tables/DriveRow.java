/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 * 
 * @author ryan.hoes
 */
public class DriveRow implements Comparable {
  private String gameCode;
  private int driveNum;
  private int teamCode;
  private int startQtr;
  private int startClock;
  private int startSpot;
  private DriveStart startReason;
  private int endQtr;
  private int endClock;
  private int endSpot;
  private DriveEnd endReason;
  private int numPlays;
  private int ydsGained;
  private int timeOfPossession;
  private boolean redzoneAttempt;

  public static DriveRow makeRow(String line) {
    DriveRow row = new DriveRow();
    String[] tokens = line.split("[\t,]", -1);
    for (int i = 0; i < tokens.length; i++) {
      tokens[i] = tokens[i].replaceAll("\"", "");
    }
    row.setGameCode(tokens[0]);
    row.setDriveNum(Integer.valueOf(tokens[1]).intValue());
    row.setTeamCode(Integer.valueOf(tokens[2]).intValue());
    row.setStartQtr(Integer.valueOf(tokens[3]).intValue());
    if (tokens[4].length() <= 0 || tokens[4].equals("")) {
      row.setStartClock(-1);
    } else {
      row.setStartClock(Integer.valueOf(tokens[4]).intValue());
    }

    row.setStartSpot(Integer.valueOf(tokens[5]).intValue());

    if (tokens[6].equalsIgnoreCase("DOWNS")) {
      row.setStartReason(DriveStart.DOWNS);
    } else if (tokens[6].equalsIgnoreCase("FUMBLE")) {
      row.setStartReason(DriveStart.FUMBLE);
    } else if (tokens[6].equalsIgnoreCase("INTERCEPTION")) {
      row.setStartReason(DriveStart.INT);
    } else if (tokens[6].equalsIgnoreCase("KICKOFF")) {
      row.setStartReason(DriveStart.KICKOFF);
    } else if (tokens[6].equalsIgnoreCase("MISSED FIELD GOAL")) {
      row.setStartReason(DriveStart.MISSED_FG);
    } else if (tokens[6].equalsIgnoreCase("POSSESSION")) {
      row.setStartReason(DriveStart.OVERTIME_POSSESSION);
    } else if (tokens[6].equalsIgnoreCase("PUNT")) {
      row.setStartReason(DriveStart.PUNT);
    } else {
      throw new UnsupportedOperationException("Invalid Start Reason: " + tokens[6]);
    }
    row.setEndQtr(Integer.valueOf(tokens[7]).intValue());
    if (tokens[8].length() <= 0 || tokens[8].equals("")) {
      row.setEndClock(-1);
    } else {
      row.setEndClock(Integer.valueOf(tokens[8]).intValue());
    }
    row.setEndSpot(Integer.valueOf(tokens[9]).intValue());

    if (tokens[10].equalsIgnoreCase("DOWNS")) {
      row.setEndReason(DriveEnd.DOWNS);
    } else if (tokens[10].equalsIgnoreCase("END OF HALF")) {
      row.setEndReason(DriveEnd.END_OF_HALF);
    } else if (tokens[10].equalsIgnoreCase("FIELD GOAL")) {
      row.setEndReason(DriveEnd.FIELD_GOAL);
    } else if (tokens[10].equalsIgnoreCase("FUMBLE")) {
      row.setEndReason(DriveEnd.FUMBLE);
    } else if (tokens[10].equalsIgnoreCase("INTERCEPTION")) {
      row.setEndReason(DriveEnd.INT);
    } else if (tokens[10].equalsIgnoreCase("MISSED FIELD GOAL")) {
      row.setEndReason(DriveEnd.MISSED_FG);
    } else if (tokens[10].equalsIgnoreCase("PUNT")) {
      row.setEndReason(DriveEnd.PUNT);
    } else if (tokens[10].equalsIgnoreCase("TOUCHDOWN")) {
      row.setEndReason(DriveEnd.TOUCHDOWN);
    } else if (tokens[10].equalsIgnoreCase("SAFETY")) {
      row.setEndReason(DriveEnd.SAFETY);
    } else {
      throw new UnsupportedOperationException("Invalid End Reason: " + tokens[10]);
    }

    row.setNumPlays(Integer.valueOf(tokens[11]).intValue());
    row.setYdsGained(Integer.valueOf(tokens[12]).intValue());
    if (tokens[13].length() <= 0 || tokens[13].equals("")) {
      row.setTimeOfPossession(-1);
    } else {
      row.setTimeOfPossession(Integer.valueOf(tokens[13]).intValue());
    }
    row.setRedzoneAttempt(getBoolean(tokens[14]));
    return row;
  }

  public static boolean getBoolean(String here) {
    if (Integer.valueOf(here) == 1) {
      return true;
    } else if (Integer.valueOf(here) == 0) {
      return false;
    }
    throw new UnsupportedOperationException("Invalid boolean value: " + here);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DriveRow) {
      DriveRow here = (DriveRow) o;
      return this.gameCode == here.gameCode && this.driveNum == here.driveNum
          && this.teamCode == here.teamCode;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + this.gameCode.hashCode();
    hash = 61 * hash + this.driveNum;
    hash = 61 * hash + this.teamCode;
    return hash;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof DriveRow) {
      return this.hashCode() - ((DriveRow) o).hashCode();
    } else {
      return 1;
    }

  }

  /**
   * @return the gameCode
   */
  public String getGameCode() {
    return gameCode;
  }

  /**
   * @param gameCode the gameCode to set
   */
  public void setGameCode(String gameCode) {
    this.gameCode = gameCode;
  }

  /**
   * @return the driveNum
   */
  public int getDriveNum() {
    return driveNum;
  }

  /**
   * @param driveNum the driveNum to set
   */
  public void setDriveNum(int driveNum) {
    this.driveNum = driveNum;
  }

  /**
   * @return the teamCode
   */
  public int getTeamCode() {
    return teamCode;
  }

  /**
   * @param teamCode the teamCode to set
   */
  public void setTeamCode(int teamCode) {
    this.teamCode = teamCode;
  }

  /**
   * @return the startQtr
   */
  public int getStartQtr() {
    return startQtr;
  }

  /**
   * @param startQtr the startQtr to set
   */
  public void setStartQtr(int startQtr) {
    this.startQtr = startQtr;
  }

  /**
   * @return the startClock
   */
  public int getStartClock() {
    return startClock;
  }

  /**
   * @param startClock the startClock to set
   */
  public void setStartClock(int startClock) {
    this.startClock = startClock;
  }

  /**
   * @return the startSpot
   */
  public int getStartSpot() {
    return startSpot;
  }

  /**
   * @param startSpot the startSpot to set
   */
  public void setStartSpot(int startSpot) {
    this.startSpot = startSpot;
  }

  /**
   * @return the startReason
   */
  public DriveStart getStartReason() {
    return startReason;
  }

  /**
   * @param startReason the startReason to set
   */
  public void setStartReason(DriveStart startReason) {
    this.startReason = startReason;
  }

  /**
   * @return the endQtr
   */
  public int getEndQtr() {
    return endQtr;
  }

  /**
   * @param endQtr the endQtr to set
   */
  public void setEndQtr(int endQtr) {
    this.endQtr = endQtr;
  }

  /**
   * @return the endClock
   */
  public int getEndClock() {
    return endClock;
  }

  /**
   * @param endClock the endClock to set
   */
  public void setEndClock(int endClock) {
    this.endClock = endClock;
  }

  /**
   * @return the endSpot
   */
  public int getEndSpot() {
    return endSpot;
  }

  /**
   * @param endSpot the endSpot to set
   */
  public void setEndSpot(int endSpot) {
    this.endSpot = endSpot;
  }

  /**
   * @return the endReason
   */
  public DriveEnd getEndReason() {
    return endReason;
  }

  /**
   * @param endReason the endReason to set
   */
  public void setEndReason(DriveEnd endReason) {
    this.endReason = endReason;
  }

  /**
   * @return the numPlays
   */
  public int getNumPlays() {
    return numPlays;
  }

  /**
   * @param numPlays the numPlays to set
   */
  public void setNumPlays(int numPlays) {
    this.numPlays = numPlays;
  }

  /**
   * @return the ydsGained
   */
  public int getYdsGained() {
    return ydsGained;
  }

  /**
   * @param ydsGained the ydsGained to set
   */
  public void setYdsGained(int ydsGained) {
    this.ydsGained = ydsGained;
  }

  /**
   * @return the timeOfPossession
   */
  public int getTimeOfPossession() {
    return timeOfPossession;
  }

  /**
   * @param timeOfPossession the timeOfPossession to set
   */
  public void setTimeOfPossession(int timeOfPossession) {
    this.timeOfPossession = timeOfPossession;
  }

  /**
   * @return the redzoneAttempt
   */
  public boolean isRedzoneAttempt() {
    return redzoneAttempt;
  }

  /**
   * @param redzoneAttempt the redzoneAttempt to set
   */
  public void setRedzoneAttempt(boolean redzoneAttempt) {
    this.redzoneAttempt = redzoneAttempt;
  }

  static enum DriveStart {
    DOWNS, FUMBLE, INT, KICKOFF, MISSED_FG, OVERTIME_POSSESSION, PUNT, ;
  }

  static enum DriveEnd {
    DOWNS, END_OF_HALF, FIELD_GOAL, FUMBLE, INT, MISSED_FG, PUNT, SAFETY, TOUCHDOWN, ;
  }


}
