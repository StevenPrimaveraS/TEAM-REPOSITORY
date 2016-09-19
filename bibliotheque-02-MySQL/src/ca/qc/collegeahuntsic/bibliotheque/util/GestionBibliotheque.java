
package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
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
	public Connexion cx;

	public LivreDAO livre;

	public MembreDAO membre;

	public ReservationDAO reservation;

	public LivreService gestionLivre;

	public MembreService gestionMembre;

	public PretService gestionPret;

	public ReservationService gestionReservation;

	public GestionInterrogation gestionInterrogation;

	/**
	 * Ouvre une connexion avec la BD relationnelle et alloue les gestionnaires
	 * de transactions et de tables.
	 *
	 * <pre>
	 *
	 * &#64;param serveur SQL
	 * &#64;param bd nom de la bade de donn�es
	 * &#64;param user user id pour �tablir une connexion avec le serveur SQL
	 * &#64;param password mot de passe pour le user id
	 * &#64;throws BibliothequeException
	 * &#64;throws SQLException
	 * </pre>
	 */
	public GestionBibliotheque(String serveur, String bd, String user, String password)
			throws BibliothequeException, SQLException {
		// allocation des objets pour le traitement des transactions
		this.cx = new Connexion(serveur, bd, user, password);
		this.livre = new LivreDAO(this.cx);
		this.membre = new MembreDAO(this.cx);
		this.reservation = new ReservationDAO(this.cx);
		this.gestionLivre = new LivreService(this.livre, this.reservation);
		this.gestionMembre = new MembreService(this.membre, this.reservation);
		this.gestionPret = new PretService(this.livre, this.membre, this.reservation);
		this.gestionReservation = new ReservationService(this.livre, this.membre, this.reservation);
		this.gestionInterrogation = new GestionInterrogation(this.cx);
	}

	/**
	 * Fermeture de la connexion
	 */
	public void fermer() throws SQLException {
		// fermeture de la connexion
		this.cx.fermer();
	}
}
