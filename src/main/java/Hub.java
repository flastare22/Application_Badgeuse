import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.KeyStore;
import java.security.Security;

public class Hub extends JFrame implements ActionListener {

    protected int hauteur;
    protected int largeur;
    protected int largeurUtile;
    protected int hauteurUtile;
    protected String serveur;
    protected String pseudoAdmin;
    protected String motsDePasseAdmin;
    private JPasswordField storePass;
    private JPasswordField keyPass;
    private JButton bValider;
    private JFrame jFrame;

    Hub(int interger){

    }

    public Hub(){

        super("Badgeuse Le Helloco");

        this.serveur= "";

        pass();

        while (this.serveur == ""){}

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
        this.largeur=500;
        this.hauteur=500;
        this.largeurUtile=(int) maximumWindowBounds.getWidth();
        this.hauteurUtile= (int) maximumWindowBounds.getHeight();
        smallScreen();

        new Identification(this);
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.revalidate();
    }

    void setAdmin(String pseudoAdmin, String motsDePasseAdmin){
        this.pseudoAdmin = pseudoAdmin;
        this.motsDePasseAdmin = motsDePasseAdmin;
    }

    public void setServeur(String serveur){
        this.serveur = serveur;
    }

    public String getServeur(){
        return this.serveur;
    }

    void bigScreen(){
        this.setSize(largeurUtile, hauteurUtile);
        this.setResizable(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);//Attention a mettre après le setVisible
        this.revalidate();
    }

    void smallScreen(){

        this.setResizable(false);
        this.setSize(this.largeur,this.hauteur);
        this.setVisible(true);
        this.setLocationRelativeTo(null);//Attention a mettre après le setVisible
        this.revalidate();

    }

    void pass(){

        Security.addProvider(new BouncyCastleProvider());

        this.jFrame = new JFrame("Identification");
        this.jFrame.setUndecorated(true);
        this.jFrame.setSize(300,130);
        this.jFrame.setResizable(false);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setVisible(true);
        this.jFrame.setLayout(null);

        JPanel jPanel = new JPanel();
        jPanel.setSize(300,150);
        jPanel.setLocation(0,0);
        jPanel.setLayout(null);
        jPanel.setBackground(Color.white);

        JLabel lMotDePassKeyStore = new JLabel("Mots de Passe Keystore");
        lMotDePassKeyStore.setSize(140,25);
        lMotDePassKeyStore.setLocation(10,10);
        jPanel.add(lMotDePassKeyStore);

        storePass = new JPasswordField();
        storePass.setSize(100,25);
        storePass.setLocation(175, 10);
        jPanel.add(storePass);

        JLabel lMotDePasseKey = new JLabel("Mots de Passe Key");
        lMotDePasseKey.setSize(140,25);
        lMotDePasseKey.setLocation(10,50);
        jPanel.add(lMotDePasseKey);

        keyPass = new JPasswordField();
        keyPass.setSize(100,25);
        keyPass.setLocation(175, 50);
        jPanel.add(keyPass);

        this.bValider = new JButton("Ok");
        bValider.setSize(50,25);
        bValider.setLocation(125, 90);
        bValider.setBackground(Color.green);
        bValider.addActionListener(this);
        jPanel.add(this.bValider);

        jFrame.setContentPane(jPanel);

    }

    String decrypte(){

        String keystorePass = new String(storePass.getPassword());
        String keyPassWord = new String(keyPass.getPassword());

        String content = "";

        try{

            InputStream keystoreStream = new FileInputStream("./src/main/java/com/fraw/store/keystore.jck");
            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, keystorePass.toCharArray());
            if (!keystore.containsAlias("badgeuse")) {
                throw new RuntimeException("Alias for key not found");
            }
            SecretKey key = (SecretKey) keystore.getKey("badgeuse", keyPassWord.toCharArray());

            Cipher cipher = Cipher.getInstance("AES");

            try (FileInputStream fileIn = new FileInputStream("./src/main/java/com/fraw/store/serveur_crypte.txt")) {

                cipher.init(Cipher.DECRYPT_MODE, key);

                try (
                        CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                        InputStreamReader inputReader = new InputStreamReader(cipherIn);
                        BufferedReader reader = new BufferedReader(inputReader)
                ) {

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    content = sb.toString();
                }

            }

        }catch (Exception e){
            System.err.println(e);
            System.exit(1);
        }

        return content;
    }

    public static void main (String [] args){

        Security.addProvider(new BouncyCastleProvider());

        try{
            InputStream keystoreStream = new FileInputStream("./src/main/java/com/fraw/store/keystore.jck");
            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, "20tocima02".toCharArray());
            if (!keystore.containsAlias("badgeuse")) {
                throw new RuntimeException("Alias for key not found");
            }
            SecretKey key = (SecretKey) keystore.getKey("badgeuse", "apiipa".toCharArray());

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key); // Initialisation en mode cryptage

            FileInputStream fis = new FileInputStream("./src/main/java/com/fraw/store/serveur.txt");
            FileOutputStream fos = new FileOutputStream(new File("./src/main/java/com/fraw/store/serveur_crypte.txt"));
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] b = new byte[64];

            try {
                while ((bis.read(b)) >= 0) {
                    cos.write(b);
                }
            } finally {
                cos.close();
                bis.close();
            }

        }catch (Exception e){
            System.err.println(e);
        }

    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.bValider){
            this.serveur = decrypte();
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        }

    }

}
