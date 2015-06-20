/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.grupa;

import com.github.somi92.seecskcommon.domain.Grupa;
import com.github.somi92.seecskserver.model.exceptions.so.PreduslovException;
import com.github.somi92.seecskserver.model.exceptions.so.SOException;
import com.github.somi92.seecskserver.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import java.sql.SQLException;

/**
 *
 * @author milos
 */
public class SOZapamtiGrupu extends ApstraktnaSistemskaOperacija {

    private Grupa grupa;

    public SOZapamtiGrupu(Grupa grupa) {
        this.grupa = grupa;
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
            dbbroker.saveOrUpdateEntity(grupa);
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
