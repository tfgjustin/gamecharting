/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

/**
 *
 * @author ryan.hoes
 */
public class PlayerGameStatsRow implements Comparable{
    private int playerCode;
    private String gameCode;
    private int numRushAttempts;
    private int rushYards;
    private int numRushTouchdown;
    private int numPassAttempts;
    private int numPassCompletions;
    private int passYards;
    private int numPassTouchdown;
    private int numInterception;
    private int num2XPPassConv;
    private int numKickoffRet;
    private int kickoffRetYards;
    private int numKickoffRetTouchdown;
    private int numPuntRet;
    private int puntRetYards;
    private int numPuntRetTouchdown;
    private int numFumbleRet;
    private int fumbleRetYards;
    private int numFumbleRetTouchdown;
    private int numINTRet;
    private int INTRetYards;
    private int numINTRetTouchdown;
    private int numMiscRet;
    private int miscRetYards;
    private int numMiscRetTouchdown;
    private int numFGAtt;
    private int numFGMade;
    private int numDef2XPAtt;
    private int numDef2XPMade;
    private int numOffXPAtt;
    private int numOffXPMade;
    private int numOff2XPAtt;
    private int numOff2XPMade;
    private int numSafety;
    private int numPunts;
    private int puntYards;
    private int numKickoffs;
    private int kickoffYards;
    private int numKickoffTouchbacks;
    private int numKickoffOutOfBounds;
    private int numKickoffOnside;
    private int numFumbles;
    private int numFumblesLost;
    private int numFumblesForced;
    private int numTacklesSolo;
    private int numTacklesAssist;
    private double numTacklesForLoss;
    private int tacklesForLossYards;
    private double numSack;
    private int sackYards;
    private int numQBHurries;
    private int numPassBrokenUp;
    private int numKickPuntBlocked;
    private int numReceptions;
    private int receptionYards;
    private int numReceptionTouchdown;
    private int points;

    
    public static PlayerGameStatsRow makeRow(String line){
        PlayerGameStatsRow row = new PlayerGameStatsRow();
        String[] tokens = line.split("[\t,]",-1);
        row.setPlayerCode(Integer.valueOf(tokens[0]).intValue());
        row.setGameCode(tokens[1]);
        row.setNumRushAttempts(Integer.valueOf(tokens[2]).intValue());
        row.setRushYards(Integer.valueOf(tokens[3]).intValue());
        row.setNumRushTouchdown(Integer.valueOf(tokens[4]).intValue());
        row.setNumPassAttempts(Integer.valueOf(tokens[5]).intValue());
        row.setNumPassCompletions(Integer.valueOf(tokens[6]).intValue());
        row.setPassYards(Integer.valueOf(tokens[7]).intValue());
        row.setNumPassTouchdown(Integer.valueOf(tokens[8]).intValue());
        row.setNumInterception(Integer.valueOf(tokens[9]).intValue());
        row.setNum2XPPassConv(Integer.valueOf(tokens[10]).intValue());
        row.setNumReceptions(Integer.valueOf(tokens[11]).intValue());
        row.setReceptionYards(Integer.valueOf(tokens[12]).intValue());
        row.setNumReceptionTouchdown(Integer.valueOf(tokens[13]).intValue());
        
        row.setNumKickoffRet(Integer.valueOf(tokens[14]).intValue());
        row.setKickoffRetYards(Integer.valueOf(tokens[15]).intValue());
        row.setNumKickoffRetTouchdown(Integer.valueOf(tokens[16]).intValue());
        row.setNumPuntRet(Integer.valueOf(tokens[17]).intValue());
        row.setPuntRetYards(Integer.valueOf(tokens[18]).intValue());
        row.setNumPuntRetTouchdown(Integer.valueOf(tokens[19]).intValue());
        row.setNumFumbleRet(Integer.valueOf(tokens[20]).intValue());
        row.setFumbleRetYards(Integer.valueOf(tokens[21]).intValue());
        row.setNumFumbleRetTouchdown(Integer.valueOf(tokens[22]).intValue());
        row.setNumINTRet(Integer.valueOf(tokens[23]).intValue());
        row.setINTRetYards(Integer.valueOf(tokens[24]).intValue());
        row.setNumINTRetTouchdown(Integer.valueOf(tokens[25]).intValue());
        row.setNumMiscRet(Integer.valueOf(tokens[26]).intValue());
        row.setMiscRetYards(Integer.valueOf(tokens[27]).intValue());
        row.setNumMiscRetTouchdown(Integer.valueOf(tokens[28]).intValue());
        row.setNumFGAtt(Integer.valueOf(tokens[29]).intValue());
        row.setNumFGMade(Integer.valueOf(tokens[30]).intValue());
        row.setNumOffXPAtt(Integer.valueOf(tokens[31]).intValue());
        row.setNumOffXPMade(Integer.valueOf(tokens[32]).intValue());
        row.setNumOff2XPAtt(Integer.valueOf(tokens[33]).intValue());
        row.setNumOff2XPMade(Integer.valueOf(tokens[34]).intValue());
        row.setNumDef2XPAtt(Integer.valueOf(tokens[35]).intValue());
        row.setNumDef2XPMade(Integer.valueOf(tokens[36]).intValue());
        row.setNumSafety(Integer.valueOf(tokens[37]).intValue());
        row.setPoints(Integer.valueOf(tokens[38]).intValue());
        row.setNumPunts(Integer.valueOf(tokens[39]).intValue());
        row.setPuntYards(Integer.valueOf(tokens[40]).intValue());
        row.setNumKickoffs(Integer.valueOf(tokens[41]).intValue());
        row.setKickoffYards(Integer.valueOf(tokens[42]).intValue());
        row.setNumKickoffTouchbacks(Integer.valueOf(tokens[43]).intValue());
        row.setNumKickoffOutOfBounds(Integer.valueOf(tokens[44]).intValue());
        row.setNumKickoffOnside(Integer.valueOf(tokens[45]).intValue());
        row.setNumFumbles(Integer.valueOf(tokens[46]).intValue());
        row.setNumFumblesLost(Integer.valueOf(tokens[47]).intValue());
        row.setNumTacklesSolo(Integer.valueOf(tokens[48]).intValue());
        row.setNumTacklesAssist(Integer.valueOf(tokens[49]).intValue());
        row.setNumTacklesForLoss(Double.valueOf(tokens[50]).doubleValue());
        row.setTacklesForLossYards(Integer.valueOf(tokens[51]).intValue());
        row.setNumSack(Double.valueOf(tokens[52]).doubleValue());
        row.setSackYards(Integer.valueOf(tokens[53]).intValue());
        row.setNumQBHurries(Integer.valueOf(tokens[54]).intValue());
        row.setNumFumblesForced(Integer.valueOf(tokens[55]).intValue());
        row.setNumPassBrokenUp(Integer.valueOf(tokens[56]).intValue());
        row.setNumKickPuntBlocked(Integer.valueOf(tokens[57]).intValue());
        return row;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof PlayerGameStatsRow){
            PlayerGameStatsRow here = (PlayerGameStatsRow)o;
            return this.gameCode.equals(here.gameCode) &&
                    this.playerCode == here.playerCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.gameCode.hashCode();
        hash = 61 * hash + this.playerCode;
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof PlayerGameStatsRow){
            return this.hashCode() - ((PlayerGameStatsRow)o).hashCode();
        }
        else{
            return 1;
        }
        
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
     * @return the numRushAttempts
     */
    public int getNumRushAttempts() {
        return numRushAttempts;
    }

