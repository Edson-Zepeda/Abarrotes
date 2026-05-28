package com.abarrotes.view.ui;

import javax.swing.table.DefaultTableModel;

/**
 * Modelo de tabla de solo lectura.
 * Evita que el usuario cambie datos directamente en las celdas sin pasar
 * por las validaciones de cada formulario.
 */

public class NonEditableTableModel extends DefaultTableModel {
    public NonEditableTableModel(Object[] columnas, int filas) {
        super(columnas, filas);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
