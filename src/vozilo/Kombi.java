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
public class Kombi extends Vozilo {
    double nosivost=5000.00;
    static int brojac=0;
   

    public Kombi() {
        super("kombi"+brojac,"BRSASIje-K-"+brojac,"BRMOTORA-K-"+brojac,new File(Utils.PROPERTIES.getProperty("DEFAULT_PICTURE_PATH")),"REGBR-K-"+brojac,TipVozilaEnum.KOMBI); //random string istraziti....
        nosivost=rand.nextDouble()*10000;
        brojac++;
    }

    public Kombi(String naziv, String brojSasije, String brojMotora, File foto, String registarskiBroj,double nosivost) {
        super(naziv, brojSasije, brojMotora, foto, registarskiBroj, TipVozilaEnum.KOMBI);
        
         this.nosivost = nosivost;
    }

 

    public double getNosivost() {
        return nosivost;
    }

    public void setNosivost(double nosivost) {
        this.nosivost = nosivost;
    }
    
    
}
