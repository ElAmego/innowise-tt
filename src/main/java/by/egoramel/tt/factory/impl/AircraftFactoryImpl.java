package by.egoramel.tt.factory.impl;

import by.egoramel.tt.entity.Aircraft;
import by.egoramel.tt.entity.AircraftData;
import by.egoramel.tt.factory.AircraftFactory;

import java.util.ArrayList;
import java.util.List;

public final class AircraftFactoryImpl implements AircraftFactory {
    @Override
    public List<Aircraft> createAircrafts(final List<AircraftData> aircraftDataList) {
        final List<Aircraft> aircrafts = new ArrayList<>();
        int index = 0;

        for (final AircraftData aircraftData: aircraftDataList) {
            final Aircraft aircraft = new Aircraft(
                    index++,
                    aircraftData.airplaneName(),
                    aircraftData.airplaneSize(),
                    aircraftData.passengersNumber(),
                    aircraftData.operation()
            );

            aircrafts.add(aircraft);
        }

        return aircrafts;
    }
}