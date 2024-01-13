package com.robware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robware.models.LoginDetails;

import java.io.*;
import java.util.stream.Collectors;

public class Main {

    private static final String DETAILS_FILE_NAME = "details.json";

    public static void main(String[] args) {
        System.out.println("Starting program");

        String fileContent = null;
        try {
            fileContent = readDetailsFile();
        } catch (FileNotFoundException e) {
            System.out.println("No details.json detected - first run");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(fileContent == null) {
            System.out.println("JSON file was null");
            return;
        }

        try {
            var mapper = new ObjectMapper();
            var details = mapper.readValue(fileContent, LoginDetails.class);

            System.out.println("Read details and got authToken: " + details.authToken());
        } catch(Exception e) {
            System.out.println("Error parsing JSON");
            e.printStackTrace();
            return;
        }
    }

    static String readDetailsFile() throws IOException {
        try(var in = new BufferedReader(new InputStreamReader(new FileInputStream(DETAILS_FILE_NAME)))) {
            return in.lines().collect(Collectors.joining("\n"));
        }
    }
}