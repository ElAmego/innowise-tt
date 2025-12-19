package by.egoramel.tt.factory;

import by.egoramel.tt.entity.Aircraft;
import by.egoramel.tt.entity.AircraftData;

import java.util.List;

public interface AircraftFactory {
    List<Aircraft> createAircrafts(final List<AircraftData> aircraftDataList);
}