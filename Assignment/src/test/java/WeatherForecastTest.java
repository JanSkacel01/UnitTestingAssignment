import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import edu.um.cps3230.ForecastAPI.DefaultExternalApiFetcher;
import edu.um.cps3230.ForecastAPI.WeatherForecast;
import edu.um.cps3230.ipFetcherAPI.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;

class WeatherForecastTest {


@Mock
    private IPFetcher mockIpFetcher;

@Mock
private BackupIPFetcher backupIPFetcher;


    @Mock
    private DefaultExternalApiFetcher defaultExternalApiFetcher;

    @BeforeEach
  public  void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
  public  void testGetForecast() {
       WeatherForecast weatherForecastMock2 = new WeatherForecast(defaultExternalApiFetcher,mockIpFetcher,new BackupIPFetcher(3000,new DefaultNetworkFetcher(new UrlWrapper())));
        // Mocking
        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(defaultExternalApiFetcher.callWeatherApi(anyString(), anyString())).thenReturn(mockResponse);


        when(mockResponse.getBody()).thenReturn(new JsonNode(createForecast().toString()));

        // Call the method to be tested
        Map<String, Double> result = weatherForecastMock2.getForecast("ABC", LocalDate.now().toString());

        // Verify interactions and assertions
        verify(defaultExternalApiFetcher, times(1)).callWeatherApi(anyString(), anyString());
        assertEquals(25.0, result.get("avgTempC"));
        assertEquals(5.0, result.get("totalPrecipMM"));
    }

    @Test
   public void testGetForecastForToday() {
        WeatherForecast weatherForecast = new WeatherForecast(defaultExternalApiFetcher, mockIpFetcher,new BackupIPFetcher(3000,new DefaultNetworkFetcher(new UrlWrapper())));

        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(defaultExternalApiFetcher.callWeatherApi(anyString(), anyString())).thenReturn(mockResponse);


        when(mockResponse.getBody()).thenReturn(new JsonNode(createForecast().toString()));


        // Set up the behavior of the mock
        when(mockIpFetcher.getPublicIP()).thenReturn("192.168.0.1");
       // when(weatherForecast.getForecast(anyString(),anyString())).thenReturn(null);
        when(defaultExternalApiFetcher.callWeatherApi("192.168.0.1",LocalDate.now().toString())).thenReturn(mockResponse);

        when(mockResponse.getBody()).thenReturn(new JsonNode(createForecast().toString()));


        // Create the WeatherForecast instance and manually inject the mocks
       //WeatherForecast weatherForecast = new WeatherForecast(mockNetworkFetcher, mockApiFetcher, mockIpFetcher);

        // Call the method under test
        Map<String,Double> result = weatherForecast.getWeatherForToday();

        Assertions.assertNotNull(result);

        verify(mockIpFetcher).getPublicIP();
        verify(defaultExternalApiFetcher,times(1)).callWeatherApi("192.168.0.1",LocalDate.now().toString());
       // verify(weatherForecast.getForecast("192.168.0.1",LocalDate.now().toString()),times(1));


    }

    @Test
    public void testGetForecastForTodayWithBackupIP() {
        WeatherForecast weatherForecast = new WeatherForecast(defaultExternalApiFetcher, mockIpFetcher,backupIPFetcher);

        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(defaultExternalApiFetcher.callWeatherApi(anyString(), anyString())).thenReturn(mockResponse);


        when(mockResponse.getBody()).thenReturn(new JsonNode(createForecast().toString()));


        // Set up the behavior of the mock
        when(mockIpFetcher.getPublicIP()).thenReturn(null);
        when(backupIPFetcher.getPublicIP()).thenReturn("192.168.0.1");
        // when(weatherForecast.getForecast(anyString(),anyString())).thenReturn(null);
        when(defaultExternalApiFetcher.callWeatherApi("192.168.0.1",LocalDate.now().toString())).thenReturn(mockResponse);

        when(mockResponse.getBody()).thenReturn(new JsonNode(createForecast().toString()));


        // Create the WeatherForecast instance and manually inject the mocks
        //WeatherForecast weatherForecast = new WeatherForecast(mockNetworkFetcher, mockApiFetcher, mockIpFetcher);

        // Call the method under test
        Map<String,Double> result = weatherForecast.getWeatherForToday();

        Assertions.assertNotNull(result);

        verify(mockIpFetcher).getPublicIP();
        verify(defaultExternalApiFetcher,times(1)).callWeatherApi("192.168.0.1",LocalDate.now().toString());
        // verify(weatherForecast.getForecast("192.168.0.1",LocalDate.now().toString()),times(1));


    }






    private JSONObject createForecast() {

        JSONObject jsonObject = new JSONObject();
        JSONObject forecastObject = new JSONObject();
        JSONArray forecastDays = new JSONArray();
        JSONObject firstDay = new JSONObject();

        JSONObject dayObject = new JSONObject();
        dayObject.put("avgtemp_c", 25.0);
        dayObject.put("totalprecip_mm", 5.0);

        firstDay.put("day", dayObject);
        forecastDays.put(firstDay);

        forecastObject.put("forecastday", forecastDays);
        jsonObject.put("forecast", forecastObject);

        return jsonObject;
    }

}
