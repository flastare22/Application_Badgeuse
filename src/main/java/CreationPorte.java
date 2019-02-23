import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class CreationPorte extends Panneau implements ActionListener {

    private JComboBox <String> comboBoxPortes;
    private JTextField nomPorte;
    private JButton bValider;


    CreationPorte (Hub frame){

        super(frame, "Creation d'une nouvelle Porte");

        JLabel nom = new JLabel("Nom de la porte* :");
        nom.setSize(100,25);
        nom.setLocation(50, frame.getHeight()/2+25);
        this.add(nom);

        this.nomPorte = new JTextField();
        this.nomPorte.setLocation(220, frame.getHeight()/2+25);
        this.nomPorte.setSize(100,25);
        this.add(nomPorte);

        JLabel nomSite = new JLabel("Localisation* :");
        nomSite.setSize(100,25);
        nomSite.setLocation(50, frame.getHeight()/2-50);
        this.add(nomSite);

        this.bValider = new JButton("Compris");
        this.bValider.setSize(100,50);
        this.bValider.setLocation(frame.getWidth()/2-60, frame.getHeight()/3*2+frame.getHeight()/6-30);
        this.bValider.setBackground(Color.GREEN);
        this.bValider.addActionListener(this);
        this.add(this.bValider);

        getSites();


        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }


    private void getSites(){

        this.comboBoxPortes = new JComboBox<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT nomSite, codeSite FROM Site GROUP BY codeSite;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                this.comboBoxPortes.addItem(resultSet.getString("nomSite")+" "+resultSet.getString("codeSite"));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println();
        }

        this.comboBoxPortes.setSize(150, 25);
        this.comboBoxPortes.setLocation(220, this.frame.getHeight()/2-50);
        this.add(this.comboBoxPortes);
    }


    private void creationPorte(){

        if(!this.nomPorte.getText().equals("") && !this.comboBoxPortes.getSelectedItem().equals("")){

            String siteSelectionne = (String) this.comboBoxPortes.getSelectedItem();
            String data [] = siteSelectionne.split(" ");
            String codeSite = data[1];

            try{

                Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                String query = "INSERT INTO Porte (nomPorte, codeSite) VALUES (?,?);";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, this.nomPorte.getText());
                preparedStatement.setString(2, codeSite);
                preparedStatement.executeUpdate();

                updateAutorisations(nomPorte.getText());

                connection.close();

            }catch (SQLException e){
                System.err.println();
            }

            this.retour.doClick();

            JOptionPane.showMessageDialog(null, "La porte "+this.nomPorte.getText()+" a bien ete creee", "Creation reussie", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Verifiez que l'ensemble des champs sont bien renseignees","Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }


    private void updateAutorisations(String nomPorte){

        LinkedList <String> personnes = new LinkedList<>();
        int idPorte=0;

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query ="SELECT pseudo FROM Personne;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                personnes.add(resultSet.getString("pseudo"));
            }

            query="SELECT idPorte FROM Porte WHERE nomPorte=?;";

            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, nomPorte);
            resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                idPorte=resultSet.getInt("idPorte");
            }

            query="INSERT INTO Autorisation (pseudo, idPorte, localisation) VALUES (?,?,?);";

            for( String personne: personnes){

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, personne);
                preparedStatement.setInt(2, idPorte);
                preparedStatement.setString(3, nomPorte);
                preparedStatement.executeUpdate();
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }
    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bValider){
            creationPorte();
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new CreationStructure(this.frame);
        }

    }

}
