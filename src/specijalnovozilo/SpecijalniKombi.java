/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specijalnovozilo;

import garageapplication.GarageApplication;
import garaza.Platforma;
import static java.lang.Thread.sleep;
import java.util.Map;
import vozilo.Kombi;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class SpecijalniKombi extends Kombi {

    private int koordinateNesreceX;
    private int koordinateNesreceY;
    private int nivoPlatformeNesrece;
    
    public static int SLEEP = 500;
    @Override
    public void run() {

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
                    sleep(SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (stigao()) {
                    return;
                }
                if (gornjaPutanja) {
                    x++;

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
                    sleep(SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stigao()) {
                    return;
                }
                if (!gornjaPutanja) {
                    x--;

                    platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
                    prethodnoStanjePolja = platforma.getMatrica()[x][y];
                    platforma.getMatrica()[x][y] = this;
                }

            }
            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
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
                sleep(SLEEP);
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
            sleep(SLEEP);
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

            platforma.getMatrica()[x - inkrement][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            sleep(SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean stigao() {
        return (x == koordinateNesreceX + 1 && y == koordinateNesreceY)
                || (x == koordinateNesreceX - 1 && y == koordinateNesreceY)
                || (x == koordinateNesreceX && y == koordinateNesreceY + 1)
                || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
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
            platforma.getMatrica()[x][y + 1] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeUlijevo(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = (unutrasnjaTraka) ? 2 : 1;

        while (x > odredisnaKoordinata && !stigao()) {
            x--;

            platforma.getMatrica()[x + 1][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeNadole(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = platforma.getRed() - ((unutrasnjaTraka) ? 2 : 1);

        while (y < odredisnaKoordinata && !stigao()) {

            y++;
            platforma.getMatrica()[x][y - 1] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kretanjeUdesno(Platforma platforma, Object prethodnoStanjePolja, boolean unutrasnjaTraka) {

        int odredisnaKoordinata = platforma.getKolona() - ((unutrasnjaTraka) ? 3 : 2);

        while (x < odredisnaKoordinata && !stigao()) {
            x++;

            platforma.getMatrica()[x - 1][y] = prethodnoStanjePolja;
            prethodnoStanjePolja = platforma.getMatrica()[x][y];
            platforma.getMatrica()[x][y] = this;

            GarageApplication.getExchanger().refreshSimulacijaMatrica();

            try {
                sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
