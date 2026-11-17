package com.itec.service.impl;

import com.itec.model.Usuario;
import com.itec.repository.UsuarioRepository;
import com.itec.service.AuthService;

/**
 * Implementacion del servicio de autenticacion (US-AUTH-01).
 * <p>
 * Regla simple de acceso: el username debe existir y la contrasena
 * debe coincidir. La sesion es unica y por consola: el usuario logueado
 * se guarda en el campo {@code sesionActiva}.
 */
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    /** Usuario de la sesion activa; null si no hay sesion iniciada. */
    private Usuario sesionActiva;

    public AuthServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String username, String password) {
        Usuario usuario = usuarioRepository.buscarPorUsername(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return null;
        }
        sesionActiva = usuario;
        return usuario;
    }

    @Override
    public void cerrarSesion() {
        sesionActiva = null;
    }
}
