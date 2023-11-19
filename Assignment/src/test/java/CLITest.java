
import edu.um.cps3230.CLI;
import edu.um.cps3230.ForecastAPI.DefaultExternalApiFetcher;
import edu.um.cps3230.ForecastAPI.WeatherForecast;
import edu.um.cps3230.ipFetcherAPI.BackupIPFetcher;
import edu.um.cps3230.ipFetcherAPI.DefaultNetworkFetcher;
import edu.um.cps3230.ipFetcherAPI.IPFetcher;
import edu.um.cps3230.ipFetcherAPI.UrlWrapper;

import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CLITest {



    private CLI cli;

    @BeforeEach
    public void setUp() {
        cli = new CLI(new WeatherForecast(new DefaultExternalApiFetcher(new Unirest()), new IPFetcher(3000, new DefaultNetworkFetcher(new UrlWrapper())), new BackupIPFetcher(3000, new DefaultNetworkFetcher(new UrlWrapper()))));
    }

    @Test
    public void testGetChoiceValidInput() {
        // Set up
        String input = "42";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        int result = cli.getChoice(scanner);

        // Verify the result
        assertEquals(42, result);
    }


    @Test
    public void testGetChoiceInvalidInputThenValidInput() {
        // Set up
        String input = "invalidInput\n42";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        int result = cli.getChoice(scanner);

        // Verify the result
        assertEquals(42, result);
    }
    @Test
    public void testGetAirportCodeValidInput() {
        // Set up the input stream to simulate user input
        String input = "ABC";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        String result = cli.getAirportCode(scanner);

        // Verify the result
        assertEquals("ABC", result);
    }

    @Test
    public void testGetAirportCodeInvalidInputThenValidInput() {
        // Set up
        String input = "XY\nXYZA\n123\nrX!Z\nXYZ \n XYZ\nX-Y-Z\nX1Z\nabc";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        String result = cli.getAirportCode(scanner);

        // Verify the result
        assertEquals("ABC", result);
    }

    @Test
    public void testGetYesterdayArrivalDateInvalidInput() {
        // Set up the input stream to simulate user input
        String inputYesterday = getDateString(-1);
        ByteArrayInputStream in = new ByteArrayInputStream(inputYesterday.getBytes());
        System.setIn(in);

        // Set up the output stream to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a scanner using the mocked input stream
        Scanner scanner = new Scanner(System.in);

        // Call the isValidDate method
        boolean result = cli.isValidDate(scanner.nextLine());

        // Assert that the result is as expected (false for invalid input)
        assertFalse(result);

        // Assert that the console output includes the error message for invalid input
        assertEquals("Invalid date. Please enter a valid date within the next 10 days from today.", outContent.toString().trim());

        // Reset System.in and System.out to their original values
        System.setIn(System.in);
        System.setOut(System.out);

    }

    @Test
    public void testGetTodayArrivalDateValidInput() {
        // Set up the input stream to simulate user input
        String inputToday = getDateString(0);
        InputStream in = new ByteArrayInputStream(inputToday.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        String result = cli.getArrivalDate(scanner);

        // Verify the result
        assertEquals(getDateString(0), result);
        assertTrue(cli.isValidDate(result));

    }

    @Test
    public void testGetArrivalDateInFiveDaysIsValidInput() {
        // Set up the input stream to simulate user input
        String inputToday = getDateString(5);
        InputStream in = new ByteArrayInputStream(inputToday.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        String result = cli.getArrivalDate(scanner);

        // Verify the result
        assertEquals(getDateString(5), result);
        assertTrue(cli.isValidDate(result));

    }

    @Test
    public void testGetArrivalDateInTenDaysIsValidInput() {
        // Set up the input stream to simulate user input
        String inputToday = getDateString(10);
        InputStream in = new ByteArrayInputStream(inputToday.getBytes());
        Scanner scanner = new Scanner(in);

        // Invoke the method
        String result = cli.getArrivalDate(scanner);

        // Verify the result
        assertEquals(getDateString(10), result);
        assertTrue(cli.isValidDate(result));

    }

    @Test
    public void testGetArrivalDateInElevenDaysIsInvalidInput() {
        // Set up the input stream to simulate user input
        String inputYesterday = getDateString(11);
        ByteArrayInputStream in = new ByteArrayInputStream(inputYesterday.getBytes());
        System.setIn(in);

        // Set up the output stream to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a DateUtils instance
        // Create a scanner using the mocked input stream
        Scanner scanner = new Scanner(System.in);

        // Call the isValidDate method
        boolean result = cli.isValidDate(scanner.nextLine());

        // Assert that the result is as expected (false for invalid input)
        assertFalse(result);

        // Assert that the console output includes the error message for invalid input
        assertEquals("Invalid date. Please enter a valid date within the next 10 days from today.", outContent.toString().trim());

        // Reset System.in and System.out to their original values
        System.setIn(System.in);
        System.setOut(System.out);

    }

    @Test
    public void testGetArrivalDateInvalidDataFormat() {
        // Set up the input stream to simulate user input
        String inputYesterday = "invalid date format";
        ByteArrayInputStream in = new ByteArrayInputStream(inputYesterday.getBytes());
        System.setIn(in);

        // Set up the output stream to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a DateUtils instance

        // Create a scanner using the mocked input stream
        Scanner scanner = new Scanner(System.in);

        // Call the isValidDate method
        boolean result = cli.isValidDate(scanner.nextLine());

        // Assert that the result is as expected (false for invalid input)
        assertFalse(result);

        // Assert that the console output includes the error message for invalid input
        assertEquals("Invalid date format. Please enter the date in the format YYYY-MM-DD.", outContent.toString().trim());

        // Reset System.in and System.out to their original values
        System.setIn(System.in);
        System.setOut(System.out);

    }

    @Test
    public void testRecommendClothingForFutureLocation_WarmWeatherNoRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return warm weather conditions
        Map<String, Double> warmWeatherForecast = getFakeWeather(15.0,0.0);
        when(weatherForecastMock.getForecast(Mockito.anyString(),Mockito.anyString())).thenReturn(warmWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
        injectedCLI.recommendClothingForFutureLocation(Mockito.anyString(),Mockito.anyString());

        // Assert that the expected output is printed to the console
        assertEquals("It will be warm so you should wear light clothing.\n" +
                "It will be not raining so you don’t need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForFutureLocation_ColdWeatherNoRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return cold weather conditions
        Map<String, Double> coldWeatherForecast = getFakeWeather(14.99,0.0);
        when(weatherForecastMock.getForecast(Mockito.anyString(),Mockito.anyString())).thenReturn(coldWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
      //  CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForFutureLocation(Mockito.anyString(),Mockito.anyString());

        // Assert that the expected output is printed to the console
        assertEquals("It will be cold so you should wear warm clothing.\n" +
                "It will be not raining so you don’t need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForFutureLocation_WarmWeatherRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return rainy weather conditions
        Map<String, Double> rainyWeatherForecast = getFakeWeather(15.0,0.1);
        when(weatherForecastMock.getForecast(Mockito.anyString(),Mockito.anyString())).thenReturn(rainyWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
        //    CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForFutureLocation(Mockito.anyString(),Mockito.anyString());

        // Assert that the expected output is printed to the console
        assertEquals("It will be warm so you should wear light clothing.\n" +
                "It will be raining so you do need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForFutureLocation_ColdWeatherRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return dry weather conditions
        Map<String, Double> rainyColdWeatherForecast = getFakeWeather(14.9,0.1);
        when(weatherForecastMock.getForecast(Mockito.anyString(),Mockito.anyString())).thenReturn(rainyColdWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
        //     CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForFutureLocation(Mockito.anyString(),Mockito.anyString());

        // Assert that the expected output is printed to the console
        assertEquals("It will be cold so you should wear warm clothing.\n" +
                "It will be raining so you do need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForCurrentLocation_WarmWeatherRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return rainy weather conditions
        Map<String, Double> rainyWarmWeatherForecast = getFakeWeather(15.0,0.1);
        when(weatherForecastMock.getWeatherForToday()).thenReturn(rainyWarmWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        injectedCLI.recommendClothingForCurrentLocation();

        // Assert that the expected output is printed to the console
        assertEquals("It is warm so you should wear light clothing.\n" +
                "It is currently raining so you do need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForCurrentLocation_ColdWeatherRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return dry weather conditions
        Map<String, Double> rainyColdWeatherForecast = getFakeWeather(14.99,0.1);
        when(weatherForecastMock.getWeatherForToday()).thenReturn(rainyColdWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
   //     CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForCurrentLocation();

        // Assert that the expected output is printed to the console
        assertEquals("It is cold so you should wear warm clothing.\n" +
                "It is currently raining so you do need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForCurrentLocation_WarmWeatherNoRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return warm weather conditions
        Map<String, Double> dryWarmWeatherForecast = getFakeWeather(15.0,0.0);
        when(weatherForecastMock.getWeatherForToday()).thenReturn(dryWarmWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
        //      CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForCurrentLocation();

        // Assert that the expected output is printed to the console
        assertEquals("It is warm so you should wear light clothing.\n" +
                "It is not raining so you don’t need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }

    @Test
    public void testRecommendClothingForCurrentLocation_ColdWeatherNoRain() {
        // Create a mock WeatherForecast object
        WeatherForecast weatherForecastMock = Mockito.mock(WeatherForecast.class);
        CLI injectedCLI = new CLI(weatherForecastMock);

        // Set up the mock to return cold weather conditions
        Map<String, Double> dryColdWeatherForecast = getFakeWeather(14.99,0.0);
        when(weatherForecastMock.getWeatherForToday()).thenReturn(dryColdWeatherForecast);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the recommendClothingForCurrentLocation method with the mock object
        //  CLI cli = new CLI(new DefaultNetworkFetcher(new UrlWrapper()));
        injectedCLI.recommendClothingForCurrentLocation();

        // Assert that the expected output is printed to the console
        assertEquals("It is cold so you should wear warm clothing.\n" +
                "It is not raining so you don’t need an umbrella.", outContent.toString().trim());

        // Reset System.out to its original value
        System.setOut(System.out);
    }


    private String getDateString(int daysFromToday) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate yesterday's date
        LocalDate DateInTenDays = currentDate.plusDays(daysFromToday);

        // Format the date as a string in "YYYY-MM-DD" format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return DateInTenDays.format(formatter);
    }


    private Map<String,Double> getFakeWeather(double avgTemp,double totalPrecip){
        Map<String, Double> result = new HashMap<>();
        result.put("avgTempC", avgTemp); // Moderate temperature
        result.put("totalPrecipMM", totalPrecip); // No precipitation
        return result;
    }



}
