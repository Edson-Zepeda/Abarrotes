package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.abarrotes.app.Main;
import com.abarrotes.model.Cliente;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Directorio de Clientes".
 * Sirve para anotar clientes frecuentes, modificar sus datos o borrarlos.
 */

public class ClienteView extends JDialog {
    // Estas son las piezas necesarias: la tabla, el modelo de datos y los cuadritos de texto.
    private NonEditableTableModel modelo;
    private JTable tabla;
    private JTextField txtId, txtNombre, txtTel, txtBuscar;
    private JLabel lblRegistros, lblModo;
    private JButton btnModificar, btnBorrar;
    private ArrayList<Cliente> listaFiltrada = new ArrayList<>();

    public ClienteView(JFrame parent) {
        super(parent, "Clientes - Abarrotes", true);
        setSize(1040, 680);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Clientes", ""), BorderLayout.NORTH);
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

        modelo = new NonEditableTableModel(new Object[]{"Numero de Cliente", "Nombre", "Telefono"}, 0);
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
        panel.setPreferredSize(new Dimension(325, 0));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        lblModo = UiFactory.tituloPanel("Nuevo cliente");
        txtId = UiFactory.campoTexto();
        txtNombre = UiFactory.campoTexto();
        txtTel = UiFactory.campoTexto();

        JButton btnAgregar = UiFactory.botonPrimario("Agregar");
        btnModificar = UiFactory.botonSecundario("Guardar cambios");
        JButton btnLimpiar = UiFactory.botonClaro("Limpiar");
        btnBorrar = UiFactory.botonPeligro("Eliminar");

        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBorrar.addActionListener(e -> borrarCliente());

        JPanel botones = new JPanel(new GridLayout(4, 1, 0, 8));
        botones.setBackground(AppTheme.SUPERFICIE);
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnLimpiar);
        botones.add(btnBorrar);

        JLabel lblNumeroCliente = UiFactory.etiqueta("Numero de Cliente");
        JLabel lblNombre = UiFactory.etiqueta("Nombre");
        JLabel lblTelefono = UiFactory.etiqueta("Telefono");
        JLabel separador = new JLabel();

        GridBagConstraints gbcLblModo = new GridBagConstraints();
        gbcLblModo.gridx = 0;
        gbcLblModo.gridy = 0;
        gbcLblModo.fill = GridBagConstraints.HORIZONTAL;
        gbcLblModo.weightx = 1;
        gbcLblModo.insets = new Insets(0, 0, 18, 0);
        panel.add(lblModo, gbcLblModo);

        GridBagConstraints gbcLblNumeroCliente = new GridBagConstraints();
        gbcLblNumeroCliente.gridx = 0;
        gbcLblNumeroCliente.gridy = 1;
        gbcLblNumeroCliente.fill = GridBagConstraints.HORIZONTAL;
        gbcLblNumeroCliente.weightx = 1;
        gbcLblNumeroCliente.insets = new Insets(0, 0, 6, 0);
        panel.add(lblNumeroCliente, gbcLblNumeroCliente);

        GridBagConstraints gbcTxtId = new GridBagConstraints();
        gbcTxtId.gridx = 0;
        gbcTxtId.gridy = 2;
        gbcTxtId.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtId.weightx = 1;
        gbcTxtId.insets = new Insets(0, 0, 14, 0);
        panel.add(txtId, gbcTxtId);

        GridBagConstraints gbcLblNombre = new GridBagConstraints();
        gbcLblNombre.gridx = 0;
        gbcLblNombre.gridy = 3;
        gbcLblNombre.fill = GridBagConstraints.HORIZONTAL;
        gbcLblNombre.weightx = 1;
        gbcLblNombre.insets = new Insets(0, 0, 6, 0);
        panel.add(lblNombre, gbcLblNombre);

        GridBagConstraints gbcTxtNombre = new GridBagConstraints();
        gbcTxtNombre.gridx = 0;
        gbcTxtNombre.gridy = 4;
        gbcTxtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtNombre.weightx = 1;
        gbcTxtNombre.insets = new Insets(0, 0, 14, 0);
        panel.add(txtNombre, gbcTxtNombre);