    /**
     * @param numRushAttempts the numRushAttempts to set
     */
    public void setNumRushAttempts(int numRushAttempts) {
        this.numRushAttempts = numRushAttempts;
    }

    /**
     * @return the rushYards
     */
    public int getRushYards() {
        return rushYards;
    }

    /**
     * @param rushYards the rushYards to set
     */
    public void setRushYards(int rushYards) {
        this.rushYards = rushYards;
    }

    /**
     * @return the numRushTouchdown
     */
    public int getNumRushTouchdown() {
        return numRushTouchdown;
    }

    /**
     * @param numRushTouchdown the numRushTouchdown to set
     */
    public void setNumRushTouchdown(int numRushTouchdown) {
        this.numRushTouchdown = numRushTouchdown;
    }

    /**
     * @return the numPassAttempts
     */
    public int getNumPassAttempts() {
        return numPassAttempts;
    }

    /**
     * @param numPassAttempts the numPassAttempts to set
     */
    public void setNumPassAttempts(int numPassAttempts) {
        this.numPassAttempts = numPassAttempts;
    }

    /**
     * @return the numPassCompletions
     */
    public int getNumPassCompletions() {
        return numPassCompletions;
    }

    /**
     * @param numPassCompletions the numPassCompletions to set
     */
    public void setNumPassCompletions(int numPassCompletions) {
        this.numPassCompletions = numPassCompletions;
    }

