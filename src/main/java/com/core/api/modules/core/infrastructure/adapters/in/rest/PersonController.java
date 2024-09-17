package com.core.api.modules.core.infrastructure.adapters.in.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.Person;
import com.core.api.modules.core.domain.ports.gateway.PersonRest;
import com.core.api.modules.core.application.services.PersonService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/persons")
public class PersonController implements PersonRest {
    @Autowired
    private PersonService personService;

    @Override
    @GetMapping
    public Response<?> getAll(
            @RequestParam(name = "idPerson", required = false) Integer idPerson,
            @RequestParam(name = "status", required = false) Integer status
    ) {
        try {
            Set<Person> persons = personService.getAll(idPerson, status);
            return new Response<>(HttpStatus.OK, "Se obtuvieron los registros correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @GetMapping("/{idPerson}")
    public Response<?> getById(@PathVariable Integer idPerson) {
        try {
            Set<Person> persons = new HashSet<>();
            persons.add(personService.getById(idPerson));
            return new Response<>(HttpStatus.OK, "Se obtuvieron los registros correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping()
    public Response<?> save(@RequestBody Person person) {
        try {
            Set<Person> persons = new HashSet<>();
            persons.add(personService.save(person));
            return new Response<>(HttpStatus.OK, "Se guardó el registro correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/enable")
    public Response<?> enable(@RequestBody Person person) {
        try {
            Set<Person> persons = new HashSet<>();
            persons.add(personService.enable(person));
            return new Response<>(HttpStatus.OK, "Se Habilitó el registro correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/disable")
    public Response<?> disable(@RequestBody Person person) {
        try {
            Set<Person> persons = new HashSet<>();
            persons.add(personService.disable(person));
            return new Response<>(HttpStatus.OK, "Se Dehabilitó el registro correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/delete")
    public Response<?> delete(@RequestBody Person person) {
        try {
            Set<Person> persons = new HashSet<>();
            persons.add(personService.delete(person));
            return new Response<>(HttpStatus.OK, "Se Elimino el registro correctamente.", persons);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

}