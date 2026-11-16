package com.itec.config;

import com.itec.model.Alumno;
import com.itec.model.Carrera;
import com.itec.model.ComisionMateria;
import com.itec.model.Cuatrimestre;
import com.itec.model.Materia;
import com.itec.model.PlanEstudio;
import com.itec.model.Profesor;
import com.itec.repository.AlumnoRepository;
import com.itec.repository.CarreraRepository;
import com.itec.repository.ComisionRepository;
import com.itec.repository.MateriaRepository;
import com.itec.repository.ProfesorRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Cargador de datos iniciales para pruebas y demostracion.
 * <p>
 * Recibe los repositorios por parametro (inyeccion de dependencias) e inserta
 * un set de datos realistas al arrancar la aplicacion, para que los menus
 * tengan contenido para operar de inmediato.
 * <p>
 * Escribe directamente sobre los repositorios (sin pasar por los services):
 * es el unico caso justificado, ya que los datos sembrados son validos por
 * construccion y el seeder se ejecuta una sola vez por arranque.
 */
public final class DataSeeder {

    /** Bandera para no duplicar la carga si se invoca mas de una vez. */
    private static boolean yaCargado = false;

    private DataSeeder() {
    }

    /** Carga alumnos, profesores, materias, comisiones y carreras de demostracion. */
    public static void cargarDatosIniciales(AlumnoRepository alumnoRepository,
                                            ProfesorRepository profesorRepository,
                                            MateriaRepository materiaRepository,
                                            ComisionRepository comisionRepository,
                                            CarreraRepository carreraRepository) {
        if (yaCargado) {
            return;
        }
        cargarAlumnos(alumnoRepository);
        cargarProfesores(profesorRepository);
        cargarMaterias(materiaRepository);
        cargarComisiones(materiaRepository, profesorRepository, comisionRepository);
        cargarCarreras(materiaRepository, carreraRepository);
        yaCargado = true;
    }

    /** 5 alumnos: 4 activos y 1 inactivo para probar la baja logica. */
    private static void cargarAlumnos(AlumnoRepository repository) {
        repository.guardar(new Alumno("Juan", "Perez", "40123456",
                "juan.perez@alumno.edu", "555-1001", true));
        repository.guardar(new Alumno("Maria", "Gomez", "40234567",
                "maria.gomez@alumno.edu", "555-1002", true));
        repository.guardar(new Alumno("Carlos", "Sanchez", "40345678",
                "carlos.sanchez@alumno.edu", "555-1003", true));
        repository.guardar(new Alumno("Lucia", "Fernandez", "40456789",
                "lucia.fernandez@alumno.edu", "555-1004", true));
        repository.guardar(new Alumno("Diego", "Torres", "40567890",
                "diego.torres@alumno.edu", "555-1005", false));
    }

    /** 5 profesores, todos activos. */
    private static void cargarProfesores(ProfesorRepository repository) {
        repository.guardar(new Profesor("Ana", "Rodriguez", "20111222",
                "ana.rodriguez@docente.edu", "555-2001", true));
        repository.guardar(new Profesor("Luis", "Martinez", "20222333",
                "luis.martinez@docente.edu", "555-2002", true));
        repository.guardar(new Profesor("Sofia", "Lopez", "20333444",
                "sofia.lopez@docente.edu", "555-2003", true));
        repository.guardar(new Profesor("Pablo", "Garcia", "20444555",
                "pablo.garcia@docente.edu", "555-2004", true));
        repository.guardar(new Profesor("Elena", "Diaz", "20555666",
                "elena.diaz@docente.edu", "555-2005", true));
    }

    /**
     * 5 materias, algunas con correlativas.
     * El orden importa: primero se crean las materias base y luego las que
     * las usan como correlativa (se enlazan las mismas instancias).
     */
    private static void cargarMaterias(MateriaRepository repository) {
        Materia programacionI = new Materia("Programacion I", 1, 1, true, null);
        Materia matematicaDiscreta = new Materia("Matematica Discreta", 1, 1, true, null);
        repository.guardar(programacionI);
        repository.guardar(matematicaDiscreta);

        Materia programacionII = new Materia("Programacion II", 1, 2, true,
                List.of(programacionI));
        Materia baseDeDatos = new Materia("Base de Datos", 2, 1, true,
                List.of(programacionI, matematicaDiscreta));
        repository.guardar(programacionII);
        repository.guardar(baseDeDatos);

        Materia redes = new Materia("Redes de Computadoras", 2, 2, true,
                List.of(baseDeDatos));
        repository.guardar(redes);
    }

    /** 2 comisiones del cuatrimestre 2026, sobre materias y profesores ya cargados. */
    private static void cargarComisiones(MateriaRepository materiaRepository,
                                         ProfesorRepository profesorRepository,
                                         ComisionRepository comisionRepository) {
        Cuatrimestre c1 = new Cuatrimestre(2026, 1,
                LocalDate.of(2026, 3, 2), LocalDate.of(2026, 7, 10));
        Cuatrimestre c2 = new Cuatrimestre(2026, 2,
                LocalDate.of(2026, 8, 3), LocalDate.of(2026, 12, 11));

        comisionRepository.agregar(new ComisionMateria(
                materiaRepository.buscarPorNombre("Programacion I"),
                c1,
                profesorRepository.buscarPorDni("20111222")));

        comisionRepository.agregar(new ComisionMateria(
                materiaRepository.buscarPorNombre("Base de Datos"),
                c2,
                profesorRepository.buscarPorDni("20222333")));
    }

    /**
     * 2 carreras con 1 plan de estudio cada una, asociando materias ya cargadas.
     * Las materias del plan son las mismas instancias del repositorio de materias.
     */
    private static void cargarCarreras(MateriaRepository materiaRepository,
                                       CarreraRepository carreraRepository) {
        // Tecnicatura en Programacion: Plan 2024 con 3 materias.
        Carrera tecnicatura = new Carrera("Tecnicatura en Programacion", null);
        PlanEstudio planTecnicatura = new PlanEstudio("Plan 2024",
                LocalDate.of(2024, 1, 1), LocalDate.of(2026, 12, 31), null);
        planTecnicatura.getMaterias().add(materiaRepository.buscarPorNombre("Programacion I"));
        planTecnicatura.getMaterias().add(materiaRepository.buscarPorNombre("Programacion II"));
        planTecnicatura.getMaterias().add(materiaRepository.buscarPorNombre("Base de Datos"));
        tecnicatura.getPlanes().add(planTecnicatura);
        carreraRepository.guardar(tecnicatura);

        // Licenciatura en Sistemas: Plan 2024 con 3 materias.
        Carrera licenciatura = new Carrera("Licenciatura en Sistemas", null);
        PlanEstudio planLicenciatura = new PlanEstudio("Plan 2024",
                LocalDate.of(2024, 1, 1), LocalDate.of(2028, 12, 31), null);
        planLicenciatura.getMaterias().add(materiaRepository.buscarPorNombre("Programacion I"));
        planLicenciatura.getMaterias().add(materiaRepository.buscarPorNombre("Matematica Discreta"));
        planLicenciatura.getMaterias().add(materiaRepository.buscarPorNombre("Redes de Computadoras"));
        licenciatura.getPlanes().add(planLicenciatura);
        carreraRepository.guardar(licenciatura);
    }
}
