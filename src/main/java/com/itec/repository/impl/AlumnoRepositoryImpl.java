package com.itec.repository.impl;

import com.itec.repository.AlumnoRepository;

import com.itec.model.Alumno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria del repositorio de alumnos.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * Clave: DNI del alumno.
 */
public class AlumnoRepositoryImpl implements AlumnoRepository {

    private static final Map<String, Alumno> ALUMNOS = new HashMap<>();

    public AlumnoRepositoryImpl() {
    }

    @Override
    public void guardar(Alumno alumno) {
        ALUMNOS.put(alumno.getDni(), alumno);
    }

    @Override
    public Alumno buscarPorDni(String dni) {
        return ALUMNOS.get(dni);
    }

    @Override
    public boolean existeDni(String dni) {
        return ALUMNOS.containsKey(dni);
    }

    @Override
    public List<Alumno> obtenerTodos() {
        return new ArrayList<>(ALUMNOS.values());
    }
}
