import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreationSite extends Panneau implements ActionListener {

    private JTextField nomSite;
    private JTextField codeSite;
    private JTextField mailSite;
    private JButton bContinuer;


    CreationSite(Hub frame){

        super(frame, "Creation d'un nouveau Site");

        JLabel lNom = new JLabel("Nom du Site* :");
        lNom.setSize(150,25);
        lNom.setLocation(50, this.frame.getHeight()/2-75);
        this.add(lNom);

        this.nomSite = new JTextField();
        this.nomSite.setLocation(220, this.frame.getHeight()/2-75);
        this.nomSite.setSize(150,25);
        this.add(nomSite);

        JLabel lCode = new JLabel("Code* :");
        lCode.setSize(150,25);
        lCode.setLocation(50, this.frame.getHeight()/2-12);
        this.add(lCode);

        this.codeSite = new JTextField();
        this.codeSite.setSize(150,25);
        this.codeSite.setLocation(220, this.frame.getHeight()/2-12);
        this.add(this.codeSite);

        JLabel lMail= new JLabel("Adresse mail site* :");
        lMail.setSize(150,25);
        lMail.setLocation(50, this.frame.getHeight()/2+50);
        this.add(lMail);

        this.mailSite = new JTextField();
        this.mailSite.setSize(150,25);
        this.mailSite.setLocation(220, this.frame.getHeight()/2+50);
        this.add(this.mailSite);

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


    private void creationSite(){

        if(!this.nomSite.getText().equals("") && !this.codeSite.getText().equals("") && !this.mailSite.getText().equals("")){
            if(!this.nomSite.getText().contains(" ") && !this.codeSite.getText().contains(" ")){

                boolean existe = false;

                try{
                    Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                    String query = "SELECT codeSite FROM Site WHERE codeSite=?;";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,this.codeSite.getText());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()){
                        existe=true;
                    }

                    connection.close();

                }catch(SQLException e){
                    System.err.println(e);
                }

                if(!existe){
                    try{
                        Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                        String query = "INSERT INTO Site VALUES (?,?,?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, this.codeSite.getText());
                        preparedStatement.setString(2, this.nomSite.getText());
                        preparedStatement.setString(3, this.mailSite.getText());
                        preparedStatement.executeUpdate();

                        connection.close();

                    }catch (SQLException e){
                        System.err.println();
                    }

                    JOptionPane.showMessageDialog(null, "Le site "+this.nomSite.getText()+" (code : "+this.codeSite.getText()+") a bien ete cree", "Creation reussie",JOptionPane.INFORMATION_MESSAGE);

                    this.retour.doClick();
                }else{
                    JOptionPane.showMessageDialog(null, "Le code de site que vous essayez de rentrer est deja utilise", "Code deja existant", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Les nom de site et code de site ne doivent pas comporter d'espace", "Attention espcae", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Veuillez renseigner l'ensemble des champs demandés", "Champs vides", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bContinuer){
            creationSite();
        }

        if (e.getSource() == this.deconnection){

            new Identification(this.frame);

        }

        if(e.getSource() == this.retour){

            new CreationStructure(this.frame);

        }
    }


}