    /**
     * @return the passYards
     */
    public int getPassYards() {
        return passYards;
    }

    /**
     * @param passYards the passYards to set
     */
    public void setPassYards(int passYards) {
        this.passYards = passYards;
    }

    /**
     * @return the numPassTouchdown
     */
    public int getNumPassTouchdown() {
        return numPassTouchdown;
    }

    /**
     * @param numPassTouchdown the numPassTouchdown to set
     */
    public void setNumPassTouchdown(int numPassTouchdown) {
        this.numPassTouchdown = numPassTouchdown;
    }

    /**
     * @return the numInterception
     */
    public int getNumInterception() {
        return numInterception;
    }

    /**
     * @param numInterception the numInterception to set
     */
    public void setNumInterception(int numInterception) {
        this.numInterception = numInterception;
    }

    /**
     * @return the num2XPPassConv
     */
    public int getNum2XPPassConv() {
        return num2XPPassConv;
    }

    /**
     * @param num2XPPassConv the num2XPPassConv to set
     */
    public void setNum2XPPassConv(int num2XPPassConv) {
        this.num2XPPassConv = num2XPPassConv;
    }

    /**
     * @return the numKickoffRet
     */
    public int getNumKickoffRet() {
        return numKickoffRet;
    }

    /**
     * @param numKickoffRet the numKickoffRet to set
     */
    public void setNumKickoffRet(int numKickoffRet) {
        this.numKickoffRet = numKickoffRet;
    }

    /**
     * @return the kickoffRetYards
     */
    public int getKickoffRetYards() {
        return kickoffRetYards;
    }

    /**
     * @param kickoffRetYards the kickoffRetYards to set
     */
    public void setKickoffRetYards(int kickoffRetYards) {
        this.kickoffRetYards = kickoffRetYards;
    }

    /**
     * @return the numKickoffRetTouchdown
     */
    public int getNumKickoffRetTouchdown() {
        return numKickoffRetTouchdown;
    }

    /**
     * @param numKickoffRetTouchdown the numKickoffRetTouchdown to set
     */
    public void setNumKickoffRetTouchdown(int numKickoffRetTouchdown) {
        this.numKickoffRetTouchdown = numKickoffRetTouchdown;
    }

    /**
     * @return the numPuntRet
     */
    public int getNumPuntRet() {
        return numPuntRet;
    }

    /**
     * @param numPuntRet the numPuntRet to set
     */
    public void setNumPuntRet(int numPuntRet) {
        this.numPuntRet = numPuntRet;
    }

    /**
     * @return the puntRetYards
     */
    public int getPuntRetYards() {
        return puntRetYards;
    }

    /**
     * @param puntRetYards the puntRetYards to set
     */
    public void setPuntRetYards(int puntRetYards) {
        this.puntRetYards = puntRetYards;
    }

    /**
     * @return the numPuntRetTouchdown
     */
    public int getNumPuntRetTouchdown() {
        return numPuntRetTouchdown;
    }

    /**
     * @param numPuntRetTouchdown the numPuntRetTouchdown to set
     */
    public void setNumPuntRetTouchdown(int numPuntRetTouchdown) {
        this.numPuntRetTouchdown = numPuntRetTouchdown;
    }

    /**
     * @return the numFumbleRet
     */
    public int getNumFumbleRet() {
        return numFumbleRet;
    }

    /**
     * @param numFumbleRet the numFumbleRet to set
     */
    public void setNumFumbleRet(int numFumbleRet) {
        this.numFumbleRet = numFumbleRet;
    }

