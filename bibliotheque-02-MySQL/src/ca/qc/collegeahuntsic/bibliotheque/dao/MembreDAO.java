// Fichier MembreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private static final String ADD_REQUEST = "INSERT INTO membre"
        + "VALUES (?, ?, ?, ?, ?, 0)";

    private static final String READ_REQUEST = "idMembre, nom, telephone, limitePret, nbpret"
        + "FROM membre"
        + "WHERE idMembre= ?";

    private static final String UPDATE_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = ? "
        + "WHERE idMembre = ?";

    private static final String UPDATE_REQUEST_INCREMENT_NB_PRET = "UPDATE membre "
        + "SET nbpret = nbPret + 1 "
        + "WHERE idMembre = ?";

    private static final String UPDATE_REQUEST_DECREMENT_NB_PRET = "UPDATE membre "
        + "SET nbpret = nbPret - 1 "
        + "WHERE idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE from membre"
        + "WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idMembre, "
        + "                                               nom, "
        + "                                               telephone, "
        + "                                               limitePret, "
        + "                                               nbPret, "
        + "                                        FROM   membre";

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
    // Source > Toggle Comment | Au cas où nécessaire plus tard
    //    /**
    //     * Verifie si un membre existe.
    //     *
    //     * @param idMembre identifiant du membre
    //     * @return boolean si le livre existe ou pas
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public boolean existe(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementExiste = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
    //            statementExiste.setInt(1,
    //                idMembre);
    //            final ResultSet resultset = statementExiste.executeQuery();
    //            final boolean membreExiste = resultset.next();
    //            resultset.close();
    //            return membreExiste;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Lecture d'un membre.
    //     *
    //     * @param idMembre identifiant du membre.
    //     * @return MembreDTO retourne un DTO de membre
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public MembreDTO getMembre(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementExiste = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
    //            statementExiste.setInt(1,
    //                idMembre);
    //            final ResultSet resultset = statementExiste.executeQuery();
    //            if(resultset.next()) {
    //                final MembreDTO membreDTO = new MembreDTO();
    //                membreDTO.setIdMembre(idMembre);
    //                membreDTO.setNom(resultset.getString(2));
    //                membreDTO.setTelephone(resultset.getLong(3));
    //                membreDTO.setLimitePret(resultset.getInt(4));
    //                membreDTO.setNbPret(resultset.getInt(5));
    //                resultset.close();
    //                return membreDTO;
    //            }
    //            resultset.close();
    //            return null;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Ajout d'un nouveau membre.
    //     *
    //     * @param idMembre identificateur du membre
    //     * @param nom nom du membre
    //     * @param telephone numero de telephone du membre
    //     * @param limitePret limite de pret du membre
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public void inscrire(int idMembre,
    //        String nom,
    //        long telephone,
    //        int limitePret) throws DAOException {
    //        try(
    //            PreparedStatement statementInsert = getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
    //            /* Ajout du membre. */
    //            statementInsert.setInt(1,
    //                idMembre);
    //            statementInsert.setString(2,
    //                nom);
    //            statementInsert.setLong(3,
    //                telephone);
    //            statementInsert.setInt(4,
    //                limitePret);
    //            statementInsert.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Incrementer le nb de pret d'un membre.
    //     *
    //     * @param idMembre identifiant du membre
    //     * @return int resultat de la commande de pret
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public int preter(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementUpdateIncrementNbPret = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_INCREMENT_NB_PRET)) {
    //            statementUpdateIncrementNbPret.setInt(1,
    //                idMembre);
    //            return statementUpdateIncrementNbPret.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Decrementer le nb de pret d'un membre.
    //     *
    //     * @param idMembre identifiant du membre
    //     * @return int resultat de la commande de retour
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public int retourner(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementUpdateDecrementNbPret = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_DECREMENT_NB_PRET)) {
    //            statementUpdateDecrementNbPret.setInt(1,
    //                idMembre);
    //            return statementUpdateDecrementNbPret.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Suppression d'un membre.
    //     *
    //     * @param idMembre identifiant du membre
    //     * @return int resultat de la suppression
    //     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    //     */
    //    public int desinscrire(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementDelete = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
    //            statementDelete.setInt(1,
    //                idMembre);
    //            return statementDelete.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO - Le membre à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement statementInsert = getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            /* Ajout du membre. */
            statementInsert.setInt(1,
                membreDTO.getIdMembre());
            statementInsert.setString(2,
                membreDTO.getNom());
            statementInsert.setLong(3,
                membreDTO.getTelephone());
            statementInsert.setInt(4,
                membreDTO.getLimitePret());
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un membre.
     *
     * @param idMembre - L'ID du membre à lire
     * @return Le membre
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public MembreDTO read(int idMembre) throws DAOException {
        MembreDTO membreDTO = null;
        try(
            PreparedStatement statementExiste = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idMembre);
            try(
                ResultSet resultset = statementExiste.executeQuery()) {
                if(resultset.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(idMembre);
                    membreDTO.setNom(resultset.getString(2));
                    membreDTO.setTelephone(resultset.getLong(3));
                    membreDTO.setLimitePret(resultset.getInt(4));
                    membreDTO.setNbPret(resultset.getInt(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membreDTO;
    }

    /**
     * Met à jour un membre.
     *
     * @param membreDTO - Le membre à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
            /* Ajout du membre. */
            statementUpdate.setString(1,
                membreDTO.getNom());
            statementUpdate.setLong(2,
                membreDTO.getTelephone());
            statementUpdate.setInt(3,
                membreDTO.getLimitePret());
            statementUpdate.setInt(4,
                membreDTO.getNbPret());
            statementUpdate.setInt(5,
                membreDTO.getIdMembre());
            statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO - Le membre à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                membreDTO.getIdMembre());
            statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon.
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<MembreDTO> getAll() throws DAOException {
        List<MembreDTO> membres = Collections.EMPTY_LIST;
        try(
            PreparedStatement getAllPreparedStatement = getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                MembreDTO membreDTO = null;
                if(resultSet.next()) {
                    membres = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(1));
                        membreDTO.setNom(resultSet.getString(2));
                        membreDTO.setTelephone(resultSet.getLong(3));
                        membreDTO.setLimitePret(resultSet.getInt(4));
                        membreDTO.setNbPret(resultSet.getInt(5));
                        membres.add(membreDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membres;
    }

    /**
    * Emprunte un livre.
    *
    * @param membreDTO - Le membre à mettre à jour
    * @return int resultat de la commande de pret
    * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
    */
    public int emprunter(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement statementUpdateIncrementNbPret = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_INCREMENT_NB_PRET)) {
            statementUpdateIncrementNbPret.setInt(1,
                membreDTO.getIdMembre());
            return statementUpdateIncrementNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO - Le membre à mettre à jour
     * @return int resultat de la commande de retour
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int retourner(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement statementUpdateDecrementNbPret = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST_DECREMENT_NB_PRET)) {
            statementUpdateDecrementNbPret.setInt(1,
                membreDTO.getIdMembre());
            return statementUpdateDecrementNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
