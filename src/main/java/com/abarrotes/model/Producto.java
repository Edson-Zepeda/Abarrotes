package com.abarrotes.model;

/**
 * Clase Modelo que representa un artículo del inventario.
 * También gestiona la lógica 
 * básica de existencia de los productos en la tienda.
 */

public class Producto {
	//Atributos de producto
	private String nombre;
    private double precio;
    private int stock;

    
    /**
     * Constructor de la clase Producto.
     * Crea una instancia de un producto con sus valores iniciales.
     */
    
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Permiten que las tablas (JTable) y los reportes lean la información 
    // sin poder modificarla directamente sin permiso.
    
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    
    // Métodos setters para modificar productos desde Inventario.
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    
    // Método para actualizar el stock después de una venta
    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }
    
    /**
     * Sobreescritura del método toString.
     * Se personaliza para que en los JComboBox de la ventana de Ventas
     * el cajero pueda ver el nombre, el precio y cuánto queda de stock 
     * antes de añadirlo al carrito.
     */
    
    @Override
    public String toString() {
        return nombre + " - $" + precio + " (Stock: " + stock + ")";
    }

}
