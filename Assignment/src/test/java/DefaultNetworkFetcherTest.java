import edu.um.cps3230.ipFetcherAPI.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultNetworkFetcherTest {


    @Mock
    private UrlWrapper mockUrlWrapper;
    @Spy
    @InjectMocks
    private DefaultNetworkFetcher networkFetcher;

   private int timeout = 3000; // Set a reasonable timeout for the test


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOpenStreamSuccessful() throws IOException {
        // Create a mock HttpURLConnection
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        // Create a mock InputStream
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        // Mock the behavior of HttpURLConnection
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);
        // Mock the behavior of UrlWrapper
        when(mockUrlWrapper.createUrl(Mockito.anyString())).thenReturn(new URL("http://example.com"));
        when(mockUrlWrapper.openConnection(new URL("http://example.com"))).thenReturn(mockConnection);
        // Call the method to test
        InputStream resultStream = networkFetcher.openStream("http://example.com", timeout);
        // Assertions
        assertNotNull(resultStream, "Expected non-null InputStream");
        // Verify that configureConnection was called
        verify(networkFetcher).configureConnection(Mockito.any(HttpURLConnection.class), anyInt());
        verify(networkFetcher).handleResponse(Mockito.any(HttpURLConnection.class));

    }



    @Test
    void testOpenStreamFailure() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        DefaultNetworkFetcher networkFetcher = new DefaultNetworkFetcher(new UrlWrapper()) {
            @Override
            public InputStream handleResponse(HttpURLConnection connection) throws IOException {
                throw new IOException("Failed to open stream. HTTP response code: " + connection.getResponseCode());
            }
        };

        assertThrows(IOException.class, () -> networkFetcher.openStream(new URL("http://example.com").toString(), 5000));
    }
    @Test
    void testConfigureConnection() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);

        DefaultNetworkFetcher networkFetcher = new DefaultNetworkFetcher(new UrlWrapper());
        networkFetcher.configureConnection(connection, timeout);

        verify(connection).setConnectTimeout(timeout);
        verify(connection).setRequestMethod("GET");
        verifyNoMoreInteractions(connection);
    }

    @Test
    void testHandleResponseSuccess() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        InputStream mockInputStream = mock(InputStream.class);
        when(connection.getInputStream()).thenReturn(mockInputStream);

        DefaultNetworkFetcher networkFetcher = new DefaultNetworkFetcher(new UrlWrapper());
        InputStream inputStream = networkFetcher.handleResponse(connection);

        assertEquals(mockInputStream, inputStream);

        verify(connection).getResponseCode();
        verify(connection).getInputStream();
        verifyNoMoreInteractions(connection, mockInputStream);
    }

    @Test
    void testHandleResponseFailure() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        int errorCode = HttpURLConnection.HTTP_NOT_FOUND;
        when(connection.getResponseCode()).thenReturn(errorCode);

        DefaultNetworkFetcher networkFetcher = new DefaultNetworkFetcher(new UrlWrapper());

        IOException exception = assertThrows(IOException.class, () -> networkFetcher.handleResponse(connection));

        assertEquals("Failed to open stream. HTTP response code: " + errorCode, exception.getMessage());

        verify(connection).getResponseCode();
        verifyNoMoreInteractions(connection);
    }

}
