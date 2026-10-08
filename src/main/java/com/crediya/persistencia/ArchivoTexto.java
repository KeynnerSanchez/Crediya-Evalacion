package com.crediya.persistencia;

import com.crediya.excepciones.ArchivoException;
import com.crediya.modelo.Exportable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ArchivoTexto {

    private static final String CARPETA = "datos";

    private ArchivoTexto() { }

    public static void guardar(String nombreArchivo, List<? extends Exportable> objetos) throws ArchivoException {
        try {
            Files.createDirectories(Paths.get(CARPETA));
            List<String> lineas = objetos.stream().map(Exportable::toLinea).collect(Collectors.toList());
            Files.write(Paths.get(CARPETA, nombreArchivo), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ArchivoException("No se pudo escribir " + nombreArchivo + ": " + e.getMessage());
        }
    }

    public static List<String> leer(String nombreArchivo) throws ArchivoException {
        Path ruta = Paths.get(CARPETA, nombreArchivo);
        if (!Files.exists(ruta)) {
            throw new ArchivoException("El archivo " + nombreArchivo + " todavia no existe.");
        }
        try {
            return Files.readAllLines(ruta, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ArchivoException("No se pudo leer " + nombreArchivo + ": " + e.getMessage());
        }
    }
}
