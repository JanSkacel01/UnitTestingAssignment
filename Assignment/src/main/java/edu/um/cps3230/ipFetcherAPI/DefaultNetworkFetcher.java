package edu.um.cps3230.ipFetcherAPI;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefaultNetworkFetcher implements NetworkFetcher{

    private final UrlWrapper urlWrapper;

    public DefaultNetworkFetcher(UrlWrapper urlWrapper) {
        this.urlWrapper = urlWrapper;
    }


    public InputStream openStream(String urlString, int timeout) throws IOException {
        URL url = urlWrapper.createUrl(urlString);
        HttpURLConnection connection = (HttpURLConnection) urlWrapper.openConnection(url);
        configureConnection(connection, timeout);
        InputStream result = handleResponse(connection);
        return result;
    }

    // Make configureConnection package-private
    public void configureConnection(HttpURLConnection connection, int timeout) throws IOException {
        connection.setConnectTimeout(timeout);
        connection.setRequestMethod("GET");
    }

    public InputStream handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("Failed to open stream. HTTP response code: " + responseCode);
        }
    }


}