        GridBagConstraints gbcLblTelefono = new GridBagConstraints();
        gbcLblTelefono.gridx = 0;
        gbcLblTelefono.gridy = 5;
        gbcLblTelefono.fill = GridBagConstraints.HORIZONTAL;
        gbcLblTelefono.weightx = 1;
        gbcLblTelefono.insets = new Insets(0, 0, 6, 0);
        panel.add(lblTelefono, gbcLblTelefono);

        GridBagConstraints gbcTxtTel = new GridBagConstraints();
        gbcTxtTel.gridx = 0;
        gbcTxtTel.gridy = 6;
        gbcTxtTel.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtTel.weightx = 1;
        gbcTxtTel.insets = new Insets(0, 0, 14, 0);
        panel.add(txtTel, gbcTxtTel);

        GridBagConstraints gbcSeparador = new GridBagConstraints();
        gbcSeparador.gridx = 0;
        gbcSeparador.gridy = 7;
        gbcSeparador.fill = GridBagConstraints.HORIZONTAL;
        gbcSeparador.weightx = 1;
        gbcSeparador.weighty = 1;
        panel.add(separador, gbcSeparador);

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 8;
        gbcBotones.fill = GridBagConstraints.HORIZONTAL;
        gbcBotones.weightx = 1;
        gbcBotones.insets = new Insets(12, 0, 0, 0);
        panel.add(botones, gbcBotones);

        return panel;
    }

    // Agrega clientes validando que no falten datos ni se repita el numero.
    private void agregarCliente() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTel.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }
        if (buscarClientePorId(id) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese numero");
            return;
        }

        Main.listaClientes.add(new Cliente(id, nombre, telefono));
        actualizarTabla();
        limpiarCampos();
    }

    // Modifica el cliente seleccionado en la tabla.
    private void modificarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla");
            return;
        }

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTel.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }

        Cliente repetido = buscarClientePorId(id);
        if (repetido != null && repetido != cliente) {
            JOptionPane.showMessageDialog(this, "Ya existe otro cliente con ese numero");
            return;
        }

        cliente.setId(id);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        actualizarTabla();
        limpiarCampos();
    }

    // Borra el cliente seleccionado con confirmacion.
    private void borrarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas borrar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            Main.listaClientes.remove(cliente);
            actualizarTabla();
            limpiarCampos();
        }
    }

    /**
     * Este metodo borra lo que hay en la tabla y vuelve a leer la lista de clientes
     * para que siempre muestre la informacion mas reciente.
     */
    private void actualizarTabla() {
        modelo.setRowCount(0);
        listaFiltrada.clear();
        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();

        for (Cliente c : Main.listaClientes) {
            if (busqueda.isEmpty()
                    || c.getId().toLowerCase().contains(busqueda)
                    || c.getNombre().toLowerCase().contains(busqueda)
                    || c.getTelefono().toLowerCase().contains(busqueda)) {
                listaFiltrada.add(c);
                modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono()});
            }
        }
        lblRegistros.setText(listaFiltrada.size() + " registros");
        actualizarEstadoBotones();
    }

    // Copia a los campos los datos del renglon elegido.
    private void cargarSeleccion() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente != null) {
            txtId.setText(cliente.getId());
            txtNombre.setText(cliente.getNombre());
            txtTel.setText(cliente.getTelefono());
            lblModo.setText("Editar cliente");
        }
        actualizarEstadoBotones();
    }

    private Cliente obtenerClienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && fila < listaFiltrada.size()) {
            return listaFiltrada.get(fila);
        }
        return null;
    }

    private Cliente buscarClientePorId(String id) {
        for (Cliente c : Main.listaClientes) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    private void limpiarCampos() {
        tabla.clearSelection();
        txtId.setText("");
        txtNombre.setText("");
        txtTel.setText("");
        lblModo.setText("Nuevo cliente");
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
