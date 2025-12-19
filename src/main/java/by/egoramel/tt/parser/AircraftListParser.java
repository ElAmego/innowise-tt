package by.egoramel.tt.parser;

import by.egoramel.tt.entity.AircraftData;
import by.egoramel.tt.exception.CustomException;

import java.util.List;

public interface AircraftListParser {
    List<AircraftData> parseList(final List<String> rows) throws CustomException;
}