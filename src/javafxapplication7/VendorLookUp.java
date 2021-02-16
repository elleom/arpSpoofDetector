/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author el_le
 */
public class VendorLookUp {

    String baseURL = "https://api.maclookup.app/v1/macs/";

    /**
     * TOKEN INVALIDO.
     * SERVIA UNICAMENTE PARA EL API DE MACVENDORS
     */
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImp0aSI6ImNkYzRhNjkyLTgyYTAtNDUzMC1hYTZiLTdlNjVkOTRiZDc0YS"
            + "J9.eyJpc3MiOiJtYWN2ZW5kb3JzIiwiYXVkIjoibWFjdmVuZG9ycyIsImp0aSI6ImNkYzRhNjkyLTgyYTAtNDUzMC1hYTZiLTdlN"
            + "jVkOTRiZDc0YSIsImlhdCI6MTU4Njk0Mzg2NSwiZXhwIjoxOTAxNDM5ODY1LCJzdWIiOiI3MjE1IiwidHlwIjoiYWNjZXNzIn0.V"
            + "k9vRrSPNbFKApzJxCo-AeJ6TOvjMY6kN3ckkm4XmWLRMHw0qeGkvLRZxpk1jw_qKewfELfYkV7krth_ADd3ew";


    public String getVendor(String macAddress) {
        System.out.println("getVendor()" + macAddress);
        try {
            StringBuilder result = new StringBuilder();
            
            //@see https://maclookup.app/api_doc
            URL url = new URL(baseURL + macAddress + "/company_name");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //conn.setRequestProperty("Authorization", "Bearer " + token);
            
            //ESPECIFICA QUE TIPO DE ARCHIVO RECIBE
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET"); //TIPO DE OPERACION
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            conn.disconnect();
            return result.toString();

            /***
             * En caso de que el servicio no este disponible
             */
        } catch (FileNotFoundException e) {
            // MAC not found
            return "N/A";
        } catch (IOException e) {
            // Error during lookup, either network or API.
            return null;
        }
    }

}
