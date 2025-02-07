package com.ejercicio.practica.solid.openClosed.clases;

import com.ejercicio.practica.solid.openClosed.interfases.SuperHabilidad;

public class Volar implements SuperHabilidad {

    @Override
    public void realizarHabilidad() {
        System.out.println("Estoy volando alto en el cielo.");
    }
}
