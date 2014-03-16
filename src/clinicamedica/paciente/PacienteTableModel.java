/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.paciente;

import clinicamedica.medicacao.Medicacao;
import clinicamedica.paciente.Paciente;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author CristianeMarques
 */
public class PacienteTableModel extends AbstractTableModel {
    
    private List<Paciente> list;
    private final String[] columnNames;
    
    public PacienteTableModel(List<Paciente> list, String[] columnNames) {
        this.list = list;
        this.columnNames = columnNames;
    }
    

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return list.get(row).getNome();
            default:
                return list.get(row).getTelefone();
        }
    }
    
    public void removeRow(int row) {
        list.remove(row);
        fireTableRowsDeleted(row, row);
    }
   
}
