package by.egoramel.tt.entity;

import by.egoramel.tt.exception.CustomException;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class Terminal {
    private static final int TERMINAL_MAX_CAPACITY = 10000;
    private static final int GATE_QUANTITY = 10;
    private static final Terminal INSTANCE = new Terminal();

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
        return INSTANCE;
    }

    public Gate occupyGate() throws CustomException {
        lock.lock();

        try {
            while (gates.isEmpty()) {
                gateAvailable.await();
            }

            return gates.poll();
        } catch (final InterruptedException e) {
            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
            throw new CustomException(e);
        } finally {
            lock.unlock();
        }
    }

    public void freeGate(final Gate gate) {
        if (gate != null) {
            lock.lock();
            try {
                gates.addLast(gate);
                gateAvailable.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public int passPassengersToAircraft(final int necessaryPassengers) {
        while (true) {
            final int currentCapacity = currentTerminalCapacity.get();
            final int possibleQuantity = Math.min(necessaryPassengers, currentCapacity);

            if (possibleQuantity <= 0) {
                return 0;
            }

            final int newCapacity = currentCapacity - possibleQuantity;
            final boolean isEqual = currentTerminalCapacity.compareAndSet(currentCapacity, newCapacity);

            if (isEqual) {
                return possibleQuantity;
            }
        }
    }

    public int passPassengersFromAircraft(final int passengersFromAircraft) {
        while (true) {
            final int currentCapacity = currentTerminalCapacity.get();
            int newCapacity = passengersFromAircraft + currentCapacity;
            int extraPassengers = 0;

            if (newCapacity > TERMINAL_MAX_CAPACITY) {
                extraPassengers = newCapacity - TERMINAL_MAX_CAPACITY;
                newCapacity = TERMINAL_MAX_CAPACITY;
            }

            final boolean isEqual = currentTerminalCapacity.compareAndSet(currentCapacity, newCapacity);
            if (isEqual) {
                return extraPassengers;
            }
        }
    }

    public int getAvailableTerminalCapacity() {
        final int currentCapacity = currentTerminalCapacity.get();
        return TERMINAL_MAX_CAPACITY - currentCapacity;
    }
}