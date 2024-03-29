package com.soc.springboothashicorpvault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.vault.core.VaultTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
@EnableConfigurationProperties(MyConfiguration.class)
@EnableScheduling // Enable Spring's scheduling capabilities
public class SpringbootHashicorpVaultApplication implements CommandLineRunner {
    
    @Autowired
    private Environment env;

    @Autowired
    private MyConfiguration myConfiguration;
    
    @Autowired
    private VaultTemplate vaultTemplate;
    
   
    

    private static final Logger logger = LoggerFactory.getLogger(SpringbootHashicorpVaultApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHashicorpVaultApplication.class, args);
    }
    
  

    @Override
    public void run(String... args) throws Exception {
        logger.info("Inside Main method");
        logger.info("Values from Vault Server");
        logger.info("Username: {}", myConfiguration.getUsername());
        logger.info("Password: {}", myConfiguration.getPassword());
        establishDatabaseConnection();
    }

    // Method to establish database connection
    private void establishDatabaseConnection() {
        Connection conn = null;
        String url = env.getProperty("DB_URL"); // Use configuration property directly
        String user = myConfiguration.getUsername();
        String password = myConfiguration.getPassword();

        try {
            Class.forName(env.getProperty("JDBC_DRIVER"));
            logger.info("Driver loaded successfully");
            conn = DriverManager.getConnection(url, user, password);
            logger.info("Connection established successfully");
        } catch (Exception e) {
            logger.error("Error establishing database connection: {}", e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    logger.info("Connection closed");
                } catch (Exception e) {
                    logger.error("Error closing database connection: {}", e.getMessage());
                }
            }
        }
    }
    @Scheduled(fixedDelay = 60000)
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
            
            con.setRequestProperty("X-Vault-Token", "hvs.wa7bPnyj2vw708hxBu8djIZD");


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
        logger.info(response.toString());
        return response.toString();
    }
    // Scheduled task to refresh secrets from Vault
//    @Scheduled(fixedDelay = 60000) // Refresh secrets every minute
//    public void refreshSecrets() {
//        // Fetch secrets from Vault and update the configuration
//        // For simplicity, you can directly update the configuration properties here
//        try {
//            // Fetch updated secrets from Vault
//            String updatedUsername = fetchUpdatedUsernameFromVault();
//            String updatedPassword = fetchUpdatedPasswordFromVault();
//
//            // Update configuration properties
//            myConfiguration.setUsername(updatedUsername);
//            myConfiguration.setPassword(updatedPassword);
//
//            logger.info("Secrets refreshed successfully.");
//        } catch (Exception e) {
//            logger.error("Error refreshing secrets: {}", e.getMessage());
//        }
//    }

    // Placeholder methods to fetch updated secrets from Vault
//    private String fetchUpdatedUsernameFromVault() {
//        // Implement logic to fetch updated username from Vault
//    	System.out.println("Response" +vaultTemplate.read("v1/secret/data/application"));
//        String username = (String)vaultTemplate.read("v1/secret/data/application")
//                .getData().get("username");
//        return username;
//    }
//
//    private String fetchUpdatedPasswordFromVault() {
//        // Implement logic to fetch updated password from Vault
//        return "updated_password_from_vault";
//    }
}
