package com.abarrotes.ticket;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.abarrotes.model.Venta;

/**
 * Servicio encargado de crear tickets PDF.
 * Todas las ventanas llaman a esta clase para no mezclar reportes con interfaz.
 */
public class TicketPdfService {
    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
    private static final DateTimeFormatter FECHA_TEXTO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA_ARCHIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private TicketPdfService() {
    }

    public static File imprimirVenta(int idVenta, String hora, String cliente, List<LineaTicket> lineas,
            double total, double recibido, double cambio) throws IOException {
        File archivo = new File(carpetaTickets(), "venta_" + String.format("%04d", idVenta)
                + "_" + FECHA_ARCHIVO.format(LocalDateTime.now()) + ".pdf");

        ArrayList<String> renglones = new ArrayList<>();
        renglones.add("#ABARROTES");
        renglones.add("Ticket de venta");
        renglones.add("Fecha: " + FECHA_TEXTO.format(LocalDateTime.now()));
        renglones.add("Venta: " + idVenta + "   Hora: " + hora);
        renglones.add("Cliente: " + cliente);
        renglones.add("---");
        renglones.add("Producto");
        renglones.add("Cant  Precio        Subtotal");
        renglones.add("---");

        for (LineaTicket linea : lineas) {
            renglones.add(cortar(linea.getProducto(), 30));
            renglones.add(String.format("%4d  %-11s %s",
                    linea.getCantidad(),
                    moneda(linea.getPrecio()),
                    moneda(linea.getSubtotal())));
        }

        renglones.add("---");
        renglones.add("Total:    " + moneda(total));
        renglones.add("Recibido: " + moneda(recibido));
        renglones.add("Cambio:   " + moneda(cambio));
        renglones.add("---");
        renglones.add("Gracias por su compra");

        PdfSimple.guardar(archivo, renglones);
        return archivo;
    }

    public static File imprimirCorte(List<Venta> ventas) throws IOException {
        File archivo = new File(carpetaTickets(), "corte_" + FECHA_ARCHIVO.format(LocalDateTime.now()) + ".pdf");

        double total = 0;
        for (Venta venta : ventas) {
            total += venta.getTotal();
        }

        ArrayList<String> renglones = new ArrayList<>();
        renglones.add("#ABARROTES");
        renglones.add("Corte de caja");
        renglones.add("Fecha: " + FECHA_TEXTO.format(LocalDateTime.now()));
        renglones.add("Operaciones: " + ventas.size());
        renglones.add("Efectivo total: " + moneda(total));
        renglones.add("Ticket promedio: " + moneda(ventas.isEmpty() ? 0 : total / ventas.size()));
        renglones.add("---");
        renglones.add("Venta  Hora   Cliente");
        renglones.add("Total");
        renglones.add("---");

        for (Venta venta : ventas) {
            renglones.add("Venta " + venta.getId() + "  " + venta.getHora()
                    + "  " + cortar(venta.getCliente(), 18));
            renglones.add(moneda(venta.getTotal()));
        }

        if (ventas.isEmpty()) {
            renglones.add("Sin ventas registradas.");
        }

        renglones.add("---");
        renglones.add("Fin del corte");

        PdfSimple.guardar(archivo, renglones);
        return archivo;
    }

    private static File carpetaTickets() throws IOException {
        File carpeta = new File("tickets");
        if (!carpeta.exists() && new File("Abarrotes/tickets").exists()) {
            carpeta = new File("Abarrotes/tickets");
        }
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta de tickets.");
        }
        return carpeta;
    }

    private static String moneda(double cantidad) {
        return MONEDA.format(cantidad);
    }

    private static String cortar(String texto, int maximo) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= maximo) {
            return texto;
        }
        return texto.substring(0, maximo);
    }
}
