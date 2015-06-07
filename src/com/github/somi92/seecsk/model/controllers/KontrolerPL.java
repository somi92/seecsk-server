/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.controllers;

import com.github.somi92.seecsk.domain.Clan;
import com.github.somi92.seecsk.domain.Clanarina;
import com.github.somi92.seecsk.domain.Grupa;
import com.github.somi92.seecsk.domain.Trening;
import com.github.somi92.seecsk.domain.Uplata;
import com.github.somi92.seecsk.model.operations.ApstraktnaSistemskaOperacija;
import com.github.somi92.seecsk.util.Ref;
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
import java.util.List;

/**
 *
 * @author milos
 */
public class KontrolerPL {
    
    private static ApstraktnaSistemskaOperacija aso;
    
    public synchronized static void kreirajClana(Ref<Clan> clan) {
        try {
            aso = new SOKreirajClana(clan);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static boolean sacuvajIliAzurirajClana(Clan clan, List<Uplata> uplateZaBrisanje) {
        try {
            SOZapamtiClana zapamtiSO = new SOZapamtiClana(clan);
            zapamtiSO.postaviUplateZaBrisanje(uplateZaBrisanje);
            aso = zapamtiSO;
            aso.izvrsiSistemskuOperaciju();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public synchronized static boolean obrisiClana(Clan clan) {
        try {
            aso = new SOObrisiClana(clan);
            aso.izvrsiSistemskuOperaciju();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public synchronized static void vratiListuClanova(Ref<List<Clan>> clanovi, boolean ucitajListe) {
        try {
            aso = new SOVratiListuClanova(clanovi, ucitajListe);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void pronadjiClanove(Ref<List<Clan>> clanovi, List<String> kriterijumPretrage, boolean ucitajListe) {
        try {
            aso = new SOPronadjiClanove(clanovi, kriterijumPretrage, ucitajListe);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void vratiListuGrupa(Ref<List<Grupa>> grupe, boolean ucitajListe) {
        try {
            aso = new SOVratiListuGrupa(grupe, ucitajListe);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void vratiClanarine(Ref<List<Clanarina>> clanarine, List<String> kriterijumPretrage, boolean ucitajUplate) {
        try {
            aso = new SOPronadjiClanarine(kriterijumPretrage, clanarine, ucitajUplate);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void zapamtiClanarine(List<Uplata> clanarine) {
        try {
            aso = new SOZapamtiClanarine(clanarine);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void vratiTreninge(Ref<List<Trening>> treninzi, List<String> kriterijumPretrage) {
        try {
            aso = new SOPronadjiTreninge(treninzi, kriterijumPretrage);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void ucitajTrening(Ref<Trening> trening) {
        try {
            aso = new SOUcitajTrening(trening);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void kreirajTrening(Ref<Trening> trening) {
        try {
            aso = new SOKreirajTrening(trening);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static void sacuvajIliAzurirajTrening(Trening trening) {
        try {
            aso = new SOZapamtiTrening(trening);
            aso.izvrsiSistemskuOperaciju();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static boolean obrisiTrening(Trening trening) {
        try {
            aso = new SOObrisiTrening(trening);
            aso.izvrsiSistemskuOperaciju();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
