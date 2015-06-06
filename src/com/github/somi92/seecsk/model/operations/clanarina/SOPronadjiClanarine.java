/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.clanarina;

import com.github.somi92.seecsk.domain.Clanarina;
import com.github.somi92.seecsk.domain.Uplata;
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
public class SOPronadjiClanarine extends ApstraktnaSistemskaOperacija {
    
    private List<String> kriterijumPretrage;
    private Ref<List<Clanarina>> clanarine;
    private boolean ucitajUplate;
    
    public SOPronadjiClanarine(List<String> kriterijumPretrage, Ref<List<Clanarina>> clanarine, boolean ucitajUplate) {
        this.kriterijumPretrage = kriterijumPretrage;
        this.clanarine = clanarine;
        this.ucitajUplate = ucitajUplate;
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
            List<Clanarina> lc = clanarine.get();
            Clanarina u = null;
            if(lc.size() > 0) {
               u = lc.get(0);
            } else {
                u = new Clanarina();
            }
            clanarine.set(dbbroker.loadEntities(u, kriterijumPretrage, ucitajUplate));
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
