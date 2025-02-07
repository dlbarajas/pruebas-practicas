package com.parque.diversiones.backend.Utils;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UtilsGeneral {

    public static List<DayOfWeek> convertirDiasADayOfWeek(List<String> dias) {
        if (dias == null || dias.isEmpty()) {
            throw new IllegalArgumentException("La lista de días no puede ser nula o vacía.");
        }

        // Convertir cada día de String a DayOfWeek
        List<DayOfWeek> diasHabilitacionConvertidos = new ArrayList<>();

        for (String dia : dias) {
            if (dia == null || dia.isEmpty()) {
                throw new IllegalArgumentException("Un día no puede ser nulo o vacío.");
            }

            // Normalizamos y convertimos el día
            String diaNormalizado = Normalizer.normalize(dia, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();

            DayOfWeek dayOfWeek = convertirDiaADayOfWeek(diaNormalizado);

            if (dayOfWeek != null) {
                diasHabilitacionConvertidos.add(dayOfWeek);
            } else {
                throw new IllegalArgumentException("Día no válido: " + dia);
            }
        }

        return diasHabilitacionConvertidos;
    }


    private static DayOfWeek convertirDiaADayOfWeek(String dia) {
        switch (dia) {
            case "lunes": return DayOfWeek.MONDAY;
            case "martes": return DayOfWeek.TUESDAY;
            case "miercoles": return DayOfWeek.WEDNESDAY;
            case "jueves": return DayOfWeek.THURSDAY;
            case "viernes": return DayOfWeek.FRIDAY;
            case "sabado": return DayOfWeek.SATURDAY;
            case "domingo": return DayOfWeek.SUNDAY;
            default: return null; // Día inválido
        }
    }

    public static boolean esFormatoValido(String fecha) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(fecha, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }



}
