import com.fraw.google.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;


public class CreationCarte extends Panneau implements ActionListener  {

    private JLabel motsDePasse;
    private JTextField tPrenom;
    private JTextField tNom;
    private JTextField tPseudo;
    private JPasswordField passWord;
    private JButton bContinuer;
    private JRadioButton oui;
    private JRadioButton non;
    private String sPseudo;
    private String sNom;
    private String sPrenom;
    private String sPassWord;
    private boolean admi;
    private boolean bool;

    CreationCarte(){
        super(new Hub(1), "");
    }


    CreationCarte(Hub frame){

        super(frame, "Creation d'une nouvelle Carte");

        this.bool=false;

        JLabel lNom= new JLabel("Nom* :");
        lNom.setSize(100,25);
        lNom.setLocation(50,frame.getHeight()/10+frame.getHeight()/10-12+50);
        this.add(lNom);

        this.tNom= new JTextField();
        this.tNom.setSize(100, 25);
        this.tNom.setLocation(220, frame.getHeight()/10+frame.getHeight()/10-12+50);
        this.add(tNom);

        JLabel lPrenom= new JLabel("Prénom* :");
        lPrenom.setSize(100,25);
        lPrenom.setLocation(50, frame.getHeight()/10*2+frame.getHeight()/10-12+50);
        this.add(lPrenom);

        this.tPrenom = new JTextField();
        this.tPrenom.setSize(100,25);
        this.tPrenom.setLocation(220, frame.getHeight()/10*2+frame.getHeight()/10-12+50);
        this.add(this.tPrenom);

        JLabel lPseudo = new JLabel("Matricule* :");
        lPseudo.setSize(100,25);
        lPseudo.setLocation(50, frame.getHeight()/10*3+frame.getHeight()/10-12+50);
        this.add(lPseudo);

        this.tPseudo= new JTextField();
        this.tPseudo.setSize(100,25);
        this.tPseudo.setLocation(220, frame.getHeight()/10*3+frame.getHeight()/10-12+50);
        this.add(this.tPseudo);

        JLabel lAdministrateur= new JLabel("Administrateur* :");
        lAdministrateur.setSize(100,25);
        lAdministrateur.setLocation(50, frame.getHeight()/10*4+frame.getHeight()/10-12+50);
        this.add(lAdministrateur);

        ButtonGroup admin= new ButtonGroup();
        this.oui = new JRadioButton("oui");
        this.oui.setSize(50,25);
        this.oui.setLocation(220, frame.getHeight()/10*4+frame.getHeight()/10-12+50);
        this.oui.setBackground(Color.WHITE);
        this.oui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(oui.isSelected()){
                    setAdministrateur(true);

                }else{
                    setAdministrateur(false);
                }
            }
        });
        this.non = new JRadioButton("non");
        this.non.setSize(75,25);
        this.non.setLocation(290, this.frame.getHeight()/10*4+this.frame.getHeight()/10-12+50);
        this.non.setBackground(Color.WHITE);
        this.non.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(non.isSelected()){
                    setAdministrateur(false);
                }
            }
        });
        admin.add(oui);
        admin.add(non);
        this.add(oui);
        this.add(non);

        this.bContinuer= new JButton("Continuer");
        this.bContinuer.setSize(100,50);
        this.bContinuer.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bContinuer.setBackground(Color.GREEN);
        this.bContinuer.addActionListener(this);
        this.add(this.bContinuer);

        this.frame.setContentPane(this);
        this.frame.revalidate();
        this.frame.repaint();
    }


    public void ajoutBd(String idCarte)throws IOException, GeneralSecurityException{

        boolean carteCreee = false;

        while(!carteCreee) {

            if (!idCarte.equals("")) {

                boolean existeDeja = chercherCarte(idCarte);

                if (existeDeja) {
                    int retour = JOptionPane.showConfirmDialog(this, "La carte existe deja et appartient à " + getPrenom(idCarte) + ", voulez vous la reinitialiser?", "Carte deja existante", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (retour == JOptionPane.OK_OPTION) {
                        supprimerCarte(idCarte);
                        creerCarte(idCarte, sPseudo, sNom, sPrenom, sPassWord, admi);
                        carteCreee = true;
                    } else {
                        new CaptureBadge(this.frame, 1, this, this);
                        JOptionPane.showMessageDialog(this, "Veuillez changer de carte!", "Carte deja existante", JOptionPane.INFORMATION_MESSAGE);
                        idCarte="";

                    }
                } else {
                    creerCarte(idCarte, sPseudo, sNom, sPrenom, sPassWord, admi);
                    carteCreee = true;
                }
            }
        }
    }

    private boolean chercherPseudo(String pseudo){
        boolean present=false;

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query = "SELECT pseudo FROM Personne WHERE pseudo=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                present=true;
            }
        }catch (SQLException e){
            System.err.println(e);
        }

        return present;
    }

    private boolean chercherCarte(String idCarte){
        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query="SELECT iDCarte from Carte;";

            Statement statement= connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                if(rs.getString("iDCarte").equals(idCarte)){
                    connection.close();
                    return true;
                }
            }
            connection.close();
        }catch(SQLException e){
            System.err.println(e);
        }

        return false;

    }

    public void creerCarte(String idCarte, String pseudo, String nom, String prenom, String motsDePasse, boolean administrateur )throws IOException, GeneralSecurityException{
        int compteur =0;
        int idPersonne=0;

        while(compteur!=10){
            int decimale= (int) (Math.random()*10);
            idPersonne=idPersonne*10+decimale;
            compteur++;
        }

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);
            String query= "INSERT INTO Personne (pseudo, nom, prenom, administrateur) VALUES (?,?,?,?);";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,pseudo);
            ps.setString(2, prenom);
            ps.setString(3, nom);
            ps.setBoolean(4, administrateur);

            ps.executeUpdate();

            String prenomNom=nom+" "+prenom;
            this.GoogleCreation(prenomNom);

            if(administrateur){
                query="CREATE USER ?@'192.168.1.0/255.255.255.0' IDENTIFIED BY ?;";
                ps= connection.prepareStatement(query);
                ps.setString(1, pseudo);
                ps.setString(2, motsDePasse);
                ps.executeUpdate();
                query="flush privileges;";
                Statement st2 = connection.createStatement();
                st2.executeUpdate(query);
                query="GRANT SELECT, UPDATE, INSERT,DELETE, GRANT OPTION ON BADGEUSELEHELLOCO.* TO ?@'192.168.1.0/255.255.255.0' IDENTIFIED BY ?;";
                ps= connection.prepareStatement(query);
                ps.setString(1, pseudo);
                ps.setString(2, motsDePasse);
                ps.executeUpdate();
                query="flush privileges;";
                st2 = connection.createStatement();
                st2.executeUpdate(query);
            }

            query="INSERT INTO Carte VALUES (?,?,?,?,?);";

            PreparedStatement ps2 = connection.prepareStatement(query);
            ps2.setString(1,idCarte);
            ps2.setString(2, this.pseudoAdmin);
            ps2.setTimestamp(3, new Timestamp((new Date()).getTime()));
            ps2.setBoolean(4, true);
            ps2.setString(5, pseudo);

            ps2.executeUpdate();

            query="SELECT idPorte, nomPorte FROM Porte";

            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);

            LinkedList <Object[]> idPorte= new LinkedList<>();
             while(resultSet.next()){
                 Object [] ligne = new Object[2];
                 ligne[0]=resultSet.getInt("idPorte");
                 ligne[1]=resultSet.getString("nomPorte");
                 idPorte.add(ligne);
             }

            for ( Object[] porte : idPorte) {
                query = "INSERT INTO Autorisation (statutAutorisation,pseudo,idPorte,localisation) VALUES (false,?,?,?);";
                PreparedStatement ps3 = connection.prepareStatement(query);
                ps3.setString(1, pseudo);
                ps3.setInt(2, (int) porte[0]);
                ps3.setString(3, (String) porte[1]);
                ps3.executeUpdate();
            }

            new CreationCarte(this.frame);

            this.resetFiled();

            JOptionPane.showMessageDialog(null,"Creation Carte terminee! N'oubliez pas de mofifier les autorisations","Création Terminée",JOptionPane.INFORMATION_MESSAGE);

            connection.close();

        }catch(SQLException e){
            System.err.println(e);
        }

    }

    private void supprimerCarte(String idCarte){
        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

            String pseudoPersonne="";

            String query="SELECT pseudo FROM Carte WHERE idCarte=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,idCarte);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                pseudoPersonne=resultSet.getString("pseudo");
            }

            query="DELETE FROM Ouverture where idCarte=?;";

            PreparedStatement preparedStatement$ = connection.prepareStatement(query);
            preparedStatement$.setString(1,idCarte);
            preparedStatement$.executeUpdate();

            query="DELETE FROM Carte where idCarte=?;";
            preparedStatement$=connection.prepareStatement(query);
            preparedStatement$.setString(1, idCarte);
            preparedStatement$.executeUpdate();

            query="DELETE FROM Autorisation where pseudo=?;";
            preparedStatement$=connection.prepareStatement(query);
            preparedStatement$.setString(1, pseudoPersonne);
            preparedStatement$.executeUpdate();

            query= "DELETE FROM Personne where pseudo=?;";
            preparedStatement$=connection.prepareStatement(query);
            preparedStatement$.setString(1, pseudoPersonne);
            preparedStatement$.executeUpdate();

            connection.close();
        }catch(SQLException o){
            System.err.println(o);
        }
    }

    private void GoogleCreation(String prenomNom)throws IOException, GeneralSecurityException {
        SheetCreation sheetCreation = new SheetCreation();
        sheetCreation.run(prenomNom);
    }

    private String getPrenom(String idCarte){

        String prenomNom="";

        try{
            Connection connection= DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);
            String query= "SELECT prenom, nom FROM Personne WHERE  pseudo in(SELECT pseudo FROM Carte where idCarte=?);";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, idCarte);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                prenomNom=rs.getString("Prenom");
                prenomNom=prenomNom+" ";
                prenomNom=prenomNom+rs.getString("Nom");
            }

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        return prenomNom;
    }

    private void resetFiled(){
        this.tPrenom.setText("");
        this.tNom.setText("");
        this.tPseudo.setText("");
    }

    private void action(){

        if(this.bool) {
            char[] ps = this.passWord.getPassword();
            this.sPassWord = "";
            for ( char i : ps) {
                sPassWord += i;
            }
        }

        if(!this.tPrenom.getText().equals("") && !this.tNom.getText().equals("") && !this.tPseudo.getText().equals("")){

            if(!chercherPseudo(this.tPseudo.getText())) {

                boolean bool = false;
                this.sPseudo = this.tPseudo.getText();
                this.sPrenom = this.tPrenom.getText();
                this.sNom = this.tNom.getText();

                if (oui.isSelected()) {
                    if (!this.sPassWord.equals("")) {
                        this.admi = true;
                        bool = true;
                    }
                } else {
                    this.admi = false;
                    bool = true;
                }

                if (bool) {
                    new CaptureBadge(this.frame, 1, this, this);
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir l'ensemble des champs demandes", "Champs Manquants", JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Le matricule que vous venez de selectionner existe deja! Veuillez le changer ou supprimer l'ancienne carte", "Matricule deja existant", JOptionPane.ERROR_MESSAGE);
                this.tPseudo.setText("");
            }

        }else{
            JOptionPane.showMessageDialog(this,"Veuillez remplir l'ensemble des champs demandes", "Champs Manquants", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setAdministrateur(boolean bool){
        if(bool){
            if(!this.bool) {
                this.motsDePasse = new JLabel("Mots de Passe* :");
                this.motsDePasse.setSize(100, 25);
                this.motsDePasse.setLocation(50, this.frame.getHeight() / 10 * 5 + this.frame.getHeight() / 10 - 12 + 50);
                this.add(this.motsDePasse);

                this.passWord = new JPasswordField();
                this.passWord.setSize(100, 25);
                this.passWord.setLocation(220, this.frame.getHeight() / 10 * 5 + this.frame.getHeight() / 10 - 12 + 50);

                this.bool = true;

                this.add(this.passWord);
                this.revalidate();
                this.repaint();
            }
        }else{
                if(this.bool) {
                    this.remove(this.motsDePasse);
                    this.remove(this.passWord);
                    this.bool=false;
                    this.revalidate();
                    this.repaint();
                }

        }
    }

    public void actionPerformed (ActionEvent e){

        if(e.getSource() == this.bContinuer){
            action();
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }
    }
}
