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
import static vozilo.Vozilo.X_NOVI_NIVO_ISPARKIRAVANJE;

/**
 *
 * @author Tijana Lakic
 */
public class SpecijalniKombi extends Kombi {

    private int koordinateNesreceX;
    private int koordinateNesreceY;
    private int nivoPlatformeNesrece;

    @Override
    public void run() {

        Map<Vozilo, String> mjestaNesrece = GarageApplication.getExchanger().mjestaNesrece;
        String mjestoNesrece[] = mjestaNesrece.get(this).split(" ");
        koordinateNesreceX = Integer.parseInt(mjestoNesrece[0]);
        koordinateNesreceY = Integer.parseInt(mjestoNesrece[1]);
        nivoPlatformeNesrece = Integer.parseInt(mjestoNesrece[2]);

        System.out.println(koordinateNesreceX + " " + koordinateNesreceY + " " + nivoPlatformeNesrece);
        boolean gornjaPutanja = false;

        Object prethodnoStanjePolja = null;

        while (trenutniNivo != nivoPlatformeNesrece) {
            predjiNaDruguPlatformu(nivoPlatformeNesrece);
        }

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);

        // provjera gdje se nalazi vozilo, kako bi se odabrala odgovarajuca putanja gornja/donja
        if (koordinateNesreceY < platforma.getRed() / 2) {

            gornjaPutanja = true;

            //racuna se opseg da bi se znalo da li je nesreca neporedno udaljena od specijalnog vozila
            if (koordinateNesreceY > y) {

                if (x == 0 || x == 4) {
                    if (koordinateNesreceX > x && koordinateNesreceX <= x + 2) {
                        gornjaPutanja = false;
                    }
                } else if (x == 3 || x == 7) {
                    if (koordinateNesreceX > x && koordinateNesreceX <= x - 2) {
                        gornjaPutanja = false;
                    }
                }
            } else {
                gornjaPutanja = true;

                //racuna se opseg da bi se znalo da li je nesreca neporedno udaljena od specijalnog vozila
                if (koordinateNesreceY > y) {

                    if (x == 0 || x == 4) {
                        if (koordinateNesreceX > x && koordinateNesreceX <= x + 2) {
                            gornjaPutanja = false;
                        }
                    } else if (x == 3 || x == 7) {
                        if (koordinateNesreceX > x && koordinateNesreceX <= x - 2) {
                            gornjaPutanja = false;
                        }
                    }
                }
            }
            //provjera da li je udes ispred parkiranog vozila
            if (!((x == koordinateNesreceX - 1) && (y == koordinateNesreceY) || (x == koordinateNesreceX + 1) && (y == koordinateNesreceY))) {

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
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if ((x == koordinateNesreceX - 1) && (y == koordinateNesreceY)) {
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
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((x == koordinateNesreceX + 1) && (y == koordinateNesreceY)) {
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
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (gornjaPutanja) {
                    prethodnoStanjePolja = gornjaPutanja(koordinateNesreceX, koordinateNesreceY, prethodnoStanjePolja);
                } else {
                    prethodnoStanjePolja = donjaPutanja(koordinateNesreceX, koordinateNesreceY, prethodnoStanjePolja);
                }
            }

            try {
                sleep(5000);
                platforma.getMatrica()[x][y] = prethodnoStanjePolja;
                platforma.getListaVozila().remove(this);
                GarageApplication.getExchanger().refreshSimulacijaMatrica();

                synchronized (platforma) {
                    platforma.notifyAll();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Object gornjaPutanja(int koordinateNesreceX, int koordinateNesreceY, Object prethodnoStanjePolja) {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        boolean stigao = false;

        if (x == 6) {

            stigao = ((x == koordinateNesreceX + 1) && y == koordinateNesreceY)
                    || (x == koordinateNesreceX && y == koordinateNesreceY - 1);

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
                stigao = ((x == koordinateNesreceX + 1) && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY + 1);

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
                stigao = ((x == koordinateNesreceX) && y == koordinateNesreceY - 1)
                        || ((x == koordinateNesreceX + 1 && y == koordinateNesreceY));
            }

            while (y < platforma.getRed() - 1 && !stigao) {

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
                stigao = ((x == koordinateNesreceX - 1) && (y == koordinateNesreceY))
                        || ((x == koordinateNesreceX) && (y == koordinateNesreceY - 1));
            }
        } else if (x == 2) {

            stigao = (x == koordinateNesreceX + 1 && y == koordinateNesreceY)
                    || (x == koordinateNesreceX && y == koordinateNesreceY + 1);
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
                stigao = (x == koordinateNesreceX + 1 && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY + 1);
            }

            while (x < platforma.getKolona() - 2 && !stigao) {
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
                stigao = ((x == koordinateNesreceX) && y == koordinateNesreceY + 1)
                        || (x == koordinateNesreceX - 1 && y == koordinateNesreceY);
            }

            while (y < platforma.getRed() - 1 && !stigao) {

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
                stigao = (x == koordinateNesreceX - 1) && y == koordinateNesreceY;
            }

        }
        return prethodnoStanjePolja;
    }

    public Object donjaPutanja(int koordinateNesreceX, int koordinateNesreceY, Object prethodnoStanjePolja) {

        Platforma platforma = GarageApplication.getExchanger().getGaraza().getPlatforme().get(trenutniNivo);
        boolean stigao = false;

        if (x == 5) {

            stigao = ((x == koordinateNesreceX - 1) && y == koordinateNesreceY)
                    || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
            while (y < platforma.getRed() - 2 && !stigao) {

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
                stigao = ((x == koordinateNesreceX - 1) && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
            }

            while (x > 2 && !stigao) {
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
                stigao = ((x == koordinateNesreceX) && y == koordinateNesreceY - 1)
                        || (x == koordinateNesreceX + 1 && y == koordinateNesreceY);
            }

            while (y > 1 && !stigao) {

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
                stigao = ((x == koordinateNesreceX + 1) && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY + 1);
            }
        } else if (x == 1) {

            stigao = ((x == koordinateNesreceX - 1) && y == koordinateNesreceY)
                    || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
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
                stigao = ((x == koordinateNesreceX - 1) && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY - 1);
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
                stigao = ((x == koordinateNesreceX) && y == koordinateNesreceY + 1)
                        || (x == koordinateNesreceX - 1 && y == koordinateNesreceY);
            }

            while (y > 1 && !stigao) {

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
                stigao = ((x == koordinateNesreceX + 1) && y == koordinateNesreceY)
                        || (x == koordinateNesreceX && y == koordinateNesreceY + 1);
            }
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
        int odredisniY = 0;
        if (trenutniNivo < nivoPlatformeNesrece) {
            odredisniY = 1;
        }
        while (y > odredisniY) {
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
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean stigao() {

        return false;

    }
}
