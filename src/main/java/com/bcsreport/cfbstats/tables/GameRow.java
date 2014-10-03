/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.util.Date;

/**
 * 
 * @author ryan.hoes
 */
public class GameRow implements Comparable {
  private String gameCode;
  private Date date;
  private int visitingTeam;
  private int homeTeam;
  private int stadium;
  private Site site;


  public static GameRow makeRow(String line) {
    GameRow row = new GameRow();
    String[] tokens = line.split("[\t,]", -1);
    row.setGameCode(tokens[0]);
    row.setDate(new Date(tokens[1]));
    row.setVisitingTeam(Integer.valueOf(tokens[2]).intValue());
    row.setHomeTeam(Integer.valueOf(tokens[3]).intValue());
    row.setStadium(Integer.valueOf(tokens[4]).intValue());
    if (tokens[5].equalsIgnoreCase("NEUTRAL")) {
      row.setSite(Site.NEUTRAL);
    } else if (tokens[5].equalsIgnoreCase("HOME")) {
      row.setSite(Site.HOME);
    } else {
      row.setSite(Site.UNKNOWN);
    }
    return row;
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof GameRow) {
      GameRow here = (GameRow) o;
      return this.gameCode.equals(here.gameCode);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + this.gameCode.hashCode();
    return hash;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof GameRow) {
      return this.hashCode() - ((GameRow) o).hashCode();
    }
    return 1;
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
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the visitingTeam
   */
  public int getVisitingTeam() {
    return visitingTeam;
  }

  /**
   * @param visitingTeam the visitingTeam to set
   */
  public void setVisitingTeam(int visitingTeam) {
    this.visitingTeam = visitingTeam;
  }

  /**
   * @return the homeTeam
   */
  public int getHomeTeam() {
    return homeTeam;
  }

  /**
   * @param homeTeam the homeTeam to set
   */
  public void setHomeTeam(int homeTeam) {
    this.homeTeam = homeTeam;
  }

  /**
   * @return the stadium
   */
  public int getStadium() {
    return stadium;
  }

  /**
   * @param stadium the stadium to set
   */
  public void setStadium(int stadium) {
    this.stadium = stadium;
  }

  /**
   * @return the site
   */
  public Site getSite() {
    return site;
  }

  /**
   * @param site the site to set
   */
  public void setSite(Site site) {
    this.site = site;
  }

  static enum Site {
    HOME, NEUTRAL, UNKNOWN, ;
  }
}
