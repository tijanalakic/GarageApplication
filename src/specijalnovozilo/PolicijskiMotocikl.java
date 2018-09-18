/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import garageapplication.GarageApplication;
import garaza.Platforma;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import utils.Utils;
import vozilo.Motocikl;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class PolicijskiMotocikl extends Motocikl implements PolicijskiInterface {

    public PolicijskiMotocikl() {

        super();
        oznaka = "P";
    }

    @Override
    public Boolean rotacija() {

        return rand.nextBoolean();
    }

    @Override
    public void uvidjaj(Platforma platforma, Object prethodnoStanjePolja) {

        int trajanjaUvidjaja = 3000 + (new Random().nextInt(7) * 1000);
        try {

            sleep(trajanjaUvidjaja);
            platforma.getListaVozila().remove(this);
            platforma.getMatrica()[x][y] = null;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();
            ArrayList<Vozilo> unesrecenaVozila = GarageApplication.getExchanger().ucesniciUNesreci.get(this);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
            String nazivFajla = Utils.PROPERTIES.getProperty("ZAPISNICI_FILE_PATH") + File.separator + formatter.format(new Date());
            String prvoVozilo = unesrecenaVozila.get(0).getNaziv();
            String drugoVozilo = unesrecenaVozila.get(1).getNaziv();
            byte[] prvoVoziloSlika = Utils.ucitajSliku(unesrecenaVozila.get(0).getFoto().getAbsolutePath());
            byte[] drugoVoziloSlika = Utils.ucitajSliku(unesrecenaVozila.get(1).getFoto().getAbsolutePath());

            Utils.upisi(prvoVozilo.getBytes(), nazivFajla);
            Utils.upisi(drugoVozilo.getBytes(), nazivFajla);
            Utils.upisi(prvoVoziloSlika, nazivFajla);
            Utils.upisi(drugoVoziloSlika, nazivFajla);

            Utils.upisiUPotjernicu(unesrecenaVozila.get(0).getRegistarskiBroj());
            Utils.upisiUPotjernicu(unesrecenaVozila.get(1).getRegistarskiBroj());

            while (!(platforma.stiglaHitna && platforma.stigliVatrogasci)) {
            }
            
            synchronized (platforma) {
                platforma.notifyAll();
            }
            platforma.stiglaHitna = false;
            platforma.stigliVatrogasci = false;
            refreshPrinudni(platforma);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
