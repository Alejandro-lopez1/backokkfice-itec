package com.itec.repository.impl;

import com.itec.repository.ProfesorRepository;

import com.itec.model.Profesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria del repositorio de profesores.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * Clave: DNI del profesor.
 */
public class ProfesorRepositoryImpl implements ProfesorRepository {

    private static final Map<String, Profesor> PROFESORES = new HashMap<>();

    public ProfesorRepositoryImpl() {
    }

    @Override
    public void guardar(Profesor profesor) {
        PROFESORES.put(profesor.getDni(), profesor);
    }

    @Override
    public Profesor buscarPorDni(String dni) {
        return PROFESORES.get(dni);
    }

    @Override
    public boolean existeDni(String dni) {
        return PROFESORES.containsKey(dni);
    }

    @Override
    public List<Profesor> obtenerTodos() {
        return new ArrayList<>(PROFESORES.values());
    }
}
