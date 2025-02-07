package com.parque.diversiones.backend.exceptions;

public class GeneralException extends RuntimeException {
    public enum Tipo {
        NOT_FOUND,
        INHABILITADO,
        ERROR,
        VALOR_NO_VALIDO,
        UNAUTHORIZED
    }

    private final Tipo tipo;

    public GeneralException(Tipo tipo, String message) {
        super(message);
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }
}