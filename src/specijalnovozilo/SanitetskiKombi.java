/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import vozilo.Kombi;

/**
 *
 * @author Tijana Lakic
 */
public class SanitetskiKombi extends SpecijalniKombi implements SanitetskiInterface {

    public SanitetskiKombi() {
        
        super();
    }

    @Override
    public Boolean rotacija() {
        return rand.nextBoolean();
    }

        @Override 
    public String toString(){
        
        return "H";
    
    
    }
    
}
