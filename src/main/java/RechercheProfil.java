import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RechercheProfil extends Panneau implements ActionListener {

    private JComboBox cProfil;
    private JButton continuer;
    private JButton supprimerProfil;

    public RechercheProfil(Hub frame){

        super(frame, "Modification/Suppression d'un profil");

        JLabel choixProfil= new JLabel("Profil a modifier :");
        choixProfil.setSize(150,25);
        choixProfil.setLocation(this.frame.getWidth()/2-150, this.frame.getHeight()/2);
        this.add(choixProfil);

        this.continuer = new JButton("Modifier");
        this.continuer.setSize(100,50);
        this.continuer.setLocation(this.frame.getWidth()/4-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.continuer.setBackground(Color.GREEN);
        this.continuer.addActionListener(this);
        this.add(this.continuer);

        this.supprimerProfil = new JButton("Supprimer");
        this.supprimerProfil.setSize(100,50);
        this.supprimerProfil.setLocation(this.frame.getWidth()/4*3-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.supprimerProfil.setBackground(Color.RED);
        this.supprimerProfil.addActionListener(this);
        this.add(this.supprimerProfil);

        this.cProfil=new JComboBox();

        getProfil();

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }

    public void getProfil(){

        this.remove(this.cProfil);
        this.cProfil = new JComboBox();
        this.cProfil.setSize(150,25);
        this.cProfil.setLocation(this.frame.getWidth()/2+10, this.frame.getHeight()/2);

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT pseudo FROM Personne WHERE profil=true;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                this.cProfil.addItem(resultSet.getString("pseudo"));
            }
        }catch (SQLException e){
            System.err.println(e);
        }

        this.add(this.cProfil);
        this.revalidate();
        this.repaint();
    }

    public void update(){
        getProfil();
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==this.supprimerProfil){

            String profil = (String) this.cProfil.getSelectedItem();

            if(profil!=null) {

                int resultat = JOptionPane.showConfirmDialog(null, "Etes-vous sur de vouloir supprimer le profil " + profil + " ?", "Suppression Profil", JOptionPane.YES_NO_OPTION);
                if (resultat == 0) {
                    try {
                        Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                        String query = "DELETE FROM Autorisation where pseudo=?;";

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, (String) this.cProfil.getSelectedItem());
                        preparedStatement.executeUpdate();

                        query = "DELETE FROM Personne where pseudo = ?;";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, (String) this.cProfil.getSelectedItem());
                        preparedStatement.executeUpdate();

                        this.update();

                        JOptionPane.showMessageDialog(null, "Le profil a bien ete supprime!", "Suppression reussie", JOptionPane.INFORMATION_MESSAGE);

                    } catch (SQLException o) {
                        System.err.println(o);
                    }
                }
            }
        }

        if(e.getSource() == this.continuer){

           new ModifierProfil(this.frame, (String) this.cProfil.getSelectedItem(), 1);

        }


        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new MenuProfils(this.frame);

        }
    }
}
