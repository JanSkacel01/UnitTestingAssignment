package edu.um.cps3230.ipFetcherAPI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlWrapper {
    public URL createUrl(String urlString) throws IOException {
        return new URL(urlString);
    }

    public HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
