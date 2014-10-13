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
public class KickoffRow implements Comparable{
    private String gameCode;
    private int playNum;
    private int teamCode;
    private int playerCode;
    private boolean attempted;
    private int yards;
    private boolean fairCatch;
    private boolean touchback;
    private boolean downed;
    private boolean outOfBounds;
    private boolean onside;
    private boolean onsideSuccess;

    public static KickoffRow makeRow(String line){
        KickoffRow row = new KickoffRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        row.setPlayNum(Integer.valueOf(tokens[1]).intValue());
        row.setTeamCode(Integer.valueOf(tokens[2]).intValue());
        row.setPlayerCode(Integer.valueOf(tokens[3]).intValue());
        row.setAttempted(getBoolean(tokens[4]));
        row.setYards(Integer.valueOf(tokens[5]).intValue());
        row.setFairCatch(getBoolean(tokens[6]));
        row.setTouchback(getBoolean(tokens[7]));
        row.setDowned(getBoolean(tokens[8]));
        row.setOutOfBounds(getBoolean(tokens[9]));
        row.setOnside(getBoolean(tokens[10]));
        row.setOnsideSuccess(getBoolean(tokens[11]));
        return row;
    }
    
    
    @Override
    public boolean equals(Object o){
        if (o instanceof KickoffRow){
            KickoffRow here = (KickoffRow)o;
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
        if (o instanceof KickoffRow){
            return this.hashCode() - ((KickoffRow)o).hashCode();
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

    /**
     * @return the touchback
     */
    public boolean isTouchback() {
        return touchback;
    }

    /**
     * @param touchback the touchback to set
     */
    public void setTouchback(boolean touchback) {
        this.touchback = touchback;
    }

    /**
     * @return the downed
     */
    public boolean isDowned() {
        return downed;
    }

    /**
     * @param downed the downed to set
     */
    public void setDowned(boolean downed) {
        this.downed = downed;
    }

    /**
     * @return the outOfBounds
     */
    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    /**
     * @param outOfBounds the outOfBounds to set
     */
    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    /**
     * @return the onside
     */
    public boolean isOnside() {
        return onside;
    }

    /**
     * @param onside the onside to set
     */
    public void setOnside(boolean onside) {
        this.onside = onside;
    }

    /**
     * @return the onsideSuccess
     */
    public boolean isOnsideSuccess() {
        return onsideSuccess;
    }

    /**
     * @param onsideSuccess the onsideSuccess to set
     */
    public void setOnsideSuccess(boolean onsideSuccess) {
        this.onsideSuccess = onsideSuccess;
    }
    
    
            
}
