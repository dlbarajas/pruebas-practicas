package com.ejercicio.practica.solid.openClosed;

import com.ejercicio.practica.solid.openClosed.clases.SuperFuerza;
import com.ejercicio.practica.solid.openClosed.clases.SuperHeroe;
import com.ejercicio.practica.solid.openClosed.clases.Velocidad;
import com.ejercicio.practica.solid.openClosed.clases.Volar;

public class Ejecucion {
    public static void main(String[] args) {

        //SpringApplication.run(PracticaApplication.class, args);

        SuperHeroe superman = new SuperHeroe();
        // Agregar habilidades
        superman.agregarHabilidad(new Volar());
        superman.agregarHabilidad(new SuperFuerza());
        // Realizar habilidades
        System.out.println("Superman está usando sus habilidades: 01");
        superman.realizarSuperHabilidades();


        SuperHeroe flash = new SuperHeroe();
        flash.agregarHabilidad(new Velocidad());
        System.out.println("flash está usando sus habilidades:02");
        flash.realizarSuperHabilidades();


    }
}
