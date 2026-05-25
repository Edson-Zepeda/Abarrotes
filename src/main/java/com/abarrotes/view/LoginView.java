package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.abarrotes.app.BaseDatos;
import com.abarrotes.model.Usuario;

/**
 * Esta ventana es la "Llave de Seguridad" del sistema.
 * Su funcion es verificar que solo las personas autorizadas puedan entrar
 * y saber si son Administradores o Empleados.
 */
public class LoginView extends JFrame {
    private static final Color ROJO = new Color(198, 40, 40);
    private static final Color ROJO_OSCURO = new Color(142, 27, 27);
    private static final Color AMARILLO = new Color(249, 200, 70);
    private static final Color FONDO = new Color(245, 246, 247);
    private static final Color SUPERFICIE = Color.WHITE;
    private static final Color BORDE = new Color(215, 220, 226);
    private static final Color TEXTO = new Color(31, 41, 51);
    private static final Color TEXTO_SUAVE = new Color(102, 112, 133);
    private static final Color PELIGRO = new Color(179, 38, 30);

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

        JPanel contentPane = new JPanel();
        contentPane.setBackground(FONDO);
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel panelMarca = new JPanel();
        panelMarca.setPreferredSize(new Dimension(330, 0));
        panelMarca.setBackground(ROJO);
        panelMarca.setBorder(BorderFactory.createEmptyBorder(34, 34, 34, 34));
        panelMarca.setLayout(new GridBagLayout());
        contentPane.add(panelMarca, BorderLayout.WEST);

        JLabel lblSistema = new JLabel("ABARROTES POS");
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblSistema.setForeground(Color.WHITE);

        GridBagConstraints gbcLblSistema = new GridBagConstraints();
        gbcLblSistema.gridx = 0;
        gbcLblSistema.gridy = 0;
        gbcLblSistema.fill = GridBagConstraints.HORIZONTAL;
        gbcLblSistema.weightx = 1.0;
        gbcLblSistema.insets = new Insets(0, 0, 8, 0);
        panelMarca.add(lblSistema, gbcLblSistema);

        JLabel lblEquipo = new JLabel("Equipo 6");
        lblEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEquipo.setForeground(new Color(255, 238, 180));

        GridBagConstraints gbcLblEquipo = new GridBagConstraints();
        gbcLblEquipo.gridx = 0;
        gbcLblEquipo.gridy = 1;
        gbcLblEquipo.fill = GridBagConstraints.HORIZONTAL;
        gbcLblEquipo.weightx = 1.0;
        gbcLblEquipo.insets = new Insets(0, 0, 8, 0);
        panelMarca.add(lblEquipo, gbcLblEquipo);

        JPanel panelFranja = new JPanel();
        panelFranja.setBackground(AMARILLO);
        panelFranja.setPreferredSize(new Dimension(0, 6));

        GridBagConstraints gbcPanelFranja = new GridBagConstraints();
        gbcPanelFranja.gridx = 0;
        gbcPanelFranja.gridy = 2;
        gbcPanelFranja.fill = GridBagConstraints.HORIZONTAL;
        gbcPanelFranja.weightx = 1.0;
        gbcPanelFranja.insets = new Insets(24, 0, 0, 0);
        panelMarca.add(panelFranja, gbcPanelFranja);

        JPanel panelAcceso = new JPanel();
        panelAcceso.setBackground(FONDO);
        panelAcceso.setBorder(BorderFactory.createEmptyBorder(34, 46, 34, 46));
        panelAcceso.setLayout(new GridBagLayout());
        contentPane.add(panelAcceso, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setPreferredSize(new Dimension(390, 340));
        panelFormulario.setBackground(SUPERFICIE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(30, 34, 30, 34)));
        panelFormulario.setLayout(new GridBagLayout());

