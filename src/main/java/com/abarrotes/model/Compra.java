package com.abarrotes.model;

/**
 * Clase Modelo que representa una compra registrada al proveedor.
 * Se usa para aumentar existencias y dejar evidencia de entradas al inventario.
 */

public class Compra {
    private int id;
    private String hora;
    private String proveedor;
    private String producto;
    private int cantidad;
    private double costoUnitario;
    private double total;

    public Compra(int id, String hora, String proveedor, String producto, int cantidad, double costoUnitario) {
        this.id = id;
        this.hora = hora;
        this.proveedor = proveedor;
        this.producto = producto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.total = cantidad * costoUnitario;
    }

    // Metodos getters para llenar la tabla de compras.
    public int getId() { return id; }
    public String getHora() { return hora; }
    public String getProveedor() { return proveedor; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getCostoUnitario() { return costoUnitario; }
    public double getTotal() { return total; }
}
