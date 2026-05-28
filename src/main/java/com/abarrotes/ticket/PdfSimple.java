package com.abarrotes.ticket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Escritor PDF muy pequeño para tickets.
 * Evita agregar más librerías y solo dibuja texto simple en una página.
 */
class PdfSimple {
    private static final int ANCHO = 260;
    private static final int MARGEN = 18;

    private PdfSimple() {
    }

    static void guardar(File archivo, List<String> renglones) throws IOException {
        int alto = Math.max(420, 70 + (renglones.size() * 15));
        String contenido = crearContenido(renglones, alto);

        ArrayList<String> objetos = new ArrayList<>();
        objetos.add("<< /Type /Catalog /Pages 2 0 R >>");
        objetos.add("<< /Type /Pages /Kids [3 0 R] /Count 1 >>");
        objetos.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + ANCHO + " " + alto
                + "] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>");
        objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /Courier /Encoding /WinAnsiEncoding >>");
        objetos.add("<< /Length " + contenido.getBytes(StandardCharsets.ISO_8859_1).length
                + " >>\nstream\n" + contenido + "endstream");

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        escribir(salida, "%PDF-1.4\n");
        ArrayList<Integer> posiciones = new ArrayList<>();

        for (int i = 0; i < objetos.size(); i++) {
            posiciones.add(salida.size());
            escribir(salida, (i + 1) + " 0 obj\n");
            escribir(salida, objetos.get(i));
            escribir(salida, "\nendobj\n");
        }

        int inicioXref = salida.size();
        escribir(salida, "xref\n");
        escribir(salida, "0 " + (objetos.size() + 1) + "\n");
        escribir(salida, "0000000000 65535 f \n");
        for (Integer posicion : posiciones) {
            escribir(salida, String.format("%010d 00000 n \n", posicion));
        }
        escribir(salida, "trailer\n");
        escribir(salida, "<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\n");
        escribir(salida, "startxref\n");
        escribir(salida, String.valueOf(inicioXref));
        escribir(salida, "\n%%EOF");

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            salida.writeTo(fos);
        }
    }

    private static String crearContenido(List<String> renglones, int alto) {
        StringBuilder contenido = new StringBuilder();
        int y = alto - 30;

        for (String renglon : renglones) {
            if ("---".equals(renglon)) {
                contenido.append("0.7 w ")
                        .append(MARGEN).append(" ").append(y)
                        .append(" m ")
                        .append(ANCHO - MARGEN).append(" ").append(y)
                        .append(" l S\n");
                y -= 13;
            } else {
                int fuente = renglon.startsWith("#") ? 14 : 9;
                String texto = renglon.startsWith("#") ? renglon.substring(1) : renglon;
                contenido.append("BT /F1 ")
                        .append(fuente)
                        .append(" Tf 1 0 0 1 ")
                        .append(MARGEN)
                        .append(" ")
                        .append(y)
                        .append(" Tm (")
                        .append(escapar(texto))
                        .append(") Tj ET\n");
                y -= fuente + 5;
            }
        }

        return contenido.toString();
    }

    private static void escribir(ByteArrayOutputStream salida, String texto) throws IOException {
        salida.write(texto.getBytes(StandardCharsets.ISO_8859_1));
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

}
