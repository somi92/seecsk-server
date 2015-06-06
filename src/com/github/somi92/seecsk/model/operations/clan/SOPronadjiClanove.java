/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.clan;

import com.github.somi92.seecsk.domain.Clan;
import com.github.somi92.seecsk.model.exceptions.so.PreduslovException;
import com.github.somi92.seecsk.model.exceptions.so.SOException;
import com.github.somi92.seecsk.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecsk.model.operations.ApstraktnaSistemskaOperacija;
import com.github.somi92.seecsk.model.operations.Ref;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author milos
 */
public class SOPronadjiClanove extends ApstraktnaSistemskaOperacija {
    
    private Ref<List<Clan>> listaClanova;
    private List<String> kriterijumPretrage;
    private boolean ucitajListe;
    
    public SOPronadjiClanove(Ref<List<Clan>> listaClanova, List<String> kriterijumPretrage, boolean ucitajListe) {
        this.listaClanova = listaClanova;
        this.kriterijumPretrage = kriterijumPretrage;
        this.ucitajListe = ucitajListe;
    }

    @Override
    protected void izvrsiValidaciju() throws ValidacijaException {
        
    }

    @Override
    protected void proveriPreduslove() throws PreduslovException {
        
    }

    @Override
    protected void izvrsiDBTransakciju() throws SOException {
        try {
            List<Clan> clanovi = listaClanova.get();
            Clan c = null;
            if(clanovi.size()>0) {
                c = clanovi.get(0);
            } else {
                c = new Clan();
            }
            listaClanova.set(dbbroker.loadEntities(c, kriterijumPretrage, ucitajListe));
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Greska -> "+this.getClass().getName()+": "+ex.getMessage());
        }
    }

    @Override
    public String vratiImeOperacije() {
        return this.getClass().getSimpleName();
    }
}
