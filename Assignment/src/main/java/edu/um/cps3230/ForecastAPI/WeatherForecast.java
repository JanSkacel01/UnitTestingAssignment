package edu.um.cps3230.ForecastAPI;

//import com.mashape.unirest.http.HttpResponse;



import edu.um.cps3230.ipFetcherAPI.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class WeatherForecast {
    private final DefaultExternalApiFetcher defaultExternalApiFetcher;

    private final IPFetcher ipFetcher;

    private final BackupIPFetcher backupIPFetcher;

    public WeatherForecast(DefaultExternalApiFetcher defaultExternalApiFetcher, IPFetcher ipFetcher, BackupIPFetcher backupIPFetcher) {
        this.defaultExternalApiFetcher = defaultExternalApiFetcher;
        this.ipFetcher = ipFetcher;
        this.backupIPFetcher = backupIPFetcher;
    }

    public Map<String, Double> getWeatherForToday() {
        String publicIP = ipFetcher.getPublicIP();
        if (publicIP == null) {
            System.out.println("Using backup ip");
            publicIP = backupIPFetcher.getPublicIP();
        }

        Map<String,Double> result = getForecast(publicIP, LocalDate.now().toString());

        return result;
    }


    public Map<String, Double> getForecast(String airportCode, String date) {
        HttpResponse<JsonNode> response = defaultExternalApiFetcher.callWeatherApi(airportCode, date);
        if (response != null) {
            JsonNode jsonObject = response.getBody();

            // Získání objektu pro první den v rámci objektu forecast
            kong.unirest.json.JSONObject forecastObject = jsonObject.getObject().getJSONObject("forecast");
            JSONArray forecastDays = forecastObject.getJSONArray("forecastday");
            JSONObject firstDay = forecastDays.getJSONObject(0);

            // Získání hodnoty pro "avgtemp_c" z objektu dne
            double avgTempC = firstDay.getJSONObject("day").getDouble("avgtemp_c");
            double totalPrecipMM = firstDay.getJSONObject("day").getDouble("totalprecip_mm");

            Map<String, Double> forecastData = new HashMap<>();
            forecastData.put("avgTempC", avgTempC);
            forecastData.put("totalPrecipMM", totalPrecipMM);

            return forecastData;
        } else {
            return null;
        }
    }

}
