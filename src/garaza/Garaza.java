/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garaza;

import garageapplication.GarageApplication;
import java.io.Serializable;
import java.util.ArrayList;
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

    public synchronized void pozivSpecijalnihVozila(int koordinataNesreceX,
            int koordinataNesreceY, int trenutniNivo) {

        Map<Vozilo, String> mjestaNesrece = GarageApplication.getExchanger().mjestaNesrece;

        boolean pozvanaPolicija = false;
        boolean pozvanSanitet = false;
        boolean pozvaniVatrogasci = false;

        int posmatraniNivo = trenutniNivo;
        boolean pozvanaVozila = false;

        while (!pozvanaVozila) {
            for (Vozilo vozilo : platforme.get(posmatraniNivo).getListaVozila()) {

                if (!pozvanaPolicija && !((Vozilo) vozilo).isAlive()) {
                    if (vozilo instanceof PolicijskiInterface) {
                        mjestaNesrece.put(vozilo, koordinataNesreceX + " " + koordinataNesreceY + " " + trenutniNivo);
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
            if (trenutniNivo == 1 && posmatraniNivo == 1) {
                posmatraniNivo++;
            } else if (trenutniNivo == platforme.size() - 1 && posmatraniNivo == platforme.size() - 1) {
                posmatraniNivo--;
            } else if (trenutniNivo == posmatraniNivo) {
                posmatraniNivo++;
            } else if (trenutniNivo >= 1 && posmatraniNivo > trenutniNivo) {
                posmatraniNivo -= 2;
            } else {
                pozvanaVozila = true;
            }
        }
    }

}
