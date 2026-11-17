package com.itec.repository.impl;

import com.itec.repository.ComisionRepository;

import com.itec.model.ComisionMateria;
import com.itec.model.Cuatrimestre;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion en memoria del repositorio de comisiones.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion aunque
 * se creen varias instancias del repositorio (comparten la misma "tabla").
 * <p>
 * Cada comision recibe un id autoincremental al ser agregada; ese id
 * funciona como su clave de busqueda (el contador simula un AUTO_INCREMENT).
 */
public class ComisionRepositoryImpl implements ComisionRepository {

    private static final List<ComisionMateria> COMISIONES = new ArrayList<>();
    private static int siguienteId = 1;

    public ComisionRepositoryImpl() {
    }

    @Override
    public void agregar(ComisionMateria comision) {
        comision.setId(siguienteId++);
        COMISIONES.add(comision);
    }

    @Override
    public List<ComisionMateria> obtenerTodos() {
        return new ArrayList<>(COMISIONES);
    }

    @Override
    public ComisionMateria buscarPorId(int id) {
        for (ComisionMateria c : COMISIONES) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean existe(ComisionMateria comision) {
        if (comision == null) {
            return false;
        }
        for (ComisionMateria c : COMISIONES) {
            if (c.getMateria() == comision.getMateria()
                    && mismoCuatrimestre(c, comision)
                    && c.getProfesor() == comision.getProfesor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compara cuatrimestres por valor (anio y numero), no por referencia:
     * cada comision nueva trae su propio objeto Cuatrimestre.
     */
    private static boolean mismoCuatrimestre(ComisionMateria a, ComisionMateria b) {
        Cuatrimestre ca = a.getCuatrimestre();
        Cuatrimestre cb = b.getCuatrimestre();
        if (ca == null || cb == null) {
            return ca == cb;
        }
        return ca.getAnio() == cb.getAnio() && ca.getNumero() == cb.getNumero();
    }
}
