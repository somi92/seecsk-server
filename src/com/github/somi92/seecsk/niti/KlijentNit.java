/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.niti;

import com.github.somi92.seecsk.domain.Clan;
import com.github.somi92.seecsk.domain.Clanarina;
import com.github.somi92.seecsk.domain.Grupa;
import com.github.somi92.seecsk.domain.Trening;
import com.github.somi92.seecsk.domain.Uplata;
import com.github.somi92.seecsk.model.controllers.KontrolerPL;
import com.github.somi92.seecsk.transfer.OdgovorObjekat;
import com.github.somi92.seecsk.transfer.ZahtevObjekat;
import com.github.somi92.seecsk.util.Ref;
import com.github.somi92.seecsk.util.SistemskeOperacije;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

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
                OdgovorObjekat oo = obradiZahtev(zo);
                out.writeObject(oo);
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
    
    private OdgovorObjekat obradiZahtev(ZahtevObjekat zo) {
        OdgovorObjekat oo = new OdgovorObjekat();
        try {
            SistemskeOperacije so = zo.getSistemskaOperacija();
            switch(so) {
                
                case SO_KREIRAJ_CLANA:
                    Ref<Clan> refClanKreiraj = zo.getParametar();
                    KontrolerPL.kreirajClana(refClanKreiraj);
                    oo.setPodaci(refClanKreiraj);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_KREIRAJ_TRENING:
                    Ref<Trening> refTrening = zo.getParametar();
                    KontrolerPL.kreirajTrening(refTrening);
                    oo.setPodaci(refTrening);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_OBRISI_CLANA:
                    Clan clanObrisi = zo.getParametar();
                    KontrolerPL.obrisiClana(clanObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_OBRISI_TRENING:
                    Trening treningObrisi = zo.getParametar();
                    KontrolerPL.obrisiTrening(treningObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_OBRISI_UPLATE:
                    // ne koristi se
                    break;
                    
                case SO_PRONADJI_CLANARINE:
                    Ref<List<Clanarina>> clanarinePronadji = zo.getParametar();
                    KontrolerPL.vratiClanarine(clanarinePronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanarinePronadji);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_PRONADJI_CLANOVE:
                    Ref<List<Clan>> clanoviPronadji = zo.getParametar();
                    KontrolerPL.pronadjiClanove(clanoviPronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanoviPronadji);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_PRONADJI_TRENINGE:
                    Ref<List<Trening>> treninziPronadji = zo.getParametar();
                    KontrolerPL.vratiTreninge(treninziPronadji, zo.getKriterijumPretrage());
                    oo.setPodaci(treninziPronadji);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_UCITAJ_TRENING:
                    Ref<Trening> treningUcitaj = zo.getParametar();
                    KontrolerPL.ucitajTrening(treningUcitaj);
                    oo.setPodaci(treningUcitaj);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_VRATI_LISTU_CLANOVA:
                    Ref<List<Clan>> clanoviLista = zo.getParametar();
                    KontrolerPL.vratiListuClanova(clanoviLista, zo.isUcitajListe());
                    oo.setPodaci(clanoviLista);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_VRATI_LISTU_GRUPA:
                    Ref<List<Grupa>> grupeLista = zo.getParametar();
                    KontrolerPL.vratiListuGrupa(grupeLista, zo.isUcitajListe());
                    oo.setPodaci(grupeLista);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_ZAPAMTI_CLANA:
                    Clan clanZapamti = zo.getParametar();
                    KontrolerPL.sacuvajIliAzurirajClana(clanZapamti, zo.getUplateZaBrisanje());
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_ZAPAMTI_CLANARINE:
                    List<Uplata> clanarineZapamti = zo.getParametar();
                    KontrolerPL.zapamtiClanarine(clanarineZapamti);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    break;
                    
                case SO_ZAPAMTI_TRENING:
                    Trening treningZapamti = zo.getParametar();
                    KontrolerPL.sacuvajIliAzurirajTrening(treningZapamti);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            oo.setGreska(ex);
            oo.setPodaci(null);
            oo.setStatusOperacije(-1);
        }
        return oo;
    }
    
}
