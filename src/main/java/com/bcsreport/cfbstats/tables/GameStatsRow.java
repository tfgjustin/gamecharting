/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 *
 * @author ryan.hoes
 */
public class GameStatsRow implements Comparable {
    private String gameCode;
    private int attendance;
    private int duration;

    public static GameStatsRow makeRow(String line){
        GameStatsRow row = new GameStatsRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setGameCode(tokens[0]);
        if (tokens[1].length() > 0){
            row.setAttendance(Integer.valueOf(tokens[1]).intValue());
        }
        else{
            row.setAttendance(-1);
        }
        if (tokens[2].length() >0){
            row.setDuration(Integer.valueOf(tokens[2]).intValue());
        }
        else{
            row.setDuration(-1);
        }
        return row;
    }
    
    
    @Override
    public boolean equals(Object o){
        if (o instanceof GameStatsRow){
            GameStatsRow here = (GameStatsRow)o;
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
        if (o instanceof GameStatsRow){
            return this.hashCode() - ((GameStatsRow)o).hashCode();
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
     * @return the attendance
     */
    public int getAttendance() {
        return attendance;
    }

    /**
     * @param attendance the attendance to set
     */
    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    
}
