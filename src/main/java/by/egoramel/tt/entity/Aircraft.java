package by.egoramel.tt.entity;

import by.egoramel.tt.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public final class Aircraft implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private final long aircraftId;
    private final String aircraftName;
    private final int aircraftSize;
    private final AtomicInteger passengersNumber;
    private final AircraftOperation aircraftOperation;

    public Aircraft(final long aircraftId, String aircraftName, final int aircraftSize, final int passengersNumber,
                    final AircraftOperation aircraftOperation) {
        this.aircraftId = aircraftId;
        this.aircraftName = aircraftName;
        this.aircraftSize = aircraftSize;
        this.passengersNumber = new AtomicInteger(passengersNumber);
        this.aircraftOperation = aircraftOperation;
    }

    @Override
    public void run() {
        LOGGER.info("Aircraft {} starting operation: {}.", aircraftName, aircraftOperation);

        try {
            final Terminal terminal = Terminal.getInstance();
            final Gate gate = terminal.occupyGate();
            LOGGER.info("Aircraft {} occupied gate {}.", aircraftName, gate);

            switch (aircraftOperation) {
                case LOAD -> loadAircraft();
                case UNLOAD -> unloadAircraft();
                default -> throw new CustomException("Invalid type of aircraft operation.");
            }

            LOGGER.debug("Aircraft {} freeing gate {}.", aircraftName, gate);
            terminal.freeGate(gate);
        } catch (final CustomException e) {
            LOGGER.error("Aircraft {}: Custom exception occurred.", aircraftName, e);
            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }
    }

    public void loadAircraft() {
        LOGGER.debug("Aircraft {} starting LOAD process.", aircraftName);
        final Terminal terminal = Terminal.getInstance();
        final int passengersNumberInt = passengersNumber.get();
        final int necessaryPassengers = aircraftSize - passengersNumberInt;

        if (necessaryPassengers <= 0) {
            LOGGER.info("Aircraft {}: No passengers needed.", aircraftName);
            return;
        }

        try {
            LOGGER.debug("Aircraft {} requesting {} passengers from terminal.", aircraftName, necessaryPassengers);
            final int passedPassengers = terminal.passPassengersToAircraft(necessaryPassengers);
            LOGGER.debug("Aircraft {} received {} passengers from terminal.", aircraftName, passedPassengers);
            if (passedPassengers <= 0) {
                LOGGER.info("Aircraft {}: No passengers received from terminal.", aircraftName);
                return;
            }

            LOGGER.info("Aircraft {} boarding passengers.", aircraftName);
            for (int i = 0; i < passedPassengers; i++) {
                passengersNumber.incrementAndGet();
                TimeUnit.SECONDS.sleep(1);
            }

            LOGGER.info("Aircraft {} completed boarding.", aircraftName);
        } catch (final InterruptedException e) {
            LOGGER.warn("Aircraft {}: Loading interrupted.", aircraftName);
            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }
    }

    public void unloadAircraft() {
        LOGGER.debug("Aircraft {} starting UNLOAD process.", aircraftName);
        final Terminal terminal = Terminal.getInstance();
        final int passengersNumberInt = passengersNumber.get();

        LOGGER.debug("Aircraft {}: current passengers to unload={}.", aircraftName, passengersNumberInt);
        if (passengersNumberInt <= 0) {
            LOGGER.info("Aircraft {}: No passengers to unload.", aircraftName);
            return;
        }

        try {
            final int availableCapacity = terminal.getAvailableTerminalCapacity();
            final int unloadQuantity = Math.min(passengersNumberInt, availableCapacity);

            if (unloadQuantity <= 0) {
                LOGGER.info("Aircraft {}: Terminal has no capacity for unloading.", aircraftName);
                return;
            }

            LOGGER.info("Aircraft {} unloading passengers.", aircraftName);
            for (int i = 0; i < unloadQuantity; i++) {
                passengersNumber.decrementAndGet();
                TimeUnit.SECONDS.sleep(1);
            }

            final int extraPassengers = terminal.passPassengersFromAircraft(unloadQuantity);

            LOGGER.info("Aircraft {} loading extra passengers.", aircraftName);
            for (int i = 0; i < extraPassengers; i++) {
                passengersNumber.incrementAndGet();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Aircraft {}: Unloading interrupted", aircraftName);
            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public int getAircraftSize() {
        return aircraftSize;
    }

    public AtomicInteger getPassengersNumber() {
        return passengersNumber;
    }

    public AircraftOperation getAircraftOperation() {
        return aircraftOperation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        final Aircraft aircraft = (Aircraft) o;
        return aircraftId == aircraft.aircraftId && aircraftSize == aircraft.aircraftSize
                && Objects.equals(aircraftName, aircraft.aircraftName)
                && (passengersNumber.get() == aircraft.passengersNumber.get())
                && aircraftOperation == aircraft.aircraftOperation;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(aircraftId);
        result = 31 * result + Objects.hashCode(aircraftName);
        result = 31 * result + aircraftSize;
        result = 31 * result + passengersNumber.hashCode();
        result = 31 * result + Objects.hashCode(aircraftOperation);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Aircraft.class.getSimpleName() + "[", "]")
                .add("aircraftId=" + aircraftId)
                .add("aircraftName='" + aircraftName + "'")
                .add("aircraftSize=" + aircraftSize)
                .add("passengersNumber=" + passengersNumber)
                .add("aircraftOperation=" + aircraftOperation)
                .toString();
    }
}