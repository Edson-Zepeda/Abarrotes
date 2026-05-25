package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.abarrotes.app.BaseDatos;
import com.abarrotes.app.Main;
import com.abarrotes.model.Usuario;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Panel de Control de Personal".
 * Sirve para dar de alta a nuevos empleados, cambiar sus claves o quitarlos del sistema.
 */

public class UsuarioView extends JDialog {
    // Definimos las piezas: tabla para ver la lista, cuadros para escribir y el selector de roles.
    private NonEditableTableModel modelo;
    private JTable tabla;
    private JTextField txtUser, txtBuscar;
    private JPasswordField txtPass;
    private JComboBox<String> comboRol;
    private JLabel lblRegistros, lblModo;
    private JButton btnModificar, btnBorrar;
    private ArrayList<Usuario> listaFiltrada = new ArrayList<>();

    public UsuarioView(JFrame parent) {
        super(parent, "Usuarios - Abarrotes POS", true);
        setSize(980, 620);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Usuarios", ""), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);

        actualizarTabla();
        actualizarEstadoBotones();
    }

    private JPanel crearContenido() {
        JPanel panel = UiFactory.panelBase();

        txtBuscar = UiFactory.campoTexto();
        JButton btnLimpiarBusqueda = UiFactory.botonClaro("Limpiar");
        lblRegistros = new JLabel();
        JPanel barraBusqueda = UiFactory.barraBusqueda(txtBuscar, btnLimpiarBusqueda, lblRegistros);

        modelo = new NonEditableTableModel(new Object[]{"Usuario", "Rol"}, 0);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tabla);

        JPanel cuerpo = new JPanel(new BorderLayout(12, 0));
        cuerpo.setBackground(AppTheme.FONDO);
        cuerpo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        cuerpo.add(crearFormulario(), BorderLayout.EAST);

        JButton btnCerrar = UiFactory.botonClaro("Regresar al menu");
        btnCerrar.addActionListener(e -> dispose());

