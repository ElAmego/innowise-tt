package by.egoramel.tt;

import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.factory.AircraftFactory;
import by.egoramel.tt.factory.impl.AircraftFactoryImpl;
import by.egoramel.tt.parser.AircraftListParser;
import by.egoramel.tt.parser.impl.AircraftListParserImpl;
import by.egoramel.tt.reader.AircraftFileReader;
import by.egoramel.tt.reader.impl.AircraftFileReaderImpl;

public class Main {
    public static void main(String[] args) throws CustomException {
        final AircraftFileReader aircraftFileReader = new AircraftFileReaderImpl("data/aircraft.txt");
        var aircraftRowList = aircraftFileReader.readAircraftFile();
        final AircraftListParser aircraftListParser = new AircraftListParserImpl();
        var aircraftDataList = aircraftListParser.parseList(aircraftRowList);
        final AircraftFactory aircraftFactory = new AircraftFactoryImpl();
        for (var i: aircraftFactory.createAircrafts(aircraftDataList)) {
            System.out.println(i);
        }
    }
}