package clinicamedica.paciente;

import clinicamedica.ClinicaMedicaMainFrame;
import clinicamedica.consulta.ConsultaList;
import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextField;

/**
 *
 * @author CristianeMarques
 */
public class PacienteList extends JInternalFrame {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    public PacienteList() {
        super("Paciente Listar", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        createPanel();
    }

    private final JButton incluirButton = new JButton("Incluir");
    private final JButton alterarButton = new JButton("Alterar");
    private final JButton excluirButton = new JButton("Excluir");
    private final JButton consultaButton = new JButton("Prontuário");

    private final PacienteJpaController controller = new PacienteJpaController(emf);
    private final List<Paciente> list = controller.findPacienteEntities();

    private final String[] columnNames = {
        "Nome",
        "Telefone"
    };

    private final PacienteTableModel tm = new PacienteTableModel(list, columnNames);

    private final JTable table = new JTable(tm);

    
    private void createPanel() {
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
       
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton)
                .addRelatedGap()
                .addButton(alterarButton)
                .addRelatedGap()
                .addButton(consultaButton)
                .addRelatedGap()
                .addButton(excluirButton);
        builder.append(bar.build(),7);
        
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
                    PacienteCreate frame = new PacienteCreate(new Paciente(), list, tm);
                    ClinicaMedicaMainFrame.getDesktop().add(frame);
                    frame.setSelected(true);
                } catch (Exception ex) {
                    Logger.getLogger(PacienteList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(incluirAction);
        
        ActionListener consultaAction = new ActionListener() {

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
                        ConsultaList frame = new ConsultaList(new ArrayList(list.get(row).getConsultaCollection()));
                        ClinicaMedicaMainFrame.getDesktop().add(frame);
                        frame.setSelected(true);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PacienteList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        consultaButton.addActionListener(consultaAction);
        
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
                        PacienteCreate frame = new PacienteCreate(list.get(row), list, tm);
                        ClinicaMedicaMainFrame.getDesktop().add(frame);
                        frame.setSelected(true);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PacienteList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        alterarButton.addActionListener(alterarAction);
        
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
                        PacienteJpaController pacienteDao = new PacienteJpaController(emf);
                        pacienteDao.destroy(list.get(row).getIdPaciente());
                        tm.removeRow(row);
                        list.remove(list.get(row));
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


    
