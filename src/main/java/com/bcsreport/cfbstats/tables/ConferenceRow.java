/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 *
 * @author ryan.hoes
 */
public class ConferenceRow implements Comparable{
    private int conferenceCode;
    private String name;
    private Division division;

    public static ConferenceRow makeRow(String line){
        String[] tokens = line.split("[\t,]",-1);
        ConferenceRow row = new ConferenceRow();
        row.setConferenceCode(Integer.valueOf(tokens[0]).intValue());
        row.setName(tokens[1]);
        if (tokens[2].equalsIgnoreCase("FBS")){
            row.setDivision(Division.FBS);
        }
        else if (tokens[2].equalsIgnoreCase("FCS")){
            row.setDivision(Division.FCS);
        }
        else{
            row.setDivision(Division.UNKNOWN);
        }
        return row;
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
     * @return the division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * @param division the division to set
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    
    @Override
    public boolean equals(Object o){
        if (o instanceof ConferenceRow){
            ConferenceRow here = (ConferenceRow)o;
            return this.conferenceCode == here.conferenceCode;
                    
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.conferenceCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof ConferenceRow){
            return this.hashCode() - ((ConferenceRow)o).hashCode();
        }
        else{
            return 1;
        }
        
    }
    
    
    static enum Division{
        FBS,
        FCS,
        DIVII,
        DIVIII,
        NAIA,
        UNKNOWN,
        ;
    }
    
}
