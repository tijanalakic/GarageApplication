/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garaza;

import java.io.Serializable;
import java.util.ArrayList;
import utils.Utils;

/**
 *
 * @author Tijana Lakic
 */
public class Garaza implements Serializable {

    private ArrayList<Platforma> platforme;
    public int brojPlatformi = Integer.parseInt(Utils.PROPERTIES.getProperty("BROJ_PLATFORMI"));

    public int getBrojPlatformi() {
        return brojPlatformi;
    }

    public void setBrojPlatformi(int brojPlatformi) {
        this.brojPlatformi = brojPlatformi;
    }

    public Garaza() {

        platforme = new ArrayList<Platforma>();
        for (int i = 0; i < brojPlatformi; i++) {

            platforme.add(new Platforma());
        }
    }

    public ArrayList<Platforma> getPlatforme() {
        return platforme;
    }

    public void setPlatforme(ArrayList<Platforma> platforme) {
        this.platforme = platforme;
    }
    
     public void addPlatforma(Platforma platforma) {
        platforme.add(platforma);
    }
    
    
}
