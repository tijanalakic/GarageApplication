/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import garageapplication.GarageApplication;
import garaza.Platforma;
import java.util.logging.Level;
import java.util.logging.Logger;
import vozilo.Kombi;

/**
 *
 * @author Tijana Lakic
 */
public class VatrogasniKombi extends SpecijalniKombi implements VatrogasniInterface{

    public VatrogasniKombi() {
       
        super();
    }

    @Override
    public Boolean rotacija() {
     
        return rand.nextBoolean();
    }

    @Override
    public String toString(){
    
        return "F";
    
    }
    
    @Override
    public void uvidjaj(Platforma platforma, Object prethodnoStanjePolja) {

        try {
            synchronized (platforma) {
                platforma.wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger("error.log").log(Level.SEVERE, null, ex);
        }
        platforma.getListaVozila().remove(this);
        platforma.getMatrica()[x][y] = prethodnoStanjePolja;
        GarageApplication.getExchanger().refreshSimulacijaMatrica();
    }
}
