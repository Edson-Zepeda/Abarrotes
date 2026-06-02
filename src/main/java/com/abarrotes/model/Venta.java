package com.abarrotes.model;

/**
 * Clase Modelo que representa una venta terminada.
 * Guarda los datos necesarios para que Corte de Caja muestre
 * un reporte completo y no solo el total en efectivo.
 */

public class Venta {
    private int id;
    private String hora;
    private String cliente;
    private double total;

    public Venta(int id, String hora, String cliente, double total) {
        this.id = id;
        this.hora = hora;
        this.cliente = cliente;
        this.total = total;
    }

    // Métodos getters para llenar la tabla de corte de caja.
    public int getId() { return id; }
    public String getHora() { return hora; }
    public String getCliente() { return cliente; }
    public double getTotal() { return total; }
}
