/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.clanarina;

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
public class SOObrisiUplate extends ApstraktnaSistemskaOperacija {
    
    private List<Uplata> uplate;
    
    public SOObrisiUplate(List<Uplata> uplate) {
        this.uplate = uplate;
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
            for(Uplata u : uplate) {
                dbbroker.deleteEntity(u);
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
