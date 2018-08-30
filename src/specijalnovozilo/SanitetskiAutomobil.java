/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import vozilo.Automobil;

/**
 *
 * @author Tijana Lakic
 */
public class SanitetskiAutomobil extends SpecijalniAutomobil implements SanitetskiInterface{

    public SanitetskiAutomobil() {
        super();
    }

    @Override
    public Boolean rotacija() {
        return rand.nextBoolean(); //rotacija samo kad se udes desi
    }

    @Override 
    public String toString(){
        
        return "H";
    
    
    }
    
    
    
}
