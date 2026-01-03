package by.egoramel.tt;

import by.egoramel.tt.entity.Aircraft;
import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.factory.AircraftFactory;
import by.egoramel.tt.factory.impl.AircraftFactoryImpl;
import by.egoramel.tt.parser.AircraftListParser;
import by.egoramel.tt.parser.impl.AircraftListParserImpl;
import by.egoramel.tt.reader.AircraftFileReader;
import by.egoramel.tt.reader.impl.AircraftFileReaderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws CustomException, ExecutionException, InterruptedException {
        final AircraftFileReader aircraftFileReader = new AircraftFileReaderImpl("data/aircraft.txt");
        var aircraftRowList = aircraftFileReader.readAircraftFile();
        final AircraftListParser aircraftListParser = new AircraftListParserImpl();
        var aircraftDataList = aircraftListParser.parseList(aircraftRowList);
        final AircraftFactory aircraftFactory = new AircraftFactoryImpl();
        var list = aircraftFactory.createAircrafts(aircraftDataList);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (Aircraft aircraft : list) {
            Callable<Void> task = () -> {
                aircraft.run();
                return null;
            };
            futures.add(executor.submit(task));
        }

        for (Future<?> future : futures) {
            future.get();
        }
        executor.shutdown();
    }
}