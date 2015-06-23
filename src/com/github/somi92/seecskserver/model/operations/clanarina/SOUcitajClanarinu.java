/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.clanarina;

import com.github.somi92.seecskcommon.domain.Clanarina;
import com.github.somi92.seecskcommon.domain.Uplata;
import com.github.somi92.seecskcommon.util.Ref;
import com.github.somi92.seecskserver.model.exceptions.so.PreduslovException;
import com.github.somi92.seecskserver.model.exceptions.so.SOException;
import com.github.somi92.seecskserver.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author milos
 */

/*
* ne koristi se
*/
public class SOUcitajClanarinu extends ApstraktnaSistemskaOperacija {
    
    private Ref<Clanarina> clanarina;

    public SOUcitajClanarinu(Ref<Clanarina> clanarina) {
        this.clanarina = clanarina;
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
            Clanarina c = dbbroker.loadEntity(clanarina.get(), false);
            List<String> kriterijumPretrage = new ArrayList<>();
            kriterijumPretrage.add("idClanarina="+c.getIdClanarina());
            Uplata u = new Uplata();
            u.setClanarina(c);
            List<Uplata> uplate = dbbroker.loadEntities(u, kriterijumPretrage, false);
            c.setUplate(uplate);
            clanarina.set(c);
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
