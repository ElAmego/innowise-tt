package by.egoramel.tt.factory.impl;

import by.egoramel.tt.entity.Aircraft;
import by.egoramel.tt.entity.AircraftData;
import by.egoramel.tt.factory.AircraftFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class AircraftFactoryImpl implements AircraftFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Aircraft> createAircrafts(final List<AircraftData> aircraftDataList) {
        LOGGER.info("Starting aircraft creation process");
        final List<Aircraft> aircrafts = new ArrayList<>();
        int index = 0;

        LOGGER.debug("Processing {} aircraft data entries.", aircraftDataList.size());

        for (final AircraftData aircraftData: aircraftDataList) {
            LOGGER.debug("Creating aircraft.");
            final Aircraft aircraft = new Aircraft(
                    index++,
                    aircraftData.airplaneName(),
                    aircraftData.airplaneSize(),
                    aircraftData.passengersNumber(),
                    aircraftData.operation()
            );

            aircrafts.add(aircraft);
        }

        LOGGER.info("Aircraft creation completed. Total created: {}.", aircrafts.size());
        return aircrafts;
    }
}