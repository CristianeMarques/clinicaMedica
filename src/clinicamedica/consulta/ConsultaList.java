package clinicamedica.consulta;

import clinicamedica.util.Util;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author CristianeMarques
 */
public class ConsultaList extends JInternalFrame {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    private final ConsultaJpaController dao = new ConsultaJpaController(emf);

    private final List<Consulta> list;

    private final JTextField dataInicio = new JTextField();
    private final JTextField dataFim = new JTextField();
    private final JTextField paciente = new JTextField();
    private final JTextField medico = new JTextField();
    private final JTextArea descricao = new JTextArea();

    
    public ConsultaList(List<Consulta> list) {
        super("Consulta Listar", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        if (list == null)
            this.list = dao.findConsultaEntities();
        else {
            this.list = list;
        }
        createPanel();
    }
    

    private void createPanel() {
        final String[] columnNames = {
            "Data/Hora Ini",
            "Data/Hora Fim",
            "Médico",
            "Paciente"
        };
        
        final ListaConsultasMedicoTableModel tm = new ListaConsultasMedicoTableModel(list, columnNames);
        
        
        final JTable table = new JTable(tm);
        table.getSelectionModel().addListSelectionListener(new SharedListSelectionHandler());
        table.doLayout();

        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);

        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        this.add(scrollPane);
        
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.appendSeparator("Consulta");
        builder.append("Data Início", dataInicio);
        builder.append("Data Fim", dataFim);
        builder.nextLine();
        builder.append("Médico", medico);
        builder.append("Paciente", paciente);
        descricao.setMinimumSize(new Dimension(400, 200));
       
        this.add(builder.build());
        
        this.add(descricao);

        this.pack();
        this.setVisible(true);
    }
    
    class SharedListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            int row = lsm.getLeadSelectionIndex();
            Consulta consulta = list.get(row);
            dataInicio.setText(Util.SDFH.format(consulta.getDataHoraInicio()));
            String dataFimStr = "";
            if (consulta.getDataHoraFim() != null) {
                dataFimStr = Util.SDFH.format(consulta.getDataHoraFim());
            }
            dataFim.setText(dataFimStr);
            descricao.setText(consulta.getDescricao());
            paciente.setText(consulta.getIdPaciente().getNome());
            medico.setText(consulta.getIdMedico().getNome());
        }
    }
}
