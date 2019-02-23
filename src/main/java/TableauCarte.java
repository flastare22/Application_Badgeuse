import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class TableauCarte extends Panneau implements CaretListener, ActionListener {

    private JPanel panelTableau;
    private JTextField barreDeRecherche;


    TableauCarte(Hub frame){

        super(frame, "Tableau des Cartes");

        JLabel lRecherche =  new JLabel("Rerchercher :");
        lRecherche.setSize(100,25);
        lRecherche.setLocation(this.frame.getWidth()/2-110, 110);
        this.add(lRecherche);

        this.barreDeRecherche = new JTextField("");
        this.barreDeRecherche.setSize(100,25);
        this.barreDeRecherche.setLocation(this.frame.getWidth()/2+10, 110);
        this.add(this.barreDeRecherche);
        this.barreDeRecherche.addCaretListener(this);

        this.panelTableau= new JPanel();

        setTable("%%");

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }


    private void setTable(String recherche){
        String [] head ={"Matricule","Nom","Prenom","Etat Carte"};

        LinkedList<Object[]> donnees = new LinkedList<>();

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT Personne.pseudo , Personne.nom, Personne.prenom  , Carte.active FROM Personne, Carte WHERE Carte.pseudo=Personne.pseudo AND  Personne.profil=false AND  ( nom LIKE ? OR prenom LIKE ? OR Personne.pseudo LIKE ? ) GROUP BY pseudo ORDER BY pseudo;";

            PreparedStatement preparedStatement =  connection.prepareStatement(query);
            preparedStatement.setString(1,recherche);
            preparedStatement.setString(2, recherche);
            preparedStatement.setString(3, recherche);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Object [] ligne = new Object[4];
                ligne[0]=resultSet.getString("Personne.pseudo");
                ligne[1]=resultSet.getString("Personne.nom");
                ligne[2]=resultSet.getString("Personne.prenom");
                if(resultSet.getBoolean("Carte.active")){
                    ligne[3]="Active";
                }else{
                    ligne[3]="Desactive";
                }

                donnees.add(ligne);
            }
            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        Object [][] data = new Object[donnees.size()] [4];

        for(int i =0; i<donnees.size();i++){
            for(int j=0; j<donnees.get(i).length;j++)
                data[i][j]=donnees.get(i)[j];
        }

        JTable tableau = new JTable(data, head);

        setPanel(tableau);

    }


    private void setPanel(JTable tableau){

        tableau.setPreferredScrollableViewportSize(new Dimension(450, 270));

        this.remove(this.panelTableau);
        this.panelTableau = new JPanel();
        this.panelTableau.setSize(500,350);
        this.panelTableau.setLocation(0,150);
        this.panelTableau.setBackground(Color.WHITE);

        this.panelTableau.add(new JScrollPane(tableau),BorderLayout.CENTER);
        this.add(this.panelTableau);
        this.revalidate();
        this.repaint();
    }


    public void caretUpdate(CaretEvent e){

        if(e.getSource()== this.barreDeRecherche){
            String valeurRechercher = this.barreDeRecherche.getText();
            valeurRechercher="%"+valeurRechercher+"%";
            setTable(valeurRechercher);
        }

    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == this.deconnection){

            new Identification(this.frame);
        }

        if(e.getSource() == this.retour){

            new MenuGestionCarte(this.frame);

        }
    }

}