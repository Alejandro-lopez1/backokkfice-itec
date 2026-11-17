package com.itec.controller;

import com.itec.model.Usuario;
import com.itec.service.AuthService;

/**
 * Controlador delgado de autenticacion (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe las credenciales de la Vista,
 * las pasa al AuthService y devuelve la respuesta. La capa de
 * presentacion sigue acoplada solo al Controlador.
 */
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /** Delega en el Service la validacion de credenciales. */
    public Usuario autenticar(String username, String password) {
        return service.autenticar(username, password);
    }

    /** Delega en el Service el cierre de sesion. */
    public void cerrarSesion() {
        service.cerrarSesion();
    }
}
