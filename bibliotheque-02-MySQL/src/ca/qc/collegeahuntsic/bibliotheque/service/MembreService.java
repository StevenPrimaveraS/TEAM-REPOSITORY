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
 * Gestion des transactions de reliées à la création et
 * suppression de membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transaction reliées à la
 * création et suppression de membres.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 *  @author Primavera Sequeira Steven
 */

public class MembreService {

    private Connexion cx;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**
      * Création d'une instance.
      * @param membre MembreDao qu'on reçoit en paramètre.
      * @param reservation ReservationDAO qu'on reçoit en paramètre.
      */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau membre dans la base de donnée.
      * S'il existe déja, une exception est levée.
      * @param idMembre id du membre qu'on veux inscrire.
      * @param nom nom du membre qu'on veut inscrire.
      * @param telephone numéro de téléphone du membre qu'on veux inscrire.
      * @param limitePret limite de prêt du membre qu'on veux inscrire.
      * @throws ServiceException -
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {
        try {
            /* Vérifie si le membre existe déja */
            if(this.membre.existe(idMembre)) {
                throw new ServiceException("Membre existe déja: "
                    + idMembre);
            }

            /* Ajout du membre. */
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(DAOException daoException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }

    /**
      * Suppression d'un membre de la base de données.
      * @param idMembre id du membre qu'on veux désinscrire.
      * @throws ServiceException -
      */
    public void desinscrire(int idMembre) throws ServiceException {
        try {
            /* Vérifie si le membre existe et son nombre de prêt en cours */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(DAOException daoException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }
} //class
