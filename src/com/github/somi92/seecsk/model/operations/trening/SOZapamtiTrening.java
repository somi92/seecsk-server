/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations.trening;

import com.github.somi92.seecsk.domain.Prisustvo;
import com.github.somi92.seecsk.domain.Trening;
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
public class SOZapamtiTrening extends ApstraktnaSistemskaOperacija {
    
    private Trening trening;
    
    public SOZapamtiTrening(Trening trening) {
        this.trening = trening;
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
            dbbroker.saveOrUpdateEntity(trening);
            List<Prisustvo> prisustva = trening.getPrisustva();
            if(prisustva != null && prisustva.size()>0) {
//                dbbroker.insertEntities(prisustva);
                for(Prisustvo p : prisustva) {
                    dbbroker.saveOrUpdateEntity(p);
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
