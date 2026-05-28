package com.abarrotes.view.ui;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Carga el icono principal del sistema.
 * Se usa el PNG completo para que Windows lo escale sin recortarlo.
 */
public class IconoApp {
    private static final String RUTA_RECURSO = "/com/abarrotes/assets/mercado.png";
    private static Image icono;

    private IconoApp() {
    }

    public static void aplicar(Window ventana) {
        Image imagen = obtenerImagen();
        if (imagen != null) {
            ventana.setIconImage(imagen);
        }
    }

    public static Image obtenerImagen() {
        if (icono == null) {
            icono = cargarImagen();
        }
        return icono;
    }

    private static Image cargarImagen() {
        try (InputStream entrada = IconoApp.class.getResourceAsStream(RUTA_RECURSO)) {
            if (entrada != null) {
                return ImageIO.read(entrada);
            }
        } catch (IOException e) {
            return null;
        }

        String rutaArchivo = "src/main/resources/com/abarrotes/assets/mercado.png";
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            archivo = new File("Abarrotes/" + rutaArchivo);
        }

        try {
            return archivo.exists() ? ImageIO.read(archivo) : null;
        } catch (IOException e) {
            return null;
        }
    }
}
