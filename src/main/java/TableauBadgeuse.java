import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class TableauBadgeuse extends Panneau implements ActionListener {

    private JPanel panelTableau;
    private JComboBox <String> comboBoxSites;
    private JComboBox <String> comboBoxPortes;
    private JLabel lPorte;

    TableauBadgeuse(Hub frame){

        super(frame, "Tableau des Badgeuses");

        this.panelTableau= new JPanel();

        this.lPorte= new JLabel();
        this.comboBoxPortes = new JComboBox<>();
        setComboBoxSites();

        String [] table = {"Tous"};

        setTable(table);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();
    }

    private void setComboBoxSites(){

        this.comboBoxSites = new JComboBox<>();
        this.comboBoxSites.addItem("Tous");

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query = "SELECT nomSite, codeSite FROM Site GROUP BY codeSite;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                this.comboBoxSites.addItem(resultSet.getString("nomSite")+" "+resultSet.getString("codeSite"));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }

        JLabel lSite=new JLabel("Site : ");
        lSite.setSize(100,25);
        lSite.setLocation(50, 110);
        this.add(lSite);

        this.comboBoxSites.setSize(150,25);
        this.comboBoxSites.setLocation(80,110);
        this.add(this.comboBoxSites);
        this.comboBoxSites.addActionListener(this);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    private void setComboBoxPortes(String codeSite){

        this.remove(this.comboBoxPortes);
        this.remove(this.lPorte);

        this.comboBoxPortes = new JComboBox<>();
        this.comboBoxPortes.addItem("Toutes");

        try{
            Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin,this.motsDePasseAdmin);

            String query = "SELECT nomPorte FROM Porte WHERE codeSite=? ;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codeSite);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                this.comboBoxPortes.addItem(resultSet.getString("nomPorte"));
            }

            connection.close();

        }catch (SQLException e){
            System.err.println(e);
        }


        this.lPorte = new JLabel("Porte :");
        this.lPorte.setSize(100,25);
        this.lPorte.setLocation(this.frame.getWidth()/2+10, 110);
        this.add(this.lPorte);

        this.comboBoxPortes.setSize(150,25);
        this.comboBoxPortes.setLocation(this.frame.getWidth()/2+50,110);
        this.add(this.comboBoxPortes);
        this.comboBoxPortes.addActionListener(this);
        this.revalidate();
        this.repaint();
    }

    private void setTable(String [] tab){
        String [] head ={"idBadgeuse","typeBadgeuse","Porte","Site"};

        LinkedList<Object[]> donnees = new LinkedList<>();

        if(tab.length==1){
            if(tab[0].equals("Tous")){
                this.remove(this.comboBoxPortes);
                this.remove(this.lPorte);
                try{
                    Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                    String query = "SELECT Badgeuse.idBadgeuse, Badgeuse.typeBadgeuse, Porte.nomPorte, Site.nomSite FROM Badgeuse, Porte, Site WHERE Badgeuse.idPorte=Porte.idPorte and Porte.codeSite=Site.codeSite GROUP BY idBadgeuse ORDER BY idBadgeuse;";

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()){
                        Object [] ligne = new Object[4];
                        ligne[0]=resultSet.getInt("Badgeuse.idBadgeuse");
                        switch (resultSet.getInt("Badgeuse.typeBadgeuse")){
                            case 0:
                                ligne[1]="Entree";
                                break;
                            case 1 :
                                ligne[1]="Sortie";
                                break;
                            case 2 :
                                ligne[1]="Accès";
                                break;
                                default:
                                    ligne[1]="Autre";
                        }
                        ligne[2]=resultSet.getString("Porte.nomPorte");
                        ligne[3]=resultSet.getString("Site.nomSite");
                        donnees.add(ligne);
                    }
                    connection.close();
                }catch (SQLException e){
                    System.err.println(e);
                }

                Object [][] data = new Object[donnees.size()] [4];

                for(int i =0; i<donnees.size();i++){
                    for(int j=0; j<donnees.get(i).length;j++) {
                        data[i][j] = donnees.get(i)[j];
                    }
                }

                JTable tableau = new JTable(data, head);

                setPanel(tableau);


            }else{

                String [] values = tab[0].split(" ");

                String codeSite=values[1];

                setComboBoxPortes(codeSite);

                try{
                    Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                    String query = "SELECT Badgeuse.idBadgeuse, Badgeuse.typeBadgeuse, Porte.nomPorte, Site.nomSite FROM Badgeuse, Porte, Site WHERE Porte.codeSite=? AND Badgeuse.idPorte=Porte.idPorte AND Porte.codeSite=Site.codeSite ORDER BY idBadgeuse;";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, codeSite);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()){
                        Object [] ligne = new Object[4];
                        ligne[0]=resultSet.getInt("Badgeuse.idBadgeuse");
                        switch (resultSet.getInt("Badgeuse.typeBadgeuse")){
                            case 0:
                                ligne[1]="Entree";
                                break;
                            case 1 :
                                ligne[1]="Sortie";
                                break;
                            case 2 :
                                ligne[1]="Accès";
                                break;
                            default:
                                ligne[1]="Autre";
                        }
                        ligne[2]=resultSet.getString("Porte.nomPorte");
                        ligne[3]=resultSet.getString("Site.nomSite");
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
        }else{
            if(tab.length==2){
                if(tab[1].equals("Toutes")){

                    String [] values = tab[0].split(" ");

                    String codeSite=values[1];

                    try{
                        Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                        String query = "SELECT Badgeuse.idBadgeuse, Badgeuse.typeBadgeuse, Porte.nomPorte, Site.nomSite FROM Badgeuse, Porte, Site WHERE Badgeuse.idPorte=Porte.idPorte and Porte.codeSite=Site.codeSite AND Porte.codeSite=? GROUP BY idBadgeuse ORDER BY idBadgeuse;";

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, codeSite);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()){
                            Object [] ligne = new Object[4];
                            ligne[0]=resultSet.getInt("Badgeuse.idBadgeuse");
                            switch (resultSet.getInt("Badgeuse.typeBadgeuse")){
                                case 0:
                                    ligne[1]="Entree";
                                    break;
                                case 1 :
                                    ligne[1]="Sortie";
                                    break;
                                case 2 :
                                    ligne[1]="Accès";
                                    break;
                                default:
                                    ligne[1]="Autre";
                            }
                            ligne[2]=resultSet.getString("Porte.nomPorte");
                            ligne[3]=resultSet.getString("Site.nomSite");
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
                }else{

                    String [] values = tab[0].split(" ");

                    String codeSite=values[1];

                    try{
                        Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                        String query = "SELECT Badgeuse.idBadgeuse, Badgeuse.typeBadgeuse, Porte.nomPorte, Site.nomSite FROM Badgeuse, Porte, Site WHERE Badgeuse.idPorte=Porte.idPorte AND Porte.codeSite=Site.codeSite AND Porte.codeSite=? AND Porte.nomPorte=? GROUP BY idBadgeuse ORDER BY idBadgeuse;";

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, codeSite);
                        preparedStatement.setString(2, tab[1]);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()){
                            Object [] ligne = new Object[4];
                            ligne[0]=resultSet.getInt("Badgeuse.idBadgeuse");
                            switch (resultSet.getInt("Badgeuse.typeBadgeuse")){
                                case 0:
                                    ligne[1]="Entree";
                                    break;
                                case 1 :
                                    ligne[1]="Sortie";
                                    break;
                                case 2 :
                                    ligne[1]="Accès";
                                    break;
                                default:
                                    ligne[1]="Autre";
                            }
                            ligne[2]=resultSet.getString("Porte.nomPorte");
                            ligne[3]=resultSet.getString("Site.nomSite");
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
            }
        }
    }

    private void setPanel(JTable tableau){

        tableau.setPreferredScrollableViewportSize(new Dimension(450, 270));

        this.remove(this.panelTableau);
        this.panelTableau = new JPanel();
        this.panelTableau.setSize(500,300);
        this.panelTableau.setLocation(0,150);
        this.panelTableau.setBackground(Color.WHITE);


        this.panelTableau.add(new JScrollPane(tableau),BorderLayout.CENTER);
        this.add(this.panelTableau);
        this.revalidate();
        this.repaint();
    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource()==this.comboBoxSites){
            if(this.comboBoxSites.getSelectedItem().equals("Tous")){
                this.remove(this.lPorte);
                this.remove(this.comboBoxPortes);
                String [] tab= {"Tous"};
                setTable(tab);
            }else{
                String [] tab = {(String) this.comboBoxSites.getSelectedItem()};
                setTable(tab);
            }
        }

        if(e.getSource()==this.comboBoxPortes){
            if(this.comboBoxPortes.getSelectedItem().equals("Toutes")){
                String [] tab ={(String) this.comboBoxSites.getSelectedItem(), "Toutes"};
                setTable(tab);
            }else{
                String [] tab = {(String) this.comboBoxSites.getSelectedItem(), (String) this.comboBoxPortes.getSelectedItem()};
                setTable(tab);
            }
        }


        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }
    }
}
