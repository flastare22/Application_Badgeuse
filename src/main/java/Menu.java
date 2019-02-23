import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends Panneau implements ActionListener {

        private JButton creationCarte;
        private JButton activationCarte;
        private JButton suppressionCarte;
        private JButton autorisation;
        private JButton creationStructure;
        private JButton tableauBadgeuses;

        Menu(Hub frame){

            super(frame, "");

            JLabel logo=new JLabel(new ImageIcon(".\\src\\main\\java\\com\\fraw\\image\\Le-Helloco-logo.png"));
            logo.setSize(this.frame.getWidth()/2,this.frame.getHeight()/2);
            logo.setLocation(this.frame.getWidth()/2-this.frame.getWidth()/4,25);
            this.add(logo);

            setPseudo(this.frame.pseudoAdmin);

            this.creationCarte= new JButton("Creation Carte");
            this.creationCarte.setSize(150, 40);
            this.creationCarte.setLocation(this.frame.getWidth()/4-75, this.frame.getHeight()/2+20);
            this.creationCarte.setBackground(Color.lightGray);
            this.creationCarte.addActionListener(this);
            this.add(this.creationCarte);

            this.suppressionCarte= new JButton("Suppression Carte");
            this.suppressionCarte.setSize(150,40);
            this.suppressionCarte.setLocation(this.frame.getWidth()/4*3-75, this.frame.getHeight()/2+20);
            this.suppressionCarte.setBackground(Color.lightGray);
            this.suppressionCarte.addActionListener(this);
            this.add(this.suppressionCarte);

            this.activationCarte= new JButton("Gestion Cartes");
            this.activationCarte.setSize(150,40);
            this.activationCarte.setLocation(this.frame.getWidth()/4-75, this.frame.getHeight()/2+90);
            this.activationCarte.setBackground(Color.lightGray);
            this.activationCarte.addActionListener(this);
            this.add(this.activationCarte);

            this.autorisation= new JButton("Autorisations");
            this.autorisation.setSize(150,40);
            this.autorisation.setLocation(this.frame.getWidth()/4*3-75, this.frame.getHeight()/2+90);
            this.autorisation.setBackground(Color.lightGray);
            this.autorisation.addActionListener(this);
            this.add(this.autorisation);

            this.creationStructure = new JButton("Creation Infrastructure");
            this.creationStructure.setSize(150,40);
            this.creationStructure.setLocation(this.frame.getWidth()/4-75, this.frame.getHeight()/2+160);
            this.creationStructure.setBackground(Color.lightGray);
            this.creationStructure.addActionListener(this);
            this.add(this.creationStructure);

            this.tableauBadgeuses = new JButton("Tableau Badgeuses");
            this.tableauBadgeuses.setSize(150,40);
            this.tableauBadgeuses.setLocation(this.frame.getWidth()/4*3-75,this.frame.getHeight()/2+160);
            this.tableauBadgeuses.setBackground(Color.lightGray);
            this.tableauBadgeuses.addActionListener(this);
            this.add(this.tableauBadgeuses);



            this.frame.setContentPane(this);
            this.frame.repaint();
            this.frame.revalidate();
            this.frame.remove(this.retour);

        }

        public void setPseudo(String pseudo){

            JLabel lBienvenue= new JLabel("Bienvenu "+pseudo+"!", JLabel.CENTER);
            lBienvenue.setSize(500,90);
            lBienvenue.setLocation(0, 0);
            lBienvenue.setFont(new Font("Bahnschrift", Font.BOLD,32));
            this.add(lBienvenue);
        }

        public void actionPerformed(ActionEvent e){

            if(e.getSource() == this.creationCarte){
                new CreationCarte(this.frame);
            }

            if(e.getSource() == this.suppressionCarte){
                new SuppressionCarte(this.frame);
            }

            if(e.getSource() == this.autorisation){
                new ConfigurationAutorisation(this.frame);
            }

            if(e.getSource() == this.creationStructure){
                new CreationStructure(this.frame);
            }

            if(e.getSource() == this.activationCarte){
                new MenuGestionCarte(this.frame);
            }

            if(e.getSource() == this.tableauBadgeuses){
                new TableauBadgeuse(this.frame);
            }

            if (e.getSource() == this.deconnection){

                new Identification(this.frame);

            }

        }

    }


