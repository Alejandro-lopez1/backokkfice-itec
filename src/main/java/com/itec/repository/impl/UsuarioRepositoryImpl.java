package com.itec.repository.impl;

import com.itec.model.Rol;
import com.itec.model.Usuario;
import com.itec.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementacion en memoria del repositorio de usuarios (US-AUTH-01).
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al
 * cargar la clase, por lo que los datos persisten durante toda la
 * ejecucion aunque se creen varias instancias del repositorio (comparten
 * la misma "tabla"). Clave: username del usuario.
 * <p>
 * Las 3 cuentas de demostracion se siembran de forma idempotente en el
 * bloque estatico (solo si no existen, por si una futura funcion de
 * alta/edicion de usuarios modifico el mapa antes de instanciar).
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private static final Map<String, Usuario> USUARIOS = new HashMap<>();

    static {
        sembrarSiNoExiste("admin", "admin123", Rol.ADMIN);
        sembrarSiNoExiste("docente", "docente123", Rol.DOCENTE);
        sembrarSiNoExiste("alumno", "alumno123", Rol.ALUMNO);
    }

    public UsuarioRepositoryImpl() {
    }

    /** Siembra idempotente: solo inserta si el username esta libre. */
    private static void sembrarSiNoExiste(String username, String password, Rol rol) {
        if (!USUARIOS.containsKey(username)) {
            USUARIOS.put(username, new Usuario(username, password, rol));
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return USUARIOS.get(username);
    }

    @Override
    public boolean existeUsername(String username) {
        return USUARIOS.containsKey(username);
    }

    @Override
    public void guardar(Usuario usuario) {
        USUARIOS.put(usuario.getUsername(), usuario);
    }
}
