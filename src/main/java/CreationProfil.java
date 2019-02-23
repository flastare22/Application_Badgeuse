import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class CreationProfil extends Panneau implements ActionListener {

    private String pseudo;
    private JScrollPane [] tableauAutorisations;
    private LinkedList<String> nomSite;
    private JButton bAnnuler;
    private JButton bValier;

    CreationProfil(Hub frame, String pseudo){

        super(frame, "");

        this.frame.bigScreen();

        JLabel label = new JLabel("Creation nouveau Profil : "+pseudo, JLabel.CENTER);
        label.setSize(this.frame.getWidth(), 20);
        label.setLocation(0,50);
        label.setFont(new Font("Bahnschrift", Font.BOLD, 24));
        this.add(label);

        System.out.println(this.frame.getWidth()+" "+this.frame.getHeight());

        this.bValier= new JButton("Valider");
        this.bValier.setSize(100,40);
        this.bValier.setLocation(this.frame.getWidth()/4*3, 10);
        this.bValier.setBackground(Color.green);
        this.bValier.addActionListener(this);
        this.add(this.bValier);

        this.bAnnuler = new JButton("Annuler");
        this.bAnnuler.setSize(100,40);
        this.bAnnuler.setLocation(this.frame.getWidth()/4*3,60);
        this.bAnnuler.setBackground(Color.red);
        this.bAnnuler.addActionListener(this);
        this.add(this.bAnnuler);

        this.deconnection.setLocation(this.frame.getWidth()-105, 0);

        this.pseudo = pseudo;

        JTabbedPane onglets = new JTabbedPane();
        onglets.setSize(this.frame.getWidth()-15, this.frame.getHeight()-this.frame.getHeight()/8-40);
        onglets.setLocation(0, this.frame.getHeight()/8);
        getPorteSite(pseudo);
        for(int i =0; i<this.tableauAutorisations.length; i++){
            onglets.add(this.nomSite.get(i), tableauAutorisations[i] );
        }
        this.add(onglets);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    private void getPorteSite(String pseudo){

        LinkedList <JScrollPane> listeTableauBadgeuse = new LinkedList<>();
        this.nomSite= new LinkedList<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT codeSite, nomSite FROM Site;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String codeSite = resultSet.getString("codeSite");
                this.nomSite.add(codeSite);
                TableauAutorisation tab = new TableauAutorisation(this.frame, codeSite, pseudo);
                listeTableauBadgeuse.add(new JScrollPane(tab));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }

        this.tableauAutorisations = new JScrollPane[listeTableauBadgeuse.size()];

        for(int i =0 ; i<listeTableauBadgeuse.size();i++){
            this.tableauAutorisations[i]= listeTableauBadgeuse.get(i);
        }
    }


    static boolean creerPersonne(String pseudo, String serveur, String pseudoAdmin, String motsDePasseAdmin) {

        try {
            Connection connection = DriverManager.getConnection(serveur, pseudoAdmin, motsDePasseAdmin);

            String query= "SELECT pseudo FROM Personne where pseudo=?;";

            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                JOptionPane.showMessageDialog(null,"Le profil que vous venez de rentrer existe deja!", "Profil existant", JOptionPane.INFORMATION_MESSAGE);

                return false;
            }

        }catch (SQLException e){
            System.err.println(e);
        }


        int compteur =0;
        int idPersonne=0;

        while(compteur!=10){
            int decimale= (int) (Math.random()*10);
            idPersonne=idPersonne*10+decimale;
            compteur++;
        }

        try{
            Connection connection = DriverManager.getConnection(serveur, pseudoAdmin,motsDePasseAdmin);
            String query= "INSERT INTO Personne (pseudo, profil, prenom, nom) VALUES (?,true,'fraw','fraw');";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,pseudo);

            ps.executeUpdate();

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

            for(int i=0; i<idPorte.size();i++) {

                query = "INSERT INTO Autorisation (statutAutorisation,pseudo,idPorte,localisation) VALUES (false,?,?,?);";
                PreparedStatement ps3 = connection.prepareStatement(query);
                ps3.setString(1, pseudo);
                ps3.setInt(2,(int)idPorte.get(i)[0]);
                ps3.setString(3,(String) idPorte.get(i)[1]);
                ps3.executeUpdate();
            }

            System.out.println("Boom c'est créée");

            connection.close();

            return true;

        }catch(SQLException e){
            System.err.println(e);
        }

        return false;

    }

    private void supprimerPersonne(String pseudo){

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query="DELETE FROM Autorisation WHERE pseudo=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pseudo);
            preparedStatement.executeUpdate();

            query="DELETE FROM Personne WHERE pseudo=?;";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pseudo);
            preparedStatement.executeUpdate();

            System.out.println("C'est supprimé");

            JOptionPane.showMessageDialog(null, "Le profil à bien ete supprime", "Supression Profil", JOptionPane.INFORMATION_MESSAGE);
            this.retour.doClick();

            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }
    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bValier){
            this.retour.doClick();
            JOptionPane.showMessageDialog(null, "Creation reussie", "Creation reussie", JOptionPane.INFORMATION_MESSAGE);
        }

        if(e.getSource() == this.bAnnuler){
            supprimerPersonne(pseudo);
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            this.frame.smallScreen();
            new MenuProfils(this.frame);

        }

    }


}
