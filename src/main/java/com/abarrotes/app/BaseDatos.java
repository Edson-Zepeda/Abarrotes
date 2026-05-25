package com.abarrotes.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.abarrotes.model.Usuario;

/**
 * Esta clase funciona como la base de datos real del sistema.
 * Usa SQLite por medio de JDBC para que los usuarios del login
 * ya no dependan solamente de la memoria del programa.
 */

public class BaseDatos {
    private static final String NOMBRE_BASE_DATOS = "abarrotes.db";

    /**
     * Abre la conexion con SQLite.
     * Si se ejecuta desde scripts usa data/, y si se ejecuta desde Eclipse
     * tambien puede encontrar la carpeta dentro de Abarrotes/data/.
     */
    private static Connection conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver SQLite JDBC.", e);
        }

        File carpetaData = new File("data");
        if (!carpetaData.exists() && new File("Abarrotes/data").exists()) {
            carpetaData = new File("Abarrotes/data");
        }
        if (!carpetaData.exists()) {
            carpetaData.mkdirs();
        }

        File archivoBaseDatos = new File(carpetaData, NOMBRE_BASE_DATOS);
        return DriverManager.getConnection("jdbc:sqlite:" + archivoBaseDatos.getPath());
    }

    /**
     * Prepara la tabla de usuarios e inserta las cuentas iniciales
     * solo cuando no existen todavia.
     */
    public static void inicializarBaseDatos() {
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "rol TEXT NOT NULL"
                + ")";

        try (Connection conexion = conectar();
             Statement statement = conexion.createStatement()) {
            statement.execute(tablaUsuarios);
            guardarUsuarioInicial(conexion, "admin", "1234", "Admin");
            guardarUsuarioInicial(conexion, "empleado", "1234", "Empleado");
            System.out.println("BASE DE DATOS: Usuarios listos en SQLite.");
        } catch (SQLException e) {
            System.out.println("ERROR BASE DE DATOS: " + e.getMessage());
        }
    }

    // Guarda usuarios de prueba sin duplicarlos si ya estan en la base.
    private static void guardarUsuarioInicial(Connection conexion, String username, String password, String rol) throws SQLException {
        String sql = "INSERT OR IGNORE INTO usuarios(username, password, rol) VALUES(?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, rol);
            ps.executeUpdate();
        }
    }

    /**
     * Carga todos los usuarios guardados para que la tabla de gestion
     * siga trabajando con la lista que ya usa el programa.
     */
    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT username, password, rol FROM usuarios ORDER BY username";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.out.println("ERROR AL CARGAR USUARIOS: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Valida directamente contra la base de datos el usuario y password
     * que se escriben en la pantalla de login.
     */
    public static Usuario validarUsuario(String username, String password) {
        String sql = "SELECT username, password, rol FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR EN LOGIN: " + e.getMessage());
        }

        return null;
    }

    /**
     * Guarda un usuario nuevo en SQLite.
     * Devuelve true cuando se guardo correctamente.
     */
    public static boolean guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(username, password, rol) VALUES(?, ?, ?)";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR AL GUARDAR USUARIO: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifica un usuario existente.
     * Se usa el username original por si tambien se cambia el nombre.
     */
    public static boolean modificarUsuario(String usernameOriginal, Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, rol = ? WHERE username = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usernameOriginal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR AL MODIFICAR USUARIO: " + e.getMessage());
            return false;
        }
    }

    /**
     * Borra un usuario de SQLite.
     * Devuelve true si realmente se elimino una cuenta.
     */
    public static boolean borrarUsuario(String username) {
        String sql = "DELETE FROM usuarios WHERE username = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR AL BORRAR USUARIO: " + e.getMessage());
            return false;
        }
    }

    // Revisa si el nombre de usuario ya existe para no repetir cuentas.
    public static boolean existeUsuario(String username) {
        String sql = "SELECT username FROM usuarios WHERE username = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR USUARIO: " + e.getMessage());
            return false;
        }
    }
}
