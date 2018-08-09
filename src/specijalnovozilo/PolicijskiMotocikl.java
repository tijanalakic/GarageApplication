/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import vozilo.Motocikl;

/**
 *
 * @author Tijana Lakic
 */
public class PolicijskiMotocikl extends Motocikl implements PolicijskiInterface{

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
    
    
    
    
}
