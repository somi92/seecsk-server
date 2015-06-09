/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecskserver.model.operations.zaposleni;

import com.github.somi92.seecskcommon.domain.Zaposleni;
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
public class SOPronadjiAdministratora extends ApstraktnaSistemskaOperacija {
    
    private Ref<Zaposleni> zaposleni;
    
    public SOPronadjiAdministratora(Ref<Zaposleni> zaposleni) {
        this.zaposleni = zaposleni;
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
            List<String> kriterijumPretrage = new ArrayList<>();
            kriterijumPretrage.add("korisnickoIme");
            kriterijumPretrage.add("sifra");
            List<Zaposleni> listaZaposlenih = dbbroker.loadEntities(zaposleni.get(), kriterijumPretrage, false);
            if(listaZaposlenih != null && listaZaposlenih.size() == 1) {
                zaposleni.set(listaZaposlenih.get(0));
            } else {
                zaposleni.set(null);
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
