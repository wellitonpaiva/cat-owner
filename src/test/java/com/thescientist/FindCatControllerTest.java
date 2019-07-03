package com.thescientist;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@MicronautTest
public class FindCatControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Mock
    private CatService service;

    @InjectMocks
    private FindCatController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() {
        try(RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {
            assertEquals(HttpStatus.OK, client.toBlocking().exchange("/findCat").status());
        }
    }

    @Test
    void whenOwnerFindsCatThenKittyKitty() throws IOException {
        when(service.whereIsCat()).thenReturn("Meow!");
        assertEquals("Kitty Kitty!", controller.whereIsCat());
    }

    @Test
    void whenOwnerNotFindsCatThenBuaaa() throws IOException {
        when(service.whereIsCat()).thenReturn(".....");
        assertEquals("Chuif chuif", controller.whereIsCat());
    }


}
