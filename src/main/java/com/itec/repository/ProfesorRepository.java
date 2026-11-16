package com.itec.repository;

import com.itec.model.Profesor;

import java.util.List;

/**
 * Contrato del repositorio de profesores (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (ProfesorRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 * Clave: DNI del profesor.
 */
public interface ProfesorRepository {

    /** Guarda (o actualiza) un profesor usando su DNI como clave. */
    void guardar(Profesor profesor);

    /** Devuelve el profesor con ese DNI, o null si no existe. */
    Profesor buscarPorDni(String dni);

    /** Indica si ya existe un profesor registrado con ese DNI. */
    boolean existeDni(String dni);

    /** Devuelve todos los profesores como una lista nueva (copia de la coleccion). */
    List<Profesor> obtenerTodos();
}
