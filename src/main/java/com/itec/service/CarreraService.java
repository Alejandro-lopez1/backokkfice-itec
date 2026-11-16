package com.itec.service;

import com.itec.model.Carrera;
import com.itec.model.PlanEstudio;

import java.util.List;

/**
 * Contrato del servicio de carreras y planes de estudio (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en CarreraServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface CarreraService {

    /** Mensajes de resultado para la Vista. */
    String MSG_CARRERA_CREADA = "Carrera creada con exito";
    String MSG_NOMBRE_OBLIGATORIO = "ERROR: el nombre es obligatorio";
    String MSG_CARRERA_DUPLICADA = "ERROR: ya existe una carrera con ese nombre";
    String MSG_CARRERA_INEXISTENTE = "ERROR: no existe una carrera con ese nombre";
    String MSG_PLAN_CREADO = "Plan de estudio creado con exito";
    String MSG_PLAN_DUPLICADO = "ERROR: ya existe un plan con ese nombre en la carrera";
    String MSG_PLAN_INEXISTENTE = "ERROR: no existe un plan con ese nombre en la carrera";
    String MSG_ANIOS_INVALIDOS = "ERROR: el anio de fin no puede ser anterior al de inicio";
    String MSG_MATERIA_INEXISTENTE = "ERROR: no existe una materia con ese nombre";
    String MSG_MATERIA_ASOCIADA = "Materia asociada al plan con exito";
    String MSG_MATERIA_YA_ASOCIADA = "ERROR: esa materia ya esta asociada al plan";

    /** Crea una carrera nueva, sin planes de estudio. */
    String crearCarrera(String nombre);

    /** Crea un plan de estudio y lo agrega a la carrera indicada. */
    String crearPlanEstudio(String nombreCarrera, String nombrePlan, int anioInicio, int anioFin);

    /** Asocia una materia existente a un plan de estudio de una carrera. */
    String asociarMateriaAPlan(String nombreCarrera, String nombrePlan, String nombreMateria);

    /** Devuelve todas las carreras (para listados de la Vista). */
    List<Carrera> listarCarreras();

    /** Devuelve los planes de la carrera indicada (lista vacia si no existe). */
    List<PlanEstudio> listarPlanesDeCarrera(String nombreCarrera);

    /** Busca una carrera por nombre. Devuelve la Carrera, o null si no existe. */
    Carrera buscarCarrera(String nombre);
}
