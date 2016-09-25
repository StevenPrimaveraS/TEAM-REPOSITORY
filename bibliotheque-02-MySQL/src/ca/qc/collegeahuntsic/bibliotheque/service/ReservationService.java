// Fichier ReservationService.java
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
 * Service de la table reservation.
 *
 * @author Primavera Sequeira Steven
 */

public class ReservationService {

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private Connexion connexion;

    /**
     * Crée le service de la table reservation.
     *
     * @param livre - livre qu'on recoit en paramètre dans la méthode
     * @param membre - membre qu'on recoit en paramètre dans la méthode
     * @param reservation - reservation qu'on reçoit en paramètre dans la méthode
     * @throws ServiceException - si une erreur survient
     */
    public ReservationService(LivreDAO livre,
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
     * Réservation d'un livre par un membre. Le livre doit être prété.
     *
     * @param idReservation - id de la réservation qu'on veut réserver.
     * @param idLivre - id du livre qu'on veut réserver.
     * @param idMembre - id du livre qu'on veut réserver
     * @param dateReservation - date de la réservation.
     * @throws ServiceException - Si une erreur survient
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws ServiceException{
        try {
            /* Verifier que le livre est prêté */
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
            if(tupleLivre.getIdMembre() == idMembre) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " déjà prêté à ce membre");
            }

            /* Vérifier que le membre existe */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date réservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de reservation inférieure à la date de prêt");
            }

            /* Vérifier que la réservation n'existe pas */
            if(this.reservation.existe(idReservation)) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " existe déjà");
            }

            /* Creation de la reservation */
            this.reservation.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.connexion.commit();
        } catch(DAOException | ConnexionException exception) {
            try {
				this.connexion.rollback();
			} catch (ConnexionException connexionException) {
				throw new ServiceException(connexionException);
			}
            throw new ServiceException(exception);
        }
    }

    /**
     * Prise d'une réservation. Le livre ne doit pas être prété. Le membre ne
     * doit pas avoir dépassé sa limite de pret. La réservation doit la être la
     * première en liste.
     *
     * @param idReservation - id de la réservation.
     * @param datePret - date du prêt de la réservation.
     * @throws ServiceException - Si une erreur survient
     */
    public void prendreRes(int idReservation,
        String datePret) throws ServiceException{
        try {
            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new ServiceException("Réservation inexistante : "
                    + idReservation);
            }

            /* Vérifie que c'est la première réservation pour le livre */
            final ReservationDTO tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new ServiceException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(tupleReservation.getIdLivre());
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + tupleReservation.getIdLivre());
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + tupleLivre.getIdLivre()
                    + " deja prêté à "
                    + tupleLivre.getIdMembre());
            }

            /* Vérifie si le membre existe et sa limite de pret */
            final MembreDTO tupleMembre = this.membre.getMembre(tupleReservation.getIdMembre());
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + tupleReservation.getIdMembre());
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + tupleReservation.getIdMembre()
                    + " atteinte");
            }

            /* Verifier si datePret >= tupleReservation.dateReservation */
            if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
                throw new ServiceException("Date de prêt inférieure à la date de réservation");
            }

            /* Enregistrement du pret. */
            if(this.livre.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.getIdMembre()) == 0) {
                throw new ServiceException("Membre supprimé par une autre transaction");
            }
            /* Eliminer la réservation */
            this.reservation.annulerRes(idReservation);
            this.connexion.commit();
        } catch(DAOException | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
            	throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     * Annulation d'une réservation. La réservation doit exister.
     *
     * @param idReservation - id de la réservation qu'on veux annuler.
     * @throws ServiceException - Si une erreur survient.
     */
    public void annulerRes(int idReservation) throws ServiceException{
        try {

            /* Vérifier que la réservation existe */
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.connexion.commit();
        } catch(DAOException | ConnexionException exception) {
            try {
				this.connexion.rollback();
			} catch (ConnexionException connexionException) {
				throw new ServiceException(connexionException);
			}
            throw new ServiceException(exception);
        }
    }
}
