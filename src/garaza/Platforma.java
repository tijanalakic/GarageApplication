/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garaza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import utils.Utils;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class Platforma implements Serializable {

    private ArrayList<Vozilo> listaVozila = new ArrayList<>();
    private Object matrica[][];
    private int kolona = Integer.parseInt(Utils.PROPERTIES.getProperty("BROJ_KOLONA"));
    private int red = Integer.parseInt(Utils.PROPERTIES.getProperty("BROJ_REDOVA"));
    private boolean imaSudar = false;
    
    public static final int[] Xparking = {0, 3, 4, 7};
    public static final int[] Yparking = {2, 3, 4, 5, 6, 7, 8, 9};

    
    public Platforma() {
        matrica = new Object[kolona][red];

        for (int i = 0; i < 8; i++) {

            matrica[0][i + 2] = "*";
            matrica[7][i + 2] = "*";

        }
        for (int i = 0; i < 6; i++) {

            matrica[3][i + 2] = "*";
            matrica[4][i + 2] = "*";

        }

    }

    public Object[][] getMatrica() {
        return matrica;
    }

    public void setMatrica() {

        Random rand = new Random();

        for (int i = 0; i < listaVozila.size(); i++) {
            Boolean parkiran = false;
            while (!parkiran) {

                int x = rand.nextInt(4);
                int y = rand.nextInt(8);

                if ("*".equals(matrica[Xparking[x]][Yparking[y]])) {
                    listaVozila.get(i).setX(Xparking[x]);
                    listaVozila.get(i).setY(Yparking[y]);
                    matrica[Xparking[x]][Yparking[y]] = listaVozila.get(i);

                    parkiran = true;

                }

            }
        }

        String ispis = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrica[j][i] == null) {
                    ispis += "   ";

                } else if (matrica[j][i] instanceof Vozilo) {
                    ispis += " (" + ((Vozilo) matrica[j][i]).getX() + ", "
                            + ((Vozilo) matrica[j][i]).getY();
                }
            }
            ispis += "\n";
        }
        System.out.println("prva: " + ispis);

    }

    public int getKolona() {
        return kolona;
    }

    public void setKolona(int kolona) {
        this.kolona = kolona;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setElement(Vozilo o) {

        listaVozila.add(o);

//        Random rand = new Random();
//        Boolean parkiran = false;
//        while (!parkiran) {
//
//            int x = rand.nextInt(4);
//            int y = rand.nextInt(8);
//            if ("*".equals(matrica[Xparking[x]][Yparking[y]])) {
//                matrica[Xparking[x]][Yparking[y]] = o;
//                listaVozila.add(o);
//                System.out.println(Xparking[x] + " " + Yparking[y]);
//                parkiran = true;
//
//            }
//        }
    }

    public Object getElement(int x, int y) {

        return matrica[x][y];
    }

    //STA CE MI OVO OVDE?????
    public ArrayList<Vozilo> getListaVozila() {
        return listaVozila;
    }

    public void setListaVozila(ArrayList<Vozilo> listaVozila) {
        this.listaVozila = listaVozila;
    }

    @Override
    public String toString() {
        String ispis = "";  //prazan string
        for (int i = 0; i < red; i++) {
            for (int j = 0; j < kolona; j++) {
                if (matrica[j][i] == null) {
                    ispis += "   ";

                } else {
                    ispis += " " + matrica[j][i] + " ";
                }
            }
            ispis += "\n";
        }
        return ispis;
    }

    public boolean isImaSudar() {
        return imaSudar;
    }

    public void setImaSudar(boolean imaSudar) {
        this.imaSudar = imaSudar;
    }

    
}
