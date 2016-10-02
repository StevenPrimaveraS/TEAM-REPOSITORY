// Fichier ReservationService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.util.List;

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

public class ReservationService extends Service{
    private static final long serialVersionUID = 1L;
    
    private ReservationDAO reservationDAO;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;


    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO - reservation qu'on reçoit en paramètre dans la méthode
     * @param livreDAO - livre qu'on recoit en paramètre dans la méthode
     * @param membreDAO - membre qu'on recoit en paramètre dans la méthode
     */
    public ReservationService(ReservationDAO reservationDAO,
    	LivreDAO livreDAO,
        MembreDAO membreDAO) {
    	setReservationDAO(reservationDAO);
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
    }

    /**
     * Ajoute une nouvelle reservation.
     *
     * @param reservationDTO La reservation à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit une reservation. Si aucune reservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param idReservation L'ID du reservation à lire
     * @return La reservation lue ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws ServiceException {
        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour une reservation.
     *
     * @param reservationDTO La reservation à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime une reservation.
     *
     * @param reservationDTO La reservation à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve toutes les reservations.
     *
     * @return La liste des reservations ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws ServiceException {
        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
    //TODO Service
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
        String dateReservation) throws ServiceException {
        try {
            /* Verifier que le livre est prêté */
            final LivreDTO tupleLivre = this.livreDAO.getLivre(idLivre);
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
            final MembreDTO tupleMembre = this.membreDAO.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date réservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de reservation inférieure à la date de prêt");
            }

            /* Vérifier que la réservation n'existe pas */
            if(this.reservationDAO.existe(idReservation)) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " existe déjà");
            }

            /* Creation de la reservation */
            this.reservationDAO.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
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
        String datePret) throws ServiceException {
        try {
            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservationDAO.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new ServiceException("Réservation inexistante : "
                    + idReservation);
            }

            /* Vérifie que c'est la première réservation pour le livre */
            final ReservationDTO tupleReservationPremiere = this.reservationDAO.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new ServiceException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livreDAO.getLivre(tupleReservation.getIdLivre());
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
            final MembreDTO tupleMembre = this.membreDAO.getMembre(tupleReservation.getIdMembre());
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
            if(this.livreDAO.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            if(this.membreDAO.preter(tupleReservation.getIdMembre()) == 0) {
                throw new ServiceException("Membre supprimé par une autre transaction");
            }
            /* Eliminer la réservation */
            this.reservationDAO.annulerRes(idReservation);
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
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
    public void annulerRes(int idReservation) throws ServiceException {
        try {

            /* Vérifier que la réservation existe */
            if(this.reservationDAO.annulerRes(idReservation) == 0) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }
    //TODO Service End
    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
	private LivreDAO getLivreDAO() {
		return livreDAO;
	}


	/**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
	private void setLivreDAO(LivreDAO livreDAO) {
		this.livreDAO = livreDAO;
	}


    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
	private MembreDAO getMembreDAO() {
		return membreDAO;
	}


	/**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
	private void setMembreDAO(MembreDAO membreDAO) {
		this.membreDAO = membreDAO;
	}


    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
	private ReservationDAO getReservationDAO() {
		return reservationDAO;
	}


	/**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
	private void setReservationDAO(ReservationDAO reservationDAO) {
		this.reservationDAO = reservationDAO;
	}
}
