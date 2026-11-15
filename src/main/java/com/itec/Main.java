package com.itec;

import com.itec.controller.AlumnoController;
import com.itec.controller.ComisionController;
import com.itec.controller.NotaController;
import com.itec.controller.ProfesorController;
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
 * 2. Se instancian los controladores (logica de negocio).
 * 3. Se instancian las vistas y se arranca el menu principal.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Controladores (capa de negocio)
        AlumnoController alumnoController = new AlumnoController();
        ProfesorController profesorController = new ProfesorController();
        ComisionController comisionController = new ComisionController();
        NotaController notaController = new NotaController();

        // 2. Vistas (capa de presentacion, reciben sus controladores)
        AlumnoView alumnoView = new AlumnoView(alumnoController);
        ProfesorView profesorView = new ProfesorView(profesorController);
        InscripcionView inscripcionView = new InscripcionView(comisionController);
        NotaView notaView = new NotaView(notaController, comisionController);

        // 3. Menu principal
        MenuPrincipalView menuPrincipalView =
                new MenuPrincipalView(alumnoView, profesorView, inscripcionView, notaView);
        menuPrincipalView.mostrar();
    }
}
