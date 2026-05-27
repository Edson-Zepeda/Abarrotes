package com.abarrotes.view.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Tema visual general del sistema.
 * Aqui se concentran colores, letras y estilos para que todas las ventanas
 * se vean como parte del mismo punto de venta.
 */

public class AppTheme {
    public static final Color ROJO = new Color(198, 40, 40);
    public static final Color ROJO_OSCURO = new Color(142, 27, 27);
    public static final Color AMARILLO = new Color(249, 200, 70);
    public static final Color VERDE = new Color(46, 125, 50);
    public static final Color PELIGRO = new Color(179, 38, 30);
    public static final Color FONDO = Color.WHITE;
    public static final Color SUPERFICIE = Color.WHITE;
    public static final Color BORDE = ROJO_OSCURO;
    public static final Color TEXTO = Color.BLACK;
    public static final Color TEXTO_SUAVE = new Color(102, 112, 133);
    public static final Color GRIS_TABLA = Color.WHITE;
    public static final Color BOTON_FONDO = Color.WHITE;
    public static final Color BOTON_BORDE = ROJO_OSCURO;

    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

    private AppTheme() {
    }

    // Prepara detalles basicos de Swing antes de abrir la primera ventana.
    public static void instalar() {
        UIManager.put("Button.font", fuenteNegrita(13));
        UIManager.put("Label.font", fuenteNormal(13));
        UIManager.put("TextField.font", fuenteNormal(13));
        UIManager.put("PasswordField.font", fuenteNormal(13));
        UIManager.put("ComboBox.font", fuenteNormal(13));
        UIManager.put("Table.font", fuenteNormal(13));
        UIManager.put("TableHeader.foreground", TEXTO);
        UIManager.put("TableHeader.font", fuenteNegrita(12));
        UIManager.put("OptionPane.messageFont", fuenteNormal(13));
        UIManager.put("OptionPane.buttonFont", fuenteNegrita(12));
    }

    public static Font fuenteNormal(int size) {
        return FUENTE.deriveFont(Font.PLAIN, size);
    }

    public static Font fuenteNegrita(int size) {
        return FUENTE.deriveFont(Font.BOLD, size);
    }

    public static Border bordeVacio(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    public static Border bordeTarjeta() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        );
    }

    public static String moneda(double cantidad) {
        return MONEDA.format(cantidad);
    }

    public static void aplicarFondo(JComponent component) {
        component.setBackground(FONDO);
        component.setForeground(TEXTO);
        component.setFont(fuenteNormal(13));
    }

    public static void configurarTabla(JTable tabla) {
        tabla.setRowHeight(34);
        tabla.setFont(fuenteNormal(13));
        tabla.setForeground(TEXTO);
        tabla.setBackground(SUPERFICIE);
        tabla.setGridColor(BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(255, 243, 199));
        tabla.setSelectionForeground(TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(fuenteNegrita(12));
        tabla.getTableHeader().setBackground(ROJO_OSCURO);
        tabla.getTableHeader().setForeground(TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    public static DefaultTableCellRenderer rendererMoneda() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (value instanceof Number) {
                    setText(moneda(((Number) value).doubleValue()));
                }
                return this;
            }
        };
    }

    public static DefaultTableCellRenderer rendererCentro() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        };
    }
}
