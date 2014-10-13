/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author ryan.hoes
 */
public class PuntReturnTable {
    SortedSet<PuntReturnRow> table;
    
    public static PuntReturnTable loadTable(File file) throws IOException{
        PuntReturnTable table = new PuntReturnTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(PuntReturnRow.makeRow(s));
        }
        return table;
    }
    
    public PuntReturnTable(){
        table = new TreeSet();
    }
    
    public void addRow(PuntReturnRow row){
        table.add(row);
    }
}
