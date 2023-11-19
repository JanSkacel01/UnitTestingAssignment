import edu.um.cps3230.ipFetcherAPI.DefaultNetworkFetcher;
import edu.um.cps3230.ipFetcherAPI.IPFetcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IPFetcherTest {

    @Mock
    private DefaultNetworkFetcher networkFetcher;

    private IPFetcher ipFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        int timeout = 3000; // Set a reasonable timeout for the test
        ipFetcher = new IPFetcher(timeout, networkFetcher);
    }

    @Test
    void testGetPublicIPSuccess() throws IOException {
        // Arrange
        when(networkFetcher.openStream(anyString(), anyInt())).thenReturn(new ByteArrayInputStream("127.0.0.1".getBytes()));

        // Act
        String publicIP = ipFetcher.getPublicIP();

        // Assert
        assertNotNull(publicIP, "Public IP should not be null");
        assertTrue(publicIP.matches("\\d+\\.\\d+\\.\\d+\\.\\d+"), "Invalid IP format");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://checkip.amazonaws.com").toString(), 3000);
    }

    @Test
    void testGetPublicIPTimeout() throws IOException {
        // Arrange
        when(networkFetcher.openStream(anyString(), anyInt())).thenThrow(new java.net.SocketTimeoutException("Mock timeout"));

        // Act
        String publicIP = ipFetcher.getPublicIP();

        // Assert
        assertNull(publicIP, "Public IP should be null due to timeout");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://checkip.amazonaws.com").toString(), 3000);
    }

    @Test
    void testGetPublicIPIOException() throws IOException {
        // Arrange
        when(networkFetcher.openStream(anyString(), anyInt())).thenThrow(new IOException("Mock IOException"));

        // Act
        String publicIP = ipFetcher.getPublicIP();

        // Assert
        assertNull(publicIP, "Public IP should be null due to IOException");

        // Verify that openStream was called with the correct URL and timeout
        verify(networkFetcher).openStream(new URL("http://checkip.amazonaws.com").toString(), 3000);
    }
}
