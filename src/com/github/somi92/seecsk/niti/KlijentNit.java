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
import com.github.somi92.seecsk.domain.Zaposleni;
import com.github.somi92.seecsk.model.controllers.KontrolerPL;
import com.github.somi92.seecsk.model.operations.clan.SOKreirajClana;
import com.github.somi92.seecsk.model.operations.clan.SOObrisiClana;
import com.github.somi92.seecsk.model.operations.clan.SOPronadjiClanove;
import com.github.somi92.seecsk.model.operations.clan.SOVratiListuClanova;
import com.github.somi92.seecsk.model.operations.clan.SOZapamtiClana;
import com.github.somi92.seecsk.model.operations.clanarina.SOPronadjiClanarine;
import com.github.somi92.seecsk.model.operations.clanarina.SOZapamtiClanarine;
import com.github.somi92.seecsk.model.operations.grupa.SOVratiListuGrupa;
import com.github.somi92.seecsk.model.operations.trening.SOKreirajTrening;
import com.github.somi92.seecsk.model.operations.trening.SOObrisiTrening;
import com.github.somi92.seecsk.model.operations.trening.SOPronadjiTreninge;
import com.github.somi92.seecsk.model.operations.trening.SOUcitajTrening;
import com.github.somi92.seecsk.model.operations.trening.SOZapamtiTrening;
import com.github.somi92.seecsk.transfer.OdgovorObjekat;
import com.github.somi92.seecsk.transfer.ZahtevObjekat;
import com.github.somi92.seecsk.util.Ref;
import com.github.somi92.seecsk.util.SistemskeOperacije;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author milos
 */
public class KlijentNit extends Thread {

    private ServerNit parent;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    private boolean verified;
    private String userName;
    
    public KlijentNit(ServerNit parent, Socket socket) {
        this.parent = parent;
        this.socket = socket;
        this.running = true;
        this.verified = false;
    }
    
    @Override
    public void run() {
        try {
            
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            
            do {
                ZahtevObjekat zo = (ZahtevObjekat) in.readObject();
                OdgovorObjekat oo = obradiZahtev(zo);
                out.writeObject(oo);
            } while(running && verified);
            
//            while(running && verified) {
//                ZahtevObjekat zo = (ZahtevObjekat) in.readObject();
//                OdgovorObjekat oo = obradiZahtev(zo);
//                out.writeObject(oo);
//            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                ServerNit.obrisiKlijenta(this);
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
            if(!verified && zo.getSistemskaOperacija() != SistemskeOperacije.SO_PRONADJI_ADMINISTRATORA) {
                throw new Exception("Nemate privilegije za navedenu operaciju.");
            }
            SistemskeOperacije so = zo.getSistemskaOperacija();
            switch(so) {
                
                case SO_KREIRAJ_CLANA:
                    Ref<Clan> refClanKreiraj = zo.getParametar();
                    KontrolerPL.kreirajClana(refClanKreiraj);
                    oo.setPodaci(refClanKreiraj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajClana.class.getSimpleName());
                    break;
                    
                case SO_KREIRAJ_TRENING:
                    Ref<Trening> refTrening = zo.getParametar();
                    KontrolerPL.kreirajTrening(refTrening);
                    oo.setPodaci(refTrening);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajTrening.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_CLANA:
                    Clan clanObrisi = zo.getParametar();
                    KontrolerPL.obrisiClana(clanObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOObrisiClana.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_TRENING:
                    Trening treningObrisi = zo.getParametar();
                    KontrolerPL.obrisiTrening(treningObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOObrisiTrening.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_UPLATE:
                    // ne koristi se
                    break;
                    
                case SO_PRONADJI_CLANARINE:
                    Ref<List<Clanarina>> clanarinePronadji = zo.getParametar();
                    KontrolerPL.vratiClanarine(clanarinePronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanarinePronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiClanarine.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_CLANOVE:
                    Ref<List<Clan>> clanoviPronadji = zo.getParametar();
                    KontrolerPL.pronadjiClanove(clanoviPronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanoviPronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiClanove.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_TRENINGE:
                    Ref<List<Trening>> treninziPronadji = zo.getParametar();
                    KontrolerPL.vratiTreninge(treninziPronadji, zo.getKriterijumPretrage());
                    oo.setPodaci(treninziPronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiTreninge.class.getSimpleName());
                    break;
                    
                case SO_UCITAJ_TRENING:
                    Ref<Trening> treningUcitaj = zo.getParametar();
                    KontrolerPL.ucitajTrening(treningUcitaj);
                    oo.setPodaci(treningUcitaj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOUcitajTrening.class.getSimpleName());
                    break;
                    
                case SO_VRATI_LISTU_CLANOVA:
                    Ref<List<Clan>> clanoviLista = zo.getParametar();
                    KontrolerPL.vratiListuClanova(clanoviLista, zo.isUcitajListe());
                    oo.setPodaci(clanoviLista);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOVratiListuClanova.class.getSimpleName());
                    break;
                    
                case SO_VRATI_LISTU_GRUPA:
                    Ref<List<Grupa>> grupeLista = zo.getParametar();
                    KontrolerPL.vratiListuGrupa(grupeLista, zo.isUcitajListe());
                    oo.setPodaci(grupeLista);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOVratiListuGrupa.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_CLANA:
                    Clan clanZapamti = zo.getParametar();
                    KontrolerPL.sacuvajIliAzurirajClana(clanZapamti, zo.getUplateZaBrisanje());
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiClana.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_CLANARINE:
                    List<Uplata> clanarineZapamti = zo.getParametar();
                    KontrolerPL.zapamtiClanarine(clanarineZapamti);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiClanarine.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_TRENING:
                    Trening treningZapamti = zo.getParametar();
                    KontrolerPL.sacuvajIliAzurirajTrening(treningZapamti);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiTrening.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_ADMINISTRATORA:
                    Ref<Zaposleni> zaposleni = zo.getParametar();
                    KontrolerPL.pronadjiAdministratora(zaposleni);
                    oo.setPodaci(zaposleni);
                    oo.setStatusOperacije(0);
                    if(zaposleni.get()!=null) {
                        verified = true;
                        userName = zaposleni.get().getKorisnickoIme();
                        ServerNit.dodajKlijenta(this);
                    } else {
                        verified = false;
                        userName = "!nepoznat_korisnik!";
                        ServerNit.obrisiKlijenta(this);
                    }
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

    public boolean isVerified() {
        return verified;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KlijentNit other = (KlijentNit) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return userName;
    }
    
    
}
