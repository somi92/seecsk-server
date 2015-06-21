/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.clanarina;

import com.github.somi92.seecskcommon.domain.Clanarina;
import com.github.somi92.seecskcommon.util.Ref;
import com.github.somi92.seecskserver.model.exceptions.so.PreduslovException;
import com.github.somi92.seecskserver.model.exceptions.so.SOException;
import com.github.somi92.seecskserver.model.exceptions.so.ValidacijaException;
import com.github.somi92.seecskserver.model.operations.ApstraktnaSistemskaOperacija;
import java.sql.SQLException;

/**
 *
 * @author milos
 */

/*
* ne koristi se
*/
public class SOKreirajClanarinu extends ApstraktnaSistemskaOperacija {

    private Ref<Clanarina> clanarina;

    public SOKreirajClanarinu(Ref<Clanarina> clanarina) {
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
            Clanarina c = clanarina.get();
            String m = dbbroker.getMaxColumnValue(c, "idClanarina", null);
            long max = Long.parseLong(m);
            c.setIdClanarina(max+1);
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
