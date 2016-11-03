// Fichier BDCreateur.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Permet de créer la BD utilisée par Biblio.java.
 * Paramètres:
 *   0- serveur SQL
 *   1- bd nom de la BD
 *   2- user id pour établir une connexion avec le serveur SQL
 *   3- mot de passe pour le user id
 *
 * @author Mathieu Lafond
 */
final class BDCreateur {
    /**
     * Constructeur privé pour empêcher toute instanciation.
     *
     * @author Primavera Sequeira Steven
     */
    private BDCreateur() {

        super();
    }

    /**
     * Crée la base de données nécessaire à l'application bibliothèque.
     *
     * Paramètres :
     *  0 - Type de serveur SQL de la BD
     *  1 - Nom du schéma de la base de données
     *  2 - Nom d'utilisateur sur le serveur SQL
     *  3 - Mot de passe sur le serveur SQL
     *
     * @param args - arguments de main, voir plus haut
     * @throws Exception - si une erreurSurvient
     * @throws SQLException - si une erreur survient
     */
    public static void main(String[] args) throws Exception,
        SQLException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        try(
            final Connexion connexion = new Connexion(args[0],
                args[1],
                args[2],
                args[3])) {

            try(
                final Statement statement = connexion.getConnection().createStatement()) {

                statement.executeUpdate("DROP TABLE membre CASCADE CONSTRAINTS");
                statement.executeUpdate("CREATE TABLE membre ( "
                    + "idMembre        number(3) check(idMembre > 0), "
                    + "nom             varchar(10) NOT NULL, "
                    + "telephone       number(10) , "
                    + "limitePret      number(2) check(limitePret > 0 and limitePret <= 10) , "
                    + "nbpret          number(2) default 0 check(nbpret >= 0) , "
                    + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                    + "CONSTRAINT limiteNbPret check(nbpret <= limitePret) "
                    + ")");

                statement.executeUpdate("DROP TABLE livre CASCADE CONSTRAINTS");
                statement.executeUpdate("CREATE TABLE livre ( "
                    + "idLivre         number(3) check(idLivre > 0) , "
                    + "titre           varchar(10) NOT NULL, "
                    + "auteur          varchar(10) NOT NULL, "
                    + "dateAcquisition date not null, "
                    + "idMembre        number(3) , "
                    + "datePret        date , "
                    + "CONSTRAINT cleLivre PRIMARY KEY (idLivre), "
                    + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre "
                    + ")");

                statement.executeUpdate("DROP TABLE reservation CASCADE CONSTRAINTS");
                statement.executeUpdate("CREATE TABLE reservation ( "
                    + "idReservation   number(3) , "
                    + "idMembre        number(3) , "
                    + "idLivre         number(3) , "
                    + "dateReservation date , "
                    + "CONSTRAINT cleReservation PRIMARY KEY (idReservation) , "
                    + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , "
                    + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre "
                    + "  ON DELETE CASCADE , "
                    + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre "
                    + "  ON DELETE CASCADE "
                    + ")");

                statement.close();
                connexion.close();
            }
        }
    }
}
