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

    @Inject
    public FindCatController(CatService service) {
        this.service = service;
    }

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String whereIsCat() throws IOException {
        return "Meow!".equals(service.whereIsCat()) ? "Kitty Kitty!" : "Chuif chuif";
    }
}