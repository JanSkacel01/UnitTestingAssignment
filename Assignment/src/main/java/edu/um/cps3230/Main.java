package edu.um.cps3230;

import edu.um.cps3230.ForecastAPI.DefaultExternalApiFetcher;
import edu.um.cps3230.ForecastAPI.WeatherForecast;
import edu.um.cps3230.ipFetcherAPI.*;
import kong.unirest.Unirest;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {

        CLI cli = new CLI(new WeatherForecast(new DefaultExternalApiFetcher(new Unirest()),new IPFetcher(3000,new DefaultNetworkFetcher(new UrlWrapper())),new BackupIPFetcher(3000,new DefaultNetworkFetcher(new UrlWrapper()))));
        cli.play();
    }
}