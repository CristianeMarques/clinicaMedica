package clinicamedica.medico;

import clinicamedica.ClinicaMedicaMainFrame;
import clinicamedica.consulta.ConsultaList;
import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import clinicamedica.paciente.PacienteList;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author CristianeMarques
 */
public class MedicoList extends JInternalFrame {
 
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    public MedicoList() {
        super("Médico Listar", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        createPanel();
    }
    
    private final JButton incluirButton = new JButton("Incluir");
    private final JButton alterarButton = new JButton("Alterar");
    private final JButton excluirButton = new JButton("Excluir");
    private final JButton agendaButton = new JButton("Ver agenda");
    
    private void createPanel() {
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendSeparator("Medico");
        builder.nextLine();
       
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton)
                .addRelatedGap()
                .addButton(alterarButton)
                .addRelatedGap()
                .addButton(agendaButton)
                .addRelatedGap()
                .addButton(excluirButton);
        builder.append(bar.build(),7);
        
        MedicoJpaController controller = new MedicoJpaController(emf);
        final List<Medico> list = controller.findMedicoEntities();
        
        final String[] columnNames = {
            "CRM",
            "Nome"
        };
             
        final MedicoTableModel tm = new MedicoTableModel(list, columnNames);
        
        final JTable table = new JTable(tm);
        table.doLayout();
        
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        builder.nextLine();
        builder.append(scrollPane, 7);
        
        ActionListener incluirAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                 try {
                    MedicoCreate frame = new MedicoCreate(new Medico(), list, tm);
                    ClinicaMedicaMainFrame.getDesktop().add(frame);
                    frame.setSelected(true);
                } catch (Exception ex) {
                    Logger.getLogger(MedicoList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(incluirAction);
        
        ActionListener alterarAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    // get the current selected row
                    int row = table.getSelectedRow();
                    // if there's no selection, but there are some rows,
                    // we'll just delete the first row
                    if(row < 0 && tm.getRowCount() > 0) {
                       row = 0;
                    }

                    // if we have a valid row to delete, do the deletion
                    if(row >= 0) {
                        Medico medico = list.get(row);
                        MedicoCreate frame = new MedicoCreate(medico, list, tm);
                        ClinicaMedicaMainFrame.getDesktop().add(frame);
                        frame.setSelected(true);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MedicoList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        alterarButton.addActionListener(alterarAction);
        
        agendaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // get the current selected row
                int row = table.getSelectedRow();
                // if there's no selection, but there are some rows,
                // we'll just delete the first row
                if(row < 0 && tm.getRowCount() > 0) {
                   row = 0;
                }

                // if we have a valid row to delete, do the deletion
                if(row >= 0) {
                    try {
                        Medico medico = list.get(row);
                        ConsultaList frame = new ConsultaList(new ArrayList(medico.getConsultaCollection()));
                        ClinicaMedicaMainFrame.getDesktop().add(frame);
                        frame.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(MedicoList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // get the current selected row
                int row = table.getSelectedRow();
                // if there's no selection, but there are some rows,
                // we'll just delete the first row
                if(row < 0 && tm.getRowCount() > 0) {
                   row = 0;
                }

                // if we have a valid row to delete, do the deletion
                if(row >= 0) {
                    try {
                        MedicoJpaController medicoDao = new MedicoJpaController(emf);
                        medicoDao.destroy(list.get(row).getIdMedico());
                        tm.removeRow(row);
                        //table.revalidate();
                    } catch (IllegalOrphanException | NonexistentEntityException ex) {
                        Logger.getLogger(PacienteList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        this.add(builder.build());
        this.pack();
        this.setVisible(true);
    }
}
