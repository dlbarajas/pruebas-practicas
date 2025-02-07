package com.ejercicio.practica.solid.openClosed.clases;

import com.ejercicio.practica.solid.openClosed.interfases.SuperHabilidad;

import java.util.ArrayList;
import java.util.List;

public class SuperHeroe {
    private List<SuperHabilidad> habilidades = new ArrayList<>();

    public void agregarHabilidad(SuperHabilidad habilidad){
        habilidades.add(habilidad);
    }

    public void realizarSuperHabilidades(){
        for (SuperHabilidad habilidad : habilidades){
            habilidad.realizarHabilidad();
        }
    }

}
