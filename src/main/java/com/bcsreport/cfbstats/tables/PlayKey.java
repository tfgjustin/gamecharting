/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.util.Objects;

/**
 *
 * @author ryan.hoes
 */
public class PlayKey implements Comparable {
    
        private String gameCode;
        private Integer playNum;
        
        public PlayKey(String game, Integer playNum){
            this.gameCode = game;
            this.playNum = playNum;
        }

    /**
     * @return the gameCode
     */
    public String getGameCode() {
        return gameCode;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayKey other = (PlayKey) obj;
        if (!Objects.equals(this.gameCode, other.gameCode)) {
            return false;
        }
        if (!Objects.equals(this.playNum, other.playNum)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.gameCode);
        hash = 11 * hash + Objects.hashCode(this.playNum);
        return hash;
    }
    /**
     * @return the playNum
     */
    public Integer getPlayNum() {
        return playNum;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PlayKey){
            return this.hashCode() - ((PlayKey)o).hashCode();
        }
        else{
            return -1;
        }
    }
    
}
