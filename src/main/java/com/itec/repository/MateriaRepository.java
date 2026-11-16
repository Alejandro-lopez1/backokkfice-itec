package com.itec.repository;

import com.itec.model.Materia;

import java.util.List;

/**
 * Contrato del repositorio de materias (patron Interface-Impl).
 * <p>
 * Las implementaciones concretas (MateriaRepositoryImpl) deciden donde
 * se guardan los datos; el resto del sistema depende solo de esta interfaz.
 * Clave: nombre de la materia.
 */
public interface MateriaRepository {

    /** Guarda (o actualiza) una materia usando su nombre como clave. */
    void guardar(Materia materia);

    /** Devuelve la materia con ese nombre, o null si no existe. */
    Materia buscarPorNombre(String nombre);

    /** Indica si ya existe una materia registrada con ese nombre. */
    boolean existeNombre(String nombre);

    /** Elimina la materia con ese nombre de la coleccion. Devuelve true si existia. */
    boolean eliminar(String nombre);

    /** Devuelve todas las materias como una lista nueva (copia de la coleccion). */
    List<Materia> obtenerTodos();
}
