package com.itec.repository;

import com.itec.model.Profesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio en memoria de profesores.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion.
 * Clave: DNI del profesor.
 */
public class ProfesorRepository {

    private static final Map<String, Profesor> PROFESORES = new HashMap<>();

    private ProfesorRepository() {
    }

    /** Guarda (o actualiza) un profesor usando su DNI como clave. */
    public static void guardar(Profesor profesor) {
        PROFESORES.put(profesor.getDni(), profesor);
    }

    /** Devuelve el profesor con ese DNI, o null si no existe. */
    public static Profesor buscarPorDni(String dni) {
        return PROFESORES.get(dni);
    }

    /** Indica si ya existe un profesor registrado con ese DNI. */
    public static boolean existeDni(String dni) {
        return PROFESORES.containsKey(dni);
    }

    /** Devuelve todos los profesores como una lista nueva (copia de la coleccion). */
    public static List<Profesor> obtenerTodos() {
        return new ArrayList<>(PROFESORES.values());
    }
}
