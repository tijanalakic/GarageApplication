/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

import garageapplication.FXMLSimulationController;
import garageapplication.GarageApplication;
import garageapplication.RootExchanger;
import garaza.Platforma;
import java.io.File;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Map;
import specijalnovozilo.InstitutionalInterface;
import specijalnovozilo.PolicijskiAutomobil;
import specijalnovozilo.PolicijskiInterface;
import specijalnovozilo.SanitetskiAutomobil;
import specijalnovozilo.SanitetskiInterface;
import specijalnovozilo.SanitetskiKombi;
import specijalnovozilo.VatrogasniInterface;
import specijalnovozilo.VatrogasniKombi;

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
    protected long vrijemeZadrzavanjaUGarazi;
    public static int[] X_PARKIRANJE = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6};
    public static int[] X_NOVI_NIVO = {2, 3, 4, 5, 6, 7};
    public static int[] X_NOVI_NIVO_ISPARKIRAVANJE = {0, 1, 2, 3, 4, 5, 6, 7};
    public static int[] Y_PARKIRANJE = {1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 8, 7, 6, 5, 4, 3, 2};//properties
    protected int[] parkingX = {0, 3, 4, 7};
    protected int[] parkingY = {2, 3, 4, 5, 6, 7, 8, 9};
    protected Boolean parkiran = true;
    protected String oznaka = "V";
    //protected transient Object specijalnoVoziloLock = new Object();

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

        return oznaka;
    }

    @Override
    public void run() {
        if (this instanceof InstitutionalInterface) {
            oznaka = oznaka + "R";
            intervencija();
        } else if (parkiran) {
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
            if (platforma.getMatrica()[x][y] instanceof Vozilo && !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                sudar(platforma, (Vozilo) platforma.getMatrica()[x][y]);
            }
            synchronized (platforma) {

                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[x - 1][y] = "*";

                platforma.getMatrica()[x][y] = this;
                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//ispod

            x++;
            if (platforma.getMatrica()[x][y] instanceof Vozilo && !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                sudar(platforma, (Vozilo) platforma.getMatrica()[x][y]);
            }
            synchronized (platforma) {

                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[x - 1][y] = null;

                platforma.getMatrica()[x][y] = this;
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (x == 3 || x == 7) {

            x--;
            if (platforma.getMatrica()[x][y] instanceof Vozilo && !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                sudar(platforma, (Vozilo) platforma.getMatrica()[x][y]);
            }
            synchronized (platforma) {

                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[x + 1][y] = "*";

                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (y > 0) {
            y--;
            if (platforma.getMatrica()[x][y] instanceof Vozilo) {//&& !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                if (!sudar(platforma, (Vozilo) platforma.getMatrica()[x][y])) {
                    y++;
                    try {
                        sleep(zadrzavanje);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            synchronized (platforma) {
                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[x][y + 1] = null;

                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int waitCount = 0;
        int waitTrigger = 5;
        while (x > 0) {
            x--;
            if (platforma.isImaSudar()) {
                uvidjaj(platforma);
            }
            if (platforma.getMatrica()[x][y] instanceof Vozilo && !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                x++;
                waitCount++;
                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (waitCount > waitTrigger) {
                    refreshPrinudni(platforma);
                }
                continue;
            }
            waitCount = 0;
            synchronized (platforma) {
                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                platforma.getMatrica()[x + 1][y] = null;

                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        platforma.getMatrica()[x][y] = null;
        platforma.getListaVozila().remove(this);
        GarageApplication.getExchanger().refreshSimulacijaMatrica();
        trenutniNivo--;

        while (trenutniNivo >= 0) {

            platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
            platforma.getListaVozila().add(this);
            x = X_NOVI_NIVO_ISPARKIRAVANJE[X_NOVI_NIVO_ISPARKIRAVANJE.length - 1];
            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int a = X_NOVI_NIVO_ISPARKIRAVANJE.length - 2; a >= 0; a--) {
                int xBefore = X_NOVI_NIVO_ISPARKIRAVANJE[a + 1];
                x = X_NOVI_NIVO_ISPARKIRAVANJE[a];
                if (platforma.isImaSudar()) {
                    uvidjaj(platforma);
                }
                if (platforma.getMatrica()[x][y] instanceof Vozilo && !(platforma.getMatrica()[x][y] instanceof InstitutionalInterface)) {
                    // sudar(platforma);
                    a++;
                    waitCount++;
                    try {
                        sleep(zadrzavanje);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (waitCount > waitTrigger) {
                        refreshPrinudni(platforma);
                    }
                    continue;
                }
                waitCount = 0;

                synchronized (platforma) {
                    if (platforma.isImaSudar()) {
                        uvidjaj(platforma);
                    }
                    platforma.getMatrica()[xBefore][y] = null;
                    platforma.getMatrica()[x][y] = this;
                    GarageApplication.getExchanger().refreshSimulacijaMatrica();
                }
                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            platforma.getListaVozila().remove(this);
            platforma.getMatrica()[x][y] = null;
            trenutniNivo--;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

        }

        if (trenutniNivo == -1 && x == 0 && y == 0) {

            vrijemeNapustanjaParkinga = new Date().getTime();
            vrijemeZadrzavanjaUGarazi = vrijemeNapustanjaParkinga - vrijemeParkiranja;
            GarageApplication.getExchanger().getGaraza().naplatiParking(this, vrijemeZadrzavanjaUGarazi);
        }
    }

    private int koordinateNesreceX;
    private int koordinateNesreceY;
    private int nivoPlatformeNesrece;

    public void intervencija() {

        int pocetnaX = x;
        int pocetnaY = y;
        int pocetniNivo = trenutniNivo;
        Map<Vozilo, String> mjestaNesrece = GarageApplication.getExchanger().mjestaNesrece;
        String mjestoNesrece[] = mjestaNesrece.get(this).split(" ");
        koordinateNesreceX = Integer.parseInt(mjestoNesrece[0]);
        koordinateNesreceY = Integer.parseInt(mjestoNesrece[1]);
        nivoPlatformeNesrece = Integer.parseInt(mjestoNesrece[2]);

        Object prethodnoStanjePolja = null;

        while (trenutniNivo != nivoPlatformeNesrece) {
            predjiNaDruguPlatformu(nivoPlatformeNesrece);
        }

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        boolean gornjaPutanja = odabirPutanje(platforma);

        //provjera da li je udes ispred parkiranog vozila
        if (!stigao()) {

            if (x == 0 || x == 4) {
                x++;
                if (y > 1) {
                    platforma.getMatrica()[x - 1][y] = "*";
                } else {
                    platforma.getMatrica()[x - 1][y] = "";
                }
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;
                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (stigao()) {
                    uvidjaj(platforma, prethodnoStanjePolja);
                    return;
                }
                if (gornjaPutanja) {
                    x++;
                    provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
                    platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
                    prethodnoStanjePolja = platforma.getMatrica()[x][y];
                    platforma.getMatrica()[x][y] = this;
                }

            } else if (x == 3 || x == 7) {

                x--;

                if (y > 1) {
                    platforma.getMatrica()[x + 1][y] = "*";
                } else {
                    platforma.getMatrica()[x + 1][y] = "";
                }
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(zadrzavanje);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stigao()) {
                    uvidjaj(platforma, prethodnoStanjePolja);
                    return;
                }
                if (!gornjaPutanja) {
                    x--;
                    provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
                    platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
                    prethodnoStanjePolja = platforma.getMatrica()[x][y];
                    platforma.getMatrica()[x][y] = this;
                }

            }
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gornjaPutanja) {
                prethodnoStanjePolja = gornjaPutanja(koordinateNesreceX, koordinateNesreceY, prethodnoStanjePolja);
            } else {
                prethodnoStanjePolja = donjaPutanja(koordinateNesreceX, koordinateNesreceY, prethodnoStanjePolja);
            }
        }

        uvidjaj(platforma, prethodnoStanjePolja);
        synchronized (platforma) {
            platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(pocetniNivo);
            Vozilo vozilo = null;

            if (this instanceof PolicijskiInterface) {

                vozilo = new PolicijskiAutomobil();
                vozilo.setX(pocetnaX);
                vozilo.setY(pocetnaY);
                vozilo.setTrenutniNivo(trenutniNivo);
                platforma.getMatrica()[pocetnaX][pocetnaY] = vozilo;
                platforma.setElement(vozilo);

            } else if (this instanceof VatrogasniInterface) {

                vozilo = new VatrogasniKombi();
                vozilo.setX(pocetnaX);
                vozilo.setY(pocetnaY);
                vozilo.setTrenutniNivo(trenutniNivo);
                platforma.getMatrica()[pocetnaX][pocetnaY] = vozilo;
                platforma.setElement(vozilo);

            } else if (this instanceof SanitetskiInterface) {

                vozilo = new SanitetskiAutomobil();
                vozilo.setX(pocetnaX);
                vozilo.setY(pocetnaY);
                vozilo.setTrenutniNivo(trenutniNivo);
                platforma.getMatrica()[pocetnaX][pocetnaY] = vozilo;
                platforma.setElement(vozilo);
            }
        }
    }

    public boolean sudar(Platforma platforma, Vozilo udarenoVozilo) {
        Random random = new Random();
        double vjerovatnoca = random.nextInt(100);
        if (vjerovatnoca <= 10) {
            synchronized (platforma) {

                platforma.setImaSudar(true);
                GarageApplication.getExchanger().getGaraza().pozivSpecijalnihVozila(this, udarenoVozilo);
            }
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

    public Object gornjaPutanja(int koordinateNesreceX, int koordinateNesreceY, Object prethodnoStanjePolja) {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        if (x == 6) {

            kretanjeUspravno(platforma, prethodnoStanjePolja, false);
            kretanjeUlijevo(platforma, prethodnoStanjePolja, false);
            kretanjeNadole(platforma, prethodnoStanjePolja, false);

        } else if (x == 2) {

            kretanjeUspravno(platforma, prethodnoStanjePolja, true);
            kretanjeUdesno(platforma, prethodnoStanjePolja, true);
            kretanjeNadole(platforma, prethodnoStanjePolja, true);

        }
        return prethodnoStanjePolja;
    }

    public Object donjaPutanja(int koordinateNesreceX, int koordinateNesreceY, Object prethodnoStanjePolja) {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        if (x == 5) {

            kretanjeNadole(platforma, prethodnoStanjePolja, true);
            kretanjeUlijevo(platforma, prethodnoStanjePolja, true);
            kretanjeUspravno(platforma, prethodnoStanjePolja, true);

        } else if (x == 1) {

            kretanjeNadole(platforma, prethodnoStanjePolja, false);
            kretanjeUdesno(platforma, prethodnoStanjePolja, false);
            kretanjeUspravno(platforma, prethodnoStanjePolja, false);
        }

        return prethodnoStanjePolja;
    }

    public void predjiNaDruguPlatformu(int nivoPlatformeNesrece) {
        Object prethodnoStanjePolja = null;

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        if (x == 0 || x == 4) {
            x++;
            platforma.getMatrica()[x - 1][y] = "*";
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
            prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);

            platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

        } else if (x == 3 || x == 7) {

            x--;

            platforma.getMatrica()[x + 1][y] = "*";
            prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);

            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

        }
        GarageApplication.getExchanger().refreshSimulacijaMatrica();

        try {
            sleep(zadrzavanje);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean unutrasnjaTraka = false;
        if (trenutniNivo < nivoPlatformeNesrece) {
            unutrasnjaTraka = true;
        }
        kretanjeUspravno(platforma, prethodnoStanjePolja, unutrasnjaTraka);

        int odredisnaKoordinata = 0;
        int inkrement = -1;
        if (trenutniNivo < nivoPlatformeNesrece) {
            odredisnaKoordinata = 7;
            inkrement = 1;
        }
        while (x != odredisnaKoordinata) {
            x += inkrement;
            prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
            platforma.getMatrica()[x - inkrement][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
        platforma.getMatrica()[x][y] = prethodnoStanjePolja;
        if (odredisnaKoordinata == 0) {
            x = 7;
        } else if (odredisnaKoordinata == 7) {
            x = 0;
        }

        platforma.getListaVozila().remove(this);
        trenutniNivo += inkrement;
        platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        platforma.getMatrica()[x][y] = this;
        platforma.getListaVozila().add(this);
        GarageApplication.getExchanger().refreshSimulacijaMatrica();

        try {
            sleep(zadrzavanje);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean stigao() {
        boolean stigao = false;

        if ((x == 2 && y == 1) && (koordinateNesreceX < x && koordinateNesreceY <= y)) {
            stigao = true;
        } else if ((x == 5 && y == 1) && (koordinateNesreceX > x && koordinateNesreceY <= y)) {
            stigao = true;
        } else {
            return (x == koordinateNesreceX + 1 && y == koordinateNesreceY)
                    || (x == koordinateNesreceX - 1 && y == koordinateNesreceY)
                    || (x == koordinateNesreceX && y == koordinateNesreceY + 1)
                    || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
        }
        return stigao;
    }

    public boolean odabirPutanje(Platforma platforma) {

        boolean gornjaPutanja = false;
        // provjera gdje se nalazi vozilo,   bi se odabrala odgovarajuca putanja gornja/donja
        if (koordinateNesreceY < (platforma.getRed() / 2)) {
            gornjaPutanja = true;

            if (koordinateNesreceY > y) {
                //racuna se opseg da bi se znalo da li je nesreca neporedno udaljena od specijalnog vozila
                if (x == 0 || x == 4) {
                    if (koordinateNesreceX > x && koordinateNesreceX <= x + 3) {
                        gornjaPutanja = false;
                    }
                } else if (x == 3 || x == 7) {
                    if (koordinateNesreceX < x && koordinateNesreceX >= x - 3) {
                        gornjaPutanja = false;
                    }
                }

            }
        } else {

            gornjaPutanja = false;
            //racuna se opseg da bi se znalo da li je nesreca neporedno udaljena od specijalnog vozila
            if (koordinateNesreceY < y) {

                if (x == 0 || x == 4) {
                    if (koordinateNesreceX > x && koordinateNesreceX <= x + 3) {
                        gornjaPutanja = true;
                    }
                } else if (x == 3 || x == 7) {
                    if (koordinateNesreceX < x && koordinateNesreceX >= x - 3) {
                        gornjaPutanja = true;
                    }
                }
            }
        }
        return gornjaPutanja;
    }

    public void kretanjeUspravno(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = (unutrasnjaTraka) ? 1 : 0;

        while (y > odredisnaKoordinata && !stigao()) {

            y--;
            synchronized (platforma.lock) {
                prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
                platforma.getMatrica()[x][y + 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeUlijevo(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = (unutrasnjaTraka) ? 2 : 1;

        while (x > odredisnaKoordinata && !stigao()) {
            x--;
            synchronized (platforma.lock) {
                prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);
                platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeNadole(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = platforma.getRed() - ((unutrasnjaTraka) ? 2 : 1);

        while (y < odredisnaKoordinata && !stigao()) {

            y++;
            synchronized (platforma.lock) {
                prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);

                platforma.getMatrica()[x][y - 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeUdesno(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = platforma.getKolona() - ((unutrasnjaTraka) ? 3 : 2);

        while (x < odredisnaKoordinata && !stigao()) {
            x++;
            synchronized (platforma.lock) {
                prethodnoStanjePolja = provjeraPrethodnogStanjaPolja(prethodnoStanjePolja);

                platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();
            }
            try {
                sleep(zadrzavanje);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshPrinudni(Platforma platforma) {
        Object matrica[][] = platforma.getMatrica();

        for (int i = 0; i < platforma.getRed(); i++) {
            for (int j = 0; j < platforma.getKolona(); j++) {
                if (matrica[j][i] instanceof Vozilo) {

                    Vozilo vozilo = (Vozilo) matrica[j][i];
                    if (vozilo.getX() != j || vozilo.getY() != i) {
                        matrica[j][i] = null;
                    }
                }
            }
        }
    }

    public Object provjeraPrethodnogStanjaPolja(Object prethodnoStanjePolja) {
        if (this instanceof InstitutionalInterface && prethodnoStanjePolja instanceof InstitutionalInterface) {
            System.out.println("usao : " + x + " " + y + prethodnoStanjePolja);
            prethodnoStanjePolja = null;
        }
        return prethodnoStanjePolja;
    }

    public Date getVrijemeUlaska() {
        return new Date(vrijemeParkiranja);
    }

}
