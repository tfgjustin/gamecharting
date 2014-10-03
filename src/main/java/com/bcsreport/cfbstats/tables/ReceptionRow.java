/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;
import static com.bcsreport.cfbstats.tables.DriveRow.getBoolean;
/**
 *
 * @author ryan.hoes
 */
public class ReceptionRow implements Comparable{
    private String gameCode;
    private int playNum;
    private int teamCode;
    private int playerCode;
    private boolean reception;
    private int yards;
    private boolean touchdown;
    private boolean firstDown;
    private int fumbles;
    private boolean fumbleLost;
    private boolean safety;

    public static ReceptionRow makeRow(String line){
        ReceptionRow row = new ReceptionRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        row.setPlayNum(Integer.valueOf(tokens[1]).intValue());
        row.setTeamCode(Integer.valueOf(tokens[2]).intValue());
        row.setPlayerCode(Integer.valueOf(tokens[3]).intValue());
        row.setReception(getBoolean(tokens[4]));
        row.setYards(Integer.valueOf(tokens[5]).intValue());
        row.setTouchdown(getBoolean(tokens[6]));
        row.setFirstDown(getBoolean(tokens[7]));
        row.setFumbles(Integer.valueOf(tokens[8]).intValue());
        row.setFumbleLost(getBoolean(tokens[9]));
        row.setSafety(getBoolean(tokens[10]));
        
        return row;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof ReceptionRow){
            ReceptionRow here = (ReceptionRow)o;
            return this.gameCode.equals(here.gameCode) &&
                    this.playNum == here.playNum &&
                    this.teamCode == here.teamCode &&
                    this.playerCode == here.playerCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.gameCode.hashCode();
        hash = 61 * hash + this.playNum;
        hash = 61 * hash + this.teamCode;
        hash = 61 * hash + this.playerCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof ReceptionRow){
            return this.hashCode() - ((ReceptionRow)o).hashCode();
        }
        else{
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
     * @return the playNum
     */
    public int getPlayNum() {
        return playNum;
    }

    /**
     * @param playNum the playNum to set
     */
    public void setPlayNum(int playNum) {
        this.playNum = playNum;
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
     * @return the playerCode
     */
    public int getPlayerCode() {
        return playerCode;
    }

    /**
     * @param playerCode the playerCode to set
     */
    public void setPlayerCode(int playerCode) {
        this.playerCode = playerCode;
    }

    /**
     * @return the reception
     */
    public boolean isReception() {
        return reception;
    }

    /**
     * @param reception the reception to set
     */
    public void setReception(boolean reception) {
        this.reception = reception;
    }

    /**
     * @return the yards
     */
    public int getYards() {
        return yards;
    }

    /**
     * @param yards the yards to set
     */
    public void setYards(int yards) {
        this.yards = yards;
    }

    /**
     * @return the touchdown
     */
    public boolean isTouchdown() {
        return touchdown;
    }

    /**
     * @param touchdown the touchdown to set
     */
    public void setTouchdown(boolean touchdown) {
        this.touchdown = touchdown;
    }

    /**
     * @return the firstDown
     */
    public boolean isFirstDown() {
        return firstDown;
    }

    /**
     * @param firstDown the firstDown to set
     */
    public void setFirstDown(boolean firstDown) {
        this.firstDown = firstDown;
    }

    /**
     * @return the fumbled
     */
    public int getFumbles() {
        return fumbles;
    }

    /**
     * @param fumbled the fumbled to set
     */
    public void setFumbles(int fumbles) {
        this.fumbles = fumbles;
    }

    /**
     * @return the fumbleLost
     */
    public boolean isFumbleLost() {
        return fumbleLost;
    }

    /**
     * @param fumbleLost the fumbleLost to set
     */
    public void setFumbleLost(boolean fumbleLost) {
        this.fumbleLost = fumbleLost;
    }

    /**
     * @return the safety
     */
    public boolean isSafety() {
        return safety;
    }

    /**
     * @param safety the safety to set
     */
    public void setSafety(boolean safety) {
        this.safety = safety;
    }
    
    
}
