package com.ejercicio.practica.solid.openClosed.clases;

import com.ejercicio.practica.solid.openClosed.interfases.SuperHabilidad;

public class SuperFuerza implements SuperHabilidad {
    @Override
    public void realizarHabilidad() {
        System.out.println("Estoy levantando un edificio entero.");
    }
}
