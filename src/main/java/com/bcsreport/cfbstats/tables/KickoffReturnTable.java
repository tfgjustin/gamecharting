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
public class KickoffReturnTable {
    SortedSet<KickoffReturnRow> table;
    
    public static KickoffReturnTable loadTable(File file) throws IOException{
        KickoffReturnTable table = new KickoffReturnTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(KickoffReturnRow.makeRow(s));
        }
        return table;
    }
    
    public KickoffReturnTable(){
        table = new TreeSet();
    }
    
    public void addRow(KickoffReturnRow row){
        table.add(row);
    }
}