    /**
     * @return the fumbleRetYards
     */
    public int getFumbleRetYards() {
        return fumbleRetYards;
    }

    /**
     * @param fumbleRetYards the fumbleRetYards to set
     */
    public void setFumbleRetYards(int fumbleRetYards) {
        this.fumbleRetYards = fumbleRetYards;
    }

    /**
     * @return the numFumbleRetTouchdown
     */
    public int getNumFumbleRetTouchdown() {
        return numFumbleRetTouchdown;
    }

    /**
     * @param numFumbleRetTouchdown the numFumbleRetTouchdown to set
     */
    public void setNumFumbleRetTouchdown(int numFumbleRetTouchdown) {
        this.numFumbleRetTouchdown = numFumbleRetTouchdown;
    }

    /**
     * @return the numINTRet
     */
    public int getNumINTRet() {
        return numINTRet;
    }

    /**
     * @param numINTRet the numINTRet to set
     */
    public void setNumINTRet(int numINTRet) {
        this.numINTRet = numINTRet;
    }

    /**
     * @return the INTRetYards
     */
    public int getINTRetYards() {
        return INTRetYards;
    }

    /**
     * @param INTRetYards the INTRetYards to set
     */
    public void setINTRetYards(int INTRetYards) {
        this.INTRetYards = INTRetYards;
    }

    /**
     * @return the numINTRetTouchdown
     */
    public int getNumINTRetTouchdown() {
        return numINTRetTouchdown;
    }

    /**
     * @param numINTRetTouchdown the numINTRetTouchdown to set
     */
    public void setNumINTRetTouchdown(int numINTRetTouchdown) {
        this.numINTRetTouchdown = numINTRetTouchdown;
    }

    /**
     * @return the numMiscRet
     */
    public int getNumMiscRet() {
        return numMiscRet;
    }

    /**
     * @param numMiscRet the numMiscRet to set
     */
    public void setNumMiscRet(int numMiscRet) {
        this.numMiscRet = numMiscRet;
    }

    /**
     * @return the miscRetYards
     */
    public int getMiscRetYards() {
        return miscRetYards;
    }

    /**
     * @param miscRetYards the miscRetYards to set
     */
    public void setMiscRetYards(int miscRetYards) {
        this.miscRetYards = miscRetYards;
    }

    /**
     * @return the numMiscRetTouchdown
     */
    public int getNumMiscRetTouchdown() {
        return numMiscRetTouchdown;
    }

    /**
     * @param numMiscRetTouchdown the numMiscRetTouchdown to set
     */
    public void setNumMiscRetTouchdown(int numMiscRetTouchdown) {
        this.numMiscRetTouchdown = numMiscRetTouchdown;
    }

    /**
     * @return the numFGAtt
     */
    public int getNumFGAtt() {
        return numFGAtt;
    }

    /**
     * @param numFGAtt the numFGAtt to set
     */
    public void setNumFGAtt(int numFGAtt) {
        this.numFGAtt = numFGAtt;
    }

    /**
     * @return the numFGMade
     */
    public int getNumFGMade() {
        return numFGMade;
    }

    /**
     * @param numFGMade the numFGMade to set
     */
    public void setNumFGMade(int numFGMade) {
        this.numFGMade = numFGMade;
    }

    /**
     * @return the numDef2XPAtt
     */
    public int getNumDef2XPAtt() {
        return numDef2XPAtt;
    }

    /**
     * @param numDef2XPAtt the numDef2XPAtt to set
     */
    public void setNumDef2XPAtt(int numDef2XPAtt) {
        this.numDef2XPAtt = numDef2XPAtt;
    }

    /**
     * @return the numDef2XPMade
     */
    public int getNumDef2XPMade() {
        return numDef2XPMade;
    }

    /**
     * @param numDef2XPMade the numDef2XPMade to set
     */
    public void setNumDef2XPMade(int numDef2XPMade) {
        this.numDef2XPMade = numDef2XPMade;
    }

    /**
     * @return the numOffXPAtt
     */
    public int getNumOffXPAtt() {
        return numOffXPAtt;
    }

