import com.fraw.arduino.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class CaptureBadge extends Panneau implements ActionListener {

    private JProgressBar bar;
    private JButton bAnnuler;
    private Connection connection;
    private Panneau panneau;


    CaptureBadge(Hub frame, int action, Panneau panneau, CreationCarte creationCarte) {

        super(frame, "Presentez une carte au lecteur");
        this.remove(this.retour);

        this.bar  = new JProgressBar();
        this.bar.setMaximum(100);
        this.bar.setMinimum(0);
        this.bar.setStringPainted(true);
        this.bar.setSize(200,25);
        this.bar.setLocation(145, 500-155);
        this.add(this.bar);

        this.bAnnuler = new JButton("Annuler");
        this.bAnnuler.setSize(100,50);
        this.bAnnuler.setLocation(this.frame.getWidth()/2-60, this.frame.getHeight()/3*2+this.frame.getHeight()/6-30);
        this.bAnnuler.setBackground(Color.red);
        this.bAnnuler.addActionListener(this);
        this.add(this.bAnnuler);

        JLabel lImage=new JLabel(new ImageIcon(".\\src\\main\\java\\com\\fraw\\image\\contactless-payment.png"));
        lImage.setSize(this.frame.getWidth()/2,this.frame.getHeight()/2);
        lImage.setLocation(this.frame.getWidth()/2-125,120);
        this.add(lImage);

        this.panneau = panneau;

        this.connection = new Connection(this.bAnnuler, action, this.frame, creationCarte);
        this.connection.start();

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }


    private void terminer(){
            this.connection.connectionArduino.close();
            this.connection.interrupt();
            System.out.println("Fin");
            this.frame.setContentPane(this.panneau);

    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bAnnuler){
            terminer();
            JOptionPane.showMessageDialog(null, "Opération Annulée", "Operation Annulée", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    class Connection extends Thread implements ActionListener{

        private ConnectionArduino connectionArduino;
        private JButton bAnnuler;
        private int action;
        private  Hub frame;
        private CreationCarte creationCarte;

        public Connection(JButton annuler, int action, Hub frame, CreationCarte creationCarte){
            this.action = action;
            this.bAnnuler = annuler;
            this.frame = frame;
            this.creationCarte = creationCarte;

        }

        public void run(){

            this.connectionArduino = new ConnectionArduino();

            connectionArduino.initialize();


            while(connectionArduino.getCompteur()<3 ){
                bar.setValue(connectionArduino.getCompteur()*30);
            }

            bar.setValue(100);

            if(connectionArduino.getCompteur()>2){

                String idCarte=connectionArduino.getIdCarte();

                if(action == 2){
                    SuppressionCarte suppressionCarte = new SuppressionCarte(this.frame);
                    suppressionCarte.recuperationInfo(idCarte);
                }else{
                    try {
                        this.creationCarte.ajoutBd(idCarte);
                    }catch (IOException e){

                    }catch (GeneralSecurityException e){

                    }
                }


            }

            connectionArduino.close();

        }

        public void actionPerformed(ActionEvent e){

            if(e.getSource() == this.bAnnuler){
                terminer();
                JOptionPane.showMessageDialog(null, "Opération Annulée", "Operation Annulée", JOptionPane.INFORMATION_MESSAGE);
            }


        }

    }


}


