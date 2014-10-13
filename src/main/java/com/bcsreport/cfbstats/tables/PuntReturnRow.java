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
public class PuntReturnRow implements Comparable{
    private String gameCode;
    private int playNum;
    private int teamCode;
    private int playerCode;
    private boolean attempted;
    private int yards;
    private boolean touchdown;
    private int fumbles;
    private boolean fumbleLost;
    private boolean safety;
    private boolean fairCatch;

    public static PuntReturnRow makeRow(String line){
        PuntReturnRow row = new PuntReturnRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        row.setPlayNum(Integer.valueOf(tokens[1]).intValue());
        row.setTeamCode(Integer.valueOf(tokens[2]).intValue());
        row.setPlayerCode(Integer.valueOf(tokens[3]).intValue());
        row.setAttempted(getBoolean(tokens[4]));
        row.setYards(Integer.valueOf(tokens[5]).intValue());
        row.setTouchdown(getBoolean(tokens[6]));
        row.setFumbles(Integer.valueOf(tokens[7]).intValue());
        row.setFumbleLost(getBoolean(tokens[8]));
        row.setSafety(getBoolean(tokens[9]));
        row.setFairCatch(getBoolean(tokens[10]));
        return row;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof PuntReturnRow){
            PuntReturnRow here = (PuntReturnRow)o;
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
        if (o instanceof PuntReturnRow){
            return this.hashCode() - ((PuntReturnRow)o).hashCode();
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
     * @return the attempted
     */
    public boolean isAttempted() {
        return attempted;
    }

    /**
     * @param attempted the attempted to set
     */
    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
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

    /**
     * @return the fairCatch
     */
    public boolean isFairCatch() {
        return fairCatch;
    }

    /**
     * @param fairCatch the fairCatch to set
     */
    public void setFairCatch(boolean fairCatch) {
        this.fairCatch = fairCatch;
    }
    
    
}
