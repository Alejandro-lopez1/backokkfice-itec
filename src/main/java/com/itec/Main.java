package com.itec;

import com.itec.config.DataSeeder;
import com.itec.controller.AlumnoController;
import com.itec.controller.ComisionController;
import com.itec.controller.NotaController;
import com.itec.controller.ProfesorController;
import com.itec.service.AlumnoService;
import com.itec.service.ComisionService;
import com.itec.service.NotaService;
import com.itec.service.ProfesorService;
import com.itec.view.AlumnoView;
import com.itec.view.InscripcionView;
import com.itec.view.MenuPrincipalView;
import com.itec.view.NotaView;
import com.itec.view.ProfesorView;

/**
 * Punto de entrada de la aplicacion Backoffice Academico.
 * <p>
 * Orden de arranque:
 * 1. Los repositorios en memoria se inicializan solos al cargar la clase.
 * 2. Se instancian los services (logica de negocio pura).
 * 3. Se instancian los controladores delgados (reciben su service).
 * 4. Se instancian las vistas y se arranca el menu principal.
 */
public class Main {

    public static void main(String[] args) {
        // 0. Datos iniciales de demostracion (alumnos, profesores, materias, comisiones)
        DataSeeder.cargarDatosIniciales();

        // 1. Services (logica de negocio pura)
        AlumnoService alumnoService = new AlumnoService();
        ProfesorService profesorService = new ProfesorService();
        ComisionService comisionService = new ComisionService();
        NotaService notaService = new NotaService();

        // 2. Controladores delgados (capa de orquestacion, reciben su service)
        AlumnoController alumnoController = new AlumnoController(alumnoService);
        ProfesorController profesorController = new ProfesorController(profesorService);
        ComisionController comisionController = new ComisionController(comisionService);
        NotaController notaController = new NotaController(notaService);

        // 3. Vistas (capa de presentacion, reciben sus controladores)
        AlumnoView alumnoView = new AlumnoView(alumnoController);
        ProfesorView profesorView = new ProfesorView(profesorController);
        InscripcionView inscripcionView = new InscripcionView(comisionController);
        NotaView notaView = new NotaView(notaController, comisionController);

        // 4. Menu principal
        MenuPrincipalView menuPrincipalView =
                new MenuPrincipalView(alumnoView, profesorView, inscripcionView, notaView);
        menuPrincipalView.mostrar();
    }
}
