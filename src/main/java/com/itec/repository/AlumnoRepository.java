package com.itec.repository;

import com.itec.model.Alumno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio en memoria de alumnos.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion.
 * Clave: DNI del alumno.
 */
public class AlumnoRepository {

    private static final Map<String, Alumno> ALUMNOS = new HashMap<>();

    private AlumnoRepository() {
    }

    /** Guarda (o actualiza) un alumno usando su DNI como clave. */
    public static void guardar(Alumno alumno) {
        ALUMNOS.put(alumno.getDni(), alumno);
    }

    /** Devuelve el alumno con ese DNI, o null si no existe. */
    public static Alumno buscarPorDni(String dni) {
        return ALUMNOS.get(dni);
    }

    /** Indica si ya existe un alumno registrado con ese DNI. */
    public static boolean existeDni(String dni) {
        return ALUMNOS.containsKey(dni);
    }

    /** Devuelve todos los alumnos como una lista nueva (copia de la coleccion). */
    public static List<Alumno> obtenerTodos() {
        return new ArrayList<>(ALUMNOS.values());
    }
}
