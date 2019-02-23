import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class SuppressionStructure extends Panneau implements ActionListener {

    private JLabel lSite;
    private JLabel lPorte;
    private JLabel lBadgeuse;
    private JComboBox <String> comboBoxSites;
    private JComboBox <String> comboBoxPortes;
    private JComboBox <String> comboBoxBadgeuses;
    private JComboBox <String> comboBoxSuppression;
    private int choix;
    private JButton bSupprimer;

    public SuppressionStructure(Hub frame){

        super(frame, "Suppression d'une structure");

        this.lSite= new JLabel("Site :", JLabel.CENTER);
        this.lSite.setSize(150,25);
        this.lSite.setLocation(this.frame.getWidth()/4-50, this.frame.getHeight()/2+20);

        this.lPorte = new JLabel("Porte :", JLabel.CENTER);
        this.lPorte.setSize(150,25);
        this.lPorte.setLocation(this.frame.getWidth()/4-50, this.frame.getHeight()/2+20);

        this.lBadgeuse = new JLabel("Badgeuse :", JLabel.CENTER);
        this.lBadgeuse.setSize(150,25);
        this.lBadgeuse.setLocation(this.frame.getWidth()/4-50, this.frame.getHeight()/2+20);

        this.comboBoxBadgeuses=new JComboBox<>();
        this.comboBoxPortes=new JComboBox<>();
        this.comboBoxSites= new JComboBox<>();

        this.bSupprimer = new JButton("Supprimer");
        this.bSupprimer.setSize(100,50);
        this.bSupprimer.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bSupprimer.setBackground(Color.red);
        this.bSupprimer.addActionListener(this);
        this.add(this.bSupprimer);

        JLabel lSupression = new JLabel("Structure à supprimer :", JLabel.CENTER);
        lSupression.setSize(150,25);
        lSupression.setLocation(this.frame.getWidth()/4-50, this.frame.getHeight()/3);
        this.add(lSupression);

        this.comboBoxSuppression = new JComboBox<>();
        this.comboBoxSuppression.setSize(150,25);
        this.comboBoxSuppression.setLocation(this.frame.getWidth()/2, this.frame.getHeight()/3);
        this.comboBoxSuppression.addItem("Site");
        this.comboBoxSuppression.addItem("Porte");
        this.comboBoxSuppression.addItem("Badgeuse");
        this.comboBoxSuppression.addActionListener(this);
        this.add(comboBoxSuppression);

        setComboBoxSites();
        this.choix=1;

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }

    private void setComboBoxSites(){

        this.remove(this.lBadgeuse);
        this.remove(this.comboBoxBadgeuses);
        this.remove(this.comboBoxPortes);
        this.remove(this.lPorte);
        this.remove(this.lSite);
        this.remove(this.comboBoxSites);

        this.comboBoxSites= new JComboBox<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query ="SELECT nomSite FROM Site;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                this.comboBoxSites.addItem(resultSet.getString("nomSite"));
            }

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        this.comboBoxSites.setSize(150,25);
        this.comboBoxSites.setLocation(this.frame.getWidth()/2, this.frame.getHeight()/2+20);
        this.add(this.comboBoxSites);
        this.add(this.lSite);
        this.repaint();

    }

    private void setComboBoxPortes(){
        this.remove(this.lBadgeuse);
        this.remove(this.comboBoxBadgeuses);
        this.remove(this.comboBoxPortes);
        this.remove(this.lPorte);
        this.remove(this.lSite);
        this.remove(this.comboBoxSites);

        this.comboBoxPortes= new JComboBox<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query ="SELECT nomPorte FROM Porte;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                this.comboBoxPortes.addItem(resultSet.getString("nomPorte"));
            }

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        this.comboBoxPortes.setSize(150,25);
        this.comboBoxPortes.setLocation(this.frame.getWidth()/2, this.frame.getHeight()/2+20);
        this.add(this.comboBoxPortes);
        this.add(this.lPorte);
        this.repaint();
    }

    private void setComboBoxBadgeuses(){
        this.remove(this.lBadgeuse);
        this.remove(this.comboBoxBadgeuses);
        this.remove(this.comboBoxPortes);
        this.remove(this.lPorte);
        this.remove(this.lSite);
        this.remove(this.comboBoxSites);

        this.comboBoxBadgeuses= new JComboBox<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query ="SELECT idBadgeuse,  FROM Badgeuse ORDER BY idBadgeuse;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                this.comboBoxBadgeuses.addItem(resultSet.getString("idBadgeuse"));
            }

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        this.comboBoxBadgeuses.setSize(150,25);
        this.comboBoxBadgeuses.setLocation(this.frame.getWidth()/2, this.frame.getHeight()/2+20);
        this.add(this.comboBoxBadgeuses);
        this.add(this.lBadgeuse);
        this.repaint();
    }

    private void supprimerSite(){
        String site = (String) this.comboBoxSites.getSelectedItem();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);


            String query="SELECT codeSite FROM Site where nomSite=?;";

            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, site);
            ResultSet resultSet = preparedStatement.executeQuery();

            String codeSite="";


            while(resultSet.next()){
                codeSite=resultSet.getString("codeSite");
            }

            LinkedList <Integer> listePorte = new LinkedList<>();

            query="SELECT idPorte FROM Porte where codeSite=?;";

            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1, codeSite);
            resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                listePorte.add(resultSet.getInt("idPorte"));
            }



            for(int porte : listePorte){
                query="DELETE FROM Badgeuse WHERE idPorte=?;";

                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setInt(1, porte);
                preparedStatement.executeUpdate();

                query="DELETE FROM Autorisation WHERE idPorte=?;";

                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setInt(1, porte);
                preparedStatement.executeUpdate();

                query = "DELETE FROM Porte where idPorte=?;";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, porte);
                preparedStatement.executeUpdate();
            }

            query = "DELETE FROM Site where codeSite=?;";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,codeSite);
            preparedStatement.executeUpdate();

            connection.close();
            JOptionPane.showMessageDialog(null, "Suppression reussite", "Suppression reussite", JOptionPane.INFORMATION_MESSAGE);
        }catch (SQLException e){
            System.err.println(e);
        }
    }

    private void supprimerPorte(){
        String porte = (String) this.comboBoxPortes.getSelectedItem();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query="SELECT idPorte FROM Porte WHERE nomPorte=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,porte);
            ResultSet resultSet= preparedStatement.executeQuery();

            int idPorte=0;

            while(resultSet.next()){
                idPorte=resultSet.getInt("idPorte");
            }

            query="DELETE FROM Badgeuse WHERE idPorte=?;";

            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, idPorte);
            preparedStatement.executeUpdate();

            query="DELETE FROM Autorisation WHERE idPorte=?;";

            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, idPorte);
            preparedStatement.executeUpdate();

            query = "DELETE FROM Porte where idPorte=?;";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idPorte);
            preparedStatement.executeUpdate();

            connection.close();

            JOptionPane.showMessageDialog(null, "Suppression reussite", "Suppression reussite", JOptionPane.INFORMATION_MESSAGE);
        }catch (SQLException e){
            System.err.println(e);
        }
    }

    private void supprimerBadgeuse(){
        int badgeuse = Integer.parseInt((String)this.comboBoxBadgeuses.getSelectedItem());

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "DELETE FROM Badgeuse where idBadgeuse=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,badgeuse);
            preparedStatement.executeUpdate();

            connection.close();
            JOptionPane.showMessageDialog(null, "Suppression reussite", "Suppression reussite", JOptionPane.INFORMATION_MESSAGE);
        }catch (SQLException e){
            System.err.println(e);
        }
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==this.comboBoxSuppression){
            String supression = (String)this.comboBoxSuppression.getSelectedItem();

            if(supression.equals("Site")){
                choix=1;
                setComboBoxSites();
            }else {
                if (supression.equals("Porte")) {
                    choix=2;
                    setComboBoxPortes();

                } else {
                    if (supression.equals("Badgeuse")) {
                        choix=3;
                        setComboBoxPortes();
                        setComboBoxBadgeuses();
                    }else {
                        choix=0;
                    }

                }
            }

        }

        if(e.getSource()==this.bSupprimer){

            int retour = JOptionPane.showConfirmDialog(null, "En supprimant cette sutructure, vous vous appretez a supprimer toutes les structures qui lui sont liees (site, porte, badgeuses). Voulez vous continuer","Confirmation suppression",JOptionPane.YES_NO_OPTION);

            if(retour==0) {
                if (choix == 1) {
                    supprimerSite();
                    setComboBoxSites();
                }else{
                    if(choix==2){
                        supprimerPorte();
                        setComboBoxPortes();
                    }else{
                        if(choix==3){
                            supprimerBadgeuse();
                            setComboBoxBadgeuses();
                        }else{
                            JOptionPane.showMessageDialog(null, "Veuillez selectionner un element à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Supression annulee", "Supression annulee", JOptionPane.INFORMATION_MESSAGE);
            }
        }


        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new CreationStructure(this.frame);

        }
    }

}
