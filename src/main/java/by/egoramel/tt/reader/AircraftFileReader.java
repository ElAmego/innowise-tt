package by.egoramel.tt.reader;

import by.egoramel.tt.exception.CustomException;

import java.util.List;

public interface AircraftFileReader {
    List<String> readAircraftFile() throws CustomException;
}