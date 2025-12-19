package by.egoramel.tt;

import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.parser.AircraftListParser;
import by.egoramel.tt.parser.impl.AircraftListParserImpl;
import by.egoramel.tt.reader.AircraftFileReader;
import by.egoramel.tt.reader.impl.AircraftFileReaderImpl;

public class Main {
    public static void main(String[] args) throws CustomException {
        final AircraftFileReader aircraftFileReader = new AircraftFileReaderImpl("data/aircraft.txt");
        var list = aircraftFileReader.readAircraftFile();
        final AircraftListParser aircraftListParser = new AircraftListParserImpl();
        System.out.println(aircraftListParser.parseList(list));
    }
}