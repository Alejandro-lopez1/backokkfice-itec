package com.itec.repository;

import com.itec.model.Materia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria del repositorio de materias.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * Clave: nombre de la materia.
 */
public class MateriaRepositoryImpl implements MateriaRepository {

    private static final Map<String, Materia> MATERIAS = new HashMap<>();

    public MateriaRepositoryImpl() {
    }

    @Override
    public void guardar(Materia materia) {
        MATERIAS.put(materia.getNombre(), materia);
    }

    @Override
    public Materia buscarPorNombre(String nombre) {
        return MATERIAS.get(nombre);
    }

    @Override
    public boolean existeNombre(String nombre) {
        return MATERIAS.containsKey(nombre);
    }

    @Override
    public List<Materia> obtenerTodos() {
        return new ArrayList<>(MATERIAS.values());
    }
}
