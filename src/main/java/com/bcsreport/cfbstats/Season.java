/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats;

import com.bcsreport.cfbstats.tables.ConferenceTable;
import com.bcsreport.cfbstats.tables.DriveTable;
import com.bcsreport.cfbstats.tables.GameRow;
import com.bcsreport.cfbstats.tables.GameStatsTable;
import com.bcsreport.cfbstats.tables.GameTable;
import com.bcsreport.cfbstats.tables.KickoffReturnTable;
import com.bcsreport.cfbstats.tables.KickoffTable;
import com.bcsreport.cfbstats.tables.PassingRow;
import com.bcsreport.cfbstats.tables.PassingTable;
import com.bcsreport.cfbstats.tables.PlayKey;
import com.bcsreport.cfbstats.tables.PlayRow;
import com.bcsreport.cfbstats.tables.PlayTable;
import com.bcsreport.cfbstats.tables.PlayerGameStatsTable;
import com.bcsreport.cfbstats.tables.PlayerTable;
import com.bcsreport.cfbstats.tables.PuntReturnTable;
import com.bcsreport.cfbstats.tables.PuntTable;
import com.bcsreport.cfbstats.tables.ReceptionTable;
import com.bcsreport.cfbstats.tables.RushRow;
import com.bcsreport.cfbstats.tables.RushTable;
import com.bcsreport.cfbstats.tables.StadiumTable;
import com.bcsreport.cfbstats.tables.TeamGameStatsTable;
import com.bcsreport.cfbstats.tables.TeamTable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ryan.hoes
 */
public class Season {
    
    private ConferenceTable confTable;
    private DriveTable driveTable;
    private GameStatsTable gameStatsTable;
    private GameTable gameTable;
    private KickoffReturnTable kickReturnTable;
    private KickoffTable kickTable;
    private PassingTable passTable;
    private PlayerGameStatsTable playerGameStatsTable;
    private PlayTable playTable;
    private PlayerTable playerTable;
    private PuntReturnTable puntReturnTable;
    private PuntTable puntTable;
    private ReceptionTable receptionTable;
    private RushTable rushTable;
    private StadiumTable stadiumTable;
    private TeamGameStatsTable teamGameStatsTable;
    private TeamTable teamTable;
    
