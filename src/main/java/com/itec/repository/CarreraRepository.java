package com.itec.repository;

import com.itec.model.Carrera;

import java.util.List;

/**
 * Contrato del repositorio de carreras (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (CarreraRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 * Clave: nombre de la carrera.
 */
public interface CarreraRepository {

    /** Guarda (o actualiza) una carrera usando su nombre como clave. */
    void guardar(Carrera carrera);

    /** Devuelve la carrera con ese nombre, o null si no existe. */
    Carrera buscarPorNombre(String nombre);

    /** Indica si ya existe una carrera registrada con ese nombre. */
    boolean existeNombre(String nombre);

    /** Devuelve todas las carreras como una lista nueva (copia de la coleccion). */
    List<Carrera> obtenerTodos();
}
