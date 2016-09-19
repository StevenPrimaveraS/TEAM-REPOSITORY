
package ca.qc.collegeahuntsic.bibliotheque.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Cette classe encapsule une connexion JDBC en fonction d'un type et d'une
 * instance de base de données.
 *
 * La méthode getServeursSupportes() indique les type de serveur supportés.
 *
 * Pré-condition : Le driver JDBC approprié doit être accessible. Post-condition
 * : La connexion est créée en mode autocommit false.
 *
 * @author Mathieu Lafond
 */
public class Connexion {

    private Connection conn;

    /**
     * Crée une connexion en mode autocommit false.
     * @param serveur -
     * @param bd -
     * @param user -
     * @param pass -
     * @throws SQLException -
     */
    public Connexion(String serveur,
        String bd,
        String user,
        String pass) throws SQLException {
        Driver d;
        try {
            if(serveur.equals("local")) {
                d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + bd,
                    user,
                    pass);
            }
            if(serveur.equals("distant")) {
                d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:oracle:thin:@collegeahuntsic.info:1521:"
                    + bd,
                    user,
                    pass);
            }
            /*
             * else if (serveur.equals("postgres")) { d = (Driver)
             * Class.forName("org.postgresql.Driver").newInstance();
             * DriverManager.registerDriver(d); conn =
             * DriverManager.getConnection( "jdbc:postgresql:" + bd, user,
             * pass); } else // access { d = (Driver)
             * Class.forName("org.postgresql.Driver").newInstance();
             * DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
             * conn = DriverManager.getConnection( "jdbc:odbc:" + bd, "", ""); }
             */

            // mettre en mode de commit manuel
            this.conn.setAutoCommit(false);

            // mettre en mode serialisable si possible
            // (plus haut niveau d'integrite l'acces concurrent aux donnees)
            final DatabaseMetaData dbmd = this.conn.getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode sérialisable :\n"
                    + "Estampille "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            } else {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
                    + "Heure "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            }
        } catch(SQLException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace(System.out);
            throw new SQLException("JDBC Driver non instancié");
        }

    }

    /**
     * fermeture d'une connexion.
     * @throws SQLException -
     */
    public void fermer() throws SQLException {
        this.conn.rollback();
        this.conn.close();
        System.out.println("Connexion fermée"
            + " "
            + this.conn);
    }

    /**
     * Effectue un commit sur la Connection JDBC.
     *
     * @throws SQLException
     *             - S'il y a une erreur avec la base de données
     */
    public void commit() throws SQLException {
        this.conn.commit();
    }

    /**
     * Effectue un rollback sur la Connection JDBC.
     *
     * @throws SQLException
     *             - S'il y a une erreur avec la base de données
     */
    public void rollback() throws SQLException {
        this.conn.rollback();
    }

    /**
     * Getter de la variable d'instance this.connection.
     *
     * @return - La valeur à utiliser pour la variable d'instance
     *         this.connection
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Retourne la liste des serveurs supportés par ce gestionnaire de connexion
     * :
     *
     * . local : MySQL installé localement distant : Oracle installé au
     * Département d'Informatique du Collège Ahuntsic postgres : Postgres
     * installé localement access : Microsoft Access installé localement et
     * inscrit dans ODBC
     *
     * @return La liste des serveurs supportés par ce gestionnaire de connexion
     */
    public static String serveursSupportes() {
        return "local : MySQL installé localement\n"
            + "distant : Oracle installé au Département d'Informatique du Collège Ahuntsic\n"
            + "postgres : Postgres installé localement\n"
            + "access : Microsoft Access installé localement et inscrit dans ODBC";
    }
} // Classe Connexion
