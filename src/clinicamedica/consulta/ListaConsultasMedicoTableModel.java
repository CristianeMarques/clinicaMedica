package clinicamedica.consulta;

import clinicamedica.util.Util;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author CristianeMarques
 */
public class ListaConsultasMedicoTableModel extends AbstractTableModel {
    
    private List<Consulta> list;
    private final String[] columnNames;
    
    public ListaConsultasMedicoTableModel(List<Consulta> list, String[] columnNames) {
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
        Consulta consulta = list.get(row);
        switch(col) {
            case 0:
                return Util.SDFH.format(consulta.getDataHoraInicio());
            case 1:
                String dataFim = "";
                if (consulta.getDataHoraFim() != null) {
                    dataFim = Util.SDFH.format(consulta.getDataHoraFim());
                }
                return dataFim;
            case 2:
                return consulta.getIdMedico().getNome();
            case 3:
                return consulta.getIdPaciente().getNome();
            default:
                return consulta.getIdPaciente().getNome();
        }
    }
    

}
