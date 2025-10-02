package org.example;

import org.example.service.LoggingService;
import org.example.service.ProcessorService;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {

        LoggingService loggingService = new LoggingService();
        ProcessorService processorService = new ProcessorService(loggingService);

        InputStream is = Main.class.getResourceAsStream("/logs.log");
        processorService.parseFile(is);
    }
}