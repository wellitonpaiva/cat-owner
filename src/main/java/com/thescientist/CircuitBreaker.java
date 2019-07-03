package com.thescientist;

import java.util.function.Supplier;

public class CircuitBreaker {

    enum State {
        OPEN,
        CLOSED,
        SEMIOPEN
    }
    private int counter;

    private State state = State.CLOSED;

    public String request(Supplier f) {
        if(state != State.OPEN) {
            counter++;
            if (counter > 10) {
                state = State.OPEN;
            }
            return (String) f.get();
        }
        return "API is not working, please try again later.";
    }

    public int getCounter() {
        return counter;
    }

    public State getState() {
        return state;
    }

    void setState(State state) {
        this.state = state;
    }
}
