package com.crediya.util;

import com.crediya.excepciones.ValidacionException;

public class Validador {

    private Validador() { }

    public static void textoObligatorio(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo '" + campo + "' es obligatorio.");
        }
        if (valor.contains(";")) {
            throw new ValidacionException("El campo '" + campo + "' no puede contener ';'.");
        }
    }

    public static void documento(String valor) throws ValidacionException {
        if (valor == null || !valor.matches("\\d{5,15}")) {
            throw new ValidacionException("El documento debe tener solo numeros (entre 5 y 15 digitos).");
        }
    }

    public static void correo(String valor) throws ValidacionException {
        if (valor == null || !valor.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new ValidacionException("El correo no tiene un formato valido (ej: nombre@correo.com).");
        }
    }

    public static void telefono(String valor) throws ValidacionException {
        if (valor == null || !valor.matches("\\d{7,15}")) {
            throw new ValidacionException("El telefono debe tener solo numeros (entre 7 y 15 digitos).");
        }
    }

    public static void positivo(double valor, String campo) throws ValidacionException {
        if (valor <= 0) {
            throw new ValidacionException("El campo '" + campo + "' debe ser mayor a 0.");
        }
    }
}
