import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class ApplicationProfil extends Panneau implements ActionListener {

    private JComboBox <String> comboBoxPersonnes;
    private JComboBox <String> comboBoxProfils;
    private JButton bAppliquer;

    ApplicationProfil(Hub frame){

        super(frame, "Allocation Profil Utilisateur");

        JLabel lPersonne = new JLabel("Personne : ");
        lPersonne.setSize(150,25);
        lPersonne.setLocation(this.frame.getWidth()/2-100, this.frame.getHeight()/2-50);
        this.add(lPersonne);

        JLabel lSite = new JLabel("Site : ");
        lSite.setSize(150,25);
        lSite.setLocation(this.frame.getWidth()/2-100, this.frame.getHeight()/2+20);
        this.add(lSite);

        this.bAppliquer = new JButton("Appliquer Profil");
        this.bAppliquer.setSize(150,50);
        this.bAppliquer.setLocation(this.frame.getWidth()/2-75, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bAppliquer.setBackground(Color.GREEN);
        this.bAppliquer.addActionListener(this);
        this.add(this.bAppliquer);

        this.comboBoxProfils= new JComboBox<>() ;
        this.comboBoxPersonnes =  new JComboBox<>();

        this.setComboBoxPersonnes();
        this.setComboBoxProfil();

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
        this.comboBoxPersonnes.setLocation(this.frame.getWidth()/2-30,this.frame.getHeight()/2-50);
        this.add(this.comboBoxPersonnes);
    }

    private void setComboBoxProfil() {

        this.comboBoxProfils = new JComboBox<>();

        try {
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT pseudo FROM Personne WHERE profil=true;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String pseudo = resultSet.getString("pseudo");
                this.comboBoxProfils.addItem(pseudo);
            }

            connection.close();

        } catch (SQLException e) {
            System.err.println(e);
        }

        this.comboBoxProfils.setSize(150,25);
        this.comboBoxProfils.setLocation(this.frame.getWidth()/2-30,this.frame.getHeight()/2+20);
        this.add(this.comboBoxProfils);
    }

    private String getPseudo(String personneSelectionnee){
        String [] data = personneSelectionnee.split(" ");
        String prenom = data[0];
        String nom = data[1];

        String pseudo = "";

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT pseudo FROM Personne WHERE nom=? and prenom=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,nom);
            preparedStatement.setString(2, prenom);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                pseudo=resultSet.getString("pseudo");
            }

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        return pseudo;
    }

    private LinkedList<Object []> recuperationInfoProfil(String profil){
        LinkedList <Object []> elementsProfil = new LinkedList<>() ;
        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT idPorte, statutAutorisation FROM Autorisation WHERE pseudo=? GROUP BY idPorte;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,profil);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Object[] data = new Object[2];
                data[0]= resultSet.getInt("idPorte");
                data[1]= resultSet.getBoolean("statutAutorisation");
                elementsProfil.add(data);
            }

            connection.close();

        }catch(SQLException e ){
            System.err.println(e);
        }

        return elementsProfil;
    }

    private void appliquerProfil(String personneSelectionee, String profilSelectionne){
        LinkedList<Object[]> elementsProfil = recuperationInfoProfil(profilSelectionne);
        String pseudo = getPseudo(personneSelectionee);

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query="UPDATE Autorisation SET statutAutorisation=? WHERE pseudo=? AND idPorte=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (Object [] data : elementsProfil) {
                preparedStatement.setBoolean(1,(boolean)data[1]);
                preparedStatement.setString(2,pseudo);
                preparedStatement.setInt(3,(int)data[0]);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Profil mis a jour avec succes","Application Profil", JOptionPane.INFORMATION_MESSAGE);

            connection.close();


        }catch (SQLException e){
            System.err.println(e);
        }

    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource()==this.bAppliquer){
            String personneSelectionnee = (String) this.comboBoxPersonnes.getSelectedItem();
            String profilSelectionne = (String) this.comboBoxProfils.getSelectedItem();

            int reponse = JOptionPane.showConfirmDialog(null, "Voulez-vous appliquer le profil "+profilSelectionne+" à "+personneSelectionnee+"?","Application Profil", JOptionPane.YES_NO_OPTION);

            if(reponse==0){

                appliquerProfil(personneSelectionnee, profilSelectionne);

            }else{
                JOptionPane.showMessageDialog(null, "Mise à jour du profil annulee", "Application Annulée", JOptionPane.INFORMATION_MESSAGE);
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
