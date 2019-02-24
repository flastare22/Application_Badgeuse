import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Identification extends Panneau implements ActionListener {

    private JButton bValider;
    private JTextField identification;
    private JPasswordField mdp;


    Identification(Hub frame){

        super(frame, "");

        this.frame.setAdmin("","");
        this.remove(this.retour);
        this.remove(this.deconnection);

        JLabel logo=new JLabel();
        logo.setSize(this.frame.getWidth()/2,this.frame.getHeight()/2);
        logo.setLocation(this.frame.getWidth()/2-this.frame.getWidth()/4,25);
        this.add(logo);

        JLabel identifiant= new JLabel("Identidiant:");
        identifiant.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        identifiant.setSize(100,25);
        identifiant.setLocation(this.frame.getWidth()/2-140,this.frame.getHeight()/3*2-40);
        this.add(identifiant);

        identification= new JTextField();
        identification.setSize(100,25);
        identification.setLocation(this.frame.getWidth()/2+20, this.frame.getHeight()/3*2-40);
        this.add(identification);

        JLabel motsDePasse= new JLabel("Mots de Passe:");
        motsDePasse.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        motsDePasse.setSize(200,25);
        motsDePasse.setLocation(this.frame.getWidth()/2-140, this.frame.getHeight()/3*2);
        this.add(motsDePasse);

        this.mdp= new JPasswordField("");
        this.mdp.setSize(100,25);
        this.mdp.setLocation(this.frame.getWidth()/2+20, this.frame.getHeight()/3*2);
        this.add(mdp);

        this.bValider=new JButton("Valider");
        this.bValider.setSize(100,50);
        this.bValider.setLocation(this.frame.getWidth()/2-50, this.frame.getHeight()/3*2+this.frame.getWidth()/6-30);
        this.bValider.setBackground(Color.GREEN);
        this.bValider.addActionListener(this);
        this.add(this.bValider);

        this.frame.setContentPane(this);
        this.frame.revalidate();
        this.frame.repaint();
    }

    private boolean verificationID(){
        String id= identification.getText();

        char [] passWord= mdp.getPassword();
        String passwd="";

        for( char car : passWord){
            passwd += car;
        }

        try{
            Connection connection= DriverManager.getConnection(this.frame.serveur,"flastare","19Wabrocad05!");

            this.frame.setAdmin(id, passwd);
            
            connection.close();

            return true;

        }catch (SQLException e){
            System.err.println(e);
        }

        return false;
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bValider){

            if(verificationID()){
                new Menu(this.frame);
            }else{
                clean();
                JOptionPane.showMessageDialog(null, "Identifiant et/ou mot de passe incorrect.","Erreur Synthaxe", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void clean(){
        remove(mdp);
        mdp = new JPasswordField();
        mdp= new JPasswordField("");
        mdp.setSize(100,25);
        mdp.setLocation(this.frame.getWidth()/2+20, this.frame.getHeight()/3*2);
        mdp.revalidate();
        mdp.setVisible(true);
        mdp.revalidate();
        add(mdp);
    }


}
