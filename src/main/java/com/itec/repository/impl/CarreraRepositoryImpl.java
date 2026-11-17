package com.itec.repository.impl;

import com.itec.repository.CarreraRepository;

import com.itec.model.Carrera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria del repositorio de carreras.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * Clave: nombre de la carrera.
 */
public class CarreraRepositoryImpl implements CarreraRepository {

    private static final Map<String, Carrera> CARRERAS = new HashMap<>();

    public CarreraRepositoryImpl() {
    }

    @Override
    public void guardar(Carrera carrera) {
        CARRERAS.put(carrera.getNombre(), carrera);
    }

    @Override
    public Carrera buscarPorNombre(String nombre) {
        return CARRERAS.get(nombre);
    }

    @Override
    public boolean existeNombre(String nombre) {
        return CARRERAS.containsKey(nombre);
    }

    @Override
    public List<Carrera> obtenerTodos() {
        return new ArrayList<>(CARRERAS.values());
    }
}
