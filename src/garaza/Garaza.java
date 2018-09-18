/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garaza;

import garageapplication.GarageApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import specijalnovozilo.PolicijskiInterface;
import specijalnovozilo.SanitetskiInterface;
import specijalnovozilo.VatrogasniInterface;
import utils.Utils;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class Garaza implements Serializable {

    private ArrayList<Platforma> platforme;
    private HashMap<Vozilo, String> evidencijaNaplateParkinga = new HashMap<>();

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

    public synchronized void pozivSpecijalnihVozila(Vozilo prvoVozilo, Vozilo drugoVozilo) {

        int koordinataNesreceX = prvoVozilo.getX();
        int koordinataNesreceY = prvoVozilo.getY();
        int trenutniNivo = prvoVozilo.getTrenutniNivo();
        Map<Vozilo, String> mjestaNesrece = GarageApplication.getExchanger().mjestaNesrece;
        Map<Vozilo, ArrayList<Vozilo>> ucesniciUNesreci = GarageApplication.getExchanger().ucesniciUNesreci;

        boolean pozvanaPolicija = false;
        boolean pozvanSanitet = false;
        boolean pozvaniVatrogasci = false;

        int posmatraniNivo = trenutniNivo;
        boolean pozvanaVozila = false;
        ArrayList<Vozilo> unesrecenaVozila = new ArrayList<>();
        unesrecenaVozila.add(prvoVozilo);
        unesrecenaVozila.add(drugoVozilo);
        while (!pozvanaVozila) {
            for (Vozilo vozilo : platforme.get(posmatraniNivo).getListaVozila()) {

                if (!pozvanaPolicija && !((Vozilo) vozilo).isAlive()) {
                    if (vozilo instanceof PolicijskiInterface) {
                        mjestaNesrece.put(vozilo, koordinataNesreceX + " " + koordinataNesreceY + " " + trenutniNivo);
                        ucesniciUNesreci.put(vozilo, unesrecenaVozila);
                        vozilo.start();
                        pozvanaPolicija = true;
                        continue;
                    }
                }
                if (!pozvanSanitet && !((Vozilo) vozilo).isAlive()) {
                    if (vozilo instanceof SanitetskiInterface) {
                        mjestaNesrece.put(vozilo, koordinataNesreceX + " " + koordinataNesreceY + " " + trenutniNivo);
                        vozilo.start();
                        pozvanSanitet = true;
                        continue;
                    }
                }
                if (!pozvaniVatrogasci && !((Vozilo) vozilo).isAlive()) {
                    if (vozilo instanceof VatrogasniInterface) {
                        mjestaNesrece.put(vozilo, koordinataNesreceX + " " + koordinataNesreceY + " " + trenutniNivo);
                        vozilo.start();
                        pozvaniVatrogasci = true;
                        continue;
                    }
                }

                if (pozvaniVatrogasci && pozvanSanitet && pozvanaPolicija) {
                    pozvanaVozila = true;
                    break;
                }
            }
//            if (trenutniNivo == 1 && posmatraniNivo == 1) {
//                posmatraniNivo++;
//            } else if (trenutniNivo == platforme.size() - 1 && posmatraniNivo == platforme.size() - 1) {
//                posmatraniNivo--;
//            } else if (trenutniNivo == posmatraniNivo) {
//                posmatraniNivo++;
//            } else if (trenutniNivo >= 1 && posmatraniNivo > trenutniNivo) {
//                posmatraniNivo -= 2;
//            } else {
//                pozvanaVozila = true;
//            }
            pozvanaVozila = true;
        }
    }

    public void naplatiParking(Vozilo vozilo, long vrijemeZadrzavanjaUGarazi) {
        String cijena = "";
        if (vrijemeZadrzavanjaUGarazi <= 3600000) {
            cijena = Utils.PROPERTIES.getProperty("CIJENA_SAT");
        } else if (vrijemeZadrzavanjaUGarazi < 10800000) {
            cijena = Utils.PROPERTIES.getProperty("CIJENA_3_SATA");
        } else {
            cijena = Utils.PROPERTIES.getProperty("CIJENA_DAN");
        }
        evidencijaNaplateParkinga.put(vozilo, cijena);
    }

    public HashMap<Vozilo, String> getEvidencijaNaplateParkinga() {
        return evidencijaNaplateParkinga;
    }

    public void setEvidencijaNaplateParkinga(HashMap<Vozilo, String> evidencijaNaplateParkinga) {
        this.evidencijaNaplateParkinga = evidencijaNaplateParkinga;
    }

}
