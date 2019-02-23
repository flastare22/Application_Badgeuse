import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGestionCarte extends Panneau implements ActionListener {

    private JButton bTableauCarte;
    private JButton activationDesactivationCarte;


    public MenuGestionCarte(Hub frame){

        super(frame, "");

        this.bTableauCarte =  new JButton("Tableau des Cartes");
        this.bTableauCarte.setSize(150,40);
        this.bTableauCarte.setLocation(this.frame.getWidth()/2-75, this.frame.getHeight()/2-100);
        this.bTableauCarte.addActionListener(this);
        this.add(this.bTableauCarte);

        this.activationDesactivationCarte = new JButton("Activation / Désactivation");
        this.activationDesactivationCarte.setSize(150,40);
        this.activationDesactivationCarte.setLocation(this.frame.getWidth()/2-75, this.frame.getHeight()/2+50);
        this.activationDesactivationCarte.addActionListener(this);
        this.add(this.activationDesactivationCarte);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bTableauCarte){
            new TableauCarte(this.frame);
        }

        if(e.getSource() == this.activationDesactivationCarte){
            new Activation(this.frame);
        }
        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }
    }


}
