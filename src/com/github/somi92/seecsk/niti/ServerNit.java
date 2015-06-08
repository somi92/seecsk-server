/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.niti;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author milos
 */
public class ServerNit extends Thread {

    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private static List<KlijentNit> klijenti;
    
    public ServerNit(int port) {
        this.port = port;
        executor = Executors.newFixedThreadPool(5);
        klijenti = new ArrayList<>();
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                executor.execute(new KlijentNit(serverSocket.accept()));
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server je zaustavljen.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
    
    public void zaustaviServer() throws IOException {
        serverSocket.close();
    }
}
