package edu.um.cps3230.ipFetcherAPI;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class BackupIPFetcher implements IPFetcherInterface{

    private final int timeout;
    private final DefaultNetworkFetcher networkFetcher;

    public BackupIPFetcher(int timeout, DefaultNetworkFetcher networkFetcher) {
        this.timeout = timeout;
        this.networkFetcher = networkFetcher;
    }

    @Override
    public String getPublicIP() {
        try {
            URL url = new URL("http://ip-api.com/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(networkFetcher.openStream(url.toString(), timeout)));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Vytvoření objektu JSON z odpovědi
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Získání hodnoty "query", což by měla být IP adresa
            return jsonResponse.getString("query");
        } catch (IOException e) {
            // Chyba při získávání veřejné IP záložním kódem
            System.out.println("Cannot get public ip. Check you internet connection.");
          //  e.printStackTrace();
            return null;
        }
    }
}
