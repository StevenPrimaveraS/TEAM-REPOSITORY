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

    private PreparedStatement stmtLivresTitreMot;

    private PreparedStatement stmtListeTousLivres;

    private Connexion connexion;

    /**
     * Creation d'une instance.
     *
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     * @param connexion - connexion de la classe
     */
    public GestionInterrogation(Connexion connexion) throws SQLException {

        this.connexion = connexion;
        this.stmtLivresTitreMot = connexion.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 "
            + "from livre t1 "
            + "where lower(titre) like ?");

        this.stmtListeTousLivres = connexion.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret "
            + "from livre t1");
    }

    /**
     * Affiche les livres contenu un mot dans le titre.
     *
     * @param mot - String reçue en paramètre pour lister les livres
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     */
    public void listerLivresTitre(String mot) throws SQLException {

        this.stmtLivresTitreMot.setString(1,
            "%"
                + mot
                + "%");
        final ResultSet rset = this.stmtLivresTitreMot.executeQuery();

        int idMembre;
        System.out.println("idLivre titre auteur idMembre dateRetour");
        while(rset.next()) {
            System.out.print(rset.getInt(1)
                + " "
                + rset.getString(2)
                + " "
                + rset.getString(3));
            idMembre = rset.getInt(4);
            if(!rset.wasNull()) {
                System.out.print(" "
                    + idMembre
                    + " "
                    + rset.getDate(5));
            }
            System.out.println();
        }
        try {
            this.connexion.commit();
        } catch(ConnexionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rset.close();
    }

    /**
     * Affiche tous les livres de la BD.
     *
     * @throws SQLException - Exception SQl pour la classe GestionInterrogation
     */
    public void listerLivres() throws SQLException {

        final ResultSet rset = this.stmtListeTousLivres.executeQuery();

        System.out.println("idLivre titre auteur idMembre datePret");
        int idMembre;
        while(rset.next()) {
            System.out.print(rset.getInt("idLivre")
                + " "
                + rset.getString("titre")
                + " "
                + rset.getString("auteur"));
            idMembre = rset.getInt("idMembre");
            if(!rset.wasNull()) {
                System.out.print(" "
                    + idMembre
                    + " "
                    + rset.getDate("datePret"));
            }
            System.out.println();
        }
        try {
            this.connexion.commit();
        } catch(ConnexionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rset.close();
    }
}
