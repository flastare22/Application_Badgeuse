import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class RecherchePersonne extends Panneau {

    private JComboBox <String> comboBoxPersonnes;
    private JButton bContinuer;

    public RecherchePersonne(Hub frame){

        super(frame, "Modification Manuelle Autorisations");


        JLabel choixProfil= new JLabel("Personne :");
        choixProfil.setSize(150,25);
        choixProfil.setLocation(this.frame.getWidth()/2-150, this.frame.getWidth()/2);
        this.add(choixProfil);

        this.bContinuer = new JButton("Modifier");
        this.bContinuer.setSize(100,50);
        this.bContinuer.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bContinuer.setBackground(Color.GREEN);
        this.bContinuer.addActionListener(this);
        this.add(this.bContinuer);

        this.comboBoxPersonnes=new JComboBox();

        setComboBoxPersonnes();

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }


    private void setComboBoxPersonnes() {

        this.comboBoxPersonnes = new JComboBox<>();

        try {
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT nom, prenom FROM Personne WHERE profil=false ORDER BY prenom;";

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
        this.comboBoxPersonnes.setLocation(this.frame.getWidth()/2-30,this.frame.getWidth()/2);
        this.add(this.comboBoxPersonnes);
    }

    public void action(){

        String nomPrenom = (String) this.comboBoxPersonnes.getSelectedItem();
        String data [] = nomPrenom.split(" ");
        String pseudo ="";

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT pseudo FROM Personne WHERE prenom=? AND nom=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,data[0]);
            preparedStatement.setString(2, data[1]);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                pseudo=resultSet.getString("pseudo");
            }

            connection.close();

            new ModifierProfil(this.frame, pseudo,2);
        }catch (SQLException e){
            System.err.println(e);
        }

    }

    public void actionPerformed (ActionEvent e){

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new MenuAutorisation(this.frame);

        }

        if(e.getSource() == this.bContinuer){
            action();
        }
    }


}