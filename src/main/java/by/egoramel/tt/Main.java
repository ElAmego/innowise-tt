package by.egoramel.tt;

import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.parser.AircraftFileReader;
import by.egoramel.tt.parser.impl.AircraftFileReaderImpl;

public class Main {
    public static void main(String[] args) throws CustomException {
        final AircraftFileReader aircraftFileReader = new AircraftFileReaderImpl("data/aircraft.txt");
        System.out.println(aircraftFileReader.parseAircraftFile());
    }
}