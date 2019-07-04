package com.thescientist;

import lombok.Getter;

import java.time.Instant;
import java.util.function.Supplier;

@Getter
public class CircuitBreaker {

    private static final String BAD_RESPONSE = "...";
    private Instant openStart;

    private int waitTime;
    private int counter;

    public CircuitBreaker(int waitTime) {
        this.waitTime = waitTime;
    }

    enum State {
        OPEN,
        CLOSED
    }

    private State state = State.CLOSED;

    public String request(Supplier f) {
        String response;
        if (state == State.OPEN && Instant.now().isAfter(openStart.plusMillis(waitTime))) {
            response = (String) f.get();
            if (!BAD_RESPONSE.equals(response)) {
                state = State.CLOSED;
                return response;
            }
        }
        if (state != State.OPEN) {
            response = (String) f.get();
            if (BAD_RESPONSE.equals(response)) {
                counter++;
                if (counter > 10) {
                    state = State.OPEN;
                }
            } else {
                counter = 0;
            }
            return response;
        }
        return "API is not working, please try again later.";
    }

    void openCircuit() {
        state = State.OPEN;
        openStart = Instant.now();
    }
}
