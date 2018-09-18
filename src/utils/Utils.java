/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import garageapplication.FXMLDocumentController;
import static garageapplication.FXMLDocumentController.ROOT_RESOURCE;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tijana Lakic
 */
public class Utils {

    public static Properties PROPERTIES;
    public static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        PROPERTIES = new Properties();

        try {
            InputStream input = new FileInputStream(ROOT_RESOURCE + "config.properties");
            PROPERTIES.load(input);
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static byte[] ucitajSliku(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if ("".equals(fileName) || fileName == null) {
            return new byte[0];
        } else {

            return Files.readAllBytes(path);
        }
    }

    public static void upisi(byte[] bytes, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        File file = new File(fileName);
        if (!((file).exists())) {
            file.createNewFile();
        }
        Files.write(path, bytes, StandardOpenOption.APPEND);
    }

    public static void upisiUPotjernicu(String registracija) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Utils.PROPERTIES.getProperty("POTJERNICA_FILE_PATH"), true)), true);
        out.println(registracija);
        out.close();
    }

    public static void kreirajCsvFajl(ArrayList<String> evidencija) throws IOException {
        String fileName = Utils.PROPERTIES.getProperty("EVIDENCIJA_NAPLATE_FILE_PATH");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)), true);
        if (!(new File(fileName)).exists()) {
            out.println(Utils.PROPERTIES.getProperty("EVIDENCIJA_NAPLATE_HEADER"));
        }
        for (String stavka : evidencija) {
            out.println(stavka);
        }
        out.close();
    }
}
