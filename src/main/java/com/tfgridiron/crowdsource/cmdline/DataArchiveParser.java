/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tfgridiron.crowdsource.cmdline;

import com.bcsreport.cfbstats.Season;
import com.bcsreport.cfbstats.tables.GameRow;
import com.bcsreport.cfbstats.tables.PassingRow;
import com.bcsreport.cfbstats.tables.PlayKey;
import com.bcsreport.cfbstats.tables.PlayRow;
import com.bcsreport.cfbstats.tables.RushRow;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author ryan.hoes
 */
public class DataArchiveParser {
    
    Season season = null;
  public DataArchiveParser() {
  }

  public void loadData(String baseDirectory) throws IOException {
    // Look for and load all the play.csv, pass.csv, etc, etc files in that directory
      season = Season.makeSeason(new File(baseDirectory));
  }

  public boolean hasGameData(String gameId) {
    // Do we have all the data for this game?
      // this is really checking to see if the game ID exists in Game.csv
      // and if it then found plays matching that game ID in Play.csv
      // checks for game data in other CSVs could be added like Rush and Passing
      if (season == null){
          throw new RuntimeException("Must first load data before looking for game data.");
      }
      return season.getGameTable().hasGame(gameId) && !season.getPlayTable().getPlays(gameId).isEmpty();
  }

  public boolean fetchRowData(String gameId, Integer playId, Map<String, String> outputColumns) {
    // For a given game/play, load the data we've found, storing the output in a mapping from "PBP" sheet column header to the value for that column
    // Return true if we actually have a data for this (game, play) tuple.
      if (season == null){
          throw new RuntimeException("Must first load data before looking for game data.");
      }
      GameRow game = season.getGameTable().getGame(gameId);
      if (game == null){
          return false;
      }
      
      PlayRow play = season.getPlayTable().getPlay(gameId, playId);
      if (play == null){
          return false;
      }
      PlayKey playKey = play.getKey();
                    
                    outputColumns.put("Play",String.valueOf(play.getPlayNum()));
                    outputColumns.put("QTR",String.valueOf(play.getQtr()));
                    if (play.getClock()>0){
                        outputColumns.put("Clock",play.getClockString());
                    }
                    else{
                        outputColumns.put("Clock","");
                    }
                    outputColumns.put("Away",season.getTeamTable().getTeamName(game.getVisitingTeam()));
                    int offenseTeamCode = play.getOffenseTeamCode();
                    if (offenseTeamCode == game.getVisitingTeam()){
                        outputColumns.put("AS",String.valueOf(play.getOffensePoints()));
                    }
                    else{
                        outputColumns.put("AS",String.valueOf(play.getDefensePoints()));
                    }
                    outputColumns.put("Home",season.getTeamTable().getTeamName(game.getHomeTeam()));
                    
                    if (offenseTeamCode == game.getVisitingTeam()){
                        outputColumns.put("HS",String.valueOf(play.getDefensePoints()));
                    }
                    else{
                        outputColumns.put("HS",String.valueOf(play.getOffensePoints()));
                    }
                    outputColumns.put("Offense",season.getTeamTable().getTeamName(offenseTeamCode));
                    PassingRow passRow = season.getPassTable().getRow(playKey);
                    RushRow rushRow = season.getRushTable().getRow(playKey);
                    if (rushRow != null && passRow != null){
                        // a play shouldn't be both a passing play and a rushing play
                        // i suppose I could have created my own exception for this
                        throw new RuntimeException("This should never happen, but it did on Game "+playKey.getGameCode()+", play "+playKey.getPlayNum());
                    }
                    if (passRow != null){
                        outputColumns.put("QB",season.getPlayerTable().getPlayerName(passRow.getPasserPlayerCode()));
                    }
                    else if (rushRow != null && rushRow.isSack()){
                        outputColumns.put("QB",season.getPlayerTable().getPlayerName(rushRow.getPlayerCode()));
                    }
                    else{
                        outputColumns.put("QB","");
                    }
                    outputColumns.put("Down",play.getDownString());
                    outputColumns.put("Dist",play.getDistanceToGoString());
                    outputColumns.put("YdLine",String.valueOf(play.getSpot()));
                    if (rushRow != null && rushRow.isSack()){
                        outputColumns.put("PlayType","SACK");
                    }
                    else{
                        outputColumns.put("PlayType",String.valueOf(play.getType()));
                    }
                    
                    if (rushRow != null){
                        outputColumns.put("Runner / Intended Receiver",season.getPlayerTable().getPlayerName(rushRow.getPlayerCode()));
                    }
                    else if (passRow != null){
                        outputColumns.put("Runner / Intended Receiver",season.getPlayerTable().getPlayerName(passRow.getReceiverPlayerCode()));
                    }
                    else{
                        outputColumns.put("Runner / Intended Receiver","");
                    }
                    if (passRow != null && passRow.isComplete()){
                        outputColumns.put("Yds",String.valueOf(passRow.getYards()));
                    }
                    else if (rushRow != null){
                        outputColumns.put("Yds",String.valueOf(rushRow.getYards()));
                    }
                    else{
                        outputColumns.put("Yds","");
                    }
                    
                    if (passRow != null && passRow.isAttempted()){
                        if (passRow.isComplete()){
                            outputColumns.put("Complete?","Y");
                        }
                        else{
                            outputColumns.put("Complete?","N");
                        }
                    }
                    else{
                        outputColumns.put("Complete?","");
                    }
      return true;
  }
}