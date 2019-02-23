import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Activation extends Panneau implements ActionListener {

    private JComboBox<String> comboBoxPersonnes;

    Activation(Hub frame) {
        super(frame, "Activation / Desactivation des cartes");

        JLabel info = new JLabel("Selectionnez le nom de la personne dont vous souhaitez activer/desactiver la carte", JLabel.CENTER);
        info.setSize(frame.getWidth(), 25);
        info.setLocation(0, frame.getHeight() / 8 * 3 - 25);
        this.add(info);

        JButton bActivation = new JButton("Activer");
        bActivation.setSize(100, 50);
        bActivation.setLocation(frame.getWidth() / 4 - 50, frame.getHeight() / 8 * 5 + frame.getHeight() / 16 - 25);
        bActivation.setBackground(Color.GREEN);
        bActivation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activation();
            }
        });
        this.add(bActivation);

        JButton bDesactivation = new JButton("Desactiver");
        bDesactivation.setSize(100, 50);
        bDesactivation.setLocation(frame.getWidth() / 4 * 3 - 50, frame.getHeight() / 8 * 5 + frame.getHeight() / 16 - 25);
        bDesactivation.setBackground(Color.GREEN);
        bDesactivation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desactivation();
            }
        });
        this.add(bDesactivation);

        comboBoxPersonne();

        this.frame.setContentPane(this);
        this.frame.repaint();
        this.frame.revalidate();

    }

    private void comboBoxPersonne() {

        this.comboBoxPersonnes = new JComboBox<>();

        try {
            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT nom, prenom FROM Personne WHERE profil=false ORDER BY prenom;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nomPrenom = resultSet.getString("prenom") + " " + resultSet.getString("nom");
                this.comboBoxPersonnes.addItem(nomPrenom);
            }

            connection.close();

        } catch (SQLException e) {
            System.err.println(e);
        }

        this.comboBoxPersonnes.setSize(150, 25);
        this.comboBoxPersonnes.setLocation(175, 238);
        this.add(this.comboBoxPersonnes);
    }

    private void activation() {

        String personne = (String) this.comboBoxPersonnes.getSelectedItem();

        try {
            if (!personne.equals("")) {
                String[] data = personne.split(" ");
                String prenom = data[0];
                String nom = data[1];

                Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

                String query = "SELECT active, Carte.pseudo FROM Carte, Personne WHERE Carte.pseudo=Personne.pseudo and prenom=? and nom=?;";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, prenom);
                preparedStatement.setString(2, nom);
                ResultSet rs = preparedStatement.executeQuery();

                boolean active = false;
                String pseudo = "";

                while (rs.next()) {
                    active = rs.getBoolean("active");
                    pseudo = rs.getString("pseudo");
                }

                if (!active) {
                    query = "UPDATE Carte SET active=true WHERE pseudo=?;";

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, pseudo);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "La carte de " + prenom + " " + nom + " est maintenant active", "Activation reussie", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "La carte de " + prenom + " " + nom + " est deja active", "Erreur activation", JOptionPane.WARNING_MESSAGE);
                }
                connection.close();
            }

        } catch (SQLException o) {
            System.err.println(o);
        }
    }


    private void desactivation() {
        String personne = (String) this.comboBoxPersonnes.getSelectedItem();

        try {
            String[] data = personne.split(" ");
            String prenom = data[0];
            String nom = data[1];

            Connection connection = DriverManager.getConnection(this.frame.serveur, this.pseudoAdmin, this.motsDePasseAdmin);

            String query = "SELECT active, Carte.pseudo FROM Carte, Personne WHERE Carte.pseudo=Personne.pseudo and prenom=? and nom=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, prenom);
            preparedStatement.setString(2, nom);
            ResultSet rs = preparedStatement.executeQuery();

            boolean active = false;
            String pseudo = "";

            while (rs.next()) {
                active = rs.getBoolean("active");
                pseudo = rs.getString("pseudo");
            }

            if (active) {

                query = "UPDATE Carte SET active=false WHERE pseudo=?;";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, pseudo);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "La carte de " + prenom + " " + nom + " est maintenant desactivee", "Activation reussie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "La carte de " + prenom + " " + nom + " est deja desactivee", "Erreur activation", JOptionPane.WARNING_MESSAGE);
            }

            connection.close();

        } catch (SQLException o) {

            System.err.println(o);

        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.deconnection) {

            new Identification(this.frame);

        }

        if (e.getSource() == this.retour) {

            new MenuGestionCarte(this.frame);

        }
    }

}
