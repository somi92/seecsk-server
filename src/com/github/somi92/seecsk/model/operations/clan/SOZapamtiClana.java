/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.clan;

import com.github.somi92.seecsk.domain.Clan;
import com.github.somi92.seecsk.domain.Uplata;
import com.github.somi92.seecsk.model.exceptions.so.PreduslovException;
import com.github.somi92.seecsk.model.exceptions.so.SOException;
import com.github.somi92.seecsk.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecsk.model.operations.ApstraktnaSistemskaOperacija;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author milos
 */
public class SOZapamtiClana extends ApstraktnaSistemskaOperacija {

    private Clan clan;
    private int rowsAffected;
    private List<Uplata> uplateBrisanje;
    
    public SOZapamtiClana(Clan clan) {
        this.clan = clan;
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
            rowsAffected = dbbroker.saveOrUpdateEntity(clan);
            if(uplateBrisanje != null && uplateBrisanje.size()>0) {
                for(Uplata u : uplateBrisanje) {
                    dbbroker.deleteEntity(u);
                }
            } 
            if(clan.getUplate() != null && clan.getUplate().size()>0) {
                List<Uplata> uplate = clan.getUplate();
                for(Uplata u : uplate) {
                    dbbroker.saveOrUpdateEntity(u);
                }
            }
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
