package clinicamedica.medico;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;

/**
 *
 * @author CristianeMarques
 */
public class MedicoCreate extends JInternalFrame {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    private final List<Medico> list;
    private final MedicoTableModel tm;
    private final Medico medico;
    
    private final JTextField crmField = new JTextField();
    private final JTextField nomeField = new JTextField();
    private final JButton incluirButton = new JButton("Incluir");
    private final JButton cancelarButton = new JButton("Cancelar");
    
    /**
     *
     * @param medico
     * @param list
     * @param tm
     */
    public MedicoCreate(Medico medico, List<Medico> list, MedicoTableModel tm) {
        super("Médico Criar", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        this.medico = medico;
        this.list = list;
        this.tm = tm;
        createPanel();
    }
    
    private void createPanel() {
        nomeField.setText(medico.getNome());
        crmField.setText(medico.getCrm()+"");
        
        if (medico.getIdMedico() == 0) {
            incluirButton.setText("Incluir");
        } else {
            incluirButton.setText("Alterar");
        }
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.appendSeparator("Médico");
        builder.append("CRM", crmField);
        builder.nextLine();
        builder.append("Nome", nomeField, 5);
        builder.nextLine();
       
        //builder.append(new JButton("Incluir"));
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton)
                .addRelatedGap()
                .addButton(cancelarButton);
        builder.append(bar.build());
        
        createIncluirActionListener();
        
        this.add(builder.build());
        this.pack();
        this.setVisible(true);
    }

    private void createIncluirActionListener() {
        ActionListener action = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    MedicoJpaController medicoDao = new MedicoJpaController(emf);
                    medico.setCrm(Integer.parseInt(crmField.getText()));
                    medico.setNome(nomeField.getText());
                    if (medico.getIdMedico() == 0) {
                        medicoDao.create(medico);
                        list.add(medico);
                    } else {
                        medicoDao.edit(medico);
                    }
                    tm.fireTableDataChanged();
                } catch (Exception ex) {
                    Logger.getLogger(MedicoCreate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(action);
    }
}


