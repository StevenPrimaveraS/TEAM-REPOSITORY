
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;

/**
 * Gestion des transactions de reli�es � la cr�ation et
 * suppresion de membres dans une biblioth�que.
 *
 * Ce programme permet de g�rer les transaction reli�es � la
 * cr�ation et suppresion de membres.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 *  @author Primavera Sequeira Steven
 */

public class MembreService {

    private Connexion cx;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**.
      * Creation d'une instance
      * @param membre MembreDao qu'on recoit en parametre.
      * @param reservation ReservationDAO qu'on recoit en parametre.
      */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau membre dans la base de donnees.
      * S'il existe deja, une exception est levee.
      * @param idMembre id du membre qu'on veux inscrire.
      * @param nom nom du membre qu'on veux inscrire.
      * @param telephone numero de telephone du membre qu'on veux inscrire.
      * @param limitePret limite de pret du membre qu'on veux inscrire.
      * @throws SQLException -
      * @throws BibliothequeException -
      * @throws Exception -
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie si le membre existe d�ja */
            if(this.membre.existe(idMembre)) {
                throw new BibliothequeException("Membre existe deja: "
                    + idMembre);
            }

            /* Ajout du membre. */
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Suppression d'un membre de la base de donnees.
      * @param idMembre id du membre qu'on veux desinscrire.
      * @throws SQLException -
      * @throws BibliothequeException -
      * @throws Exception -
      */
    public void desinscrire(int idMembre) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie si le membre existe et son nombre de pret en cours */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new BibliothequeException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " a des r�servations");
            }

            /* Suppression du membre */
            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
} //class
