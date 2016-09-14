
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.TupleLivre;
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
 * </pre>
 */
public class LivreDAO {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**
      * Creation d'une instance
      */
    public LivreDAO(LivreDAO livre,
        ReservationDAO reservation) {
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau livre dans la base de donn�es.
      * S'il existe deja, une exception est lev�e.
      */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie si le livre existe d�ja */
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
      */
    public void vendre(int idLivre) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            TupleLivre tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.idMembre);
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " r�serv� ");
            }

            /* Suppression du livre. */
            int nb = this.livre.vendre(idLivre);
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
