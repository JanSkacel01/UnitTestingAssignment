package edu.um.cps3230.ForecastAPI;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class DefaultExternalApiFetcher implements ExternalApiFetcher {

    private final Unirest unirestRequest;

    public DefaultExternalApiFetcher(Unirest unirestRequest) {
        this.unirestRequest = unirestRequest;
    }


    @Override
    public HttpResponse<JsonNode> callWeatherApi(String airportCode, String date) {
        HttpResponse<JsonNode> result = null;
        try {
            return unirestRequest.get("https://weatherapi-com.p.rapidapi.com/forecast.json?q=" + airportCode + "&days=1&dt=" + date)
                    .header("X-RapidAPI-Key", "dc9e93349emsh9b1887f071ec401p1fa267jsn27dec67338e6")
                    .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                    .asJson();
        } catch (Exception e) {
            System.out.println("Cannot get weather forecast. Check you internet connection.");
            //throw new RuntimeException(e);
        }
        return result;
    }
}