        GridBagConstraints gbcPanelFormulario = new GridBagConstraints();
        gbcPanelFormulario.gridx = 0;
        gbcPanelFormulario.gridy = 0;
        gbcPanelFormulario.fill = GridBagConstraints.BOTH;
        gbcPanelFormulario.weightx = 1.0;
        gbcPanelFormulario.weighty = 1.0;
        panelAcceso.add(panelFormulario, gbcPanelFormulario);

        JLabel lblTitulo = new JLabel("Iniciar sesion");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(TEXTO);

        GridBagConstraints gbcLblTitulo = new GridBagConstraints();
        gbcLblTitulo.gridx = 0;
        gbcLblTitulo.gridy = 0;
        gbcLblTitulo.fill = GridBagConstraints.HORIZONTAL;
        gbcLblTitulo.weightx = 1.0;
        gbcLblTitulo.insets = new Insets(0, 0, 4, 0);
        panelFormulario.add(lblTitulo, gbcLblTitulo);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(TEXTO_SUAVE);

        GridBagConstraints gbcLblUsuario = new GridBagConstraints();
        gbcLblUsuario.gridx = 0;
        gbcLblUsuario.gridy = 1;
        gbcLblUsuario.fill = GridBagConstraints.HORIZONTAL;
        gbcLblUsuario.weightx = 1.0;
        gbcLblUsuario.insets = new Insets(18, 0, 6, 0);
        panelFormulario.add(lblUsuario, gbcLblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsuario.setForeground(TEXTO);
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setPreferredSize(new Dimension(280, 36));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        GridBagConstraints gbcTxtUsuario = new GridBagConstraints();
        gbcTxtUsuario.gridx = 0;
        gbcTxtUsuario.gridy = 2;
        gbcTxtUsuario.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtUsuario.weightx = 1.0;
        gbcTxtUsuario.insets = new Insets(0, 0, 18, 0);
        panelFormulario.add(txtUsuario, gbcTxtUsuario);

        JLabel lblPassword = new JLabel("Contrasena");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(TEXTO_SUAVE);

        GridBagConstraints gbcLblPassword = new GridBagConstraints();
        gbcLblPassword.gridx = 0;
        gbcLblPassword.gridy = 3;
        gbcLblPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcLblPassword.weightx = 1.0;
        gbcLblPassword.insets = new Insets(0, 0, 6, 0);
        panelFormulario.add(lblPassword, gbcLblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setForeground(TEXTO);
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setPreferredSize(new Dimension(280, 36));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        GridBagConstraints gbcTxtPassword = new GridBagConstraints();
        gbcTxtPassword.gridx = 0;
        gbcTxtPassword.gridy = 4;
        gbcTxtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtPassword.weightx = 1.0;
        gbcTxtPassword.insets = new Insets(0, 0, 8, 0);
        panelFormulario.add(txtPassword, gbcTxtPassword);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblError.setForeground(PELIGRO);
        lblError.setHorizontalAlignment(SwingConstants.LEFT);

        GridBagConstraints gbcLblError = new GridBagConstraints();
        gbcLblError.gridx = 0;
        gbcLblError.gridy = 5;
        gbcLblError.fill = GridBagConstraints.HORIZONTAL;
        gbcLblError.weightx = 1.0;
        gbcLblError.insets = new Insets(0, 0, 16, 0);
        panelFormulario.add(lblError, gbcLblError);

        JButton btnEntrar = new JButton("INGRESAR");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(ROJO_OSCURO);
        btnEntrar.setOpaque(true);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setPreferredSize(new Dimension(280, 40));
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnEntrar.addActionListener(e -> validarAcceso());
        txtPassword.addActionListener(e -> validarAcceso());
        getRootPane().setDefaultButton(btnEntrar);

        GridBagConstraints gbcBtnEntrar = new GridBagConstraints();
        gbcBtnEntrar.gridx = 0;
        gbcBtnEntrar.gridy = 6;
        gbcBtnEntrar.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnEntrar.weightx = 1.0;
        gbcBtnEntrar.insets = new Insets(0, 0, 0, 0);
        panelFormulario.add(btnEntrar, gbcBtnEntrar);
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
