/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.trening;

import com.github.somi92.seecsk.domain.Trening;
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
public class SOPronadjiTreninge extends ApstraktnaSistemskaOperacija {
    
    private Ref<List<Trening>> listaTreninga;
    private List<String> kriterijumPretrage;
    
    public SOPronadjiTreninge(Ref<List<Trening>> listaTreninga, List<String> kriterijumPretrage) {
        this.listaTreninga = listaTreninga;
        this.kriterijumPretrage = kriterijumPretrage;
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
            Trening t = null;
            if(listaTreninga.get().size()>0) {
                t = listaTreninga.get().get(0);
            } else {
                t = new Trening();
            }
            listaTreninga.set(dbbroker.loadEntities(t, kriterijumPretrage, false));
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
