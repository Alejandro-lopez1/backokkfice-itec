package com.itec.repository;

import com.itec.model.Materia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio en memoria de materias.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion.
 * Clave: nombre de la materia.
 */
public class MateriaRepository {

    private static final Map<String, Materia> MATERIAS = new HashMap<>();

    private MateriaRepository() {
    }

    /** Guarda (o actualiza) una materia usando su nombre como clave. */
    public static void guardar(Materia materia) {
        MATERIAS.put(materia.getNombre(), materia);
    }

    /** Devuelve la materia con ese nombre, o null si no existe. */
    public static Materia buscarPorNombre(String nombre) {
        return MATERIAS.get(nombre);
    }

    /** Indica si ya existe una materia registrada con ese nombre. */
    public static boolean existeNombre(String nombre) {
        return MATERIAS.containsKey(nombre);
    }

    /** Devuelve todas las materias como una lista nueva (copia de la coleccion). */
    public static List<Materia> obtenerTodos() {
        return new ArrayList<>(MATERIAS.values());
    }
}
