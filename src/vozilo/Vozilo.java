/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

import garageapplication.FXMLSimulationController;
import garageapplication.GarageApplication;
import garaza.Platforma;
import java.io.File;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 *
 * @author Tijana Lakic
 */
public abstract class Vozilo extends Thread implements Serializable {

    protected String naziv;
    protected String brojSasije;
    protected String brojMotora;
    protected File foto;
    protected String registarskiBroj;
    protected TipVozilaEnum tip;
    public static Random rand = new Random();
    protected File path = new File("");
    protected int x = 0, y = 0;
    protected int trenutniNivo = 0;
    protected long vrijemeParkiranja = 0;
    protected long vrijemeNapustanjaParkinga = 0;
    protected long zadrzavanje = 500;
    public static int[] X_PARKIRANJE = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6};
    public static int[] X_NOVI_NIVO = {2, 3, 4, 5, 6, 7};
    public static int[] X_NOVI_NIVO_ISPARKIRAVANJE = {0, 1, 2, 3, 4, 5, 6, 7};
    public static int[] Y_PARKIRANJE = {1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 8, 7, 6, 5, 4, 3, 2};//properties
    protected int[] parkingX = {0, 3, 4, 7};
    protected int[] parkingY = {2, 3, 4, 5, 6, 7, 8, 9};
    protected Boolean parkiran = true;

    public Vozilo() {
    }

    public Vozilo(String naziv, String brojSasije, String brojMotora, File foto, String registarskiBroj, TipVozilaEnum tip) {

        this.naziv = naziv;
        this.brojSasije = brojSasije;
        this.brojMotora = brojMotora;
        this.foto = foto;
        this.registarskiBroj = registarskiBroj;
        this.tip = tip;
        this.vrijemeParkiranja = new Date().getTime();

    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getBrojSasije() {
        return brojSasije;
    }

    public void setBrojSasije(String brojSasije) {
        this.brojSasije = brojSasije;
    }

    public String getBrojMotora() {
        return brojMotora;
    }

    public void setBrojMotora(String brojMotora) {
        this.brojMotora = brojMotora;
    }

    public File getFoto() {
        return foto;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }

    public TipVozilaEnum getTip() {
        return tip;
    }

    public String getRegistarskiBroj() {
        return registarskiBroj;
    }

    public void setRegistarskiBroj(String registarskiBroj) {
        this.registarskiBroj = registarskiBroj;
    }

    @Override
    public String toString() {
        return "V";
    }

    @Override
    public void run() {
        if (parkiran) {
            isparkiraj();
        } else {
            vozi();
        }
    }
    // @Override

    void vozi() {
        trenutniNivo = 0;
        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        int i = 0;
        for (int k = 0; k < 2; k++) {
            x = X_PARKIRANJE[i];
            y = Y_PARKIRANJE[i];
            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();
            i++;
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            platforma.getMatrica()[x][y] = null;
        } //prva dva polja pomjera se svima isto!

        while (platforma.getListaVozila().size() > 28) {

            for (int a = 0; a < X_NOVI_NIVO.length; a++) {
                platforma.getMatrica()[X_NOVI_NIVO[a]][1] = this;//mora provjeriti sudar i staviti umjesto [1] y neki
                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                platforma.getMatrica()[X_NOVI_NIVO[a]][1] = null;

            }
            platforma.getListaVozila().remove(this);
            trenutniNivo++;
            platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
            platforma.getListaVozila().add(this);
            i = 0;
        }

        while (!parkiran) {

            platforma.getMatrica()[x][y] = null;
            x = X_PARKIRANJE[i];
            y = Y_PARKIRANJE[i];
            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parkiran = parkiraj();
            i++;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

        }

    }
// U ovoj funkciji je potrebno provjeriti da ukoliko ima dva koraka do parkinga
// da li ce na prvom koraku doci do sudara, takodje nakon pomjeranja za jedno dodati jos jedno do parkinga
// to se odnosi na oba slucaja

    public Boolean getParkiran() {
        return parkiran;
    }

    public void setParkiran(Boolean parkiran) {
        this.parkiran = parkiran;
    }

    public Boolean parkiraj() {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        int udaljenostLijeva = 0;
        int udaljenostDesna = 0;
        if (x == 1) {
            udaljenostLijeva = -1;
            udaljenostDesna = 2;
        } else if (x == 6) {
            udaljenostLijeva = -2;
            udaljenostDesna = 1;
        }
        Boolean parkirao = false;

        if (udaljenostLijeva != 0) {

            int udaljenostManja = 0;
            int udaljenostVeca = 0;

            if (Math.abs(udaljenostLijeva) > Math.abs(udaljenostDesna)) {
                udaljenostManja = udaljenostDesna;
                udaljenostVeca = udaljenostLijeva;
            } else {
                udaljenostManja = udaljenostLijeva;
                udaljenostVeca = udaljenostDesna;
            }
            if ("*".equals(platforma.getMatrica()[x + udaljenostManja][y])) {
                platforma.getMatrica()[x][y] = null;
                x += udaljenostManja;
                platforma.getMatrica()[x][y] = this;
                parkirao = true;

            } else if ("*".equals(platforma.getMatrica()[x + udaljenostVeca][y])) {

                platforma.getMatrica()[x][y] = null;
                int temp = 0;
                if (udaljenostVeca < 0) {
                    temp = x - 1;
                } else {
                    temp = x + 1;
                }
                platforma.getMatrica()[temp][y] = this;
                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                platforma.getMatrica()[temp][y] = null;

                x += udaljenostVeca;
                platforma.getMatrica()[x][y] = this;

                parkirao = true;

            }
        }

        return parkirao;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTrenutniNivo() {
        return trenutniNivo;
    }

    public void setTrenutniNivo(int trenutniNivo) {
        this.trenutniNivo = trenutniNivo;
    }

    public long getZadrzavanje() {

        return zadrzavanje;
    }

    public void isparkiraj() {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        if (x == 0 || x == 4) {
            x++;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {
                sudar(platforma);
            }
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            platforma.getMatrica()[x - 1][y] = "*";

            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {
                sudar(platforma);
            }
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            platforma.getMatrica()[x - 1][y] = null;

            platforma.getMatrica()[x][y] = this;

        } else if (x == 3 || x == 7) {

            x--;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {
                sudar(platforma);
            }
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            platforma.getMatrica()[x + 1][y] = "*";

            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (y > 0) {
            y--;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {
                sudar(platforma);
            }
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            platforma.getMatrica()[x][y + 1] = null;

            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (x > 0) {
            x--;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {
                sudar(platforma);
            }
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            platforma.getMatrica()[x + 1][y] = null;

            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        platforma.getListaVozila().remove(this);
        platforma.getMatrica()[x][y] = null;
        trenutniNivo--;

        while (trenutniNivo >= 0) {

            platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
            platforma.getListaVozila().add(this);
            for (int a = X_NOVI_NIVO_ISPARKIRAVANJE.length - 1; a >= 0; a--) {

                if (platforma.getMatrica()[a][0] instanceof Vozilo) {
                    sudar(platforma);
                }
                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[X_NOVI_NIVO_ISPARKIRAVANJE[a]][0] = this;
                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                platforma.getMatrica()[X_NOVI_NIVO_ISPARKIRAVANJE[a]][0] = null;

            }
            platforma.getListaVozila().remove(this);
            trenutniNivo--;
        }
        GarageApplication.getExchanger().refreshSimulacijaMatrica();

        if (trenutniNivo == 0 && x == 0 && y == 0) {

            vrijemeNapustanjaParkinga = new Date().getTime();
            zadrzavanje = vrijemeNapustanjaParkinga - vrijemeParkiranja;

        }
    }

    public boolean sudar(Platforma platforma) {
        Random random = new Random();
        double vjerovatnoca = random.nextDouble();
        if (vjerovatnoca < 0.1) {

            platforma.setImaSudar(true);

            GarageApplication.getExchanger().getGaraza().pozivSpecijalnihVozila(x, y, trenutniNivo);
            return true;

        } else {
            return false;
        }
    }

    public void uvidjaj(Platforma platforma) {
        try {
            synchronized (platforma) {
                platforma.wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger("error.log").log(Level.SEVERE, null, ex);
        }
        platforma.setImaSudar(false);

    }

    public void uvidjaj(Platforma platforma, Object prethodnoStanjePolja) {
    }
}
