
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;

/**
 * Gestion des transactions de reli�es � la cr�ation et
 * suppresion de livres dans une biblioth�que.
 *
 * Ce programme permet de g�rer les transaction reli�es � la
 * cr�ation et suppresion de livres.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
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
      * @throws SQLException -
      * @throws BibliothequeException -
      * @throws Exception -
      */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* Vérifie si le livre existe déja */
            if(this.livre.existe(idLivre)) {
                throw new BibliothequeException("Livre existe deja: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.cx.commit();
        } catch(Exception e) {
            //        System.out.println(e);
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Vente d'un livre.
      * @param idLivre id du livre qu'on veux vendre.
      * @throws SQLException -
      * @throws BibliothequeException -
      * @throws Exception -
      */
    public void vendre(int idLivre) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.getIdMembre());
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " r�serv� ");
            }

            /* Suppression du livre. */
            final int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
