package com.soc.springboothashicorpvault;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VaultController {

    @GetMapping("/vault/secrets")
    public String getSecretData() {
        HttpURLConnection con = null;
        StringBuilder response = new StringBuilder();

        try {
            // Create a URL object with the specified URL
            URL urlObj = new URL("http://localhost:8200/v1/secret/data/application");
            
            // Open a connection to the URL
            con = (HttpURLConnection) urlObj.openConnection();

            // Set the request method to GET
            con.setRequestMethod("GET");

            // Get the response code
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response body
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            // Handle exceptions properly
            System.err.println("Error retrieving secret data: " + e.getMessage());
        } finally {
            // Close the connection
            if (con != null) {
                con.disconnect();
            }
        }

        // Return the response body as a String
        return response.toString();
    }
}
