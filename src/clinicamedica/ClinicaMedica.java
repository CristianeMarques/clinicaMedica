/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clinicamedica;

import clinicamedica.paciente.PacienteJpaController;
import clinicamedica.controller.exceptions.IllegalOrphanException;
import clinicamedica.controller.exceptions.NonexistentEntityException;
import clinicamedica.paciente.Paciente;
import clinicamedica.util.Util;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author CristianeMarques
 */
public class ClinicaMedica {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaMedicaPU");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClinicaMedica cm = new ClinicaMedica();
        //cm.createPacientes();
        //cm.updatePacientes();
        //cm.deletePacientes();
        
        cm.createPanel();
    }

    private void createPacientes() {
        try {
            PacienteJpaController pacienteDao = new PacienteJpaController(emf);
            Paciente p = new Paciente();
            p.setCpf("12345678901");
            p.setNome("Fulano da Silva");
            p.setDataNascimento(Util.SDF.parse("01/04/1920"));
            p.setEndereco("Rua dos Canarios, 1290");
            p.setIdade(93);
            p.setTelefone("555112345678");
            pacienteDao.create(p);
        } catch (ParseException ex) {
            Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updatePacientes() {
        try {
            PacienteJpaController pacienteDao = new PacienteJpaController(emf);
            Paciente p = pacienteDao.findPacienteByCpf("12345678901");
            p.setCpf("99999999999");
            p.setNome("Fulano de Tal");
            pacienteDao.edit(p);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deletePacientes() {
        try {
            PacienteJpaController pacienteDao = new PacienteJpaController(emf);
            Paciente p = pacienteDao.findPacienteByCpf("99999999999");
            pacienteDao.destroy(p.getIdPaciente());
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final JTextField idField = new JTextField();
    private final JTextField cpfField = new JTextField();
    private final JTextField nomeField = new JTextField();
    private final JTextField dataNascimentoField = new JTextField();
    private final JTextField enderecoField = new JTextField();
    private final JTextField idadeField = new JTextField();
    private final JTextField telefoneField = new JTextField();
    private final JButton incluirButton = new JButton("Incluir");
    private final JButton cancelarButton = new JButton("Cancelar");
    
    private void createPanel() {
        FormLayout layout = new FormLayout(
                "right:max(30dlu;p), 4dlu, 80dlu, 7dlu, " // 1st major column
                + "right:max(30dlu;p), 4dlu, 80dlu", // 2nd major column
            ""); // add rows dynamically
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.appendSeparator("Paciente");
        builder.append("Código", idField);
        builder.nextLine();
        builder.append("CPF", cpfField);
        builder.nextLine();
        builder.append("Nome", nomeField, 5);
        builder.nextLine();
        builder.append("Endereço", enderecoField, 5);
        builder.nextLine();
        builder.append("Dt Nasc", dataNascimentoField);
        builder.append("Idade", idadeField);
        builder.nextLine();
        builder.append("Telefone", telefoneField);
        builder.nextLine();

        //builder.append(new JButton("Incluir"));
        ButtonBarBuilder bar = new ButtonBarBuilder()
                .addButton(incluirButton)
                .addRelatedGap()
                .addButton(cancelarButton);
        builder.append(bar.build());
        
        createIncluirActionListener();
        
        JFrame frame = new JFrame();
        frame.add(builder.build());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void createIncluirActionListener() {
        ActionListener action = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                 try {
                    PacienteJpaController pacienteDao = new PacienteJpaController(emf);
                    Paciente p = new Paciente();
                    p.setCpf(cpfField.getText());
                    p.setNome(nomeField.getText());
                    p.setDataNascimento(Util.SDF.parse(dataNascimentoField.getText()));
                    p.setEndereco(enderecoField.getText());
                    p.setIdade(Integer.parseInt(idadeField.getText()));
                    p.setTelefone(telefoneField.getText());
                    pacienteDao.create(p);
                } catch (ParseException ex) {
                    Logger.getLogger(ClinicaMedica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        incluirButton.addActionListener(action);
    }
}
