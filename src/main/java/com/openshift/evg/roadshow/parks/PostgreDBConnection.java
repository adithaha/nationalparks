package com.openshift.evg.roadshow.parks;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmorales on 11/08/16.
 */
@Component
public class PostgreDBConnection {

    private static final String FILENAME = "/nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PostgreDBConnection() {
    }

    @PostConstruct
    public void initConnection() {
    	this.createParksTable();
    	this.loadParks();
    }

    /*
     * Load from embedded list of parks using FILENAME
     */
    public void loadParks() {
        System.out.println("[DEBUG] PostgreDBConnection.loadParks()");

        try {
        	
        	loadParks(resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + FILENAME).getInputStream());
       
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
    }

    public void loadParks(String fileLocation) {
        System.out.println("[DEBUG] PostgreDBConnection.loadParks(" + fileLocation + ")");

        try {
            loadParks(new FileInputStream(new File(fileLocation)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
    }

    public void loadParks(InputStream is) {
        System.out.println("[DEBUG] PostgreDBConnection.loadParks(InputStream)");

        List<Document> docs = new ArrayList<Document>();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            String currentLine = null;
            int i = 1;
            while ((currentLine = in.readLine()) != null) {
                String s = currentLine.toString();
                
                JacksonJsonParser parser = new JacksonJsonParser();
            	Map<String, Object> data = parser.parseMap(s);
                
            	Object[] obj = {	data.get("countryCode"),
			            			data.get("countryName"),
			            			data.get("name"),
			            			data.get("toponymName"),
			            			((List)data.get("coordinates")).get(0),
			            			((List)data.get("coordinates")).get(1),
			            			
							};
            	
            	jdbcTemplate.update("INSERT INTO park(country_code, country_name, name, toponym_name, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?);", obj);
            	
            	
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading parks. Return empty list");
            }
        }
    }
    
    public void createParksTable() {
        System.out.println("[DEBUG] PostgreDBConnection.createParksTable()");

        try {
        	String sql = "drop table if exists park";
        	
        	jdbcTemplate.execute(sql);
        	
        	sql = "CREATE TABLE park " +
					"( " +
					    "id serial NOT NULL , " +
					    "country_code character varying(2) NOT NULL, " +
					    "country_name character varying(50) NOT NULL, " +
					    "name character varying(200) NOT NULL, " +
					    "toponym_name character varying(200) NOT NULL, " +
					    "latitude double precision NOT NULL, " +
					    "longitude double precision NOT NULL, " +
					    "CONSTRAINT park_pkey PRIMARY KEY (id) " +
					"); ";
        	
        	jdbcTemplate.execute(sql);
                
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating parks table." + e.getMessage());
        } 
    }


}
