import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAutorisation extends Panneau implements ActionListener {

    private JButton associerProfil;
    private JButton modificationManuelle;


    MenuAutorisation(Hub frame){

        super(frame, "Menu de configuration des Autorisations");

        this.associerProfil = new JButton("Appliquer un profil");
        this.associerProfil.setSize(150,40);
        this.associerProfil.setLocation(this.frame.getWidth()/2-75, this.frame.getHeight()/3+25);
        this.associerProfil.addActionListener(this);
        this.add(this.associerProfil);

        this.modificationManuelle = new JButton("Modification Profils");
        this.modificationManuelle.setSize(150,40);
        this.modificationManuelle.setLocation(this.frame.getWidth()/2-75,this.frame.getHeight()/3*2-25);
        this.modificationManuelle.addActionListener(this);
        this.add(this.modificationManuelle);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }

    public void actionPerformed(ActionEvent e){


        if(e.getSource() == this.associerProfil){
            new ApplicationProfil(this.frame);
        }

        if(e.getSource() == this.modificationManuelle){
            new RecherchePersonne(this.frame);
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new ConfigurationAutorisation(this.frame);

        }

    }

}
