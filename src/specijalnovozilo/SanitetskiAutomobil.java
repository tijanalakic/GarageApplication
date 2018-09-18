/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import garageapplication.GarageApplication;
import garaza.Platforma;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import vozilo.Automobil;

/**
 *
 * @author Tijana Lakic
 */
public class SanitetskiAutomobil extends Automobil implements SanitetskiInterface {

    public SanitetskiAutomobil() {
        super();
        oznaka = "H";
    }

    @Override
    public Boolean rotacija() {
        return rand.nextBoolean(); //rotacija samo kad se udes desi
    }

    @Override
    public void uvidjaj(Platforma platforma, Object prethodnoStanjePolja) {

        platforma.stiglaHitna = true;
        
        try {
            synchronized (platforma) {
                platforma.wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger("error.log").log(Level.SEVERE, null, ex);
        }
        platforma.getListaVozila().remove(this);
        platforma.getMatrica()[x][y] = null;
        GarageApplication.getExchanger().refreshSimulacijaMatrica();
    }

}
