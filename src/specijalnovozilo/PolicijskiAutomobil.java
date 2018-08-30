/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import java.io.File;
import java.util.Random;
import vozilo.Automobil;

/**
 *
 * @author Tijana Lakic
 */
public class PolicijskiAutomobil extends SpecijalniAutomobil implements PolicijskiInterface{

    Random rand=new Random();
    
    
    public PolicijskiAutomobil() {
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
    
}
