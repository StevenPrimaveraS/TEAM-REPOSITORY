// Fichier MembreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mathieu Lafond
 */

public class MembreDAO extends DAO {
    private static final long serialVersionUID = 1L;
    /*
    private static final String ADD_REQUEST = "INSERT INTO membre(idmembre, nom, telephone, limitepret, nbpret)"
     + "VALUES (?, ?, ?, ?, ?, 0)";
    
    private static final String READ_REQUEST = "idMembre, nom, telephone, limitePret, nbpret"
     + "FROM membre"
     + "WHERE idMembre= ?";
    
    private static final String UPDATE_REQUEST_INCR_NB_PRET = "UPDATE membre "
     + "set nbpret = nbPret + 1 where idMembre = ?"
     + "WHERE idMembre = ?";
    
     private static final String UPDATE_REQUEST_DEC_NB_PRET = "UPDATE membre "
     + "set nbpret = nbPret - 1 where idMembre = ?"
     + "WHERE idMembre = ?";
    
    private static final String DELETE_REQUEST = "DELETE from membre"
     + "WHERE idMembre = ?";
    
    private static final String GET_ALL_REQUEST = "select idMembre, nom, telephone, limitePret, nbpret"
    +"FROM membre";
    */

    private PreparedStatement statementExiste;

    private PreparedStatement statementInsert;

    private PreparedStatement statementupdateIncrNbPret;

    private PreparedStatement statementUpdateDecNbPret;

    private PreparedStatement statementDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     * @throws SQLException si une erreur survient
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public MembreDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.statementExiste = connexion.getConnection()
                .prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
            this.statementInsert = connexion.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
                + "values (?,?,?,?,0)");
            this.statementupdateIncrNbPret = connexion.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
            this.statementUpdateDecNbPret = connexion.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
            this.statementDelete = connexion.getConnection().prepareStatement("delete from membre where idmembre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Verifie si un membre existe.
     *
     * @param idMembre identifiant du membre
     * @return boolean si le livre existe ou pas
     * @throws SQLException si une erreur survient
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public boolean existe(int idMembre) throws DAOException {
        try {
            this.statementExiste.setInt(1,
                idMembre);
            final ResultSet resultset = this.statementExiste.executeQuery();
            final boolean membreExiste = resultset.next();
            resultset.close();
            return membreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lecture d'un membre.
     *
     * @param idMembre identifiant du membre.
     * @throws SQLException si une erreur survient.
     * @return MembreDTO.
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public MembreDTO getMembre(int idMembre) throws DAOException {
        try {
            this.statementExiste.setInt(1,
                idMembre);
            final ResultSet resultset = this.statementExiste.executeQuery();
            if(resultset.next()) {
                final MembreDTO tupleMembre = new MembreDTO();
                tupleMembre.setIdMembre(idMembre);
                tupleMembre.setNom(resultset.getString(2));
                tupleMembre.setTelephone(resultset.getLong(3));
                tupleMembre.setLimitePret(resultset.getInt(4));
                tupleMembre.setNbPret(resultset.getInt(5));
                resultset.close();
                return tupleMembre;
            }
            resultset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Ajout d'un nouveau membre.
     *
     * @param idMembre identificateur du membre
     * @param nom nom du membre
     * @param telephone numero de telephone du membre
     * @param limitePret limite de pret du membre
     * @throws SQLException si une erreur survient
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {
        try {
            /* Ajout du membre. */
            this.statementInsert.setInt(1,
                idMembre);
            this.statementInsert.setString(2,
                nom);
            this.statementInsert.setLong(3,
                telephone);
            this.statementInsert.setInt(4,
                limitePret);
            this.statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Incrementer le nb de pret d'un membre.
     *
     * @param idMembre identifiant du membre
     * @throws SQLException si une erreur survient
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int preter(int idMembre) throws DAOException {
        try {
            this.statementupdateIncrNbPret.setInt(1,
                idMembre);
            return this.statementupdateIncrNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Decrementer le nb de pret d'un membre.
     *
     * @param idMembre identifiant du membre
     * @throws SQLException si une erreur survient
     * @return int resultat de la commande de retour
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int retourner(int idMembre) throws DAOException {
        try {
            this.statementUpdateDecNbPret.setInt(1,
                idMembre);
            return this.statementUpdateDecNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Suppression d'un membre.
     *
     * @param idMembre identifiant du membre
     * @throws SQLException si une erreur survient
     * @return int resultat de la suppression
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int desinscrire(int idMembre) throws DAOException {
        try {
            this.statementDelete.setInt(1,
                idMembre);
            return this.statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
