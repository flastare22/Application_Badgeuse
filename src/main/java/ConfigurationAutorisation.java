import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationAutorisation extends Panneau implements ActionListener {

    private JButton modifierProfil;
    private JButton modifierAutorisation;

    public ConfigurationAutorisation(Hub frame){

        super(frame, "Menu de configuration des Autorisations");

        this.modifierProfil = new JButton("Creer / Modifier Profils");
        this.modifierProfil.setSize(150,40);
        this.modifierProfil.setLocation(frame.getWidth()/2-75, frame.getHeight()/3+25);
        this.modifierProfil.addActionListener(this);
        this.add(this.modifierProfil);

        this.modifierAutorisation = new JButton("Autorisations Utilisateur");
        this.modifierAutorisation.setSize(155, 40);
        this.modifierAutorisation.setLocation(frame.getWidth()/2-78, frame.getHeight()/3*2-25);
        this.modifierAutorisation.addActionListener(this);
        this.add(this.modifierAutorisation);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.modifierProfil){
            new MenuProfils(this.frame);
        }

        if(e.getSource() == this.modifierAutorisation){
            new MenuAutorisation(this.frame);
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }

    }
}
