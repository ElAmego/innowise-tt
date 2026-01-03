package by.egoramel.tt.entity;

import by.egoramel.tt.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class Terminal {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int TERMINAL_MAX_CAPACITY = 10000;
    private static final int GATE_QUANTITY = 10;
    private static final Terminal instance = new Terminal();

    private final AtomicInteger currentTerminalCapacity;
    private final ArrayDeque<Gate> gates;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition gateAvailable = lock.newCondition();

    private Terminal() {
        currentTerminalCapacity = new AtomicInteger(0);
        gates = new ArrayDeque<>(GATE_QUANTITY);

        for (int i = 0; i < GATE_QUANTITY; i++) {
            final Gate gate = new Gate(i);
            gates.offer(gate);
        }
    }

    public static Terminal getInstance() {
        return instance;
    }

    public Gate occupyGate() throws CustomException {
        LOGGER.debug("Request to occupy gate received.");
        lock.lock();

        Gate gate = null;

        try {
            while (gates.isEmpty()) {
                LOGGER.debug("No gates available, waiting...");
                gateAvailable.await();
            }

            LOGGER.info("Gate occupied, remaining gates: {}.", gates.size());
            gate = gates.poll();
        } catch (final InterruptedException e) {
            LOGGER.error("Gate occupation interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            LOGGER.trace("Lock released after gate occupation attempt.");
        }

        return gate;
    }

    public void freeGate(final Gate gate) {
        LOGGER.debug("Request to free gate: {}.", gate);

        if (gate != null) {
            lock.lock();
            LOGGER.trace("Lock acquired for gate freeing.");
            try {
                gates.addLast(gate);
                LOGGER.info("Gate {} freed, available gates: {}.", gate, gates.size());
                gateAvailable.signal();
                LOGGER.trace("Gate availability signal sent.");
            } finally {
                lock.unlock();
                LOGGER.trace("Lock released after gate freeing.");
            }
        }
    }

    public int passPassengersToAircraft(final int necessaryPassengers) {
        LOGGER.debug("Request to pass {} passengers to aircraft.", necessaryPassengers);
        while (true) {
            final int currentCapacity = currentTerminalCapacity.get();
            final int possibleQuantity = Math.min(necessaryPassengers, currentCapacity);
            LOGGER.trace("Current terminal capacity: {}, possible to pass: {}.", currentCapacity, possibleQuantity);

            if (possibleQuantity <= 0) {
                LOGGER.debug("No passengers available in terminal to pass to aircraft.");
                return 0;
            }

            final int newCapacity = currentCapacity - possibleQuantity;
            final boolean isEqual = currentTerminalCapacity.compareAndSet(currentCapacity, newCapacity);

            if (isEqual) {
                LOGGER.info("Passed passengers to aircraft. Terminal capacity changed from {} to {}.",
                        currentCapacity, newCapacity);
                return possibleQuantity;
            }
        }
    }

    public int passPassengersFromAircraft(final int passengersFromAircraft) {
        LOGGER.debug("Request to receive {} passengers from aircraft.", passengersFromAircraft);
        while (true) {
            final int currentCapacity = currentTerminalCapacity.get();
            int newCapacity = passengersFromAircraft + currentCapacity;
            int extraPassengers = 0;
            LOGGER.trace("Current capacity: {}, proposed new capacity: {}.", currentCapacity, newCapacity);

            if (newCapacity > TERMINAL_MAX_CAPACITY) {
                extraPassengers = newCapacity - TERMINAL_MAX_CAPACITY;
                newCapacity = TERMINAL_MAX_CAPACITY;
            }

            final boolean isEqual = currentTerminalCapacity.compareAndSet(currentCapacity, newCapacity);
            if (isEqual) {
                LOGGER.info("Received passengers from aircraft. Terminal capacity changed from {} to {}. " +
                        "Extra passengers: {}", currentCapacity, newCapacity, extraPassengers);
                return extraPassengers;
            }
        }
    }

    public int getAvailableTerminalCapacity() {
        final int currentCapacity = currentTerminalCapacity.get();
        return TERMINAL_MAX_CAPACITY - currentCapacity;
    }
}