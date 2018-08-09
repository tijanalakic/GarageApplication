/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

/**
 *
 * @author Tijana Lakic
 */
public interface SanitetskiInterface extends InstitutionalInterface{
 
    PrioritetEnum PRIORITET = PrioritetEnum.URGENT;

    
    @Override
    Boolean rotacija();
    

}
