package com.itec.repository;

import com.itec.model.ComisionMateria;
import com.itec.model.Cuatrimestre;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio en memoria de comisiones de materias.
 * <p>
 * Simula una base de datos: la coleccion es static y se inicializa al cargar
 * la clase, por lo que los datos persisten durante toda la ejecucion.
 * <p>
 * Cada comision recibe un id autoincremental al ser agregada; ese id
 * funciona como su clave de busqueda (el contador simula un AUTO_INCREMENT).
 */
public class ComisionRepository {

    private static final List<ComisionMateria> COMISIONES = new ArrayList<>();
    private static int siguienteId = 1;

    private ComisionRepository() {
    }

    /** Agrega una comision y le asigna su id autoincremental. */
    public static void agregar(ComisionMateria comision) {
        comision.setId(siguienteId++);
        COMISIONES.add(comision);
    }

    /** Devuelve todas las comisiones como una lista nueva (copia de la coleccion). */
    public static List<ComisionMateria> obtenerTodos() {
        return new ArrayList<>(COMISIONES);
    }

    /** Devuelve la comision con ese id, o null si no existe. */
    public static ComisionMateria buscarPorId(int id) {
        for (ComisionMateria c : COMISIONES) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /** Indica si existe una comision con la misma materia, cuatrimestre y profesor. */
    public static boolean existe(ComisionMateria comision) {
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
