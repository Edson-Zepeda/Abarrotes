package com.abarrotes.model;

/**
 * Clase Modelo que representa a un Cliente dentro del sistema.
 * Sigue el patrón de diseño de "Objeto de Datos" para estructurar 
 * la información que se mostrará en las tablas y formularios.
 */


public class Cliente {
	
	// Atributos (Encapsulamiento) ---
    // Se definen como private para proteger la integridad de los datos, 
    // cumpliendo con el principio de encapsulamiento de la POO.
	
	private String id;
    private String nombre;
    private String telefono;
    
    
    /**
     * Constructor de la clase Cliente.
     * Sirve para inicializar un nuevo objeto con todos sus datos necesarios
     * al momento de ser creado (instanciado).
     */
    
    public Cliente(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
 
    // Métodos Getter
    // Son necesarios para que componentes externos, como el DefaultTableModel 
    // de la vista (JTable), puedan acceder a los valores de forma segura.
    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    
    // Métodos setters para modificar clientes desde la pantalla de gestión.
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    
    /**
     * Sobreescritura del método toString.
     * * ¿Por qué es importante? 
     * Cuando agregamos un objeto 'Cliente' a un JComboBox (como en la Terminal de Ventas), 
     * Swing llama automáticamente a este método para saber qué texto mostrar 
     * en la lista desplegable. Al retornar el 'nombre', el usuario ve el nombre 
     * del cliente y no la dirección de memoria del objeto.
     */
    
    @Override
    public String toString() {
        return nombre;
    }

}
