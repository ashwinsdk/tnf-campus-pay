package main;

import config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.trace("TRACE: Application Started");

        logger.debug("DEBUG: Initializing Database");

        try {

            logger.info("Attempting Database Connection...");

            System.out.println(
                    DatabaseConfig.getConnection());

            logger.info(
                    "Database Connected Successfully");

        } catch (Exception e) {

            logger.error(
                    "Database Connection Failed",
                    e);
        }

        logger.warn(
                "WARN: This is a sample warning");

        logger.error(
                "ERROR: Sample error message");

        logger.info(
                "Application Execution Completed");
    }
}