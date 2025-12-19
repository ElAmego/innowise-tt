package by.egoramel.tt.entity;

import java.util.Objects;
import java.util.StringJoiner;

@SuppressWarnings("unused")
public final class Aircraft {
    private final long airplaneId;
    private final String airplaneName;
    private final int airplaneSize;
    private final int passengersNumber;
    private final AircraftOperation aircraftOperation;

    public Aircraft(final long airplaneId, String airplaneName, final int airplaneSize, final int passengersNumber, final AircraftOperation aircraftOperation) {
        this.airplaneId = airplaneId;
        this.airplaneName = airplaneName;
        this.airplaneSize = airplaneSize;
        this.passengersNumber = passengersNumber;
        this.aircraftOperation = aircraftOperation;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Aircraft.class.getSimpleName() + "[", "]")
                .add("airplaneId=" + airplaneId)
                .add("airplaneName='" + airplaneName + "'")
                .add("airplaneSize=" + airplaneSize)
                .add("passengersNumber=" + passengersNumber)
                .add("aircraftOperation=" + aircraftOperation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Aircraft aircraft = (Aircraft) o;
        return airplaneId == aircraft.airplaneId && airplaneSize == aircraft.airplaneSize && passengersNumber == aircraft.passengersNumber && Objects.equals(airplaneName, aircraft.airplaneName) && aircraftOperation == aircraft.aircraftOperation;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(airplaneId);
        result = 31 * result + Objects.hashCode(airplaneName);
        result = 31 * result + airplaneSize;
        result = 31 * result + passengersNumber;
        result = 31 * result + Objects.hashCode(aircraftOperation);
        return result;
    }

    public long getAirplaneId() {
        return airplaneId;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    public int getAirplaneSize() {
        return airplaneSize;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public AircraftOperation getAircraftOperation() {
        return aircraftOperation;
    }
}