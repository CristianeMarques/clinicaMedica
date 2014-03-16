package clinicamedica.medico;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author CristianeMarques
 */
public class MedicoTableModel extends AbstractTableModel {
    
    private final List<Medico> list;
    private final String[] columnNames;
    
    public MedicoTableModel(List<Medico> list, String[] columnNames) {
        this.list = list;
        this.columnNames = columnNames;
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
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
                return list.get(row).getCrm();
            default:
                return list.get(row).getNome();
        }
    }
    
    public void removeRow(int row) {
        list.remove(row);
        fireTableRowsDeleted(row, row);
    }
   
}
