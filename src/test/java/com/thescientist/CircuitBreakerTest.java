package com.thescientist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CircuitBreakerTest {

    private final int waitTime = 1000;
    private CircuitBreaker cb = new CircuitBreaker(waitTime);

    @Mock
    CatService catService;
    private static final String GOOD_RESPONSE = "Meow!";
    private static final String BAD_RESPONSE = "...";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenAPIisWorkingThenCircuitIsClosed() {
        when(catService.whereIsCat()).thenReturn(GOOD_RESPONSE);
        assertEquals(GOOD_RESPONSE, cb.request(catService::whereIsCat));
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void whenAPIFailsOneTimeCounterAddsPlusOne() {
        when(catService.whereIsCat()).thenReturn(BAD_RESPONSE);
        assertEquals(BAD_RESPONSE, cb.request(catService::whereIsCat));
        assertEquals(1, cb.getCounter());
    }

    @Test
    void givenFiveFailsIfAPIIsBankCounterReset() {
        when(catService.whereIsCat()).thenReturn(BAD_RESPONSE);
        for (int i = 0; i < 5; i++) {
            assertEquals(BAD_RESPONSE, cb.request(catService::whereIsCat));
        }
        assertEquals(5, cb.getCounter());
        when(catService.whereIsCat()).thenReturn(GOOD_RESPONSE);
        assertEquals(GOOD_RESPONSE, cb.request(catService::whereIsCat));
        assertEquals(0, cb.getCounter());
    }

    @Test
    void whenAPIFailsMoreThanTenTimesStatusMoveToOpen() {
        when(catService.whereIsCat()).thenReturn(BAD_RESPONSE);
        for (int i = 0; i <= 10; i++) {
            cb.request(catService::whereIsCat);
        }
        assertEquals(11, cb.getCounter());
        assertEquals(CircuitBreaker.State.OPEN, cb.getState());
    }

    @Test
    void whenStateOpenDontCallAPI() {
        cb.openCircuit();
        assertEquals("API is not working, please try again later.", cb.request(catService::whereIsCat));
        verify(catService, times(0)).whereIsCat();
    }

    @Test
    void givenStateOpenAndAPIworkingWhenPresetTimePassThenStateIsClosed() throws InterruptedException {
        when(catService.whereIsCat()).thenReturn(GOOD_RESPONSE);

        cb.openCircuit();

        assertEquals(CircuitBreaker.State.OPEN, cb.getState());
        sleep(waitTime);

        assertEquals(GOOD_RESPONSE, cb.request(catService::whereIsCat));
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }
}