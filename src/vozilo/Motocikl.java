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
public class Motocikl extends Vozilo {
    static int brojac=0;
    public Motocikl() {
          super("motor"+brojac,"BRSASIJE-M-"+brojac,"BRMOTORA-M-"+brojac,new File(Utils.PROPERTIES.getProperty("DEFAULT_PICTURE_PATH")),"REGBR-M-"+brojac,TipVozilaEnum.MOTOR); //random string istraziti....
          brojac++;
    }

    public Motocikl(String naziv, String brojSasije, String brojMotora, File foto, String registarskiBroj) {
        super(naziv, brojSasije, brojMotora, foto, registarskiBroj, TipVozilaEnum.MOTOR);
    }
    
    
    
}
