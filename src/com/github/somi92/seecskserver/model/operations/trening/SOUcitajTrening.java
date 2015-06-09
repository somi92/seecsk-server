/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.trening;

import com.github.somi92.seecskcommon.domain.Prisustvo;
import com.github.somi92.seecskcommon.domain.Trening;
import com.github.somi92.seecskserver.model.exceptions.so.PreduslovException;
import com.github.somi92.seecskserver.model.exceptions.so.SOException;
import com.github.somi92.seecskserver.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import com.github.somi92.seecskcommon.util.Ref;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author milos
 */
public class SOUcitajTrening extends ApstraktnaSistemskaOperacija {
    
    private Ref<Trening> trening;

    public SOUcitajTrening(Ref<Trening> trening) {
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
            Trening t = trening.get();
            t = dbbroker.loadEntity(t, false);
            List<String> kriterijumPretrage = new ArrayList<>();
            kriterijumPretrage.add("idTrening="+t.getIdTrening());
            Prisustvo p = new Prisustvo();
            p.setTrening(t);
            List<Prisustvo> prisustvaList = dbbroker.loadEntities(p, kriterijumPretrage, false);
            ArrayList<Prisustvo> prisustva = new ArrayList<>();
            for(Prisustvo pr : prisustvaList) {
                prisustva.add(pr);
            }
            t.setPrisustva(prisustva);
            trening.set(t);
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
