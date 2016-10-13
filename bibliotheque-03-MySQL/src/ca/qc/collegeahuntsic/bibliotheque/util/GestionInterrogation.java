// Fichier GestionInterrogation.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 * Gestion des transactions d'interrogation dans une bibliothèque.
 * Ce programme permet de faire diverses interrogations
 * sur l'état de la bibliothèque.
 *
 * Pré-condition :
 *   la base de données de la bibliothèque doit exister.
 * Post-condition :
 *   le programme effectue les maj associées à chaque transaction.
 *
 * @author Mathieu Lafond
 */

public class GestionInterrogation {

    private PreparedStatement statementLivresTitreMot;

    private PreparedStatement statementListeTousLivres;

    private Connexion connexion;

    /**
     * Creation d'une instance.
     *
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     * @param connexion - connexion de la classe
     */
    public GestionInterrogation(Connexion connexion) throws SQLException {

        this.connexion = connexion;
        this.statementLivresTitreMot = connexion.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 "
            + "from livre t1 "
            + "where lower(titre) like ?");

        this.statementListeTousLivres = connexion.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret "
            + "from livre t1");
    }

    /**
     * Affiche les livres contenu un mot dans le titre.
     *
     * @param mot - String reçue en paramètre pour lister les livres
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     */
    public void listerLivresTitre(String mot) throws SQLException {

        this.statementLivresTitreMot.setString(1,
            "%"
                + mot
                + "%");
        final ResultSet resultset = this.statementLivresTitreMot.executeQuery();

        int idMembre;
        System.out.println("idLivre titre auteur idMembre dateRetour");
        while(resultset.next()) {
            System.out.print(resultset.getInt(1)
                + " "
                + resultset.getString(2)
                + " "
                + resultset.getString(3));
            idMembre = resultset.getInt(4);
            if(!resultset.wasNull()) {
                System.out.print(" "
                    + idMembre
                    + " "
                    + resultset.getDate(5));
            }
            System.out.println();
        }
        try {
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            connexionException.printStackTrace();
        }
        resultset.close();
    }

    /**
     * Affiche tous les livres de la BD.
     *
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     */
    public void listerLivres() throws SQLException {

        final ResultSet resultset = this.statementListeTousLivres.executeQuery();

        System.out.println("idLivre titre auteur idMembre datePret");
        int idMembre;
        while(resultset.next()) {
            System.out.print(resultset.getInt("idLivre")
                + " "
                + resultset.getString("titre")
                + " "
                + resultset.getString("auteur"));
            idMembre = resultset.getInt("idMembre");
            if(!resultset.wasNull()) {
                System.out.print(" "
                    + idMembre
                    + " "
                    + resultset.getDate("datePret"));
            }
            System.out.println();
        }
        try {
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            connexionException.printStackTrace();
        }
        resultset.close();
    }
}
