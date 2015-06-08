/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.mainserver;

import com.github.somi92.seecsk.gui.FServer;
import com.github.somi92.sqldbb.broker.DBBroker;

/**
 *
 * @author milos
 */
public class Main {
    
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FServer().setVisible(true);
            }
        });
    }
}
