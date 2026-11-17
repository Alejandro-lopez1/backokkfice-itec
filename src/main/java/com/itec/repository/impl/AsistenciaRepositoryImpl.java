package com.itec.repository.impl;

import com.itec.repository.AsistenciaRepository;

import com.itec.model.Asistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion en memoria del repositorio de asistencias.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * Las asistencias no tienen clave unica en el modelo, por lo que se usan
 * un ArrayList (orden de registro) y filtros por contenido.
 */
public class AsistenciaRepositoryImpl implements AsistenciaRepository {

    private static final List<Asistencia> ASISTENCIAS = new ArrayList<>();

    public AsistenciaRepositoryImpl() {
    }

    @Override
    public void agregar(Asistencia asistencia) {
        ASISTENCIAS.add(asistencia);
    }

    @Override
    public List<Asistencia> obtenerTodas() {
        return new ArrayList<>(ASISTENCIAS);
    }
}
