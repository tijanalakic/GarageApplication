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
public interface PolicijskiInterface extends InstitutionalInterface{
    
    PrioritetEnum PRIORITET = PrioritetEnum.MEDIUM;
    
    @Override
    Boolean rotacija();
  
}
