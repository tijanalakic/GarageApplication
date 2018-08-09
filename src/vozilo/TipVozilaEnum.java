/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

/**
 *
 * @author Tijana Lakic
 */
public enum TipVozilaEnum {
    AUTO("Automobil"),
    KOMBI("Kombi"),
    MOTOR("Motocikl");
    String tip;

    private TipVozilaEnum(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return tip;
    }
    
    
}
