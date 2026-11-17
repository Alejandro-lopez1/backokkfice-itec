package com.itec.repository;

import com.itec.model.Usuario;

/**
 * Repositorio de usuarios del sistema (US-AUTH-01).
 * <p>
 * Contrato de persistencia en memoria: la clave de la "tabla" es el
 * username. Se sembran 3 cuentas de demostracion (admin, docente, alumno).
 */
public interface UsuarioRepository {

    /** Busca un usuario por su username; null si no existe. */
    Usuario buscarPorUsername(String username);

    /** Indica si ya existe un usuario con ese username. */
    boolean existeUsername(String username);

    /** Guarda (inserta o reemplaza) un usuario. */
    void guardar(Usuario usuario);
}