        panel.add(barraBusqueda, BorderLayout.NORTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTabla(); }
            public void removeUpdate(DocumentEvent e) { actualizarTabla(); }
            public void changedUpdate(DocumentEvent e) { actualizarTabla(); }
        });
        btnLimpiarBusqueda.addActionListener(e -> txtBuscar.setText(""));
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(305, 0));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        lblModo = UiFactory.tituloPanel("Nuevo usuario");
        txtUser = UiFactory.campoTexto();
        txtPass = UiFactory.campoPassword();
        comboRol = new JComboBox<>(new String[]{"Admin", "Empleado"});
        comboRol.setFont(AppTheme.fuenteNormal(13));

        JButton btnAgregar = UiFactory.botonPrimario("Registrar");
        btnModificar = UiFactory.botonSecundario("Guardar cambios");
        JButton btnLimpiar = UiFactory.botonClaro("Limpiar");
        btnBorrar = UiFactory.botonPeligro("Eliminar");

        btnAgregar.addActionListener(e -> registrarUsuario());
        btnModificar.addActionListener(e -> modificarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBorrar.addActionListener(e -> borrarUsuario());

        JPanel botones = new JPanel(new GridLayout(4, 1, 0, 8));
        botones.setBackground(AppTheme.SUPERFICIE);
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnLimpiar);
        botones.add(btnBorrar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(lblModo, gbc);
        agregarCampo(panel, gbc, "Usuario", txtUser);
        agregarCampo(panel, gbc, "Contrasena", txtPass);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(UiFactory.etiqueta("Rol"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);
        panel.add(comboRol, gbc);

        gbc.gridy++;
        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(12, 0, 0, 0);
        panel.add(botones, gbc);

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JTextField campo) {
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(UiFactory.etiqueta(etiqueta), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);
        panel.add(campo, gbc);
    }

    // Guarda el usuario nuevo en SQLite y en la lista general del programa (Main).
    private void registrarUsuario() {
        String usuarioTexto = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (!usuarioTexto.isEmpty() && !pass.isEmpty()) {
            if (BaseDatos.existeUsuario(usuarioTexto)) {
                JOptionPane.showMessageDialog(this, "Ese usuario ya existe");
                return;
            }

            Usuario usuario = new Usuario(usuarioTexto, pass, comboRol.getSelectedItem().toString());
            if (BaseDatos.guardarUsuario(usuario)) {
                Main.listaUsuarios.add(usuario);
                actualizarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el usuario en la base de datos");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
        }
    }

    // Modifica usuario seleccionado y actualiza tambien SQLite.
    private void modificarUsuario() {
        Usuario seleccionado = obtenerUsuarioSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla");
            return;
        }

        String usernameOriginal = seleccionado.getUsername();
        String usuarioTexto = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String rol = comboRol.getSelectedItem().toString();

        if (usuarioTexto.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }
        if (!usernameOriginal.equalsIgnoreCase(usuarioTexto) && BaseDatos.existeUsuario(usuarioTexto)) {
            JOptionPane.showMessageDialog(this, "Ese usuario ya existe");
            return;
        }
        if (seleccionado.getRol().equals("Admin") && !rol.equals("Admin") && contarAdmins() <= 1) {
            JOptionPane.showMessageDialog(this, "Debe existir al menos un administrador");
            return;
        }

        Usuario usuarioNuevo = new Usuario(usuarioTexto, pass, rol);
        if (BaseDatos.modificarUsuario(usernameOriginal, usuarioNuevo)) {
            seleccionado.setUsername(usuarioTexto);
            seleccionado.setPassword(pass);
            seleccionado.setRol(rol);
            actualizarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar el usuario");
        }
    }

    // Borra el usuario seleccionado de SQLite y de la lista del programa.
    private void borrarUsuario() {
        Usuario seleccionado = obtenerUsuarioSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla");
            return;
        }
        if (seleccionado.getRol().equals("Admin") && contarAdmins() <= 1) {
            JOptionPane.showMessageDialog(this, "Debe existir al menos un administrador");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas borrar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            if (BaseDatos.borrarUsuario(seleccionado.getUsername())) {
                Main.listaUsuarios.remove(seleccionado);
                actualizarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo borrar el usuario");
            }
        }
    }

    /**
     * Este metodo limpia la tabla y vuelve a dibujar a los usuarios
     * leyendo la lista actualizada desde la memoria (Main).
     */
    private void actualizarTabla() {
        modelo.setRowCount(0);
        listaFiltrada.clear();
        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();

        for (Usuario u : Main.listaUsuarios) {
            if (busqueda.isEmpty()
                    || u.getUsername().toLowerCase().contains(busqueda)
                    || u.getRol().toLowerCase().contains(busqueda)) {
                listaFiltrada.add(u);
                modelo.addRow(new Object[]{u.getUsername(), u.getRol()});
            }
        }
        lblRegistros.setText(listaFiltrada.size() + " registros");
        actualizarEstadoBotones();
    }

    private void cargarSeleccion() {
        Usuario usuario = obtenerUsuarioSeleccionado();
        if (usuario != null) {
            txtUser.setText(usuario.getUsername());
            txtPass.setText(usuario.getPassword());
            comboRol.setSelectedItem(usuario.getRol());
            lblModo.setText("Editar usuario");
        }
        actualizarEstadoBotones();
    }

    private Usuario obtenerUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && fila < listaFiltrada.size()) {
            return listaFiltrada.get(fila);
        }
        return null;
    }

    private int contarAdmins() {
        int total = 0;
        for (Usuario u : Main.listaUsuarios) {
            if (u.getRol().equals("Admin")) {
                total++;
            }
        }
        return total;
    }

    private void limpiarCampos() {
        tabla.clearSelection();
        txtUser.setText("");
        txtPass.setText("");
        comboRol.setSelectedIndex(0);
        lblModo.setText("Nuevo usuario");
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = tabla != null && tabla.getSelectedRow() >= 0;
        if (btnModificar != null) {
            btnModificar.setEnabled(haySeleccion);
        }
        if (btnBorrar != null) {
            btnBorrar.setEnabled(haySeleccion);
        }
    }
}