    /**
     * @param numOffXPAtt the numOffXPAtt to set
     */
    public void setNumOffXPAtt(int numOffXPAtt) {
        this.numOffXPAtt = numOffXPAtt;
    }

    /**
     * @return the numOffXPMade
     */
    public int getNumOffXPMade() {
        return numOffXPMade;
    }

    /**
     * @param numOffXPMade the numOffXPMade to set
     */
    public void setNumOffXPMade(int numOffXPMade) {
        this.numOffXPMade = numOffXPMade;
    }

    /**
     * @return the numOff2XPAtt
     */
    public int getNumOff2XPAtt() {
        return numOff2XPAtt;
    }

    /**
     * @param numOff2XPAtt the numOff2XPAtt to set
     */
    public void setNumOff2XPAtt(int numOff2XPAtt) {
        this.numOff2XPAtt = numOff2XPAtt;
    }

    /**
     * @return the numOff2XPMade
     */
    public int getNumOff2XPMade() {
        return numOff2XPMade;
    }

    /**
     * @param numOff2XPMade the numOff2XPMade to set
     */
    public void setNumOff2XPMade(int numOff2XPMade) {
        this.numOff2XPMade = numOff2XPMade;
    }

    /**
     * @return the numSafety
     */
    public int getNumSafety() {
        return numSafety;
    }

    /**
     * @param numSafety the numSafety to set
     */
    public void setNumSafety(int numSafety) {
        this.numSafety = numSafety;
    }

    /**
     * @return the numPunts
     */
    public int getNumPunts() {
        return numPunts;
    }

    /**
     * @param numPunts the numPunts to set
     */
    public void setNumPunts(int numPunts) {
        this.numPunts = numPunts;
    }

    /**
     * @return the puntYards
     */
    public int getPuntYards() {
        return puntYards;
    }

    /**
     * @param puntYards the puntYards to set
     */
    public void setPuntYards(int puntYards) {
        this.puntYards = puntYards;
    }

    /**
     * @return the numKickoffs
     */
    public int getNumKickoffs() {
        return numKickoffs;
    }

    /**
     * @param numKickoffs the numKickoffs to set
     */
    public void setNumKickoffs(int numKickoffs) {
        this.numKickoffs = numKickoffs;
    }

    /**
     * @return the kickoffYards
     */
    public int getKickoffYards() {
        return kickoffYards;
    }

    /**
     * @param kickoffYards the kickoffYards to set
     */
    public void setKickoffYards(int kickoffYards) {
        this.kickoffYards = kickoffYards;
    }

    /**
     * @return the numKickoffTouchbacks
     */
    public int getNumKickoffTouchbacks() {
        return numKickoffTouchbacks;
    }

    /**
     * @param numKickoffTouchbacks the numKickoffTouchbacks to set
     */
    public void setNumKickoffTouchbacks(int numKickoffTouchbacks) {
        this.numKickoffTouchbacks = numKickoffTouchbacks;
    }

    /**
     * @return the numKickoffOutOfBounds
     */
    public int getNumKickoffOutOfBounds() {
        return numKickoffOutOfBounds;
    }

    /**
     * @param numKickoffOutOfBounds the numKickoffOutOfBounds to set
     */
    public void setNumKickoffOutOfBounds(int numKickoffOutOfBounds) {
        this.numKickoffOutOfBounds = numKickoffOutOfBounds;
    }

    /**
     * @return the numKickoffOnside
     */
    public int getNumKickoffOnside() {
        return numKickoffOnside;
    }

    /**
     * @param numKickoffOnside the numKickoffOnside to set
     */
    public void setNumKickoffOnside(int numKickoffOnside) {
        this.numKickoffOnside = numKickoffOnside;
    }

    /**
     * @return the numFumbles
     */
    public int getNumFumbles() {
        return numFumbles;
    }

    /**
     * @param numFumbles the numFumbles to set
     */
    public void setNumFumbles(int numFumbles) {
        this.numFumbles = numFumbles;
    }

    /**
     * @return the numFumblesLost
     */
    public int getNumFumblesLost() {
        return numFumblesLost;
    }

    /**
     * @param numFumblesLost the numFumblesLost to set
     */
    public void setNumFumblesLost(int numFumblesLost) {
        this.numFumblesLost = numFumblesLost;
    }

