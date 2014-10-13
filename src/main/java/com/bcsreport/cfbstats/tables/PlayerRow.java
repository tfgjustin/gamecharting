/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 *
 * @author ryan.hoes
 */
public class PlayerRow implements Comparable{
    private int playerCode;
    private int teamCode;
    private String lastName;
    private String firstName;
    private String uniformNumber;
    private ClassYear classYear;
    private Position position;
    private int height;
    private int weight;
    private String city;
    private String state;
    private String country;
    private String lastSchool;

    static enum ClassYear{
        SR,
        JR,
        SO,
        FR,
        UNKNOWN,
        ;
        
    }
    
    
    
    @Override
    public boolean equals(Object o){
        if (o instanceof PlayerRow){
            PlayerRow here = (PlayerRow)o;
            return this.teamCode == here.teamCode &&
                    this.playerCode == here.playerCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.teamCode;
        hash = 61 * hash + this.playerCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof PlayerRow){
            return this.hashCode() - ((PlayerRow)o).hashCode();
        }
        else{
            return 1;
        }
        
    }
    
    
    
    public static PlayerRow makeRow(String line){
        return makeRow(line, "[\t,]");
    }
    
    public static PlayerRow makeRow(String line, String delim){
        PlayerRow row = new PlayerRow();
        String[] tokens = line.split(delim,-1);
        
        row.setPlayerCode(Integer.valueOf(tokens[0]).intValue());
        row.setTeamCode(Integer.valueOf(tokens[1]).intValue());
        row.setLastName(tokens[2]);
        row.setFirstName(tokens[3]);
        row.setUniformNumber(tokens[4]);
        if (tokens[5].length() == 0 || tokens[5].equals("")){
            row.setClassYear(ClassYear.UNKNOWN);
        }
        else{
            try{
                row.setClassYear(ClassYear.valueOf(tokens[5]));
            }
            catch (IllegalArgumentException ex1){
                row.setClassYear(ClassYear.UNKNOWN);
            }
            catch (Exception ex){
                throw ex;
            }
        }
        if (tokens[6].length() == 0 || tokens[6].equals("")){
            row.setPosition(Position.UNKNOWN);
        }
        else{
            try{
                row.setPosition(Position.valueOf(tokens[6]));
            }
            catch (IllegalArgumentException ex1){
                row.setPosition(Position.UNKNOWN);
            }
        }
        if (tokens[7].length()<=0){
            row.setHeight(-1);
        }
        else{
            try{
                row.setHeight(Integer.valueOf(tokens[7]).intValue());
            }
            catch (NumberFormatException ex){
                row.setHeight(-1);
                System.out.println(line);
            }
        }
        if (tokens[8].length() <= 0){
            row.setWeight(-1);
        }
        else{
            try{
                row.setWeight(Integer.valueOf(tokens[8]).intValue());
            }
            catch (NumberFormatException ex){
                row.setWeight(-1);
                System.out.println(line);
            }
        }
        row.setCity(tokens[9]);
        row.setState(tokens[10]);
        row.setCountry(tokens[11]);
        row.setLastSchool(tokens[12]);
        return row;
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
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the uniformNumber
     */
    public String getUniformNumber() {
        return uniformNumber;
    }

    /**
     * @param uniformNumber the uniformNumber to set
     */
    public void setUniformNumber(String uniformNumber) {
        this.uniformNumber = uniformNumber;
    }

    /**
     * @return the classYear
     */
    public ClassYear getClassYear() {
        return classYear;
    }

    /**
     * @param classYear the classYear to set
     */
    public void setClassYear(ClassYear classYear) {
        this.classYear = classYear;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
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
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the lastSchool
     */
    public String getLastSchool() {
        return lastSchool;
    }

    /**
     * @param lastSchool the lastSchool to set
     */
    public void setLastSchool(String lastSchool) {
        this.lastSchool = lastSchool;
    }
    
    static enum Position{
        ATH,
        C,
        CB,
        DB,
        DE,
        DL,
        DS,
        DT,
        FB,
        FL,
        FS,
        HB,
        HOLD,
        ILB,
        K,
        LB,
        LS,
        MLB,
        NG,
        NT,
        OG,
        OL,
        OLB,
        OT,
        P,
        PK,
        QB,
        RB,
        ROV,
        S,
        SB,
        SE,
        SN,
        SS,
        TB,
        TE,
        WR,
        TEAM,
        UNKNOWN,
        ;
        
    }
    
    
}
