/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.kategorija;

import com.github.somi92.seecskcommon.domain.Kategorija;
import com.github.somi92.seecskcommon.util.Ref;
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
public class SOVratiListuKategorija extends ApstraktnaSistemskaOperacija {

    private Ref<List<Kategorija>> kategorije;

    public SOVratiListuKategorija(Ref<List<Kategorija>> kategorije) {
        this.kategorije = kategorije;
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
            kategorije.set(dbbroker.loadEntities(new Kategorija(), null, false));
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
