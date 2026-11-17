package com.itec.view;

import com.itec.controller.AuthController;
import com.itec.model.Usuario;

/**
 * Vista de login por consola (US-AUTH-01).
 * <p>
 * Solicita usuario y contrasena, con un maximo de 3 intentos. Si las
 * credenciales son validas devuelve el Usuario logueado; si se agotan
 * los intentos devuelve null (el Main debe terminar el programa).
 */
public class AuthView {

    /** Maximo de intentos de login permitidos. */
    public static final int MAX_INTENTOS = 3;

    private final AuthController controller;

    public AuthView(AuthController controller) {
        this.controller = controller;
    }

    /**
     * Muestra el formulario de login y gestiona los reintentos.
     *
     * @return el Usuario logueado, o null si se agotaron los 3 intentos
     */
    public Usuario mostrarLogin() {
        System.out.println("############################");
        System.out.println("#    BACKOFFICE ACADEMICO   #");
        System.out.println("#        Iniciar sesion     #");
        System.out.println("############################");

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            String username = ConsoleInput.leerLinea("Usuario: ");
            String password = ConsoleInput.leerLinea("Contrasena: ");

            Usuario usuario = controller.autenticar(username, password);
            if (usuario != null) {
                System.out.println("Bienvenido, " + usuario.getUsername()
                        + " (" + usuario.getRol() + ")");
                return usuario;
            }
            System.out.println("Credenciales invalidas");
            System.out.println("Intento " + intento + " de " + MAX_INTENTOS);
        }
        return null;
    }
}
