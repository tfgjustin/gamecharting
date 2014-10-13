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
public class PlayRow implements Comparable{
    private String gameCode;
    private int playNum;
    private int qtr;
    private int clock;
    private int offenseTeamCode;
    private int defenseTeamCode;
    private int offensePoints;
    private int defensePoints;
    private Down down;
    private int distanceToGo;
    private int spot;
    private PlayType type;
    private int driveNum;
    private boolean driveAtt;

    public static PlayRow makeRow(String line){
        PlayRow row = new PlayRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        row.setPlayNum(Integer.valueOf(tokens[1]).intValue());
        row.setQtr(Integer.valueOf(tokens[2]).intValue());
        if (tokens[3].length() == 0 || tokens[3].equals("")){
            row.setClock(-1);
        }
        else{
            row.setClock(Integer.valueOf(tokens[3]).intValue());
        }
        row.setOffenseTeamCode(Integer.valueOf(tokens[4]).intValue());
        row.setDefenseTeamCode(Integer.valueOf(tokens[5]).intValue());
        row.setOffensePoints(Integer.valueOf(tokens[6]).intValue());
        row.setDefensePoints(Integer.valueOf(tokens[7]).intValue());
        if (tokens[8].length() == 0 || tokens[8].equals("")){
            row.setDown(Down.UNKNOWN);
        }
        else if (tokens[8].equals("1")){
            row.setDown(Down.FIRST);
        }
        else if (tokens[8].equals("2")){
            row.setDown(Down.SECOND);
        }
        else if (tokens[8].equals("3")){
            row.setDown(Down.THIRD);
        }
        else if (tokens[8].equals("4")){
            row.setDown(Down.FOURTH);
        }
        else{
            throw new UnsupportedOperationException("Invalid Down: "+tokens[8]);
        }
        if (tokens[9].length() <= 0 || tokens[9].equals("")){
            row.setDistanceToGo(-1);
        }
        else{
            row.setDistanceToGo(Integer.valueOf(tokens[9]).intValue());
        }
        row.setSpot(Integer.valueOf(tokens[10]).intValue());
        row.setType(PlayType.valueOf(tokens[11]));
        if (tokens[12].length() <= 0 || tokens[12].equals("")){
            row.setDriveNum(-1);
        }
        else{
            row.setDriveNum(Integer.valueOf(tokens[12]).intValue());
        }
        if (tokens[13].length() <= 0 || tokens[12].equals("")){
            row.setDriveAtt(false);
        }
        else{
            row.setDriveAtt(getBoolean(tokens[13]));
        }
        
        return row;
    }
    
    public PlayKey getKey(){
        return new PlayKey(this.getGameCode(), this.getPlayNum());
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof PlayRow){
            PlayRow here = (PlayRow)o;
            return this.gameCode.equals(here.gameCode) &&
                    this.playNum == here.playNum;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.gameCode.hashCode();
        hash = 61 * hash + this.playNum;
        
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof PlayRow){
            return this.hashCode() - ((PlayRow)o).hashCode();
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
     * @return the qtr
     */
    public int getQtr() {
        return qtr;
    }

    /**
     * @param qtr the qtr to set
     */
    public void setQtr(int qtr) {
        this.qtr = qtr;
    }

    /**
     * @return the clock
     */
    public int getClock() {
        return clock;
    }
    
    public String getClockString(){
        int minutes = clock / 60;
        int seconds = clock % 60;
        if (seconds < 10){
            return minutes+":0"+seconds;
        }
        else{
            return minutes+":"+seconds;
        }
    }

    /**
     * @param clock the clock to set
     */
    public void setClock(int clock) {
        this.clock = clock;
    }

    /**
     * @return the offenseTeamCode
     */
    public int getOffenseTeamCode() {
        return offenseTeamCode;
    }

    /**
     * @param offenseTeamCode the offenseTeamCode to set
     */
    public void setOffenseTeamCode(int offenseTeamCode) {
        this.offenseTeamCode = offenseTeamCode;
    }

    /**
     * @return the defenseTeamCode
     */
    public int getDefenseTeamCode() {
        return defenseTeamCode;
    }

    /**
     * @param defenseTeamCode the defenseTeamCode to set
     */
    public void setDefenseTeamCode(int defenseTeamCode) {
        this.defenseTeamCode = defenseTeamCode;
    }

    /**
     * @return the offensePoints
     */
    public int getOffensePoints() {
        return offensePoints;
    }

    /**
     * @param offensePoints the offensePoints to set
     */
    public void setOffensePoints(int offensePoints) {
        this.offensePoints = offensePoints;
    }

    /**
     * @return the defensePoints
     */
    public int getDefensePoints() {
        return defensePoints;
    }

    /**
     * @param defensePoints the defensePoints to set
     */
    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    /**
     * @return the down
     */
    public Down getDown() {
        return down;
    }
    
    public String getDownString(){
        switch (this.down){
            case FIRST:  return "1";
            case SECOND: return "2";
            case THIRD:  return "3";
            case FOURTH: return "4";
            default:     return "";
        }
    }

    /**
     * @param down the down to set
     */
    public void setDown(Down down) {
        this.down = down;
    }

    /**
     * @return the distanceToGo
     */
    public int getDistanceToGo() {
        return distanceToGo;
    }
    
    public String getDistanceToGoString(){
        String down = getDownString();
        if (down.length() > 0){
            return String.valueOf(getDistanceToGo());
        }
        else{
            return "";
        }
    }

    /**
     * @param distanceToGo the distanceToGo to set
     */
    public void setDistanceToGo(int distanceToGo) {
        this.distanceToGo = distanceToGo;
    }

    /**
     * @return the spot
     */
    public int getSpot() {
        return spot;
    }

    /**
     * @param spot the spot to set
     */
    public void setSpot(int spot) {
        this.spot = spot;
    }

    /**
     * @return the type
     */
    public PlayType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(PlayType type) {
        this.type = type;
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
     * @return the driveAtt
     */
    public boolean isDriveAtt() {
        return driveAtt;
    }

    /**
     * @param driveAtt the driveAtt to set
     */
    public void setDriveAtt(boolean driveAtt) {
        this.driveAtt = driveAtt;
    }
    
    public static enum Down{
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        UNKNOWN,
        ;
    }
    public static enum PlayType{
        ATTEMPT,
        FIELD_GOAL,
        KICKOFF,
        PASS,
        PENALTY,
        PUNT,
        RUSH,
        TIMEOUT,
        ;
    }
    
    
    
}
