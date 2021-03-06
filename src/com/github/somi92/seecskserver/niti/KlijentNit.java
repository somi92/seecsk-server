/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.niti;


import com.github.somi92.seecskcommon.domain.Clan;
import com.github.somi92.seecskcommon.domain.Clanarina;
import com.github.somi92.seecskcommon.domain.Grupa;
import com.github.somi92.seecskcommon.domain.Kategorija;
import com.github.somi92.seecskcommon.domain.Trening;
import com.github.somi92.seecskcommon.domain.Uplata;
import com.github.somi92.seecskcommon.domain.Zaposleni;
import com.github.somi92.seecskcommon.transfer.OdgovorObjekat;
import com.github.somi92.seecskcommon.transfer.ZahtevObjekat;
import com.github.somi92.seecskcommon.util.Ref;
import com.github.somi92.seecskcommon.util.SistemskeOperacije;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_KREIRAJ_CLANA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_KREIRAJ_TRENING;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_OBRISI_CLANA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_OBRISI_TRENING;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_OBRISI_UPLATE;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_PRONADJI_ADMINISTRATORA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_PRONADJI_CLANARINE;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_PRONADJI_CLANOVE;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_PRONADJI_TRENINGE;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_UCITAJ_TRENING;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_VRATI_LISTU_CLANOVA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_VRATI_LISTU_GRUPA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_ZAPAMTI_CLANA;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_ZAPAMTI_CLANARINE;
import static com.github.somi92.seecskcommon.util.SistemskeOperacije.SO_ZAPAMTI_TRENING;
import com.github.somi92.seecskserver.model.controllers.KontrolerAL;
import com.github.somi92.seecskserver.model.operations.clan.SOKreirajClana;
import com.github.somi92.seecskserver.model.operations.clan.SOObrisiClana;
import com.github.somi92.seecskserver.model.operations.clan.SOPronadjiClanove;
import com.github.somi92.seecskserver.model.operations.clan.SOUcitajClana;
import com.github.somi92.seecskserver.model.operations.clan.SOVratiListuClanova;
import com.github.somi92.seecskserver.model.operations.clan.SOZapamtiClana;
import com.github.somi92.seecskserver.model.operations.clanarina.SOKreirajClanarinu;
import com.github.somi92.seecskserver.model.operations.clanarina.SOPronadjiClanarine;
import com.github.somi92.seecskserver.model.operations.clanarina.SOUcitajClanarinu;
import com.github.somi92.seecskserver.model.operations.clanarina.SOZapamtiClanarine;
import com.github.somi92.seecskserver.model.operations.grupa.SOKreirajGrupu;
import com.github.somi92.seecskserver.model.operations.grupa.SOVratiListuGrupa;
import com.github.somi92.seecskserver.model.operations.grupa.SOZapamtiGrupu;
import com.github.somi92.seecskserver.model.operations.kategorija.SOVratiListuKategorija;
import com.github.somi92.seecskserver.model.operations.trening.SOKreirajTrening;
import com.github.somi92.seecskserver.model.operations.trening.SOObrisiTrening;
import com.github.somi92.seecskserver.model.operations.trening.SOPronadjiTreninge;
import com.github.somi92.seecskserver.model.operations.trening.SOUcitajTrening;
import com.github.somi92.seecskserver.model.operations.trening.SOZapamtiTrening;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
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
    private Date datumPrijavljivanja;
    private String ipPort;
    
    public KlijentNit(ServerNit parent) {
        this.parent = parent;
        this.running = true;
        this.verified = false;
            
    }
    
    @Override
    public void run() {
        try {
            
//            in = new ObjectInputStream(socket.getInputStream());
//            out = new ObjectOutputStream(socket.getOutputStream());
//            out.flush();
            
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
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                ServerNit.obrisiKlijenta(this);
                in.close();
                out.close();
                socket.close();
                System.out.println("Klijent "+this+" je odjavljen.");
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
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
                    KontrolerAL.kreirajClana(refClanKreiraj);
                    oo.setPodaci(refClanKreiraj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajClana.class.getSimpleName());
                    break;
                    
                case SO_KREIRAJ_TRENING:
                    Ref<Trening> refTrening = zo.getParametar();
                    KontrolerAL.kreirajTrening(refTrening);
                    oo.setPodaci(refTrening);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajTrening.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_CLANA:
                    Clan clanObrisi = zo.getParametar();
                    KontrolerAL.obrisiClana(clanObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOObrisiClana.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_TRENING:
                    Trening treningObrisi = zo.getParametar();
                    KontrolerAL.obrisiTrening(treningObrisi);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOObrisiTrening.class.getSimpleName());
                    break;
                    
                case SO_OBRISI_UPLATE:
                    // ne koristi se
                    break;
                    
                case SO_PRONADJI_CLANARINE:
                    Ref<List<Clanarina>> clanarinePronadji = zo.getParametar();
                    KontrolerAL.vratiClanarine(clanarinePronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanarinePronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiClanarine.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_CLANOVE:
                    Ref<List<Clan>> clanoviPronadji = zo.getParametar();
                    KontrolerAL.pronadjiClanove(clanoviPronadji, zo.getKriterijumPretrage(), zo.isUcitajListe());
                    oo.setPodaci(clanoviPronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiClanove.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_TRENINGE:
                    Ref<List<Trening>> treninziPronadji = zo.getParametar();
                    KontrolerAL.vratiTreninge(treninziPronadji, zo.getKriterijumPretrage());
                    oo.setPodaci(treninziPronadji);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOPronadjiTreninge.class.getSimpleName());
                    break;
                    
                case SO_UCITAJ_TRENING:
                    Ref<Trening> treningUcitaj = zo.getParametar();
                    KontrolerAL.ucitajTrening(treningUcitaj);
                    oo.setPodaci(treningUcitaj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOUcitajTrening.class.getSimpleName());
                    break;
                    
                case SO_VRATI_LISTU_CLANOVA:
                    Ref<List<Clan>> clanoviLista = zo.getParametar();
                    KontrolerAL.vratiListuClanova(clanoviLista, zo.isUcitajListe());
                    oo.setPodaci(clanoviLista);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOVratiListuClanova.class.getSimpleName());
                    break;
                    
                case SO_KREIRAJ_GRUPU:
                    Ref<Grupa> grupaRef = zo.getParametar();
                    KontrolerAL.kreirajGrupu(grupaRef);
                    oo.setPodaci(grupaRef);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajGrupu.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_GRUPU:
                    Grupa grupa = zo.getParametar();
                    KontrolerAL.zapamtiGrupu(grupa);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiGrupu.class.getSimpleName());
                    break;
                
                case SO_VRATI_LISTU_GRUPA:
                    Ref<List<Grupa>> grupeLista = zo.getParametar();
                    KontrolerAL.vratiListuGrupa(grupeLista, zo.isUcitajListe());
                    oo.setPodaci(grupeLista);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOVratiListuGrupa.class.getSimpleName());
                    break;
                    
                case SO_VRATI_LISTU_KATEGORIJA:
                    Ref<List<Kategorija>> kategorije = zo.getParametar();
                    KontrolerAL.vratiListuKategorija(kategorije);
                    oo.setPodaci(kategorije);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOVratiListuKategorija.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_CLANA:
                    Clan clanZapamti = zo.getParametar();
                    KontrolerAL.sacuvajIliAzurirajClana(clanZapamti, zo.getUplateZaBrisanje());
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiClana.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_CLANARINE:
                    List<Uplata> clanarineZapamti = zo.getParametar();
                    KontrolerAL.zapamtiClanarine(clanarineZapamti, zo.getUplateZaBrisanje());
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiClanarine.class.getSimpleName());
                    break;
                    
                case SO_ZAPAMTI_TRENING:
                    Trening treningZapamti = zo.getParametar();
                    KontrolerAL.sacuvajIliAzurirajTrening(treningZapamti);
                    oo.setPodaci(null);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOZapamtiTrening.class.getSimpleName());
                    break;
                    
                case SO_PRONADJI_ADMINISTRATORA:
                    Ref<Zaposleni> zaposleni = zo.getParametar();
                    KontrolerAL.pronadjiAdministratora(zaposleni);
                    oo.setPodaci(zaposleni);
                    if(zaposleni.get()!=null) {
                        verified = true;
                        userName = zaposleni.get().getKorisnickoIme();
                        oo.setStatusOperacije(0);
                        ServerNit.dodajKlijenta(this);
                    } else {
                        oo.setStatusOperacije(-1);
                        verified = false;
                        userName = "!nepoznat_korisnik!";
//                        ServerNit.obrisiKlijenta(this);
                    }
                    break;
                    
                case SO_UCITAJ_CLANA:
                    Ref<Clan> clanUcitaj = zo.getParametar();
                    KontrolerAL.ucitajClana(clanUcitaj);
                    oo.setPodaci(clanUcitaj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOUcitajClana.class.getSimpleName());
                    break;
                    
                case SO_KREIRAJ_CLANARINU:
                    Ref<Clanarina> clanarinaKreiraj = zo.getParametar();
                    KontrolerAL.kreirajClanarinu(clanarinaKreiraj);
                    oo.setPodaci(clanarinaKreiraj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOKreirajClanarinu.class.getSimpleName());
                    break;
                    
                case SO_UCITAJ_CLANARINU:
                    Ref<Clanarina> clanarinaUcitaj = zo.getParametar();
                    KontrolerAL.ucitajClanarinu(clanarinaUcitaj);
                    oo.setPodaci(clanarinaUcitaj);
                    oo.setStatusOperacije(0);
                    ServerNit.azurirajEvidenciju("<"+userName+">: operacija "+SOUcitajClanarinu.class.getSimpleName());
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            oo.setGreska(new Exception(ex.getMessage()));
            oo.setPodaci(null);
            oo.setStatusOperacije(-1);
            System.out.println(ex.getMessage());
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

    void postaviSocket(Socket socket) {
        try {
            this.socket = socket;
            ipPort = socket.getInetAddress().toString()+":"+socket.getPort();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void odjaviKlijenta() {
        try {
            in.close();
            out.close();
            socket.close();
            running = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public Date getDatumPrijavljivanja() {
        return datumPrijavljivanja;
    }

    public void setDatumPrijavljivanja(Date datumPrijavljivanja) {
        this.datumPrijavljivanja = datumPrijavljivanja;
    }

    public String getIpPort() {
        return ipPort;
    }
    
    
}
