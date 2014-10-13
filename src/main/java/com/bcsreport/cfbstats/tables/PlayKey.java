/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 * 
 * @author ryan.hoes
 */
public class PlayKey implements Comparable {

  private String gameCode;
  private Integer playNum;

  public PlayKey(String game, Integer playNum) {
    this.gameCode = game;
    this.playNum = playNum;
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
    final PlayKey other = (PlayKey) obj;
    if (!this.gameCode.equals(other.gameCode)) {
      return false;
    }
    if (!this.playNum.equals(other.playNum)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + this.gameCode.hashCode();
    hash = 11 * hash + this.playNum.hashCode();
    return hash;
  }

  /**
   * @return the playNum
   */
  public Integer getPlayNum() {
    return playNum;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof PlayKey) {
      return this.hashCode() - ((PlayKey) o).hashCode();
    }
    return -1;
  }
}
