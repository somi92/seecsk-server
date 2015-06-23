/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.controllers;

import com.github.somi92.seecskcommon.domain.Clan;
import com.github.somi92.seecskcommon.domain.Clanarina;
import com.github.somi92.seecskcommon.domain.Grupa;
import com.github.somi92.seecskcommon.domain.Kategorija;
import com.github.somi92.seecskcommon.domain.Trening;
import com.github.somi92.seecskcommon.domain.Uplata;
import com.github.somi92.seecskcommon.domain.Zaposleni;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import com.github.somi92.seecskcommon.util.Ref;
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
import com.github.somi92.seecskserver.model.operations.zaposleni.SOPronadjiAdministratora;
import java.util.List;

/**
 *
 * @author milos
 */
public class KontrolerAL {
    
    private static ApstraktnaSistemskaOperacija aso;
    
    public synchronized static void kreirajClana(Ref<Clan> clan) throws Exception {
        aso = new SOKreirajClana(clan);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void sacuvajIliAzurirajClana(Clan clan, List<Uplata> uplateZaBrisanje) throws Exception {
        SOZapamtiClana zapamtiSO = new SOZapamtiClana(clan);
        zapamtiSO.postaviUplateZaBrisanje(uplateZaBrisanje);
        aso = zapamtiSO;
        aso.izvrsiSistemskuOperaciju();
            
    }
    
    public synchronized static void obrisiClana(Clan clan) throws Exception {
        aso = new SOObrisiClana(clan);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void vratiListuClanova(Ref<List<Clan>> clanovi, boolean ucitajListe) throws Exception {
        aso = new SOVratiListuClanova(clanovi, ucitajListe);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void pronadjiClanove(Ref<List<Clan>> clanovi, List<String> kriterijumPretrage, boolean ucitajListe) throws Exception {
        aso = new SOPronadjiClanove(clanovi, kriterijumPretrage, ucitajListe);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void kreirajGrupu(Ref<Grupa> grupa) throws Exception {
        aso = new SOKreirajGrupu(grupa);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void zapamtiGrupu(Grupa grupa) throws Exception {
        aso = new SOZapamtiGrupu(grupa);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void vratiListuGrupa(Ref<List<Grupa>> grupe, boolean ucitajListe) throws Exception {
        aso = new SOVratiListuGrupa(grupe, ucitajListe);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void vratiListuKategorija(Ref<List<Kategorija>> kategorije) throws Exception {
        aso = new SOVratiListuKategorija(kategorije);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void vratiClanarine(Ref<List<Clanarina>> clanarine, List<String> kriterijumPretrage, boolean ucitajUplate) throws Exception {
        aso = new SOPronadjiClanarine(kriterijumPretrage, clanarine, ucitajUplate);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void zapamtiClanarine(List<Uplata> clanarine, List<Uplata> uplateZaBrisanje) throws Exception {
        SOZapamtiClanarine zc = new SOZapamtiClanarine(clanarine);
        zc.postaviUplateZaBrisanje(uplateZaBrisanje);
        aso = zc;
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void vratiTreninge(Ref<List<Trening>> treninzi, List<String> kriterijumPretrage) throws Exception {
        aso = new SOPronadjiTreninge(treninzi, kriterijumPretrage);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void ucitajTrening(Ref<Trening> trening) throws Exception {
        aso = new SOUcitajTrening(trening);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void kreirajTrening(Ref<Trening> trening) throws Exception {
        aso = new SOKreirajTrening(trening);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void sacuvajIliAzurirajTrening(Trening trening) throws Exception {
        aso = new SOZapamtiTrening(trening);
        aso.izvrsiSistemskuOperaciju();
        
    }
    
    public synchronized static void obrisiTrening(Trening trening) throws Exception {
        aso = new SOObrisiTrening(trening);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void pronadjiAdministratora(Ref<Zaposleni> zaposleni) throws Exception {
        aso = new SOPronadjiAdministratora(zaposleni);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void ucitajClana(Ref<Clan> clan) throws Exception {
        aso = new SOUcitajClana(clan);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void kreirajClanarinu(Ref<Clanarina> clanarina) throws Exception {
        aso = new SOKreirajClanarinu(clanarina);
        aso.izvrsiSistemskuOperaciju();
    }
    
    public synchronized static void ucitajClanarinu(Ref<Clanarina> clanarina) throws Exception {
        aso = new SOUcitajClanarinu(clanarina);
        aso.izvrsiSistemskuOperaciju();
    }
}
