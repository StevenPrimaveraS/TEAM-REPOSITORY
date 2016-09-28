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
    
    private static final String ADD_REQUEST = "INSERT INTO membre(idmembre, nom, telephone, limitepret, nbpret)"
     + "VALUES (?, ?, ?, ?, ?, 0)";
    
    private static final String READ_REQUEST = "idMembre, nom, telephone, limitePret, nbpret"
     + "FROM membre"
     + "WHERE idMembre= ?";
    
    private static final String UPDATE_REQUEST_INCREMENT_NB_PRET = "UPDATE membre "
     + "set nbpret = nbPret + 1 where idMembre = ?"
     + "WHERE idMembre = ?";
    
     private static final String UPDATE_REQUEST_DECREMENT_NB_PRET = "UPDATE membre "
     + "set nbpret = nbPret - 1 where idMembre = ?"
     + "WHERE idMembre = ?";
    
    private static final String DELETE_REQUEST = "DELETE from membre"
     + "WHERE idMembre = ?";
/*
    private static final String GET_ALL_REQUEST = "select idMembre, nom, telephone, limitePret, nbpret"
    +"FROM membre";
*/
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public MembreDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Verifie si un membre existe.
     *
     * @param idMembre identifiant du membre
     * @return boolean si le livre existe ou pas
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public boolean existe(int idMembre) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idMembre);
            final ResultSet resultset = statementExiste.executeQuery();
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
     * @return MembreDTO retourne un DTO de membre
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public MembreDTO getMembre(int idMembre) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idMembre);
            final ResultSet resultset = statementExiste.executeQuery();
            if(resultset.next()) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(idMembre);
                membreDTO.setNom(resultset.getString(2));
                membreDTO.setTelephone(resultset.getLong(3));
                membreDTO.setLimitePret(resultset.getInt(4));
                membreDTO.setNbPret(resultset.getInt(5));
                resultset.close();
                return membreDTO;
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
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {
        try (PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            /* Ajout du membre. */
            statementInsert.setInt(1,
                idMembre);
            statementInsert.setString(2,
                nom);
            statementInsert.setLong(3,
                telephone);
            statementInsert.setInt(4,
                limitePret);
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Incrementer le nb de pret d'un membre.
     *
     * @param idMembre identifiant du membre
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int preter(int idMembre) throws DAOException {
        try (PreparedStatement statementUpdateIncrementNbPret = this.getConnexion().getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_INCREMENT_NB_PRET)) {
            statementUpdateIncrementNbPret.setInt(1,
                idMembre);
            return statementUpdateIncrementNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Decrementer le nb de pret d'un membre.
     *
     * @param idMembre identifiant du membre
     * @return int resultat de la commande de retour
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int retourner(int idMembre) throws DAOException {
        try (PreparedStatement statementUpdateDecrementNbPret = this.getConnexion().getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_DECREMENT_NB_PRET)) {
            statementUpdateDecrementNbPret.setInt(1,
                idMembre);
            return statementUpdateDecrementNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Suppression d'un membre.
     *
     * @param idMembre identifiant du membre
     * @return int resultat de la suppression
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int desinscrire(int idMembre) throws DAOException {
        try (PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idMembre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
