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
public class PuntTable {
    SortedSet<PuntRow> table;
    
    public static PuntTable loadTable(File file) throws IOException{
        PuntTable table = new PuntTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(PuntRow.makeRow(s));
        }
        return table;
    }
    
    public PuntTable(){
        table = new TreeSet();
    }
    
    public void addRow(PuntRow row){
        table.add(row);
    }
}
