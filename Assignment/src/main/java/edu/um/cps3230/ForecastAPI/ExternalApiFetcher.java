package edu.um.cps3230.ForecastAPI;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public interface ExternalApiFetcher {
    HttpResponse<JsonNode> callWeatherApi(String airportCode, String date);

}