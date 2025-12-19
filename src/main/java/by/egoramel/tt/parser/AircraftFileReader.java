package by.egoramel.tt.parser;

import by.egoramel.tt.exception.CustomException;

import java.util.List;

public interface AircraftFileReader {
    List<String> parseAircraftFile() throws CustomException;
}