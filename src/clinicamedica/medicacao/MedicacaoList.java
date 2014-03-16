package clinicamedica.medicacao;

import clinicamedica.medico.MedicoCreate;
import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class MedicacaoList extends JInternalFrame {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");

     
    public MedicacaoList() {
        super("Medicação Listar", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        createPanel();
    }
     
    private final JTextField nomeField = new JTextField();
    private final JButton incluirButton = new JButton("Incluir");
    private final JButton excluirButton = new JButton("Excluir");
    
    private void createPanel() {
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.appendSeparator("Medicação");
        builder.append("Nome", nomeField, 5);
        builder.nextLine();
       
        //builder.append(new JButton("Incluir"));
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton)
                .addRelatedGap()
                .addButton(excluirButton);
        builder.append(bar.build(),7);
        
        MedicacaoJpaController controller = new MedicacaoJpaController(emf);
        final List<Medicacao> list = controller.findMedicacaoEntities();
        
        final String[] columnNames = {
            "Código",
            "Medicação"
        };
             
        final MedicacaoTableModel tm = new MedicacaoTableModel(list, columnNames);
        
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
                    MedicacaoJpaController medicacaoDao = new MedicacaoJpaController(emf);
                    Medicacao r = new Medicacao();
                    r.setNome(nomeField.getText());
                    medicacaoDao.create(r);
                    list.add(r);
                    //tm.fireTableRowsInserted(i, i1);
                    tm.fireTableDataChanged();
                } catch (Exception ex) {
                    Logger.getLogger(MedicoCreate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(incluirAction);
        
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
                        MedicacaoJpaController medicacaoDao = new MedicacaoJpaController(emf);
                        medicacaoDao.destroy(list.get(row).getIdMedicacao());
                        list.remove(row);
                        tm.fireTableRowsDeleted(row, row);                        
                        //tm.removeRow(row);
                        //table.revalidate();
                    } catch (IllegalOrphanException | NonexistentEntityException ex) {
                        Logger.getLogger(MedicacaoList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        this.add(builder.build());
        this.pack();
        this.setVisible(true);
    }
}

