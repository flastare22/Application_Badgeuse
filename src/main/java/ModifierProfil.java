import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.LinkedList;

class ModifierProfil extends Panneau {

    private JScrollPane [] tableauAutorisations;
    private LinkedList<String> nomSite;
    private int index;


    ModifierProfil(Hub frame, String pseudo, int index){

        super(frame, "");

        this.frame.bigScreen();
        this.index = index;

        JLabel label = new JLabel("Mofification Profil : "+pseudo, JLabel.CENTER);
        label.setSize(this.frame.getWidth(), 20);
        label.setLocation(0,50);
        label.setFont(new Font("Bahnschrift", Font.BOLD, 24));
        this.add(label);

        this.deconnection.setLocation(this.frame.getWidth()-105, 0);

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
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.145:5789/BADGEUSELEHELLOCO",this.pseudoAdmin, this.motsDePasseAdmin);

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

        for(int i =0; i<listeTableauBadgeuse.size(); i++){
            this.tableauAutorisations[i] = listeTableauBadgeuse.get(i);
        }
    }
    public void actionPerformed (ActionEvent e){

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            this.frame.smallScreen();

            if(index==1) {
                new RechercheProfil(this.frame);
            }else{
                new RecherchePersonne(this.frame);
            }

        }
    }

}
