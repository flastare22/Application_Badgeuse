import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SuppressionCarte extends Panneau implements ActionListener {


    private JButton bContinuer;

    SuppressionCarte(Hub frame){

        super(frame, "Suppression d'une Carte");

        JLabel lLogo=new JLabel(new ImageIcon(".\\src\\main\\java\\com\\fraw\\image\\contactless-payment.png"));
        lLogo.setSize(this.frame.getWidth()/2,this.frame.getHeight()/2);
        lLogo.setLocation(this.frame.getWidth()/2-125,120);
        this.add(lLogo);

        this.bContinuer = new JButton("Continuer");
        this.bContinuer.setSize(100,50);
        this.bContinuer.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bContinuer.setBackground(Color.GREEN);
        this.bContinuer.addActionListener(this);
        this.add(this.bContinuer);

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    protected void recuperationInfo(String idCarte){

        String prenom="";
        String nom="";
        String pseudoPersonne="";

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.145:5789/BADGEUSELEHELLOCO",this.pseudoAdmin, this.motsDePasseAdmin);

            String query="SELECT pseudo FROM Carte where idCarte=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,idCarte);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                pseudoPersonne=resultSet.getString("pseudo");
            }

            query="SELECT nom, prenom FROM Personne where pseudo=?;";
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,pseudoPersonne);
            resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                prenom=resultSet.getString("prenom");
                nom=resultSet.getString("nom");
            }

            if(!prenom.equals("")) {
                supprimerCarte(prenom, nom, idCarte, pseudoPersonne);
            }else{
                JOptionPane.showMessageDialog(null,"La carte placee sur le detecteur n'existe pas!!!","Suppression annulée", JOptionPane.INFORMATION_MESSAGE);
            }

            connection.close();

        }catch (SQLException e){
            System.err.println();
        }
    }


    private void supprimerCarte(String prenom, String nom, String idCarte, String pseudoPersonne){
        String reponse=JOptionPane.showInputDialog(this, "Vous allez supprimer la carte de "+prenom+" "+nom+". Saisisser 'SUPPRIMER' pour confirmer", "");
        if(reponse.equals("SUPPRIMER") || reponse.equals("supprimer") || reponse.equals("Supprimer")){
            try{
                Connection connection = DriverManager.getConnection(this.frame.serveur,this.pseudoAdmin, this.motsDePasseAdmin);

                String query="DELETE FROM Ouverture where idCarte=?;";

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

                JOptionPane.showMessageDialog(this, "Suppression reussie", "Suppression reussie",JOptionPane.INFORMATION_MESSAGE);

                new SuppressionStructure(this.frame);

                connection.close();
            }catch(SQLException o){
                System.err.println(o);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Suppression annulee","Suppression annulee", JOptionPane.INFORMATION_MESSAGE);
            new CaptureBadge(this.frame, 2, this, new CreationCarte());
        }
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bContinuer){
            new CaptureBadge(this.frame, 2, this, new CreationCarte());
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new Menu(this.frame);

        }
    }


}
