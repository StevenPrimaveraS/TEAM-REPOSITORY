// Fichier GestionBibliotheque.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Système de gestion d'une bibliothèque
 *
 * Ce programme permet de gérer les transaction de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédée avec JDBC.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 *
 * @author Mathieu Lafond
 */
public class GestionBibliotheque {
    private Connexion connexion;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    private LivreService livreService;

    private MembreService membreService;

    private PretService pretService;

    private ReservationService reservationService;

    private GestionInterrogation gestionInterrogation;

    /**
     * Ouvre une connexion avec la BD relationnelle et alloue les gestionnaires
     * de transactions et de tables.
     *
     * @param serveur SQL
     * @param bd nom de la bade de données
     * @param user user id pour établir une connexion avec le serveur SQL
     * @param password mot de passe pour le user id
     * @throws BibliothequeException - si une erreur par rapport à la bibliothèque survient
     */
    public GestionBibliotheque(String serveur,
        String bd,
        String user,
        String password) throws BibliothequeException {
        // allocation des objets pour le traitement des transactions
        try {
            this.connexion = new Connexion(serveur,
                bd,
                user,
                password);
            this.livreDAO = new LivreDAO(this.connexion);
            this.membreDAO = new MembreDAO(this.connexion);
            this.reservationDAO = new ReservationDAO(this.connexion);
            this.livreService = new LivreService(this.livreDAO,
                this.reservationDAO);
            this.membreService = new MembreService(this.membreDAO,
                this.reservationDAO);
            this.pretService = new PretService(this.livreDAO,
                this.membreDAO,
                this.reservationDAO);
            this.reservationService = new ReservationService(this.livreDAO,
                this.membreDAO,
                this.reservationDAO);
            this.gestionInterrogation = new GestionInterrogation(this.connexion);
        } catch(
            ServiceException
            | SQLException exception) {
            //La SQLException est dans gestionInterrogation
            throw new BibliothequeException(exception);
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }

    /**
     * Getter de la variable d'instance <code>this.connexion</code>.
     *
     * @return La variable d'instance <code>this.connexion</code>
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    public void setConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     * Getter de la variable d'instance <code>this.livre</code>.
     *
     * @return La variable d'instance <code>this.livre</code>
     */
    public LivreDAO getLivre() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livre</code>.
     *
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livre</code>
     */
    public void setLivre(LivreDAO livre) {
        this.livreDAO = livre;
    }

    /**
     * Getter de la variable d'instance <code>this.membre</code>.
     *
     * @return La variable d'instance <code>this.membre</code>
     */
    public MembreDAO getMembre() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membre</code>.
     *
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membre</code>
     */
    public void setMembre(MembreDAO membre) {
        this.membreDAO = membre;
    }

    /**
     * Getter de la variable d'instance <code>this.reservation</code>.
     *
     * @return La variable d'instance <code>this.reservation</code>
     */
    public ReservationDAO getReservation() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservation</code>.
     *
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservation</code>
     */
    public void setReservation(ReservationDAO reservation) {
        this.reservationDAO = reservation;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @return La variable d'instance <code>this.gestionLivre</code>
     */
    public LivreService getGestionLivre() {
        return this.livreService;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @param gestionLivre La valeur à utiliser pour la variable d'instance <code>this.gestionLivre</code>
     */
    public void setGestionLivre(LivreService gestionLivre) {
        this.livreService = gestionLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @return La variable d'instance <code>this.gestionMembre</code>
     */
    public MembreService getGestionMembre() {
        return this.membreService;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @param gestionMembre La valeur à utiliser pour la variable d'instance <code>this.gestionMembre</code>
     */
    public void setGestionMembre(MembreService gestionMembre) {
        this.membreService = gestionMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @return La variable d'instance <code>this.gestionPret</code>
     */
    public PretService getGestionPret() {
        return this.pretService;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @param gestionPret La valeur à utiliser pour la variable d'instance <code>this.gestionPret</code>
     */
    public void setGestionPret(PretService gestionPret) {
        this.pretService = gestionPret;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @return La variable d'instance <code>this.gestionReservation</code>
     */
    public ReservationService getGestionReservation() {
        return this.reservationService;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @param gestionReservation La valeur à utiliser pour la variable d'instance <code>this.gestionReservation</code>
     */
    public void setGestionReservation(ReservationService gestionReservation) {
        this.reservationService = gestionReservation;
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
     *
     * @throws BibliothequeException - si une erreur survient
     */
    public void fermer() throws BibliothequeException {
        // fermeture de la connexion
        try {
            this.connexion.fermer();
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }
}
