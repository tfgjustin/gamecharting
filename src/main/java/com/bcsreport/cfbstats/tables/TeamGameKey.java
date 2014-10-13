/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 * 
 * @author ryan.hoes
 */
public class TeamGameKey implements Comparable {

  private String gameCode;
  private Integer teamCode;

  public TeamGameKey(String game, Integer teamId) {
    this.gameCode = game;
    this.teamCode = teamId;
  }

  /**
   * @return the gameCode
   */
  public String getGameCode() {
    return gameCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TeamGameKey other = (TeamGameKey) obj;
    if (!this.gameCode.equals(other.gameCode)) {
      return false;
    }
    if (!this.teamCode.equals(other.teamCode)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + this.gameCode.hashCode();
    hash = 11 * hash + this.teamCode.hashCode();
    return hash;
  }

  /**
   * @return the teamCode
   */
  public Integer getTeamCode() {
    return teamCode;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof TeamGameKey) {
      return this.hashCode() - ((TeamGameKey) o).hashCode();
    }
    return -1;
  }

  @Override
  public String toString() {
    return this.gameCode + ":" + this.teamCode;
  }
}
