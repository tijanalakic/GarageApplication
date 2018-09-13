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
import vozilo.Motocikl;

/**
 *
 * @author Tijana Lakic
 */
public class PolicijskiMotocikl extends SpecijalniMotocikl implements PolicijskiInterface{

    public PolicijskiMotocikl() {
        
        super();
    }

    @Override
    public Boolean rotacija() {

        return rand.nextBoolean();
    }

       @Override 
    public String toString(){
        
        return "P";
    
    
    }
    
       @Override
    public void uvidjaj(Platforma platforma, Object prethodnoStanjePolja){
        
         int trajanjaUvidjaja = 3000 + (new Random().nextInt(7)*1000);
         try {
            
            sleep(trajanjaUvidjaja);
            platforma.getListaVozila().remove(this);
            platforma.getMatrica()[x][y] = prethodnoStanjePolja;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            synchronized (platforma) {
                platforma.notifyAll();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
