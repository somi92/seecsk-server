/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.niti;

import com.github.somi92.seecskserver.gui.FServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

/**
 *
 * @author milos
 */
public class ServerNit extends Thread {

    private static FServer parent;
    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private static List<KlijentNit> klijenti;
    
    public ServerNit(FServer parent, int port) {
        this.parent = parent;
        this.port = port;
        executor = Executors.newFixedThreadPool(5);
        klijenti = new ArrayList<>();
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                KlijentNit kn = new KlijentNit(this);
                kn.postaviSocket(socket);
                kn.setDatumPrijavljivanja(Calendar.getInstance().getTime());
                executor.execute(kn);
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
        for(KlijentNit kn : klijenti) {
            kn.odjaviKlijenta();
        }
    }
    
    public synchronized static void dodajKlijenta(KlijentNit kn) {
        klijenti.add(kn);
        parent.azurirajListu(klijenti);
        parent.azurirajEvidenciju("<"+kn+"> prijavljen na sistem");
    }
    
    public synchronized static void obrisiKlijenta(KlijentNit kn) {
        klijenti.remove(kn);
        parent.azurirajListu(klijenti);
        parent.azurirajEvidenciju("<"+kn+"> odbačen");
    }
    
    public synchronized static void azurirajEvidenciju(String text) {
        parent.azurirajEvidenciju(text);
    }
}
