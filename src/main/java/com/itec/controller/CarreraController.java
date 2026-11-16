package com.itec.controller;

import com.itec.model.Carrera;
import com.itec.model.PlanEstudio;
import com.itec.service.CarreraService;

import java.util.List;

/**
 * Controlador delgado de carreras y planes de estudio (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * CarreraService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class CarreraController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_CARRERA_INEXISTENTE = CarreraService.MSG_CARRERA_INEXISTENTE;

    private final CarreraService service;

    public CarreraController(CarreraService service) {
        this.service = service;
    }

    /** Delega en el Service la creacion validada de la carrera. */
    public String crearCarrera(String nombre) {
        return service.crearCarrera(nombre);
    }

    /** Delega en el Service la creacion validada del plan de estudio. */
    public String crearPlanEstudio(String nombreCarrera, String nombrePlan, int anioInicio, int anioFin) {
        return service.crearPlanEstudio(nombreCarrera, nombrePlan, anioInicio, anioFin);
    }

    /** Delega en el Service la asociacion de la materia al plan. */
    public String asociarMateriaAPlan(String nombreCarrera, String nombrePlan, String nombreMateria) {
        return service.asociarMateriaAPlan(nombreCarrera, nombrePlan, nombreMateria);
    }

    /** Delega en el Service el listado de todas las carreras. */
    public List<Carrera> listarCarreras() {
        return service.listarCarreras();
    }

    /** Delega en el Service el listado de planes de una carrera. */
    public List<PlanEstudio> listarPlanesDeCarrera(String nombreCarrera) {
        return service.listarPlanesDeCarrera(nombreCarrera);
    }

    /** Delega en el Service la busqueda por nombre. */
    public Carrera buscarCarrera(String nombre) {
        return service.buscarCarrera(nombre);
    }
}
