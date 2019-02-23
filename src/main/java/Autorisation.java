import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Autorisation extends Panneau implements ActionListener {

    private JComboBox<String> comboBoxPersonnes;
    private JButton bContinuer;

    public Autorisation(Hub frame){

        super(frame, "Panneau de Gestion des Autorisations");

        JLabel lInfo = new JLabel("Selectionnez la personne dont vous souhaitez modifier les autorisations:", JLabel.CENTER);
        lInfo.setSize(500,25);
        lInfo.setLocation(125,0);
        this.add(lInfo);

        this.bContinuer = new JButton("Continuer");
        this.bContinuer.setSize(100,50);
        this.bContinuer.setLocation(200,300);
        this.bContinuer.setBackground(Color.green);
        this.bContinuer.addActionListener(this);
        this.add(this.bContinuer);

        comboBoxPersonne();

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    private void comboBoxPersonne() {

        this.comboBoxPersonnes = new JComboBox<>();

        try {
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT nom, prenom FROM Personne ORDER BY prenom;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nomPrenom = resultSet.getString("prenom") + " " + resultSet.getString("nom");
                this.comboBoxPersonnes.addItem(nomPrenom);
            }

            connection.close();

        } catch (SQLException e) {
            System.err.println(e);
        }

        this.comboBoxPersonnes.setSize(150,25);
        this.comboBoxPersonnes.setLocation(175,225);
        this.add(this.comboBoxPersonnes);
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()== this.bContinuer){
            String prenomNom= (String)this.comboBoxPersonnes.getSelectedItem();
            String data [] = prenomNom.split(" ");
            String prenom = data[0];
            String nom = data[1];
            String pseudo = "";

            try{
                Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

                String query="SELECT pseudo FROM Personne where nom=? and prenom=?;";

                PreparedStatement preparedStatement= connection.prepareStatement(query);
                preparedStatement.setString(1,nom);
                preparedStatement.setString(2,prenom);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    pseudo=resultSet.getString("pseudo");
                }

                connection.close();
            }catch (SQLException o){
                System.err.println(o);
            }
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new MenuAutorisation(this.frame);

        }
    }
}