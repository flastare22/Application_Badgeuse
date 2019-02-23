import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public abstract class Panneau extends JPanel implements ActionListener {

    protected Hub frame;
    protected JButton deconnection;
    protected JButton retour;
    protected String pseudoAdmin;
    protected String motsDePasseAdmin;

    public Panneau(Hub frame, String titre){

        this.frame = frame;
        this.pseudoAdmin = this.frame.pseudoAdmin;
        this.motsDePasseAdmin = this.frame.motsDePasseAdmin;

        this.setSize(this.frame.getWidth(), this.frame.getHeight());
        this.setLocation(0,0);
        this.setLayout(null);
        this.setBackground(Color.white);

        JLabel label = new JLabel(titre, JLabel.CENTER);
        label.setSize(this.frame.getWidth(), 90);
        label.setLocation(0,50);
        label.setFont(new Font("Bahnschrift", Font.BOLD, 24));
        this.add(label);

        this.retour=new JButton("Retour");
        this.retour.setSize(100,25);
        this.retour.setLocation(0,0);
        this.retour.setBackground(Color.RED);
        this.retour.addActionListener(this);
        this.add(retour);

        this.deconnection = new JButton("Deconnection");
        this.deconnection.setSize(100, 25);
        this.deconnection.setLocation(this.frame.getWidth() - 105, 0);
        this.deconnection.setBackground(Color.gray);
        this.deconnection.addActionListener(this);
        this.add(deconnection);

    }

    void bigScreen(){

        this.frame.remove(this.deconnection);
        this.deconnection = new JButton("Deconnection");
        this.deconnection.setSize(100, 25);
        this.deconnection.setLocation(this.frame.largeurUtile - 105, 0);
        this.deconnection.setBackground(Color.gray);
        this.deconnection.addActionListener(this);
        this.add(deconnection);
    }

    public void actionPerformed(ActionEvent e){}


}
