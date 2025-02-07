package com.ejercicio.practica.solid.openClosed.clases;

import com.ejercicio.practica.solid.openClosed.interfases.SuperHabilidad;

public class Velocidad implements SuperHabilidad {
    @Override
    public void realizarHabilidad() {
        System.out.println("Tengo super velocidad");
    }
}
