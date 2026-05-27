package com.abarrotes.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
    private static final Color FONDO = Color.WHITE;
    private static final Color SUPERFICIE = Color.WHITE;
    private static final Color BORDE = ROJO_OSCURO;
    private static final Color TEXTO = Color.BLACK;
    private static final Color TEXTO_SUAVE = new Color(102, 112, 133);
    private static final Color PELIGRO = new Color(179, 38, 30);
    private static final Color BOTON_FONDO = Color.WHITE;
    private static final Color BOTON_BORDE = ROJO_OSCURO;

    private JPanel contentPane;
    private JPanel panelMarca;
    private JPanel panelFranja;
    private JPanel panelFormulario;
    private JLabel lblSistema;
    private JLabel lblEquipo;
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private JButton btnEntrar;

    public LoginView() {
        initialize();
    }

    private void initialize() {
        setTitle("Abarrotes - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 520);
        setMinimumSize(new Dimension(860, 520));
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(FONDO);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        panelMarca = new JPanel();
        panelMarca.setBackground(ROJO);
        panelMarca.setBounds(0, 0, 330, 520);
        panelMarca.setLayout(null);
        contentPane.add(panelMarca);

        lblSistema = new JLabel("ABARROTES");
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setBounds(34, 185, 260, 45);
        panelMarca.add(lblSistema);

        lblEquipo = new JLabel("Equipo 6");
        lblEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEquipo.setForeground(Color.WHITE);
        lblEquipo.setBounds(36, 236, 220, 26);
        panelMarca.add(lblEquipo);

        panelFranja = new JPanel();
        panelFranja.setBackground(AMARILLO);
        panelFranja.setBounds(36, 280, 210, 6);
        panelMarca.add(panelFranja);

        panelFormulario = new JPanel();
        panelFormulario.setBackground(SUPERFICIE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(30, 34, 30, 34)));
        panelFormulario.setBounds(400, 82, 390, 340);
        panelFormulario.setLayout(null);
        contentPane.add(panelFormulario);

        lblTitulo = new JLabel("Iniciar sesion");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(TEXTO);
        lblTitulo.setBounds(34, 30, 300, 36);
        panelFormulario.add(lblTitulo);

        lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(TEXTO_SUAVE);
        lblUsuario.setBounds(34, 86, 300, 20);
        panelFormulario.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsuario.setForeground(TEXTO);
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtUsuario.setBounds(34, 110, 320, 36);
        panelFormulario.add(txtUsuario);

        lblPassword = new JLabel("Contrasena");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(TEXTO_SUAVE);
        lblPassword.setBounds(34, 164, 300, 20);
        panelFormulario.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setForeground(TEXTO);
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtPassword.setBounds(34, 188, 320, 36);
        panelFormulario.add(txtPassword);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblError.setForeground(TEXTO_SUAVE);
        lblError.setHorizontalAlignment(SwingConstants.LEFT);
        lblError.setBounds(34, 232, 320, 20);
        panelFormulario.add(lblError);

        btnEntrar = new JButton("INGRESAR");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setForeground(TEXTO);
        btnEntrar.setBackground(BOTON_FONDO);
        btnEntrar.setOpaque(true);
        btnEntrar.setContentAreaFilled(true);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOTON_BORDE),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btnEntrar.setBounds(34, 268, 320, 40);
        btnEntrar.addActionListener(e -> validarAcceso());
        panelFormulario.add(btnEntrar);

        txtPassword.addActionListener(e -> validarAcceso());
        getRootPane().setDefaultButton(btnEntrar);
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
