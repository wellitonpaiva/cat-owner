package com.thescientist;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import javax.inject.Inject;
import java.io.IOException;

@Controller("/findCat")
public class FindCatController {

    private CatService service;
    private CircuitBreaker cb = new CircuitBreaker(20000);

    @Inject
    public FindCatController(CatService service) {
        this.service = service;
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String whereIsCat() {
        String response = "[" + cb.request(service::whereIsCat) + "]";
        if("Meow!".equals(response)) {
            return "Kitty Kitty! " + response;
        } else {
            return "Chuif chuif " + response;
        }
    }
}