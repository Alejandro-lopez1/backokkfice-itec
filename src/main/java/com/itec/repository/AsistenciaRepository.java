package com.itec.repository;

import com.itec.model.Asistencia;

import java.util.List;

/**
 * Contrato del repositorio de asistencias (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (AsistenciaRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 */
public interface AsistenciaRepository {

    /** Agrega una asistencia al registro. */
    void agregar(Asistencia asistencia);

    /** Devuelve todas las asistencias como una lista nueva (copia de la coleccion). */
    List<Asistencia> obtenerTodas();
}
