package com.core.api.modules.core.infrastructure.adapters.in.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.Role;
import com.core.api.modules.core.domain.ports.gateway.RoleRest;
import com.core.api.modules.core.application.services.RoleService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController implements RoleRest {
    @Autowired
    private RoleService roleService;

    @Override
    @GetMapping
    public Response<?> getAll(
            @RequestParam(name = "idRole", required = false) Integer idRole,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "status", required = false) Integer status
    ) {
        try {
            Set<Role> roles = roleService.getAll(idRole, name, status);
            return new Response<>( HttpStatus.OK, "Se obtuvieron los registros correctamente.", roles);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @GetMapping("/{idRole}")
    public Response<?> getById(@PathVariable Integer idRole) {
       try { 
           Set<Role> roles = new HashSet<>();
           roles.add(roleService.getById(idRole));
           return new Response<>( HttpStatus.OK, "Se obtuvieron los registros correctamente.",roles);
       } catch( Exception e ) { 
           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
       }
   }

    @Override
    @PostMapping()
    public Response<?> save(@RequestBody Role role) {
       try { 
           Set<Role> roles = new HashSet<>();
           roles.add(roleService.save(role));
           return new Response<>( HttpStatus.OK, "Se guardó el registro correctamente.",roles);
       } catch( Exception e ) { 
           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
       }
   }

    @Override
    @PostMapping("/enable")
    public Response<?> enable(@RequestBody Role role) {
       try { 
           Set<Role> roles = new HashSet<>();
           roles.add(roleService.enable(role));
           return new Response<>( HttpStatus.OK, "Se Habilitó el registro correctamente.",roles);
       } catch( Exception e ) { 
           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
       }
   }

    @Override
    @PostMapping("/disable")
    public Response<?> disable(@RequestBody Role role) {
       try { 
           Set<Role> roles = new HashSet<>();
           roles.add(roleService.disable(role));
        return new Response<>( HttpStatus.OK, "Se Dehabilitó el registro correctamente.",roles);
       } catch( Exception e ) { 
         return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
       }
   }

    @Override
    @PostMapping("/delete")
    public Response<?> delete(@RequestBody Role role) {
       try { 
           Set<Role> roles = new HashSet<>();
           roles.add(roleService.delete(role));
           return new Response<>( HttpStatus.OK, "Se Elimino el registro correctamente.",roles);
       } catch( Exception e ) { 
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
       }
   }

}