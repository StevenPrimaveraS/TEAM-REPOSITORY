
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table livre.
 * 
 * @author Primavera Sequeira Steven
 */
public class LivreService {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion connexion;

    /**
     * Crée le service de la table livre.
     * 
     * @param livre - livreDAO
     * @param reservation - Gere une reservation
     */
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.connexion = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

	/**
	 * Ajout d'un nouveau livre dans la base de données.
	 * S'il existe deja, une exception est levée.
	 * 
	 * @param idLivre id du livre qu'on veux acquerir.
	 * @param titre titre du livre qu'on veux acquerir.
	 * @param auteur auteur du livre qu'on veux acquerir.
	 * @param dateAcquisition date d'acquisition du livre qu'on veux acquerir.
	 * @throws ServiceException -
	 */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException {
        try {
            /* Vérifie si le livre existe déjà */
            if(this.livre.existe(idLivre)) {
                throw new ServiceException("Livre existe déjà: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            try {
                this.connexion.commit();
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }
    }

    /**
     * Vente d'un livre.
     * 
     * @param idLivre id du livre qu'on veux vendre.
     * @throws SQLException -
     * @throws ServiceException -
     * @throws Exception -
     */
    public void vendre(int idLivre) throws ServiceException {
        LivreDTO tupleLivre = null;
        try {
            tupleLivre = this.livre.getLivre(idLivre);
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }
        if(tupleLivre == null) {
            throw new ServiceException("Livre inexistant: "
                + idLivre);
        }
        if(tupleLivre.getIdMembre() != 0) {
            throw new ServiceException("Livre "
                + idLivre
                + " prêté a "
                + tupleLivre.getIdMembre());
        }
        try {
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " réservé ");
            }
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }

        /* Suppression du livre. */
        int nb = 0;
        try {
            nb = this.livre.vendre(idLivre);
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }
        if(nb == 0) {
            throw new ServiceException("Livre "
                + idLivre
                + " inexistant");
        }
        try {
            this.connexion.commit();
        } catch(ConnexionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
