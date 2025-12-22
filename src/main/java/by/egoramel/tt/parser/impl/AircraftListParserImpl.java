package by.egoramel.tt.parser.impl;

import by.egoramel.tt.entity.AircraftData;
import by.egoramel.tt.entity.AircraftOperation;
import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.parser.AircraftListParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static by.egoramel.tt.entity.AircraftOperation.LOAD;
import static by.egoramel.tt.entity.AircraftOperation.UNLOAD;

public final class AircraftListParserImpl implements AircraftListParser {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUBSTRING_REGEX = ", ";

    @Override
    public List<AircraftData> parseList(final List<String> rows) throws CustomException {
        LOGGER.info("Starting parsing of aircraft data list with {} rows.", rows.size());
        final List<AircraftData> aircraftDataList = new ArrayList<>();

        for (final String row: rows) {
            LOGGER.debug("Processing row: {}.", row);
            final AircraftData aircraftData = createAircraftData(row);
            aircraftDataList.add(aircraftData);
        }

        LOGGER.info("Successfully parsed {} aircraft data entries.", aircraftDataList.size());
        return aircraftDataList;
    }

    private AircraftData createAircraftData(final String row) throws CustomException {
        LOGGER.debug("Creating AircraftData from row: {}.", row);
        final String[] subRows = row.split(SUBSTRING_REGEX);

        final String airplaneName = subRows[0];
        LOGGER.debug("Extracted airplane name: {}.", airplaneName);

        final String airplaneSizeString = subRows[1];
        final int airplaneSize = Integer.parseInt(airplaneSizeString);
        LOGGER.debug("Parsed airplane size: {}.", airplaneSize);

        final String passengerNumbersString = subRows[2];
        final int passengerNumbers = Integer.parseInt(passengerNumbersString);
        LOGGER.debug("Parsed passenger numbers: {}.", passengerNumbers);

        if (passengerNumbers < 0 || passengerNumbers > airplaneSize) {
            LOGGER.error("Invalid passenger numbers: {} for airplane size: {}.", passengerNumbers, airplaneSize);
            throw new CustomException("Invalid integer in row.");
        }

        final String operationString = subRows[3];
        LOGGER.debug("Extracted operation string: {}.", operationString);
        final AircraftOperation operation = switch (operationString) {
            case "LOAD" -> LOAD;
            case "UNLOAD" -> UNLOAD;
            default -> throw new CustomException("Invalid value in row.");
        };

        LOGGER.debug("Successfully created AircraftData for {} with operation {}.", airplaneName, operation);
        return new AircraftData(airplaneName, airplaneSize, passengerNumbers, operation);
    }
}