package com.abarrotes.model;

/**
 * Clase Modelo que representa a los usuarios del sistema.
 * Importante para la seguridad, ya que permite gestionar 
 * el acceso y los permisos (roles) dentro de la aplicación.
 */

public class Usuario {
	//Atributos necesarios para Usuarios del sistema
	private String username;
    private String password;
    private String rol; // "Administrador" o "Empleado"
    
    /**
     * Constructor de la clase Usuario.
     * - "Administrador": Acceso total 
     * - "Empleado": Acceso limitado 
     */
    
    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }
    
    // Métodos getters
    // Proporcionan los datos necesarios para el proceso de Login 
    // y para llenar la tabla de gestión de usuarios.
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    
    // Métodos setters para actualizar usuarios desde el panel de control.
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }

}
