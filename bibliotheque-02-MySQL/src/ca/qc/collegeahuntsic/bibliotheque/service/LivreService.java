// Fichier LivreService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

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

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

    private Connexion connexion;

    /**
     * Crée le service de la table livre.
     *
     * @param livre - livreDAO
     * @param reservation - Gère une reservation
     */
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.connexion = livre.getConnexion();
        this.livreDAO = livre;
        this.reservationDAO = reservation;
    }

    /**
     * Ajout d'un nouveau livre dans la base de données.
     * S'il existe deja, une exception est levée.
     * 
     * @param idLivre id du livre qu'on veux acquerir.
     * @param titre titre du livre qu'on veux acquerir.
     * @param auteur auteur du livre qu'on veux acquerir.
     * @param dateAcquisition date d'acquisition du livre qu'on veux acquerir.
     * @throws ServiceException - Si une erreur survient
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException {
        try {
            /* Vérifie si le livre existe déjà */
            if(this.livreDAO.existe(idLivre)) {
                throw new ServiceException("Livre existe déjà: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livreDAO.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.connexion.commit();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }

    /**
     * Vente d'un livre.
     *
     * @param idLivre id du livre qu'on veux vendre.
     * @throws ServiceException - Si une erreur survient
     */
    public void vendre(int idLivre) throws ServiceException {
        try {
            LivreDTO tupleLivre = null;
            tupleLivre = this.livreDAO.getLivre(idLivre);
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
            if(this.reservationDAO.getReservationLivre(idLivre) != null) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " réservé ");
            }

            /* Suppression du livre. */
            int nb = 0;
            nb = this.livreDAO.vendre(idLivre);
            if(nb == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }
}
