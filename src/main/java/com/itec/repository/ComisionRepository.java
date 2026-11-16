package com.itec.repository;

import com.itec.model.ComisionMateria;

import java.util.List;

/**
 * Contrato del repositorio de comisiones (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (ComisionRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 */
public interface ComisionRepository {

    /** Agrega una comision y le asigna su id autoincremental. */
    void agregar(ComisionMateria comision);

    /** Devuelve todas las comisiones como una lista nueva (copia de la coleccion). */
    List<ComisionMateria> obtenerTodos();

    /** Devuelve la comision con ese id, o null si no existe. */
    ComisionMateria buscarPorId(int id);

    /** Indica si existe una comision con la misma materia, cuatrimestre y profesor. */
    boolean existe(ComisionMateria comision);
}
