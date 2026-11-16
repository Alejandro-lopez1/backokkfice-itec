package com.itec.repository;

import com.itec.model.Alumno;

import java.util.List;

/**
 * Contrato del repositorio de alumnos (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (AlumnoRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 * Clave: DNI del alumno.
 */
public interface AlumnoRepository {

    /** Guarda (o actualiza) un alumno usando su DNI como clave. */
    void guardar(Alumno alumno);

    /** Devuelve el alumno con ese DNI, o null si no existe. */
    Alumno buscarPorDni(String dni);

    /** Indica si ya existe un alumno registrado con ese DNI. */
    boolean existeDni(String dni);

    /** Devuelve todos los alumnos como una lista nueva (copia de la coleccion). */
    List<Alumno> obtenerTodos();
}
