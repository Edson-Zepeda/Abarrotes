package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.abarrotes.app.BaseDatos;
import com.abarrotes.model.Usuario;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es la "Llave de Seguridad" del sistema.
 * Su funcion es verificar que solo las personas autorizadas puedan entrar
 * y saber si son Administradores o Empleados.
 */

public class LoginView extends JFrame {
    // Espacios donde el usuario escribira su nombre y contrasena.
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginView() {
        setTitle("Abarrotes POS - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 520);
        setMinimumSize(new Dimension(760, 480));
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(AppTheme.FONDO);
        setContentPane(contentPane);

        contentPane.add(crearPanelMarca(), BorderLayout.WEST);
        contentPane.add(crearPanelAcceso(), BorderLayout.CENTER);
    }

    private JPanel crearPanelMarca() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(330, 0));
        panel.setBackground(AppTheme.ROJO);
        panel.setBorder(AppTheme.bordeVacio(34, 34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lblSistema = new JLabel("ABARROTES POS");
        lblSistema.setFont(AppTheme.fuenteNegrita(30));
        lblSistema.setForeground(Color.WHITE);

        JLabel lblEquipo = new JLabel("Equipo 6");
        lblEquipo.setFont(AppTheme.fuenteNormal(18));
        lblEquipo.setForeground(new Color(255, 238, 180));

        JLabel lblLinea = new JLabel("Caja rapida, inventario y corte diario");
        lblLinea.setFont(AppTheme.fuenteNormal(13));
        lblLinea.setForeground(Color.WHITE);

        JPanel franja = new JPanel();
        franja.setBackground(AppTheme.AMARILLO);
        franja.setPreferredSize(new Dimension(0, 6));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(lblSistema, gbc);
        gbc.gridy++;
        panel.add(lblEquipo, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(24, 0, 24, 0);
        panel.add(franja, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(lblLinea, gbc);

        return panel;
    }

    private JPanel crearPanelAcceso() {
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setBackground(AppTheme.FONDO);
        exterior.setBorder(AppTheme.bordeVacio(34, 46, 34, 46));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SUPERFICIE);
        form.setBorder(AppTheme.bordeTarjeta());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel titulo = new JLabel("Iniciar sesion");
        titulo.setFont(AppTheme.fuenteNegrita(26));
        titulo.setForeground(AppTheme.TEXTO);

        JLabel subtitulo = new JLabel("Ingresa tus credenciales para abrir el punto de venta.");
        subtitulo.setFont(AppTheme.fuenteNormal(13));
        subtitulo.setForeground(AppTheme.TEXTO_SUAVE);

        txtUsuario = UiFactory.campoTexto();
        txtPassword = UiFactory.campoPassword();
        lblError = new JLabel(" ");
        lblError.setFont(AppTheme.fuenteNegrita(12));
        lblError.setForeground(AppTheme.PELIGRO);
        lblError.setHorizontalAlignment(SwingConstants.LEFT);

        JButton btnEntrar = UiFactory.botonPrimario("INGRESAR");
        btnEntrar.addActionListener(e -> validarAcceso());
        txtPassword.addActionListener(e -> validarAcceso());
        getRootPane().setDefaultButton(btnEntrar);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        form.add(titulo, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 26, 0);
        form.add(subtitulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        form.add(UiFactory.etiqueta("Usuario"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 18, 0);
        form.add(txtUsuario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        form.add(UiFactory.etiqueta("Contrasena"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        form.add(txtPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        form.add(lblError, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(btnEntrar, gbc);

        exterior.add(form, new GridBagConstraints());
        return exterior;
    }

    // Logica para validar y dar accesos.
    private void validarAcceso() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Escribe usuario y contrasena.");
            return;
        }

        Usuario usuario = BaseDatos.validarUsuario(user, pass);
        if (usuario != null) {
            MenuPrincipal menu = new MenuPrincipal(usuario.getRol());
            menu.setVisible(true);
            menu.setLocationRelativeTo(null);
            this.dispose();
            return;
        }

        // Si se ingreso mal, se muestra el error sin sacar al usuario del flujo.
        lblError.setText("Usuario o contrasena incorrectos.");
        txtPassword.setText("");
        txtPassword.requestFocus();
    }
}