    /**
     * @return the numFumblesForced
     */
    public int getNumFumblesForced() {
        return numFumblesForced;
    }

    /**
     * @param numFumblesForced the numFumblesForced to set
     */
    public void setNumFumblesForced(int numFumblesForced) {
        this.numFumblesForced = numFumblesForced;
    }

    /**
     * @return the numTacklesSolo
     */
    public int getNumTacklesSolo() {
        return numTacklesSolo;
    }

    /**
     * @param numTacklesSolo the numTacklesSolo to set
     */
    public void setNumTacklesSolo(int numTacklesSolo) {
        this.numTacklesSolo = numTacklesSolo;
    }

    /**
     * @return the numTacklesAssist
     */
    public int getNumTacklesAssist() {
        return numTacklesAssist;
    }

    /**
     * @param numTacklesAssist the numTacklesAssist to set
     */
    public void setNumTacklesAssist(int numTacklesAssist) {
        this.numTacklesAssist = numTacklesAssist;
    }

    /**
     * @return the numTacklesForLoss
     */
    public double getNumTacklesForLoss() {
        return numTacklesForLoss;
    }

    /**
     * @param numTacklesForLoss the numTacklesForLoss to set
     */
    public void setNumTacklesForLoss(double numTacklesForLoss) {
        this.numTacklesForLoss = numTacklesForLoss;
    }

    /**
     * @return the tacklesForLossYards
     */
    public int getTacklesForLossYards() {
        return tacklesForLossYards;
    }

    /**
     * @param tacklesForLossYards the tacklesForLossYards to set
     */
    public void setTacklesForLossYards(int tacklesForLossYards) {
        this.tacklesForLossYards = tacklesForLossYards;
    }

    /**
     * @return the numSack
     */
    public double getNumSack() {
        return numSack;
    }

    /**
     * @param numSack the numSack to set
     */
    public void setNumSack(double numSack) {
        this.numSack = numSack;
    }

    /**
     * @return the sackYards
     */
    public int getSackYards() {
        return sackYards;
    }

    /**
     * @param sackYards the sackYards to set
     */
    public void setSackYards(int sackYards) {
        this.sackYards = sackYards;
    }

    /**
     * @return the numQBHurries
     */
    public int getNumQBHurries() {
        return numQBHurries;
    }

    /**
     * @param numQBHurries the numQBHurries to set
     */
    public void setNumQBHurries(int numQBHurries) {
        this.numQBHurries = numQBHurries;
    }

    /**
     * @return the numPassBrokenUp
     */
    public int getNumPassBrokenUp() {
        return numPassBrokenUp;
    }

    /**
     * @param numPassBrokenUp the numPassBrokenUp to set
     */
    public void setNumPassBrokenUp(int numPassBrokenUp) {
        this.numPassBrokenUp = numPassBrokenUp;
    }

    /**
     * @return the numKickPuntBlocked
     */
    public int getNumKickPuntBlocked() {
        return numKickPuntBlocked;
    }

    /**
     * @param numKickPuntBlocked the numKickPuntBlocked to set
     */
    public void setNumKickPuntBlocked(int numKickPuntBlocked) {
        this.numKickPuntBlocked = numKickPuntBlocked;
    }

    /**
     * @return the numReceptions
     */
    public int getNumReceptions() {
        return numReceptions;
    }

    /**
     * @param numReceptions the numReceptions to set
     */
    public void setNumReceptions(int numReceptions) {
        this.numReceptions = numReceptions;
    }

    /**
     * @return the receptionYards
     */
    public int getReceptionYards() {
        return receptionYards;
    }

    /**
     * @param receptionYards the receptionYards to set
     */
    public void setReceptionYards(int receptionYards) {
        this.receptionYards = receptionYards;
    }

    /**
     * @return the numReceptionTouchdown
     */
    public int getNumReceptionTouchdown() {
        return numReceptionTouchdown;
    }

    /**
     * @param numReceptionTouchdown the numReceptionTouchdown to set
     */
    public void setNumReceptionTouchdown(int numReceptionTouchdown) {
        this.numReceptionTouchdown = numReceptionTouchdown;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
    
    
}
