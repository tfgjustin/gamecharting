/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 *
 * @author ryan.hoes
 */
public class TeamRow implements Comparable{
    private int teamCode;
    private String name;
    private int conferenceCode;

    public static TeamRow makeRow(String line){
        TeamRow row = new TeamRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setTeamCode(Integer.valueOf(tokens[0]).intValue());
        row.setName(tokens[1]);
        row.setConferenceCode(Integer.valueOf(tokens[2]).intValue());
        return row;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof TeamRow){
            TeamRow here = (TeamRow)o;
            return this.teamCode == here.teamCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.teamCode;
        
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof TeamRow){
            return this.hashCode() - ((TeamRow)o).hashCode();
        }
        else{
            return 1;
        }
        
    }
    /**
     * @return the gameCode
     */
    public int getTeamCode() {
        return teamCode;
    }

    /**
     * @param gameCode the gameCode to set
     */
    public void setTeamCode(int teamCode) {
        this.teamCode = teamCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the conferenceCode
     */
    public int getConferenceCode() {
        return conferenceCode;
    }

    /**
     * @param conferenceCode the conferenceCode to set
     */
    public void setConferenceCode(int conferenceCode) {
        this.conferenceCode = conferenceCode;
    }
    
    
}
