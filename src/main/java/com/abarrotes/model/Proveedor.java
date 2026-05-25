package com.abarrotes.model;

/**
 * Clase Modelo que representa a un Proveedor en el sistema.
 * Esta clase permite organizar la cadena de suministros de la tienda, 
 * facilitando el contacto con las marcas que surten los productos.
 */

public class Proveedor {
	//Atributos minimos necesarios para provedor
	private String nombre;
    private String empresa;
    private String contacto;
    
    /**
     * Constructor de la clase Proveedor.
     */
    
    public Proveedor(String nombre, String empresa, String contacto) {
        this.nombre = nombre;
        this.empresa = empresa;
        this.contacto = contacto;
    }
    
    //Metodos getters
    // Proporcionan acceso de "solo lectura" a los datos desde las tablas
    // de la vista ProveedorView.
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public String getContacto() { return contacto; }
    
    // Metodos setters para actualizar proveedores desde la ventana.
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    @Override
    public String toString() {
        return empresa + " - " + nombre;
    }

}
