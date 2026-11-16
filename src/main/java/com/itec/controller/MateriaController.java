package com.itec.controller;

import com.itec.model.Materia;
import com.itec.service.MateriaService;

/**
 * Controlador delgado de materias (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * MateriaService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class MateriaController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_NO_ENCONTRADA = MateriaService.MSG_NO_ENCONTRADA;

    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    /** Delega en el Service la creacion validada de la materia. */
    public String crearMateria(String nombre, int anio, int cuatrimestre) {
        return service.crearMateria(nombre, anio, cuatrimestre);
    }

    /** Delega en el Service la busqueda por nombre. */
    public Materia buscarMateria(String nombre) {
        return service.buscarMateria(nombre);
    }

    /** Delega en el Service el listado de todas las materias. */
    public java.util.List<Materia> listarTodas() {
        return service.listarTodas();
    }

    /** Delega en el Service la modificacion de la materia. */
    public String modificarMateria(String nombreOriginal, String nuevoNombre, int nuevoAnio, int nuevoCuatrimestre) {
        return service.modificarMateria(nombreOriginal, nuevoNombre, nuevoAnio, nuevoCuatrimestre);
    }

    /** Delega en el Service la baja logica. */
    public String darDeBajaLogica(String nombre) {
        return service.darDeBajaLogica(nombre);
    }

    /** Delega en el Service el agregado de correlativas. */
    public String agregarCorrelativa(String nombreMateria, String nombreCorrelativa) {
        return service.agregarCorrelativa(nombreMateria, nombreCorrelativa);
    }
}
