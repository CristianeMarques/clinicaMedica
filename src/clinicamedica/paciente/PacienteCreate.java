package clinicamedica.paciente;

import clinicamedica.ClinicaMedica;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import clinicamedica.util.Util;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
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
public class PacienteCreate extends JInternalFrame {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    private final JTextField cpfField = new JTextField();
    private final JTextField nomeField = new JTextField();
    private final JTextField dataNascimentoField = new JTextField();
    private final JTextField enderecoField = new JTextField();
    private final JTextField telefoneField = new JTextField();
    private final JButton incluirButton = new JButton("Incluir");

    private final List<Paciente> list;
    private final PacienteTableModel tm;
    private final Paciente paciente;

    public PacienteCreate(Paciente paciente, List<Paciente> list, PacienteTableModel tm) {
        super("Paciente Criar/Alterar",
                true,
                true,
                true,
                true);
        this.list = list;
        this.tm = tm;
        this.paciente = paciente;
        if (paciente.getIdPaciente() == 0) {
            incluirButton.setText("Incluir");
        } else {
            incluirButton.setText("Alterar");
        }

        cpfField.setText(paciente.getCpf());
        nomeField.setText(paciente.getNome());
        enderecoField.setText(paciente.getEndereco());
        telefoneField.setText(paciente.getTelefone());
        if (paciente.getDataNascimento() != null) {
            dataNascimentoField.setText(Util.SDF.format(paciente.getDataNascimento()));
        }
        
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.appendSeparator("Paciente");
        builder.append("CPF", cpfField);
        builder.nextLine();
        builder.append("Nome", nomeField, 5);
        builder.nextLine();
        builder.append("Endereço", enderecoField, 5);
        builder.nextLine();
        builder.append("Dt Nasc", dataNascimentoField);
        builder.nextLine();
        builder.append("Telefone", telefoneField);
        builder.nextLine();

        //builder.append(new JButton("Incluir"));
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton);
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
                    PacienteJpaController dao = new PacienteJpaController(emf);
                    paciente.setCpf(cpfField.getText());
                    paciente.setNome(nomeField.getText());
                    paciente.setDataNascimento(Util.SDF.parse(dataNascimentoField.getText()));
                    paciente.setEndereco(enderecoField.getText());
                    paciente.setTelefone(telefoneField.getText());
                    if (paciente.getIdPaciente() == 0) {
                        dao.create(paciente);
                        list.add(paciente);
                    } else {
                        dao.edit(paciente);
                    }
                    tm.fireTableDataChanged();
                } catch (ParseException ex) {
                    Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(PacienteCreate.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(PacienteCreate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(action);
    }

}
