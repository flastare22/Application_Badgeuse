import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreationBadgeuse extends Panneau implements ActionListener {

    private JLabel lPorte;
    private JLabel lTypeBadgeuse;
    private JComboBox <String> comboBoxPortes;
    private JComboBox <String> comboBoxTypesBadgeuses;
    private  JComboBox <String> comboBoxSites;
    private JButton bContinuer;


    CreationBadgeuse(Hub frame){

        super(frame, "Creation d'une nouvelle Badgeuse");

        JLabel lSite = new JLabel("Site* :");
        lSite.setSize(100,25);
        lSite.setLocation(50, this.frame.getHeight()/2-75);
        this.add(lSite);

        this.bContinuer = new JButton("Continuer");
        this.bContinuer.setSize(100,50);
        this.bContinuer.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bContinuer.setBackground(Color.GREEN);
        this.bContinuer.addActionListener(this);
        this.add(this.bContinuer);

        this.comboBoxPortes = new JComboBox<>();
        this.lPorte = new JLabel();
        this.comboBoxTypesBadgeuses = new JComboBox<>();
        this.lTypeBadgeuse = new JLabel();

        getSite();


        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }


    private void getSite(){

        this.comboBoxSites = new JComboBox<>() ;

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query = "SELECT nomSite, codeSite FROM Site GROUP BY codeSite;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                comboBoxSites.addItem(resultSet.getString("nomSite")+" "+resultSet.getString("codeSite"));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println();
        }

        this.comboBoxSites.setSize(150,25);
        this.comboBoxSites.setLocation(220,this.frame.getHeight()/2-75);
        this.add(comboBoxSites);
        this.comboBoxSites.addActionListener(this);

    }


    private void getPorte(String codeSite){

        this.remove(this.comboBoxPortes);
        this.remove(this.lPorte);

        this.comboBoxPortes = new JComboBox<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query = "SELECT nomPorte FROM Porte WHERE codeSite=? ;";

           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, codeSite);
           ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                System.out.println("Hello");
                System.out.println(resultSet.getString("nomPorte"));
                this.comboBoxPortes.addItem(resultSet.getString("nomPorte"));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }


        this.lPorte = new JLabel("Localisation* :");
        this.lPorte.setSize(100,25);
        this.lPorte.setLocation(50, this.frame.getHeight()/2-12);
        this.add(this.lPorte);

        this.comboBoxPortes.setSize(150,25);
        this.comboBoxPortes.setLocation(220,this.frame.getHeight()/2-12);
        this.add(this.comboBoxPortes);
        this.comboBoxPortes.addActionListener(this);
        this.revalidate();
        this.repaint();
    }

    private void getType(){
        this.remove(this.lTypeBadgeuse);
        this.remove(this.comboBoxTypesBadgeuses);

        this.lTypeBadgeuse = new JLabel("Type de porte");
        this.lTypeBadgeuse.setSize(100,25);
        this.lTypeBadgeuse.setLocation(50, this.frame.getHeight()/2+50);
        this.add(this.lTypeBadgeuse);

        this.comboBoxTypesBadgeuses = new JComboBox<>();
        this.comboBoxTypesBadgeuses.setSize(100, 25);
        this.comboBoxTypesBadgeuses.setLocation(220, this.frame.getHeight()/2+50);
        this.comboBoxTypesBadgeuses.setBackground(Color.WHITE);
        this.comboBoxTypesBadgeuses.addItem("Entree");
        this.comboBoxTypesBadgeuses.addItem("Sortie");
        this.comboBoxTypesBadgeuses.addItem("Controle Acces");
        this.add(this.comboBoxTypesBadgeuses);

        this.revalidate();
        this.repaint();
    }


    private void creationBadgeuse(){

        String site = (String) this.comboBoxSites.getSelectedItem();
        String data [] = site.split(" ");
        String codeSite = data[1];
        String porte = (String) this.comboBoxPortes.getSelectedItem();
        String natureBadgeuse = (String) this.comboBoxTypesBadgeuses.getSelectedItem();
        int type = -1;
        if(natureBadgeuse != null) {
            switch (natureBadgeuse) {
                case "Entree":
                    type = 0;
                    break;
                case "Sortie":
                    type = 1;
                    break;
                case "Controle Acces":
                    type = 2;
                    break;
                default:
                    type = 100;
            }
        }

        int idPorte=0;

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT idPorte FROM Porte WHERE codeSite=? and nomPorte=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codeSite);
            preparedStatement.setString(2, porte);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                idPorte=resultSet.getInt("idPorte");
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "INSERT INTO Badgeuse(idPorte,typeBadgeuse) VALUES (?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,idPorte);
            preparedStatement.setInt(2, type);
            preparedStatement.executeUpdate();

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        JOptionPane.showMessageDialog(null, "La badgeuse a bien ete creee","Creation reussie", JOptionPane.INFORMATION_MESSAGE);
    }


    public void actionPerformed (ActionEvent e){

        /*if(e.getSource()== this.comboBoxSites){
            String selectedItem = (String) this.comboBoxSites.getSelectedItem();
            String [] data = selectedItem.split(" ");
            getPorte(data[1]);
        }*/

        if(e.getSource()==this.comboBoxSites){
            String site = (String) this.comboBoxSites.getSelectedItem();
            if(!site.equals("")) {
                String data[] = site.split(" ");
                String codeSite = data[1];
                getPorte(codeSite);
            }else{
                this.remove(this.comboBoxPortes);
                this.remove(this.lTypeBadgeuse);
                this.remove(this.comboBoxTypesBadgeuses);
                this.remove(this.lTypeBadgeuse);
                this.revalidate();
                this.repaint();
            }
        }

        if(e.getSource() == this.comboBoxPortes){
            String porte = (String) this.comboBoxPortes.getSelectedItem();
            if(!porte.equals("")){
                getType();
            }
        }

        if(e.getSource() == this.bContinuer){
            creationBadgeuse();
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new CreationStructure(this.frame);

        }

    }
}
