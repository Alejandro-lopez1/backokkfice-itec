package com.itec;

import com.itec.config.DataSeeder;
import com.itec.controller.AlumnoController;
import com.itec.controller.AsistenciaController;
import com.itec.controller.CarreraController;
import com.itec.controller.ComisionController;
import com.itec.controller.MateriaController;
import com.itec.controller.NotaController;
import com.itec.controller.ProfesorController;
import com.itec.repository.AlumnoRepository;
import com.itec.repository.impl.AlumnoRepositoryImpl;
import com.itec.repository.AsistenciaRepository;
import com.itec.repository.impl.AsistenciaRepositoryImpl;
import com.itec.repository.CarreraRepository;
import com.itec.repository.impl.CarreraRepositoryImpl;
import com.itec.repository.ComisionRepository;
import com.itec.repository.impl.ComisionRepositoryImpl;
import com.itec.repository.MateriaRepository;
import com.itec.repository.impl.MateriaRepositoryImpl;
import com.itec.repository.ProfesorRepository;
import com.itec.repository.impl.ProfesorRepositoryImpl;
import com.itec.service.AlumnoService;
import com.itec.service.impl.AlumnoServiceImpl;
import com.itec.service.AsistenciaService;
import com.itec.service.impl.AsistenciaServiceImpl;
import com.itec.service.CarreraService;
import com.itec.service.impl.CarreraServiceImpl;
import com.itec.service.ComisionService;
import com.itec.service.impl.ComisionServiceImpl;
import com.itec.service.MateriaService;
import com.itec.service.impl.MateriaServiceImpl;
import com.itec.service.NotaService;
import com.itec.service.impl.NotaServiceImpl;
import com.itec.service.ProfesorService;
import com.itec.service.impl.ProfesorServiceImpl;
import com.itec.view.AlumnoView;
import com.itec.view.AsistenciaView;
import com.itec.view.CarreraView;
import com.itec.view.InscripcionView;
import com.itec.view.MenuPrincipalView;
import com.itec.view.MateriaView;
import com.itec.view.NotaView;
import com.itec.view.ProfesorView;

/**
 * Punto de entrada de la aplicacion Backoffice Academico.
 * <p>
 * Esta clase es la unica que conoce las implementaciones concretas
 * (patron Interface-Impl): aca se hace el "cableado" por inyeccion de
 * dependencias, de abajo hacia arriba:
 * RepositoryImpl -> ServiceImpl -> Controller -> View.
 * <p>
 * El resto del sistema (vistas, controladores, services) depende solo de
 * las interfaces, nunca de las implementaciones.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Repositorios (persistencia en memoria). Las colecciones son
        //    static en los Impl: las instancias comparten la misma "tabla".
        AlumnoRepository alumnoRepository = new AlumnoRepositoryImpl();
        ProfesorRepository profesorRepository = new ProfesorRepositoryImpl();
        MateriaRepository materiaRepository = new MateriaRepositoryImpl();
        ComisionRepository comisionRepository = new ComisionRepositoryImpl();
        AsistenciaRepository asistenciaRepository = new AsistenciaRepositoryImpl();
        CarreraRepository carreraRepository = new CarreraRepositoryImpl();

        // 2. Datos iniciales de demostracion (alumnos, profesores, materias,
        //    comisiones, carreras)
        DataSeeder.cargarDatosIniciales(
                alumnoRepository,
                profesorRepository,
                materiaRepository,
                comisionRepository,
                carreraRepository);

        // 3. Services (logica de negocio pura, reciben sus repositorios)
        AlumnoService alumnoService = new AlumnoServiceImpl(alumnoRepository);
        ProfesorService profesorService = new ProfesorServiceImpl(profesorRepository);
        MateriaService materiaService = new MateriaServiceImpl(materiaRepository);
        ComisionService comisionService = new ComisionServiceImpl(
                alumnoRepository, profesorRepository, materiaRepository, comisionRepository);
        NotaService notaService = new NotaServiceImpl(comisionRepository);
        AsistenciaService asistenciaService = new AsistenciaServiceImpl(
                comisionRepository, asistenciaRepository);
        CarreraService carreraService = new CarreraServiceImpl(carreraRepository, materiaRepository);

        // 4. Controladores delgados (capa de orquestacion, reciben su service)
        AlumnoController alumnoController = new AlumnoController(alumnoService);
        ProfesorController profesorController = new ProfesorController(profesorService);
        MateriaController materiaController = new MateriaController(materiaService);
        ComisionController comisionController = new ComisionController(comisionService);
        NotaController notaController = new NotaController(notaService);
        AsistenciaController asistenciaController = new AsistenciaController(asistenciaService);
        CarreraController carreraController = new CarreraController(carreraService);

        // 5. Vistas (capa de presentacion, reciben sus controladores)
        AlumnoView alumnoView = new AlumnoView(alumnoController);
        ProfesorView profesorView = new ProfesorView(profesorController);
        MateriaView materiaView = new MateriaView(materiaController);
        InscripcionView inscripcionView = new InscripcionView(comisionController);
        NotaView notaView = new NotaView(notaController, comisionController);
        AsistenciaView asistenciaView = new AsistenciaView(asistenciaController, comisionController);
        CarreraView carreraView = new CarreraView(carreraController);

        // 6. Menu principal
        MenuPrincipalView menuPrincipalView = new MenuPrincipalView(
                alumnoView, profesorView, materiaView, inscripcionView, notaView,
                asistenciaView, carreraView);
        menuPrincipalView.mostrar();
    }
}
