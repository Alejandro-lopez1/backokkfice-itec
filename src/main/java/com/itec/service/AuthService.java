package com.itec.service;

import com.itec.model.Usuario;

/**
 * Servicio de autenticacion (US-AUTH-01).
 * <p>
 * Logica de negocio de acceso: validar credenciales y manejar la
 * sesion activa del operador.
 */
public interface AuthService {

    /**
     * Valida las credenciales contra el repositorio de usuarios.
     *
     * @return el Usuario autenticado, o null si el username no existe
     *         o la contrasena no coincide
     */
    Usuario autenticar(String username, String password);

    /** Cierra la sesion activa (deja el usuario logueado en null). */
    void cerrarSesion();
}
