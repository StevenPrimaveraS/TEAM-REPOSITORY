// Fichier IReservationDAO.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;

/**
 * Interface DAO pour manipuler les réservations dans la base de données.
 *
 * @author Gilles Benichou
 */
public interface IReservationDAO extends IDAO {
    /**
     * Trouve les livres à partir d'un titre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun livre
     * n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre ID du livre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des livres correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si le titre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les livres à partir d'un titre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun livre
     * n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idMembre ID du membre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des livres correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si le titre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;
}
