package com.LeahGrace.MySQLDriverSpringBootApi.controllers;

import com.LeahGrace.MySQLDriverSpringBootApi.models.Developer;
import com.LeahGrace.MySQLDriverSpringBootApi.repositories.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {
    @Autowired
    private DeveloperRepository repository;

    @GetMapping
    public @ResponseBody
    List<Developer> getDevelopers() {
        return repository.findAll();
    }

    @PostMapping
    public @ResponseBody Developer createDeveloper(@RequestBody Developer newDeveloper){
       // newDeveloper = repository.save(newDeveloper);
        return (Developer) repository.save(newDeveloper);
    }

}
