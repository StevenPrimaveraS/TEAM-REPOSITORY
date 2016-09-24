// Fichier PretService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table pret.
 *
 * @author Primavera Sequeira Steven
 */

public class PretService {

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private Connexion connexion;

    /**
     * Creation d'une instance. La connection de l'instance de livre et de
     * membre doit être la même que connexion, afin d'assurer l'intégrité des
     * transactions.
     *
     * @param livre parametre a utiliser de la classe LivreDAO
     * @param membre paramatere membreDAO a utiliser
     * @param reservation parametre reservationDAO à utiliser
     * @throws ServiceException type d'exception à utiliser
     */

    public PretService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws ServiceException {

        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new ServiceException("Les instances de livre, de membre et de reservation n'utilisent pas la même connexion au serveur");
        }
        this.connexion = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Pret d'un livre à un membre. Le livre ne doit pas être prêté. Le membre
     * ne doit pas avoir dépassé sa limite de pret.
     *
     * @param idLivre paramtère livre a donner en utilisant la méthode.
     * @param idMembre paramètre idMembre qu'on doit donner en utilisant la méthode.
     * @param datePret date dur prêt qu'on recoit dans la méthode.
     * @throws ServiceException - Si une erreur survient
     */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws ServiceException {
        try {
            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " deja prêté a "
                    + tupleLivre.getIdMembre());
            }

            /* Vérifie si le membre existe et sa limite de pret */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new ServiceException("Limite de pret du membre "
                    + idMembre
                    + " atteinte");
            }

            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                idMembre,
                datePret);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            final int nb2 = this.membre.preter(idMembre);
            if(nb2 == 0) {
                throw new ServiceException("Membre supprimé par une autre transaction");
            }
            try {
                this.connexion.commit();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch(DAOException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw new ServiceException(daoException);
        }
    }

    /**
     * Renouvellement d'un pret. Le livre doit être prêté. Le livre ne doit pas
     * être réservé.
     *
     * @param idLivre id du Livre qu'on veut renouveler.
     * @param datePret date de prêt du livre qu'on veut renouveler.            -
     * @throws ServiceException - Si une erreur survient.
     */
    public void renouveler(int idLivre,
        String datePret) throws ServiceException {
        try {
            /* Verifier si le livre est prêté */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }

            /* Verifier si date renouvellement >= datePret */
            if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de renouvellement inférieure à la date de prêt");
            }

            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                tupleLivre.getIdMembre(),
                datePret);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourner un livre prêté Le livre doit être prêté.
     *
     * @param idLivre id du livre qu'on retourne
     * @param dateRetour date du retour du livre qu'on veut retourner
     * @throws ServiceException - Si une erreur surtvient
     */
    public void retourner(int idLivre,
        String dateRetour) throws ServiceException{
        try {
            /* Verifier si le livre est prêté */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté ");
            }

            /* Verifier si date retour >= datePret */
            if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de retour inférieure à la date de prêt");
            }

            /* Retour du prêt. */
            final int nb1 = this.livre.retourner(idLivre);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }

            final int nb2 = this.membre.retourner(tupleLivre.getIdMembre());
            if(nb2 == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw new ServiceException(daoException);
        }
    }
}
