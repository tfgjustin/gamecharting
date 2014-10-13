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
public class KickoffTable {
    SortedSet<KickoffRow> table;
    
    public static KickoffTable loadTable(File file) throws IOException{
        KickoffTable table = new KickoffTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(KickoffRow.makeRow(s));
        }
        return table;
    }
    
    public KickoffTable(){
        table = new TreeSet();
    }
    
    public void addRow(KickoffRow row){
        table.add(row);
    }
}
