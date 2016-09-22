
package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Syst�me de gestion d'une biblioth�que
 *
 * <pre>
 * Ce programme permet de g�rer les transaction de base d'une
 * biblioth�que.  Il g�re des livres, des membres et des
 * r�servations. Les donn�es sont conserv�es dans une base de
 * donn�es relationnelles acc�d�e avec JDBC.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * </pre>
 *
 * @author Math -
 */
public class GestionBibliotheque {
    private Connexion cx;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private LivreService gestionLivre;

    private MembreService gestionMembre;

    private PretService gestionPret;

    private ReservationService gestionReservation;

    private GestionInterrogation gestionInterrogation;

    /**
     * Ouvre une connexion avec la BD relationnelle et alloue les gestionnaires
     * de transactions et de tables.
     * @param serveur SQL
     * @param bd nom de la bade de données
     * @param user user id pour établir une connexion avec le serveur SQL
     * @param password mot de passe pour le user id
     * @throws BibliothequeException -
     * @throws SQLException -
     */
    public GestionBibliotheque(String serveur,
        String bd,
        String user,
        String password) throws BibliothequeException,
        SQLException {
        // allocation des objets pour le traitement des transactions
        try {
            this.cx = new Connexion(serveur,
                bd,
                user,
                password);
        } catch(ConnexionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            this.livre = new LivreDAO(this.cx);

            this.membre = new MembreDAO(this.cx);
            this.reservation = new ReservationDAO(this.cx);
            this.gestionLivre = new LivreService(this.livre,
                this.reservation);
            this.gestionMembre = new MembreService(this.membre,
                this.reservation);
            this.gestionPret = new PretService(this.livre,
                this.membre,
                this.reservation);
            this.gestionReservation = new ReservationService(this.livre,
                this.membre,
                this.reservation);
            this.gestionInterrogation = new GestionInterrogation(this.cx);
        } catch(
            DAOException
            | ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    public Connexion getCx() {
        return this.cx;
    }

    /**
     * Setter de la variable d'instance <code>this.cx</code>.
     *
     * @param cx La valeur à utiliser pour la variable d'instance <code>this.cx</code>
     */
    public void setCx(Connexion cx) {
        this.cx = cx;
    }

    /**
     * Getter de la variable d'instance <code>this.livre</code>.
     *
     * @return La variable d'instance <code>this.livre</code>
     */
    public LivreDAO getLivre() {
        return this.livre;
    }

    /**
     * Setter de la variable d'instance <code>this.livre</code>.
     *
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livre</code>
     */
    public void setLivre(LivreDAO livre) {
        this.livre = livre;
    }

    /**
     * Getter de la variable d'instance <code>this.membre</code>.
     *
     * @return La variable d'instance <code>this.membre</code>
     */
    public MembreDAO getMembre() {
        return this.membre;
    }

    /**
     * Setter de la variable d'instance <code>this.membre</code>.
     *
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membre</code>
     */
    public void setMembre(MembreDAO membre) {
        this.membre = membre;
    }

    /**
     * Getter de la variable d'instance <code>this.reservation</code>.
     *
     * @return La variable d'instance <code>this.reservation</code>
     */
    public ReservationDAO getReservation() {
        return this.reservation;
    }

    /**
     * Setter de la variable d'instance <code>this.reservation</code>.
     *
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservation</code>
     */
    public void setReservation(ReservationDAO reservation) {
        this.reservation = reservation;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @return La variable d'instance <code>this.gestionLivre</code>
     */
    public LivreService getGestionLivre() {
        return this.gestionLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @param gestionLivre La valeur à utiliser pour la variable d'instance <code>this.gestionLivre</code>
     */
    public void setGestionLivre(LivreService gestionLivre) {
        this.gestionLivre = gestionLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @return La variable d'instance <code>this.gestionMembre</code>
     */
    public MembreService getGestionMembre() {
        return this.gestionMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @param gestionMembre La valeur à utiliser pour la variable d'instance <code>this.gestionMembre</code>
     */
    public void setGestionMembre(MembreService gestionMembre) {
        this.gestionMembre = gestionMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @return La variable d'instance <code>this.gestionPret</code>
     */
    public PretService getGestionPret() {
        return this.gestionPret;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @param gestionPret La valeur à utiliser pour la variable d'instance <code>this.gestionPret</code>
     */
    public void setGestionPret(PretService gestionPret) {
        this.gestionPret = gestionPret;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @return La variable d'instance <code>this.gestionReservation</code>
     */
    public ReservationService getGestionReservation() {
        return this.gestionReservation;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @param gestionReservation La valeur à utiliser pour la variable d'instance <code>this.gestionReservation</code>
     */
    public void setGestionReservation(ReservationService gestionReservation) {
        this.gestionReservation = gestionReservation;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionInterrogation</code>.
     *
     * @return La variable d'instance <code>this.gestionInterrogation</code>
     */
    public GestionInterrogation getGestionInterrogation() {
        return this.gestionInterrogation;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionInterrogation</code>.
     *
     * @param gestionInterrogation La valeur à utiliser pour la variable d'instance <code>this.gestionInterrogation</code>
     */
    public void setGestionInterrogation(GestionInterrogation gestionInterrogation) {
        this.gestionInterrogation = gestionInterrogation;
    }

    /**
     * Fermeture de la connexion.
     * @throws SQLException -
     */
    public void fermer() {
        // fermeture de la connexion
        try {
            this.cx.fermer();
        } catch(ConnexionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
