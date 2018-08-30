/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import garageapplication.GarageApplication;
import garaza.Platforma;
import static java.lang.Thread.sleep;
import vozilo.Kombi;
import vozilo.Vozilo;
import static vozilo.Vozilo.X_NOVI_NIVO_ISPARKIRAVANJE;

/**
 *
 * @author Tijana Lakic
 */
public class SpecijalniKombi extends Kombi {

    @Override
    public void run() {
        int koordinateNesreceX = GarageApplication.getExchanger().KOORDINATE_NESRECE_X;
        int koordinateNesreceY = GarageApplication.getExchanger().KOORDINATE_NESRECE_Y;
        int nivoPlatformeNesrece = GarageApplication.getExchanger().NIVO_PLATFORME_NESRECE;

        boolean gornjaPutanja = false;

        Object prethodnoStanjePolja = null;
        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        if (koordinateNesreceX < platforma.getRed() / 2) {
            gornjaPutanja = true;
        }

        if (trenutniNivo != nivoPlatformeNesrece) {
            predjiNaDruguPlatformu(nivoPlatformeNesrece);
        } // ovdje treba napraviti provjeru da li se udes desio ispred parkiranog vozila

        if (x == 0 || x == 4) {
            x++;
            platforma.getMatrica()[x - 1][y] = "*";
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gornjaPutanja) {
                x++;

                platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;
            }

        } else if (x == 3 || x == 7) {

            x--;

            platforma.getMatrica()[x + 1][y] = "*";
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            if (!gornjaPutanja) {
                x--;

                platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;
            }

        }
        GarageApplication.getExchanger().refreshSimulacijaMatrica();

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (gornjaPutanja) {
            gornjaPutanja(koordinateNesreceX, koordinateNesreceY, prethodnoStanjePolja);
        }

        try {
            sleep(3000);
        } catch (InterruptedException ex) {

        }
    }

    public void gornjaPutanja(int koordinateNesreceX, int koordinateNesreceY, Object prethodnoStanjePolja) {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        boolean stigao = false;

        if (x == 6) {
            while (y > 0 && !stigao) {

                y--;
                platforma.getMatrica()[x][y + 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x >= koordinateNesreceX) && y == koordinateNesreceY;
            }

            while (x > 1 && !stigao) {
                x--;

                platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x == koordinateNesreceX) && y >= koordinateNesreceY;
            }

            while (y < platforma.getRed() && !stigao) {

                y++;
                platforma.getMatrica()[x][y - 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x >= koordinateNesreceX) && y == koordinateNesreceY;
            }
        } else if (x == 2) {

            while (y < 1 && !stigao) {

                y--;
                platforma.getMatrica()[x][y + 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x >= koordinateNesreceX) && y == koordinateNesreceY;
            }

            while (x < platforma.getKolona() - 1 && !stigao) {
                x++;

                platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x == koordinateNesreceX) && y >= koordinateNesreceY;
            }

            while (y < platforma.getKolona() - 1 && !stigao) {

                y++;
                platforma.getMatrica()[x][y - 1] = prethodnoStanjePolja;
                prethodnoStanjePolja = platforma.getMatrica()[x][y];
                platforma.getMatrica()[x][y] = this;

                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stigao = (x >= koordinateNesreceX) && y == koordinateNesreceY;
            }

        }
    }

    public void donjaPutanja() {

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
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                platforma.getMatrica()[temp][y] = null;

                x += udaljenostVeca;
                platforma.getMatrica()[x][y] = this;

                parkirao = true;

            }
        }
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
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;

            platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

        } else if (x == 3 || x == 7) {

            x--;

            platforma.getMatrica()[x + 1][y] = "*";
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

        }
        GarageApplication.getExchanger().refreshSimulacijaMatrica();

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (y > 0) {
            y--;
            platforma.getMatrica()[x][y + 1] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // ovdje srediti da se izvuce gore pa onda lijevo ili desno
        
        int odredisnaKoordinata = 0;
        int inkrement = -1;
        if (trenutniNivo < nivoPlatformeNesrece) {
            odredisnaKoordinata = 7;
            inkrement = 1;
        }
        while (x != odredisnaKoordinata) {
            x += inkrement;

            platforma.getMatrica()[x - inkrement][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        platforma.getListaVozila().remove(this);
        platforma.getMatrica()[x][y] = prethodnoStanjePolja;
        trenutniNivo += inkrement;

    }
}
