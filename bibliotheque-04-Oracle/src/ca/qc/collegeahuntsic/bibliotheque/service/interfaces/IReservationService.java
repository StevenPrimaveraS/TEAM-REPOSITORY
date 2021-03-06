// Fichier IReservationService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;

/**
 * Interface pour ReservationService.<br />
 * Elle hérite de IService.
 *
 * @author Mathieu Lafond
 */
public interface IReservationService extends IService {
    /**
     * Ajoute une nouvelle reservation dans la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La reservation à ajouter
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la reservation est <code>null</code>
     * @throws InvalidDTOClassException Si la classe de la reservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void add(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Lit une reservation à partir de la base de données. Si aucune reservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param connexion La connexion à utiliser
     * @param idReservation L'ID de la reservation à lire
     * @return La reservation lue ; <code>null</code> sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire de la reservation est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    ReservationDTO get(Connexion connexion,
        String idReservation) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException;

    /**
     * Met à jour une reservation dans la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La reservation à mettre à jour
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la reservation est <code>null</code>
     * @throws InvalidDTOClassException Si la classe de la reservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void update(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Supprime une reservation de la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La reservation à supprimer
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la reservation est <code>null</code>
     * @throws InvalidDTOClassException Si la classe de la reservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void delete(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Trouve toutes les reservations de la base de données. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucune
     * reservation n'est trouvée, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste de toutes les reservations ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     * Trouve les réservations à partir d'un membre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>.
     * Si aucune réservation n'est trouvée, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du membre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     * Trouve les réservations à partir d'un livre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>.
     * Si aucune réservation n'est trouvée, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     * Place une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La réservation à placer
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire du membre est <code>null</code> ou si la clef primaire du livre est <code>null</code>
     * @throws MissingDTOException Si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException Si le livre est déjà prêté au membre
     * @throws ExistingReservationException Si le membre a déjà réservé ce livre
     * @throws InvalidDTOClassException Si la classe de la réservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void placer(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Utilise une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La réservation à placer
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire de la réservation est <code>null</code>, si la clef primaire du membre est <code>null</code> ou si la clef primaire du livre est <code>null</code>
     * @throws MissingDTOException Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ExistingReservationException Si la réservation n'est pas la première de la liste
     * @throws ExistingLoanException Si le livre est déjà prêté au membre
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws InvalidDTOClassException Si la classe du membre n'est pas celle que prend en charge le DAO ou si la classe du n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void utiliser(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Annule une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La reservation à annuler
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire de la réservation est <code>null</code>
     * @throws MissingDTOException Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidDTOClassException Si la classe de la réservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void annuler(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        ServiceException;

}
