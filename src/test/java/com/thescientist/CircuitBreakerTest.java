package com.thescientist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CircuitBreakerTest {

    private CircuitBreaker cb = new CircuitBreaker();

    @Mock
    CatService catService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenAPIisWorkingThenCircuitIsClosed() throws IOException {
        String response = "Meow!";
        when(catService.whereIsCat()).thenReturn(response);
        assertEquals(response, cb.request(catService::whereIsCat));
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void whenAPIFailsOneTimeCounterAddsPlusOne() {
        String badResponse = "...";
        when(catService.whereIsCat()).thenReturn(badResponse);
        assertEquals(badResponse, cb.request(catService::whereIsCat));
        assertEquals(1, cb.getCounter());
    }

    @Test
    void whenAPIFailsMoreThanTenTimesStatusMoveToOpen() {
        String badResponse = "...";
        when(catService.whereIsCat()).thenReturn(badResponse);
        for (int i = 0; i <= 10; i++) {
            cb.request(catService::whereIsCat);
        }
        assertEquals(11, cb.getCounter());
        assertEquals(CircuitBreaker.State.OPEN, cb.getState());
    }

    @Test
    void whenStateOpenDontCallAPI() {
        cb.setState(CircuitBreaker.State.OPEN);
        assertEquals("API is not working, please try again later.", cb.request(catService::whereIsCat));
        verify(catService, times(0)).whereIsCat();
    }
}