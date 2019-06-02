package com.thescientist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatServiceIntegrationTest {

    private CatService service;

    @BeforeEach
    void setUp() {
        service = new CatService();
    }

    @Test
    void whereIsCat() throws IOException {
        assertTrue("Meow!".equals(service.whereIsCat()) || ".....".equals(service.whereIsCat()));
    }
}