package com.itec.service.impl;

import com.itec.service.CarreraService;

import com.itec.model.Carrera;
import com.itec.model.Materia;
import com.itec.model.PlanEstudio;
import com.itec.repository.CarreraRepository;
import com.itec.repository.MateriaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del servicio de carreras y planes de estudio.
 * <p>
 * Recibe los repositorios por constructor (inyeccion de dependencias): el
 * de carreras para persistir la jerarquia Carrera -> PlanEstudio -> Materia
 * y el de materias para validar las materias que se asocian a un plan.
 * No imprime en consola ni usa Scanner: devuelve resultados y mensajes para
 * que la Vista los presente.
 */
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final MateriaRepository materiaRepository;

    public CarreraServiceImpl(CarreraRepository carreraRepository,
                              MateriaRepository materiaRepository) {
        this.carreraRepository = carreraRepository;
        this.materiaRepository = materiaRepository;
    }

    @Override
    public String crearCarrera(String nombre) {
        String nombreLimpio = normalizar(nombre);
        if (nombreLimpio == null) {
            return MSG_NOMBRE_OBLIGATORIO;
        }
        if (carreraRepository.existeNombre(nombreLimpio)) {
            return MSG_CARRERA_DUPLICADA;
        }

        Carrera carrera = new Carrera(nombreLimpio, null);
        carreraRepository.guardar(carrera);
        return MSG_CARRERA_CREADA;
    }

    @Override
    public String crearPlanEstudio(String nombreCarrera, String nombrePlan, int anioInicio, int anioFin) {
        Carrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            return MSG_CARRERA_INEXISTENTE;
        }

        String planLimpio = normalizar(nombrePlan);
        if (planLimpio == null) {
            return MSG_NOMBRE_OBLIGATORIO;
        }
        if (anioFin < anioInicio) {
            return MSG_ANIOS_INVALIDOS;
        }
        if (buscarPlan(carrera, planLimpio) != null) {
            return MSG_PLAN_DUPLICADO;
        }

        PlanEstudio plan = new PlanEstudio(planLimpio,
                LocalDate.of(anioInicio, 1, 1),
                LocalDate.of(anioFin, 12, 31),
                null);
        carrera.getPlanes().add(plan);
        return MSG_PLAN_CREADO;
    }

    @Override
    public String asociarMateriaAPlan(String nombreCarrera, String nombrePlan, String nombreMateria) {
        Carrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            return MSG_CARRERA_INEXISTENTE;
        }

        String planLimpio = normalizar(nombrePlan);
        PlanEstudio plan = planLimpio == null ? null : buscarPlan(carrera, planLimpio);
        if (plan == null) {
            return MSG_PLAN_INEXISTENTE;
        }

        String materiaLimpia = normalizar(nombreMateria);
        Materia materia = materiaLimpia == null
                ? null
                : materiaRepository.buscarPorNombre(materiaLimpia);
        if (materia == null) {
            return MSG_MATERIA_INEXISTENTE;
        }

        if (plan.getMaterias().contains(materia)) {
            return MSG_MATERIA_YA_ASOCIADA;
        }

        plan.getMaterias().add(materia);
        return MSG_MATERIA_ASOCIADA;
    }

    @Override
    public List<Carrera> listarCarreras() {
        return carreraRepository.obtenerTodos();
    }

    @Override
    public List<PlanEstudio> listarPlanesDeCarrera(String nombreCarrera) {
        Carrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            return new ArrayList<>();
        }
        return carrera.getPlanes();
    }

    @Override
    public Carrera buscarCarrera(String nombre) {
        if (normalizar(nombre) == null) {
            return null;
        }
        return carreraRepository.buscarPorNombre(normalizar(nombre));
    }

    /** Busca un plan por nombre dentro de la carrera (ignora mayusculas). */
    private PlanEstudio buscarPlan(Carrera carrera, String nombrePlan) {
        for (PlanEstudio p : carrera.getPlanes()) {
            if (p.getNombre().equalsIgnoreCase(nombrePlan)) {
                return p;
            }
        }
        return null;
    }

    /** Quita espacios de los extremos; devuelve null si queda vacio. */
    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
