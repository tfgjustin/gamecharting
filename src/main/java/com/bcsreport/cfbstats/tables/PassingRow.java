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
public class PassingRow implements Comparable{
    private String gameCode;
    private int playNum;
    private int teamCode;
    private int passerPlayerCode;
    private int receiverPlayerCode;
    private boolean attempted;
    private boolean complete;
    private boolean touchdown;
    private boolean intercepted;
    private boolean firstDown;
    private boolean dropped;
    private int yards;

    public static PassingRow makeRow(String line){
        PassingRow row = new PassingRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        row.setPlayNum(Integer.valueOf(tokens[1]).intValue());
        row.setTeamCode(Integer.valueOf(tokens[2]).intValue());
        row.setPasserPlayerCode(Integer.valueOf(tokens[3]).intValue());
        if (tokens[4].length() <= 0 || tokens[4].equals("")){
            row.setReceiverPlayerCode(-1);
        }
        else{
            row.setReceiverPlayerCode(Integer.valueOf(tokens[4]).intValue());
        }
        row.setAttempted(getBoolean(tokens[5]));
        row.setComplete(getBoolean(tokens[6]));
        row.setYards(Integer.valueOf(tokens[7]).intValue());
        row.setTouchdown(getBoolean(tokens[8]));
        row.setIntercepted(getBoolean(tokens[9]));
        row.setFirstDown(getBoolean(tokens[10]));
        row.setDropped(getBoolean(tokens[11]));
        
        return row;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof PassingRow){
            PassingRow here = (PassingRow)o;
            return this.gameCode.equals(here.gameCode) &&
                    this.playNum == here.playNum &&
                    this.teamCode == here.teamCode &&
                    this.passerPlayerCode == here.passerPlayerCode &&
                    this.receiverPlayerCode == here.receiverPlayerCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.gameCode.hashCode();
        hash = 61 * hash + this.playNum;
        hash = 61 * hash + this.teamCode;
        hash = 61 * hash + this.passerPlayerCode;
        hash = 61 * hash + this.receiverPlayerCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof PassingRow){
            return this.hashCode() - ((PassingRow)o).hashCode();
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
     * @return the passerPlayerCode
     */
    public int getPasserPlayerCode() {
        return passerPlayerCode;
    }

    /**
     * @param passerPlayerCode the passerPlayerCode to set
     */
    public void setPasserPlayerCode(int passerPlayerCode) {
        this.passerPlayerCode = passerPlayerCode;
    }

    /**
     * @return the receiverPlayerCode
     */
    public int getReceiverPlayerCode() {
        return receiverPlayerCode;
    }

    /**
     * @param receiverPlayerCode the receiverPlayerCode to set
     */
    public void setReceiverPlayerCode(int receiverPlayerCode) {
        this.receiverPlayerCode = receiverPlayerCode;
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
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
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
     * @return the intercepted
     */
    public boolean isIntercepted() {
        return intercepted;
    }

    /**
     * @param intercepted the intercepted to set
     */
    public void setIntercepted(boolean intercepted) {
        this.intercepted = intercepted;
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
     * @return the dropped
     */
    public boolean isDropped() {
        return dropped;
    }

    /**
     * @param dropped the dropped to set
     */
    public void setDropped(boolean dropped) {
        this.dropped = dropped;
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
    
    
}
