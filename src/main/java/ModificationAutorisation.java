import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.LinkedList;

public class ModificationAutorisation extends Panneau {

    private JScrollPane [] tableauAutorisations;
    private LinkedList<String> nomSite;

    public ModificationAutorisation(Hub frame, String pseudo){

        super(frame, "Creation nouveau Profil : "+pseudo);

        JTabbedPane onglets = new JTabbedPane();
        onglets.setSize(this.frame.getWidth(), this.frame.getHeight()-this.frame.getHeight()/8);
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
            Connection connection = DriverManager.getConnection(this.frame.getServeur(),this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT codeSite, nomSite FROM Site;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String codeSite = resultSet.getString("codeSite");
                this.nomSite.add(codeSite);
                //TableauAutorisation tab = new TableauAutorisation();
                //listeTableauBadgeuse.add(new JScrollPane(tab));
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

    public void actionPerformed(ActionEvent e){


        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new MenuAutorisation(this.frame);

        }
    }

}
