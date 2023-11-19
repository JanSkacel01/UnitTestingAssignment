
import edu.um.cps3230.ipFetcherAPI.BackupIPFetcher;
import edu.um.cps3230.ipFetcherAPI.DefaultNetworkFetcher;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BackupIPFetcherTest {

    @Mock
    private DefaultNetworkFetcher networkFetcher;

    private BackupIPFetcher backupIPFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        int timeout = 3000; // Set a reasonable timeout for the test
        backupIPFetcher = new BackupIPFetcher(timeout, networkFetcher);
    }
    @AfterEach
    void tearDown(){
        networkFetcher = null;
        backupIPFetcher=null;
    }

    @Test
    void testGetPublicIPSuccess() throws IOException {
        // Arrange
        when(networkFetcher.openStream(any(), anyInt())).thenReturn(new ByteArrayInputStream("{\"status\":\"success\",\"country\":\"Malta\",\"countryCode\":\"MT\",\"region\":\"32\",\"regionName\":\"Il-Mosta\",\"city\":\"Mosta\",\"zip\":\"MST\",\"lat\":35.9142,\"lon\":14.4253,\"timezone\":\"Europe/Malta\",\"isp\":\"GO P.L.C\",\"org\":\"\",\"as\":\"AS15735 GO p.l.c.\",\"query\":\"47.11.19.119\"}".getBytes()));

        // Act
        String publicIP = backupIPFetcher.getPublicIP();

        // Assert
        assertNotNull(publicIP, "Public IP should not be null");
        assertTrue(publicIP.matches("\\d+\\.\\d+\\.\\d+\\.\\d+"), "Invalid IP format");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://ip-api.com/json").toString(), 3000);
    }

    @Test
    void testGetPublicIPTimeout() throws IOException {
        // Arrange
        when(networkFetcher.openStream(any(), anyInt())).thenThrow(new java.net.SocketTimeoutException("Mock timeout"));

        // Act
        String publicIP = backupIPFetcher.getPublicIP();

        // Assert
        assertNull(publicIP, "Public IP should be null due to timeout");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://ip-api.com/json").toString(), 3000);
    }

    @Test
    void testGetPublicIPIOException() throws IOException {
        // Arrange
        when(networkFetcher.openStream(any(), anyInt())).thenThrow(new IOException("Mock IOException"));

        // Act
        String publicIP = backupIPFetcher.getPublicIP();

        // Assert
        assertNull(publicIP, "Public IP should be null due to IOException");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://ip-api.com/json").toString(), 3000);
    }
}
