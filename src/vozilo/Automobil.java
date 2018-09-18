/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

import java.io.File;
import utils.Utils;

/**
 *
 * @author Tijana Lakic
 */
public class Automobil extends Vozilo {
    int brojVrata=5;
    int[] brojVrataAuta={3,5,7};
    static int brojac=0;
    

    public Automobil() {
        super("Auto"+brojac,"BRSASIJE-A-"+brojac,"BRMOTORA-A-"+brojac,new File(Utils.PROPERTIES.getProperty("DEFAULT_PICTURE_PATH")),"REGBR-A-"+brojac,TipVozilaEnum.AUTO); 
        brojVrata=brojVrataAuta[rand.nextInt(3)];
        brojac++;
    }
    
    public Automobil(String naziv, String brojSasije, String brojMotora, File foto, String registarskiBroj, int brojVrata) {
        super(naziv, brojSasije, brojMotora, foto, registarskiBroj, TipVozilaEnum.AUTO);
        brojVrata=this.brojVrata;
    }

    public int getBrojVrata() {
        return brojVrata;
    }

    public void setBrojVrata(int brojVrata) {
        this.brojVrata = brojVrata;
    }

    
    
    
}
