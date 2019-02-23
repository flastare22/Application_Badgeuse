import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuProfils extends Panneau implements ActionListener {

    private JButton nouveauProfil;
    private JButton modificationProfils;

    MenuProfils(Hub frame){

        super(frame, "Menu de configuration des Profils");

        this.nouveauProfil = new JButton("Nouveau Profil");
        this.nouveauProfil.setSize(150,40);
        this.nouveauProfil.setLocation(this.frame.getWidth()/2-75, this.frame.getHeight()/3+25);
        this.nouveauProfil.addActionListener(this);
        this.add(this.nouveauProfil);

        this.modificationProfils = new JButton("Modifier Profil Existant");
        this.modificationProfils.setSize(150,40);
        this.modificationProfils.setLocation(this.frame.getWidth()/2-75,this.frame.getHeight()/3*2-25);
        this.modificationProfils.addActionListener(this);
        this.add(this.modificationProfils);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    private void setNouveauProfil(){

        try {
            String reponse = JOptionPane.showInputDialog(null, "Entrez le nom du nouveau profil", "Nouveau Profil", JOptionPane.OK_CANCEL_OPTION);
            if (!reponse.equals("")) {
                if(CreationProfil.creerPersonne(reponse, this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin)) {
                    new CreationProfil(this.frame, reponse);
                    System.out.println("Prout");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Creation annulee", "Creation annulee", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(null, "Creation annulee", "Creation annulee", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.nouveauProfil){
            setNouveauProfil();
        }

        if(e.getSource() == this.modificationProfils){
            new RechercheProfil(this.frame);
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new ConfigurationAutorisation(this.frame);

        }
    }


}
