package com.soc.springboothashicorpvault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.Versioned;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties(MyConfiguration.class)
public class SpringbootHashicorpVaultApplication implements CommandLineRunner {
	
	@Autowired
	Environment env;
	
	Logger log = LoggerFactory.getLogger(SpringbootHashicorpVaultApplication.class);
	
	private final MyConfiguration myConfiguration;
	
	public SpringbootHashicorpVaultApplication(MyConfiguration myConfiguration)
	{
		this.myConfiguration=myConfiguration;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHashicorpVaultApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Inside Main method");
		log.info("Values from Vault Server");
		log.info("Username " + myConfiguration.getUsername());
		log.info("Password " + myConfiguration.getPassword());
		
		Connection conn =null;
		
		String url = env.getProperty("DB_URL");
		log.info("URL IS "+url);
		String user =myConfiguration.getUsername();
		String password=myConfiguration.getPassword();
		try {
			Class.forName(env.getProperty("JDBC_DRIVER"));
			log.info("---DRIVER LOADED SUCCESFULLY---");
			conn=DriverManager.getConnection(url,user,password);
			log.info("Connection establish successfuly");
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
		}


		
	}


}
