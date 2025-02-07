package com.parque.diversiones.backend.constants;

/**
 * Clase que maneja las variables
 * contantes de la aplicacion
 */
public class Constantes {
    private Constantes() {
        throw new IllegalArgumentException("Constantes class");
    }

    public static final String USUARIO_NO_POSEE_PRIVILEGIOS  = "Usuario con permisos insuficientes";
    public static final String REGISTRO_NO_ENCONTRADO        = "Registro no encontrado.";
    public static final String REGISTRO_ENCONTRADO           = "Registro encontrado.";
    public static final String REGISTRO_INHABILITADO         = "Registro inhabilitado.";
    public static final String REGISTRO_ELIMINADO            = "Registro eliminado.";
    public static final String DATOS_OBTENIDOS               = "Datos obtenidos.";
    public static final String DATOS_GUARDADOS               = "Datos guardados. ";
    public static final String DATOS_ACTUALIZADO             = "Datos actualizados.";
    public static final String REGISTRO_ID_NO_ENCONTRADO     = "No se encontro un resgistro con el ID ";
    public static final String ERROR_GUARDAR_REGISTRO        = "Error al guardar el registro: ";
    public static final String EDAD_NO_VALIDA                = "Debido a que el cliente es menor de edad se debe registrar la información de un familiar.";
    public static final String REGISTRO_INACTIVO_O_ELIMINADO = "Registro inactivo o eliminado con ID ";
    public static final String MENSAJE_PROMOCION             = "Gracias por registrarse. Te enviaremos información de nuestras promociones y ofertas del mes";
    public static final String PERMISOS_INSUFICIENTES        = "Empleado con numero de identificación: ";
    public static final String ESTACION_INHABILITADA         = "Lo sentimos, la estación ";
    public static final String ESTACION_INHABILITADA_FECHA_ESTADO = "Lo sentimos, para es esta temporada o fecha la estación se encuentra inhabilitada ";
    public static final String FECHA_CON_FORMATO_INVALIDO        = "El campo fechaAdquisicion no cumple con el formato yyyy-MM-dd.";
}
