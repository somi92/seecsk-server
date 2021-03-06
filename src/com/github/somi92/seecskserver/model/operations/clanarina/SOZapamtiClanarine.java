/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.clanarina;

import com.github.somi92.seecskcommon.domain.Uplata;
import com.github.somi92.seecskserver.model.exceptions.so.PreduslovException;
import com.github.somi92.seecskserver.model.exceptions.so.SOException;
import com.github.somi92.seecskserver.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author milos
 */
public class SOZapamtiClanarine extends ApstraktnaSistemskaOperacija {
    
    private List<Uplata> uplate;
    private List<Uplata> uplateBrisanje;
    
    public SOZapamtiClanarine(List<Uplata> uplate) {
        this.uplate = uplate;
    }
 
    public void postaviUplateZaBrisanje(List<Uplata> uplateBrisanje) {
        this.uplateBrisanje = uplateBrisanje;
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
            if(uplateBrisanje != null && uplateBrisanje.size()>0) {
                for(Uplata u : uplateBrisanje) {
                    dbbroker.deleteEntity(u);
                }
            } 
            for(Uplata u : uplate) {
                dbbroker.saveOrUpdateEntity(u);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Greska -> "+this.getClass().getName()+": "+ex.getMessage());
            throw new SOException("Greska -> "+this.getClass().getName()+": "+ex.getMessage());
        }
    }

    @Override
    public String vratiImeOperacije() {
        return this.getClass().getSimpleName();
    }
}
