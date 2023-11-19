package edu.um.cps3230.ipFetcherAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public interface NetworkFetcher {
    InputStream openStream(String urlString, int timeout) throws IOException;
}