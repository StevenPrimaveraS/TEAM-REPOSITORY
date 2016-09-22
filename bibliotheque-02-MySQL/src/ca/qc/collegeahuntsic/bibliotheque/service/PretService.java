
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.SQLException;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions de reli�es aux pr�ts de livres aux membres dans une
 * biblioth�que.
 *
 * Ce programme permet de g�rer les transactions pr�ter, renouveler et
 * retourner.
 *
 * Pr�-condition la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition le programme effectue les maj associ�es � chaque transaction
 *
 * @author Primavera Sequeira Steven
 */

public class PretService {

	private LivreDAO livre;

	private MembreDAO membre;

	private ReservationDAO reservation;

	private Connexion cx;

	/**
	 * Creation d'une instance. La connection de l'instance de livre et de
	 * membre doit �tre la m�me que cx, afin d'assurer l'int�grit� des
	 * transactions.
	 *
	 * @param livre
	 *            parametre a utiliser de la classe LivreDAO
	 * @param membre
	 *            paramatere membreDAO a utiliser
	 * @param reservation
	 *            parametre reservationDAO à utiliser
	 * @throws BibliothequeException
	 *             type d'exception à utiliser
	 */

	public PretService(LivreDAO livre, MembreDAO membre, ReservationDAO reservation) throws ServiceException {
		try {
			if (livre.getConnexion() != membre.getConnexion() || reservation.getConnexion() != membre.getConnexion()) {
				throw new ServiceException(
						"Les instances de livre, de membre et de reservation n'utilisent pas la m�me connexion au serveur");
			}
			this.cx = livre.getConnexion();
			this.livre = livre;
			this.membre = membre;
			this.reservation = reservation;
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}

	}

	/**
	 * Pret d'un livre � un membre. Le livre ne doit pas �tre pr�t�. Le membre
	 * ne doit pas avoir d�pass� sa limite de pret.
	 *
	 * @param idLivre
	 *            paramtere livre a donner en utilisant la méthode.
	 * @param idMembre
	 *            paramadetre idMembre qu'on doit donner en utilisant la
	 *            méthode.
	 * @param datePret
	 *            date dur pret qu'on recoit dans la méthode.
	 * @throws SQLException
	 *             -s
	 * @throws BibliothequeException
	 *             -
	 * @throws Exception
	 *             -
	 */
	public void preter(int idLivre, int idMembre, String datePret) throws ServiceException {
		try {
			/* Verfier si le livre est disponible */
			final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
			if (tupleLivre == null) {
				throw new ServiceException("Livre inexistant: " + idLivre);
			}
			if (tupleLivre.getIdMembre() != 0) {
				throw new ServiceException("Livre " + idLivre + " deja prete a " + tupleLivre.getIdMembre());
			}

			/* V�rifie si le membre existe et sa limite de pret */
			final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
			if (tupleMembre == null) {
				throw new ServiceException("Membre inexistant: " + idMembre);
			}
			if (tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
				throw new ServiceException("Limite de pret du membre " + idMembre + " atteinte");
			}

			/* V�rifie s'il existe une r�servation pour le livre */
			final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
			if (tupleReservation != null) {
				throw new ServiceException("Livre r�serv� par : " + tupleReservation.getIdMembre() + " idReservation : "
						+ tupleReservation.getIdReservation());
			}

			/* Enregistrement du pret. */
			final int nb1 = this.livre.preter(idLivre, idMembre, datePret);
			if (nb1 == 0) {
				throw new ServiceException("Livre supprim� par une autre transaction");
			}
			final int nb2 = this.membre.preter(idMembre);
			if (nb2 == 0) {
				throw new ServiceException("Membre supprim� par une autre transaction");
			}
			this.cx.commit();
		} catch (DAOException daoException) {
			this.cx.rollback();
			throw new ServiceException(daoException);
		}
	}

	/**
	 * Renouvellement d'un pret. Le livre doit �tre pr�t�. Le livre ne doit pas
	 * �tre r�serv�.
	 *
	 * @param idLivre
	 *            id du Livre qu'on veux renouveler
	 * @param datePret
	 *            date de pret du livre qu'on veux renouveler.
	 * @throws SQLException
	 *             -
	 * @throws BibliothequeException
	 *             -
	 * @throws Exception
	 *             -
	 */
	public void renouveler(int idLivre, String datePret) throws ServiceException {
		try {
			/* Verifier si le livre est pr�t� */
			final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
			if (tupleLivre == null) {
				throw new ServiceException("Livre inexistant: " + idLivre);
			}
			if (tupleLivre.getIdMembre() == 0) {
				throw new ServiceException("Livre " + idLivre + " n'est pas prete");
			}

			/* Verifier si date renouvellement >= datePret */
			if (Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
				throw new ServiceException("Date de renouvellement inferieure � la date de pret");
			}

			/* V�rifie s'il existe une r�servation pour le livre */
			final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
			if (tupleReservation != null) {
				throw new ServiceException("Livre r�serv� par : " + tupleReservation.getIdMembre() + " idReservation : "
						+ tupleReservation.getIdReservation());
			}

			/* Enregistrement du pret. */
			final int nb1 = this.livre.preter(idLivre, tupleLivre.getIdMembre(), datePret);
			if (nb1 == 0) {
				throw new ServiceException("Livre supprime par une autre transaction");
			}
			this.cx.commit();
		} catch (DAOException daoException) {
			this.cx.rollback();
			throw new ServiceException(daoException);
		}
	}

	/**
	 * Retourner un livre pr�t� Le livre doit �tre pr�t�.
	 *
	 * @param idLivre
	 *            id du livre qu'on retourne
	 * @param dateRetour
	 *            date du retour du livre qu'on veux retourner
	 * @throws SQLException
	 *             -
	 * @throws BibliothequeException
	 *             -
	 * @throws Exception
	 *             -
	 */
	public void retourner(int idLivre, String dateRetour) throws ServiceException {
		try {
			/* Verifier si le livre est pr�t� */
			final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
			if (tupleLivre == null) {
				throw new ServiceException("Livre inexistant: " + idLivre);
			}
			if (tupleLivre.getIdMembre() == 0) {
				throw new ServiceException("Livre " + idLivre + " n'est pas pr�t� ");
			}

			/* Verifier si date retour >= datePret */
			if (Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
				throw new ServiceException("Date de retour inferieure � la date de pret");
			}

			/* Retour du pret. */
			final int nb1 = this.livre.retourner(idLivre);
			if (nb1 == 0) {
				throw new ServiceException("Livre supprim� par une autre transaction");
			}

			final int nb2 = this.membre.retourner(tupleLivre.getIdMembre());
			if (nb2 == 0) {
				throw new ServiceException("Livre supprim� par une autre transaction");
			}
			this.cx.commit();
		} catch (DAOException daoException) {
			this.cx.rollback();
			throw new ServiceException(daoException);
		}
	}
}
