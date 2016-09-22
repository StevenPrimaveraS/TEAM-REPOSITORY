
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions de reliées à la création et
 * suppresion de livres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transaction reliées à la
 * création et suppresion de livres.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 *   @author Primavera Sequeira Steven
 */
public class LivreService {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**.
      * Creation d'une instance
      * @param livre - livreDAO
      * @param reservation - Gere une reservation
      */
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau livre dans la base de données.
      * S'il existe deja, une exception est levée.
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
            /* Vérifie si le livre existe déja */
            if(this.livre.existe(idLivre)) {
                throw new ServiceException("Livre existe deja: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.cx.commit();
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }
    }

    /**
      * Vente d'un livre.
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
                + " prete a "
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
            this.cx.commit();
        } catch(DAOException daoException) {
            // TODO Auto-generated catch block
            throw new ServiceException(daoException);
        }

    }
}
