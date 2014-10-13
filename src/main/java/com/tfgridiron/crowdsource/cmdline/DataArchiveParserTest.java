/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tfgridiron.crowdsource.cmdline;

import com.bcsreport.cfbstats.Season;
import com.bcsreport.cfbstats.tables.GameRow;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ryan.hoes
 */
public class DataArchiveParserTest {
    
    public static void main(String[] args) throws IOException{
        List<String> columns = new ArrayList<String>();
        columns.add("Play");
        columns.add("QTR");
        columns.add("Clock");
        columns.add("Away");
        columns.add("AS");
        columns.add("Home");
        columns.add("HS");
        columns.add("Offense");
        columns.add("QB");	
        columns.add("Down");
        columns.add("Dist");
        columns.add("YdLine");
        columns.add("PlayType");
        columns.add("Runner / Intended Receiver");
        columns.add("Yds");
        columns.add("Complete?");
        
        DataArchiveParser parser = new DataArchiveParser();
        parser.loadData("my directory");
        Season season = Season.makeSeason(new File("my directory"));
        List<GameRow> games = season.getGameTable().getGameList();
        DateFormat formatter = new SimpleDateFormat("YYYYMMdd");
        
        for (GameRow game : games){
            if (parser.hasGameData(game.getGameCode())){
                String gameDate = formatter.format(game.getDate());
                String awayTeamName = season.getTeamTable().getTeamName(game.getVisitingTeam()).replaceAll("\"","");
                String homeTeamName = season.getTeamTable().getTeamName(game.getHomeTeam()).replaceAll("\"","");
                String fileName = gameDate+"_"+awayTeamName+" vs "+homeTeamName+" (GID_"+game.getGameCode()+").csv";
                PrintWriter print = new PrintWriter(new FileWriter("my test directory\\"+fileName));
                boolean keepGoing = true;
                int playNum = 1;
                boolean first = true;
                for (String col : columns){
                        if (first){
                            first = false;
                            print.print(col);        
                        }
                        else{
                            print.print(","+col);
                        }
                    }
                print.println();
                while (keepGoing){
                    Map<String,String> playData = new HashMap<String,String>();
                    keepGoing = parser.fetchRowData(game.getGameCode(), playNum, playData);
                    if (keepGoing){
                        first = true;
                        for (String col : columns){
                            String value = playData.get(col);
                            if (value == null){
                                throw new RuntimeException("Couldn't find "+col);
                            }
                            if (first){
                                first = false;
                                print.print(playData.get(col));        
                            }
                            else{
                                print.print(","+playData.get(col));
                            }
                        }
                        print.println();
                    }
                    playNum++;
                }
                print.close();
            }
        }
        
        
    }
    
    

}
