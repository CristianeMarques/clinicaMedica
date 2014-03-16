/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica.medicacao;

import clinicamedica.medicacao.Medicacao;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author CristianeMarques
 */
public class MedicacaoTableModel extends AbstractTableModel {
    
    private List<Medicacao> list;
    private final String[] columnNames;
    
    public MedicacaoTableModel(List<Medicacao> list, String[] columnNames) {
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
                return list.get(row).getIdMedicacao();
            default:
                return list.get(row).getNome();
        }
    }

   
}
