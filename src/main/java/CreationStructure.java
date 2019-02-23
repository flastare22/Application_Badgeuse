import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreationStructure extends Panneau implements ActionListener {


    private JButton creationPorte;
    private JButton creationSite;
    private JButton creationBadgeuse;
    private JButton suppressionStructure;


    CreationStructure(Hub frame){

        super(frame, "Creation d'une nouvelle structure");

        this.creationSite = new JButton("Nouveau Site");
        this.creationSite.setSize(150,40);
        this.creationSite.setLocation(this.frame.getWidth()/2-75, this.frame.getWidth()/3-20);
        this.creationSite.setBackground(Color.lightGray);
        this.creationSite.addActionListener(this);
        this.add(this.creationSite);

        this.creationPorte = new JButton("Nouvelle Porte");
        this.creationPorte.setSize(150,40);
        this.creationPorte.setLocation(this.frame.getWidth()/2-75, this.frame.getWidth()/2-20);
        this.creationPorte.setBackground(Color.lightGray);
        this.creationPorte.addActionListener(this);
        this.add(this.creationPorte);

        this.creationBadgeuse = new JButton("Nouvelle Badgeuse");
        this.creationBadgeuse.setSize(150,40);
        this.creationBadgeuse.setLocation(this.frame.getWidth()/2-75, this.frame.getWidth()/3*2-20);
        this.creationBadgeuse.setBackground(Color.lightGray);
        this.creationBadgeuse.addActionListener(this);
        this.add(this.creationBadgeuse);

        this.suppressionStructure = new JButton("Suppression Structure");
        this.suppressionStructure.setSize(150,40);
        this.suppressionStructure.setLocation(this.frame.getWidth()/2-75, this.frame.getWidth()/3*2+(this.frame.getWidth()/2-this.frame.getWidth()/3)-20);
        this.suppressionStructure.setBackground(Color.red);
        this.suppressionStructure.addActionListener(this);
        this.add(this.suppressionStructure);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.creationSite){
            new CreationSite(this.frame);
        }

        if(e.getSource() == this.creationPorte){
            new CreationPorte(this.frame);
        }

        if(e.getSource() == this.creationBadgeuse){
            new CreationBadgeuse(this.frame);
        }

        if(e.getSource() == this.suppressionStructure){
            new SuppressionStructure(this.frame);
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }
    }
}