    public static Season makeSeason(File dir) throws IOException{
        Season season = new Season();
        season.setConfTable(ConferenceTable.loadTable(new File(dir,"conference.csv")));
        season.setDriveTable(DriveTable.loadTable(new File(dir,"drive.csv")));
        
        season.setGameStatsTable(GameStatsTable.loadTable(new File(dir,"game-statistics.csv")));
        season.setGameTable(GameTable.loadTable(new File(dir,"game.csv")));
        season.setKickReturnTable(KickoffReturnTable.loadTable(new File(dir,"kickoff-return.csv")));
        season.setKickTable(KickoffTable.loadTable(new File(dir,"kickoff.csv")));
       
        season.setPassTable(PassingTable.loadTable(new File(dir,"pass.csv")));
        season.setPlayTable(PlayTable.loadTable(new File(dir,"play.csv")));
        season.setPlayerGameStatsTable(PlayerGameStatsTable.loadTable(new File(dir,"player-game-statistics.csv")));
        // using different delimiter to handle commas in players names/high schools/etc
        season.setPlayerTable(PlayerTable.loadTable(new File(dir,"player.csv"),",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
        season.setPuntReturnTable(PuntReturnTable.loadTable(new File(dir,"punt-return.csv")));
        season.setPuntTable(PuntTable.loadTable(new File(dir,"punt.csv")));
        season.setReceptionTable(ReceptionTable.loadTable(new File(dir,"reception.csv")));
        season.setRushTable(RushTable.loadTable(new File(dir,"rush.csv")));
        season.setStadiumTable(StadiumTable.loadTable(new File(dir,"stadium.csv")));
        season.setTeamGameStatsTable(TeamGameStatsTable.loadTable(new File(dir,"team-game-statistics.csv")));
        season.setTeamTable(TeamTable.loadTable(new File(dir,"team.csv")));
        return season;
    }
    
    
    /**
     * @return the confTable
     */
    public ConferenceTable getConfTable() {
        return confTable;
    }

    /**
     * @param confTable the confTable to set
     */
    public void setConfTable(ConferenceTable confTable) {
        this.confTable = confTable;
    }

    /**
     * @return the driveTable
     */
    public DriveTable getDriveTable() {
        return driveTable;
    }

    /**
     * @param driveTable the driveTable to set
     */
    public void setDriveTable(DriveTable driveTable) {
        this.driveTable = driveTable;
    }

    /**
     * @return the gameStatsTable
     */
    public GameStatsTable getGameStatsTable() {
        return gameStatsTable;
    }

    /**
     * @param gameStatsTable the gameStatsTable to set
     */
    public void setGameStatsTable(GameStatsTable gameStatsTable) {
        this.gameStatsTable = gameStatsTable;
    }

    /**
     * @return the gameTable
     */
    public GameTable getGameTable() {
        return gameTable;
    }

    /**
     * @param gameTable the gameTable to set
     */
    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    /**
     * @return the kickReturnTable
     */
    public KickoffReturnTable getKickReturnTable() {
        return kickReturnTable;
    }

    /**
     * @param kickReturnTable the kickReturnTable to set
     */
    public void setKickReturnTable(KickoffReturnTable kickReturnTable) {
        this.kickReturnTable = kickReturnTable;
    }

    /**
     * @return the kickTable
     */
    public KickoffTable getKickTable() {
        return kickTable;
    }

    /**
     * @param kickTable the kickTable to set
     */
    public void setKickTable(KickoffTable kickTable) {
        this.kickTable = kickTable;
    }

    /**
     * @return the passTable
     */
    public PassingTable getPassTable() {
        return passTable;
    }

    /**
     * @param passTable the passTable to set
     */
    public void setPassTable(PassingTable passTable) {
        this.passTable = passTable;
    }

    /**
     * @return the playerGameStatsTable
     */
    public PlayerGameStatsTable getPlayerGameStatsTable() {
        return playerGameStatsTable;
    }

    /**
     * @param playerGameStatsTable the playerGameStatsTable to set
     */
    public void setPlayerGameStatsTable(PlayerGameStatsTable playerGameStatsTable) {
        this.playerGameStatsTable = playerGameStatsTable;
    }

    /**
     * @return the playTable
     */
    public PlayTable getPlayTable() {
        return playTable;
    }

    /**
     * @param playTable the playTable to set
     */
    public void setPlayTable(PlayTable playTable) {
        this.playTable = playTable;
    }

    /**
     * @return the playerTable
     */
    public PlayerTable getPlayerTable() {
        return playerTable;
    }

    /**
     * @param playerTable the playerTable to set
     */
    public void setPlayerTable(PlayerTable playerTable) {
        this.playerTable = playerTable;
    }

    /**
     * @return the puntReturnTable
     */
    public PuntReturnTable getPuntReturnTable() {
        return puntReturnTable;
    }

    /**
     * @param puntReturnTable the puntReturnTable to set
     */
    public void setPuntReturnTable(PuntReturnTable puntReturnTable) {
        this.puntReturnTable = puntReturnTable;
    }

    /**
     * @return the puntTable
     */
    public PuntTable getPuntTable() {
        return puntTable;
    }

    /**
     * @param puntTable the puntTable to set
     */
    public void setPuntTable(PuntTable puntTable) {
        this.puntTable = puntTable;
    }

    /**
     * @return the receptionTable
     */
    public ReceptionTable getReceptionTable() {
        return receptionTable;
    }

    /**
     * @param receptionTable the receptionTable to set
     */
    public void setReceptionTable(ReceptionTable receptionTable) {
        this.receptionTable = receptionTable;
    }

    /**
     * @return the rushTable
     */
    public RushTable getRushTable() {
        return rushTable;
    }

    /**
     * @param rushTable the rushTable to set
     */
    public void setRushTable(RushTable rushTable) {
        this.rushTable = rushTable;
    }

    /**
     * @return the stadiumTable
     */
    public StadiumTable getStadiumTable() {
        return stadiumTable;
    }

    /**
     * @param stadiumTable the stadiumTable to set
     */
    public void setStadiumTable(StadiumTable stadiumTable) {
        this.stadiumTable = stadiumTable;
    }

    /**
     * @return the teamGameStatsTable
     */
    public TeamGameStatsTable getTeamGameStatsTable() {
        return teamGameStatsTable;
    }

    /**
     * @param teamGameStatsTable the teamGameStatsTable to set
     */
    public void setTeamGameStatsTable(TeamGameStatsTable teamGameStatsTable) {
        this.teamGameStatsTable = teamGameStatsTable;
    }

    /**
     * @return the teamTable
     */
    public TeamTable getTeamTable() {
        return teamTable;
    }

    /**
     * @param teamTable the teamTable to set
     */
    public void setTeamTable(TeamTable teamTable) {
        this.teamTable = teamTable;
    }
}
