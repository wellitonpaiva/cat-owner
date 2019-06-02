package com.thescientist;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Singleton
public class CatService {

    public String whereIsCat() throws IOException {
        String uri = "https://micronaut-circuit-breaker-test.appspot.com/whereIsMyCat";
        HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}
