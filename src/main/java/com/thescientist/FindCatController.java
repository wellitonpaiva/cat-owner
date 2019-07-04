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
        return "Meow!".equals(cb.request(service::whereIsCat)) ? "Kitty Kitty!" : "Chuif chuif";
    }
}