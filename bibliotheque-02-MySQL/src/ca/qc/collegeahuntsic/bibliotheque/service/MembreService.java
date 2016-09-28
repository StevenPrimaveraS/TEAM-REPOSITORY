// Fichier MembreService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table membre.
 *
 * @author Primavera Sequeira Steven
 */

public class MembreService {

    private Connexion connexion;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Création d'une instance.
     *
     * @param membre MembreDao qu'on reçoit en paramètre.
     * @param reservation ReservationDAO qu'on reçoit en paramètre.
     */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        this.connexion = membre.getConnexion();
        this.membreDAO = membre;
        this.reservationDAO = reservation;
    }

    /**
     * Ajout d'un nouveau membre dans la base de donnée.
     * S'il existe déja, une exception est levée.
     *
     * @param idMembre id du membre qu'on veut inscrire.
     * @param nom nom du membre qu'on veut inscrire.
     * @param telephone numéro de téléphone du membre qu'on veut inscrire.
     * @param limitePret limite de prêt du membre qu'on veut inscrire.
     * @throws ServiceException - Si une erreur survient.
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {
        try {
            /* Vérifie si le membre existe déjà */
            if(this.membreDAO.existe(idMembre)) {
                throw new ServiceException("Membre existe déjà: "
                    + idMembre);
            }

            /* Ajout du membre. */
            this.membreDAO.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.connexion.commit();
        } catch(DAOException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }

    /**
     * Suppression d'un membre de la base de données.
     *
     * @param idMembre id du membre qu'on veut désinscrire.
     * @throws ServiceException - Si une erreur survient
     */
    public void desinscrire(int idMembre) throws ServiceException {
        try {
            /* Vérifie si le membre existe et son nombre de prêt en cours */
            final MembreDTO tupleMembre = this.membreDAO.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(this.reservationDAO.getReservationMembre(idMembre) != null) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            final int nb = this.membreDAO.desinscrire(idMembre);
            if(nb == 0) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.connexion.commit();
        } catch(DAOException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }
} //class
