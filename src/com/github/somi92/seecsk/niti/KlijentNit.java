/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.niti;

import com.github.somi92.seecsk.domain.Clan;
import com.github.somi92.seecsk.model.controllers.KontrolerPL;
import com.github.somi92.seecsk.transfer.OdgovorObjekat;
import com.github.somi92.seecsk.transfer.ZahtevObjekat;
import com.github.somi92.seecsk.util.Ref;
import com.github.somi92.seecsk.util.SistemskeOperacije;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author milos
 */
public class KlijentNit extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    
    public KlijentNit(Socket socket) {
        this.socket = socket;
        this.running = true;
    }
    
    @Override
    public void run() {
        try {
            
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            
            while(running) {
                ZahtevObjekat zo = (ZahtevObjekat) in.readObject();
                
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void obradiZahtev(ZahtevObjekat zo) {
        OdgovorObjekat oo = new OdgovorObjekat();
        SistemskeOperacije so = zo.getSistemskaOperacija();
        switch(so) {
            
            case SO_KREIRAJ_CLANA:
                Ref<Clan> ref = zo.getParametar();
                KontrolerPL.kreirajClana(ref);
            break;
                
            case SO_KREIRAJ_TRENING:
            break;
            
            case SO_OBRISI_CLANA:
            break;
                
            case SO_OBRISI_TRENING:
            break;
                
            case SO_OBRISI_UPLATE:
            break;
                
            case SO_PRONADJI_CLANARINE:
            break;
                
            case SO_PRONADJI_CLANOVE:
            break;
                
            case SO_PRONADJI_TRENINGE:
            break;
                
            case SO_UCITAJ_TRENING:
            break;
                
            case SO_VRATI_LISTU_CLANOVA:
            break;
                
            case SO_VRATI_LISTU_GRUPA:
            break;
                
            case SO_ZAPAMTI_CLANA:
            break;
                
            case SO_ZAPAMTI_CLANARINE:
            break;
                
            case SO_ZAPAMTI_TRENING:
            break;
        }
    }
    
}
