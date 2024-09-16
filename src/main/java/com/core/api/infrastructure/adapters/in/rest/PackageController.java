package com.core.api.infrastructure.adapters.in.rest;

import com.core.api.application.services.PackageService;
import com.core.api.domain.models.Response;
import com.core.api.domain.models.Package;
import com.core.api.domain.ports.gateway.PackageRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/packages")
public class PackageController implements PackageRest {

    @Autowired
    private PackageService packageService;

    @Override
    @PostMapping
    public Response<?> createPackage(
            @RequestBody Package packageObject
    ) {
        try {
            packageService.createPackage(packageObject);
            return new Response<>(
                    HttpStatus.OK,
                    "Package generado correctamente",
                    null
            );
        } catch (Exception e) {
            return new Response<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null
            );
        }
    }
}
