/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.seecsk.model.operations;

import com.github.somi92.seecsk.model.exceptions.so.PreduslovException;
import com.github.somi92.seecsk.model.exceptions.so.SOException;
import com.github.somi92.seecsk.model.exceptions.so.ValidacijaException;
import com.github.somi92.sqldbb.broker.DBBroker;

/**
 *
 * @author milos
 */
public abstract class ApstraktnaSistemskaOperacija {
    
    protected DBBroker dbbroker;
    
    public ApstraktnaSistemskaOperacija() {
        dbbroker = new DBBroker();
    }
    
    public void izvrsiSistemskuOperaciju() throws Exception {
        try {
            
            otvoriBazuPodataka();
            
            izvrsiValidaciju();
            proveriPreduslove();
            izvrsiDBTransakciju();
            
            potvrdiDBTransakciju();
            
        } catch (ValidacijaException ex) {
            throw new Exception(ex.getMessage());
        } catch (PreduslovException ex) {
            throw new Exception(ex.getMessage());
        } catch (SOException ex) {
            ponistiDBTransakciju();
            throw new Exception(ex.getMessage());
        } finally {
            zatvoriBazuPodataka();
        }
    }
    
    private void otvoriBazuPodataka() {
        dbbroker.openDatabaseConnection();
    }
    
    private void zatvoriBazuPodataka() {
        dbbroker.closeDatabaseConnection();
    }
    
    private void potvrdiDBTransakciju() {
        dbbroker.commitTransaction();
    }
    
    private void ponistiDBTransakciju() {
        dbbroker.rollbackTransaction();
    }
    
    protected abstract void izvrsiValidaciju() throws ValidacijaException;
    protected abstract void proveriPreduslove() throws PreduslovException;
    protected abstract void izvrsiDBTransakciju() throws SOException;
    
    public abstract String vratiImeOperacije();
}
