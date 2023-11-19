package edu.um.cps3230;


import edu.um.cps3230.ForecastAPI.WeatherForecast;
import edu.um.cps3230.ipFetcherAPI.DefaultNetworkFetcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class CLI {

    private final WeatherForecast weatherForecast;

    public CLI(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = getChoice(scanner);
            scanner.nextLine();
            switch (choice) {
                case 1:
                    recommendClothingForCurrentLocation();
                    break;
                case 2:
                    String airportCode = getAirportCode(scanner);
                    String arrivalDate = getArrivalDate(scanner);
                    recommendClothingForFutureLocation(airportCode, arrivalDate);
                    break;
                case 3:
                    System.out.println("Exiting WeatherWear.com. \nGoodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    private void displayMenu() {
        System.out.println("WeatherWear.com");
        System.out.println("----------------");
        System.out.println("1. Recommend clothing for current location");
        System.out.println("2. Recommend clothing for future location");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
    }

    public int getChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextInt();
    }

    public String getAirportCode(Scanner scanner) {
        String airportCode = null;
        boolean isValidInput = false;

        while (!isValidInput) {
            System.out.print("Enter airport code (IATA format):");
            airportCode = scanner.nextLine().toUpperCase(); // Převod na velká písmena pro konzistenci

            // Kontrola délky a základního formátu
            if (airportCode.length() == 3 && airportCode.matches("[A-Z]{3}")) {
                isValidInput = true;
            } else {
                System.out.println("Invalid airport code format. Please enter a 3-letter code in IATA format.");
            }
        }

        return airportCode;
    }

    public String getArrivalDate(Scanner scanner) {
        String arrivalDate = null;

        while (true) {
            System.out.print("Enter arrival date (YYYY-MM-DD, within 10 days from today): ");
            arrivalDate = scanner.nextLine();

            if (isValidDate(arrivalDate)) {
                break;
            }
        }

        return arrivalDate;
    }

    public boolean isValidDate(String dateStr) {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate maxAllowedDate = currentDate.plusDays(11);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(dateStr, formatter);

            if (!parsedDate.isBefore(currentDate) && parsedDate.isBefore(maxAllowedDate)){
                return true;
            }else {
                System.out.println("Invalid date. Please enter a valid date within the next 10 days from today.");
                    return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
            return false;
        }
    }

    public void recommendClothingForCurrentLocation() {
       // WeatherForecast weatherForecast = new WeatherForecast(defaultNetworkFetcher, new DefaultExternalApiFetcher(), new IPFetcher(3000, defaultNetworkFetcher),new BackupIPFetcher(3000,defaultNetworkFetcher));

        // Implement logic for recommending clothing for the current location


        Map<String,Double> forecast = weatherForecast.getWeatherForToday();
        if (forecast == null) return;
        double avgTemp = forecast.get("avgTempC");
        double totalPrecip = forecast.get("totalPrecipMM");

        if (avgTemp >= 15){
            System.out.println("It is warm so you should wear light clothing.");
        }
        else {
            System.out.println("It is cold so you should wear warm clothing.");
        }

        if (totalPrecip > 0){
            System.out.println("It is currently raining so you do need an umbrella.");
        }
        else {
            System.out.println("It is not raining so you don’t need an umbrella.");
        }
    }

    public void recommendClothingForFutureLocation(String airportCode,String arrivalDate) {

       // WeatherForecast weatherForecast = new WeatherForecast(defaultNetworkFetcher, new DefaultExternalApiFetcher(), new IPFetcher(3000,defaultNetworkFetcher),new BackupIPFetcher(3000,defaultNetworkFetcher));


        Map<String,Double> forecast= weatherForecast.getForecast(airportCode,arrivalDate);
        if (forecast == null) return;
        double avgTemp = forecast.get("avgTempC");
        double totalPrecip = forecast.get("totalPrecipMM");

        if (avgTemp >= 15){
            System.out.println("It will be warm so you should wear light clothing.");
        }
        else {
            System.out.println("It will be cold so you should wear warm clothing.");
        }

        if (totalPrecip > 0){
            System.out.println("It will be raining so you do need an umbrella.");
        }
        else {
            System.out.println("It will be not raining so you don’t need an umbrella.");
        }
    }
}
