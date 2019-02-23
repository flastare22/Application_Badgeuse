import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.LinkedList;

public class TableauAutorisation extends JScrollPane implements MouseListener {

    private Object [][] tableauBouton;
    private String pseudo;
    private String codeSite;
    private Hub frame;

    TableauAutorisation(Hub frame, String codeSite,  String pseudo) {

        this.frame = frame;

        this.pseudo=pseudo;
        this.codeSite=codeSite;

        initialisationTableau();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    private void initialisationTableau(){

        int compteur = 0;
        LinkedList<String> nomPorte = new LinkedList<>();
        System.out.println("Ok");

        try {
            Connection connection = DriverManager.getConnection(this.frame.getServeur(), this.frame.pseudoAdmin,this.frame.motsDePasseAdmin);
            String query = "Select nomPorte from Porte where codeSite=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,codeSite);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nomPorte.add(resultSet.getString("nomPorte"));
                compteur++;
            }
            connection.close();
        }catch (SQLException e){
            System.err.println(e);
        }

        int size = nomPorte.size();

        JPanel jPanel = new JPanel();
        int dimension = 0;
        int rows = 0;
        if(size % 8 != 0){
            dimension = (size/8)*25+25;
            rows = size/8+1;
        }else{
            dimension = (size/8)*25;
            rows = size/8;
        }
        System.out.println(dimension+" "+rows);
        jPanel.setPreferredSize(new Dimension(0, dimension));
        jPanel.setLayout(new GridLayout(rows,8));//3000/40
        System.out.println(jPanel.getLayout());
        jPanel.setLocation(0, 0);
        jPanel.setBackground(Color.WHITE);

        tableauBouton = new Object [compteur][3];

        for(int i =0 ; i<compteur; i++){
            boolean statutAutorisation=false;
            try{
                Connection connection=DriverManager.getConnection(this.frame.getServeur(),this.frame.pseudoAdmin,this.frame.motsDePasseAdmin);

                String query="SELECT statutAutorisation from Autorisation where pseudo=? and idPorte=(SELECT idPorte from Porte where nomPorte=? AND codeSite=?);";

                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setString(1, pseudo);
                preparedStatement.setString(2, nomPorte.get(i));
                preparedStatement.setString(3, this.codeSite);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    statutAutorisation=resultSet.getBoolean("statutAutorisation");
                }
                connection.close();
            }catch (SQLException e){
                System.err.println();
            }
            tableauBouton[i][0]=statutAutorisation;
            tableauBouton[i][2]=nomPorte.get(i);

            JButton jButton = new JButton(nomPorte.get(i));

            jPanel.add(jButton);

            if ((boolean)tableauBouton[i][0]) {
                jButton.setBackground(Color.green);
            }else{
                jButton.setBackground(Color.red);
            }

            jButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    for(int i=0; i<tableauBouton.length; i++){
                        if(tableauBouton[i][1].equals(jButton)){
                            boolean FEC=(boolean)tableauBouton[i][0];
                            boolean changement=false;
                            if(FEC){
                                jButton.setBackground(Color.red);
                                tableauBouton[i][0]=false;
                                changement=false;
                            }
                            if(!FEC){
                                jButton.setBackground(Color.green);
                                tableauBouton[i][0]=true;
                                changement=true;
                            }
                            try{
                                Connection connection = DriverManager.getConnection(frame.getServeur(),frame.pseudoAdmin,frame.motsDePasseAdmin);

                                String query="UPDATE Autorisation SET statutAutorisation=? WHERE idPorte=(SELECT idPorte from Porte Where nomPorte=? and codeSite=?) and pseudo=?;";

                                PreparedStatement preparedStatement= connection.prepareStatement(query);
                                preparedStatement.setBoolean(1,changement );
                                preparedStatement.setString(2,(String)tableauBouton[i][2]);
                                preparedStatement.setString(3, codeSite);
                                preparedStatement.setString(4, pseudo);
                                preparedStatement.executeUpdate();
                                connection.close();
                            }catch (SQLException o){
                                System.err.println();
                            }
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            tableauBouton[i][1]=jButton;
            jPanel.repaint();
            jPanel.revalidate();

        }


        this.add(jPanel);
        this.repaint();
        this.setViewportView(jPanel);
    }
}
