package by.egoramel.tt.parser.impl;

import by.egoramel.tt.entity.AircraftData;
import by.egoramel.tt.entity.AircraftOperation;
import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.parser.AircraftListParser;

import java.util.ArrayList;
import java.util.List;

import static by.egoramel.tt.entity.AircraftOperation.LOAD;
import static by.egoramel.tt.entity.AircraftOperation.UNLOAD;

public final class AircraftListParserImpl implements AircraftListParser {
    private static final String SUBSTRING_REGEX = ", ";

    @Override
    public List<AircraftData> parseList(final List<String> rows) throws CustomException {
        final List<AircraftData> aircraftDataList = new ArrayList<>();

        for (final String row: rows) {
            final AircraftData aircraftData = createAircraftData(row);
            aircraftDataList.add(aircraftData);
        }

        return aircraftDataList;
    }

    private AircraftData createAircraftData(final String row) throws CustomException {
        final String[] subRows = row.split(SUBSTRING_REGEX);

        final String airplaneName = subRows[0];

        final String airplaneSizeString = subRows[1];
        final int airplaneSize = Integer.parseInt(airplaneSizeString);

        final String passengerNumbersString = subRows[2];
        final int passengerNumbers = Integer.parseInt(passengerNumbersString);

        if (passengerNumbers < 0 || passengerNumbers > airplaneSize) {
            throw new CustomException("Invalid integer in row.");
        }

        final String operationString = subRows[3];
        final AircraftOperation operation = switch (operationString) {
            case "LOAD" -> LOAD;
            case "UNLOAD" -> UNLOAD;
            default -> throw new CustomException("Invalid value in row.");
        };

        return new AircraftData(airplaneName, airplaneSize, passengerNumbers, operation);
    }
}