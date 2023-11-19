package edu.um.cps3230.ipFetcherAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IPFetcher implements IPFetcherInterface {

    private final int timeout;
    private final DefaultNetworkFetcher defaultNetworkFetcher;

    public IPFetcher(int timeout, DefaultNetworkFetcher defaultNetworkFetcher) {
        this.timeout = timeout;
        this.defaultNetworkFetcher = defaultNetworkFetcher;
    }

    @Override
    public String getPublicIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(defaultNetworkFetcher.openStream(whatismyip.toString(), timeout)));
                String ip = in.readLine();
                in.close();
                return ip;
            }catch (IOException ex) {
            return null;
        }
    }
}