package clinicamedica;

import clinicamedica.consulta.ConsultaJpaController;
import clinicamedica.consulta.ConsultaList;
import clinicamedica.medicacao.MedicacaoList;
import clinicamedica.medico.MedicoList;
import clinicamedica.paciente.PacienteList;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.awt.*;

/*
 * InternalFrameDemo.java requires:
 *   MyInternalFrame.java
 */
public final class ClinicaMedicaMainFrame extends JFrame
                               implements ActionListener {
    private static final JDesktopPane desktop = new JDesktopPane();

    public static JDesktopPane getDesktop() {
        return desktop;
    }
    
    
    public ClinicaMedicaMainFrame() {
        super("Clinica Médica");

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);

        //Set up the GUI.
         //a specialized layered pane
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        JMenuItem menuItem;
        menuItem = new JMenuItem("Médico");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_M, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("medico-list");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Paciente");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("paciente-list");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Medicação");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("medicacao-list");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Consulta");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("consulta-list");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    //React to menu selections.
    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame frame = null;
        switch (e.getActionCommand()) {
            case "medico-list":
                frame = new MedicoList();
                break;
            case "paciente-list":
                frame = new PacienteList();
                break;
            case "medicacao-list":
                frame = new MedicacaoList();
                break;
            case "consulta-list":
                frame = new ConsultaList(null);
                break;
            default:
                quit();
                break;
        }
        frame.setVisible(true); //necessary as of 1.3
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ex) {}
    }

    //Quit the application.
    protected void quit() {
        System.exit(0);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        ClinicaMedicaMainFrame frame = new ClinicaMedicaMainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}