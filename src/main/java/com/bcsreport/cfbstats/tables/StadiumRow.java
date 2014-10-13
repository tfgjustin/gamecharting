package com.bcsreport.cfbstats.tables;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ryan.hoes
 */
public class StadiumRow implements Comparable{
    private int stadiumCode;
    private String name;
    private String city;
    private String state;
    private int capacity;
    private int yearOpened;
    private String surface;

    public static StadiumRow makeRow(String line){
        StadiumRow row = new StadiumRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setStadiumCode(Integer.valueOf(tokens[0]).intValue());
        row.setName(tokens[1]);
        row.setCity(tokens[2]);
        row.setState(tokens[3]);
        row.setCapacity(Integer.valueOf(tokens[4]).intValue());
        row.setSurface(tokens[5]);
        row.setYearOpened(Integer.valueOf(tokens[6]).intValue());
        return row;
    }
    @Override
    public boolean equals(Object o){
        if (o instanceof StadiumRow){
            StadiumRow here = (StadiumRow)o;
            return this.stadiumCode == here.stadiumCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.stadiumCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof StadiumRow){
            return this.hashCode() - ((StadiumRow)o).hashCode();
        }
        else{
            return 1;
        }
        
    }
    
    /**
     * @return the stadiumCode
     */
    public int getStadiumCode() {
        return stadiumCode;
    }

    /**
     * @param stadiumCode the stadiumCode to set
     */
    public void setStadiumCode(int stadiumCode) {
        this.stadiumCode = stadiumCode;
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
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the yearOpened
     */
    public int getYearOpened() {
        return yearOpened;
    }

    /**
     * @param yearOpened the yearOpened to set
     */
    public void setYearOpened(int yearOpened) {
        this.yearOpened = yearOpened;
    }

    /**
     * @return the surface
     */
    public String getSurface() {
        return surface;
    }

    /**
     * @param surface the surface to set
     */
    public void setSurface(String surface) {
        this.surface = surface;
    }
    
    
}
