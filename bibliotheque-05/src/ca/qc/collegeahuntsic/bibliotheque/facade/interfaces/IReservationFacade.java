// Fichier IReservationFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import org.hibernate.Session;

/**
 * Interface de façade pour manipuler les réservations dans la base de données.
 *
 * @author Gilles Bénichou
 */
public interface IReservationFacade extends IFacade {
    /**
     * Place une réservation.
     *
     * @param session La session Hibernate à utiliser
     * @param reservationDTO La réservation à placer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException Si le livre est déjà prêté au membre
     * @throws ExistingReservationException Si le membre a déjà réservé ce livre
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void placer(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException;

    /**
     * Utilise une réservation.
     *
     * @param session La session Hibernate à utiliser
     * @param reservationDTO La réservation à utiliser
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws ExistingReservationException Si la réservation n'est pas la première de la liste
     * @throws ExistingLoanException Si le livre est déjà prêté au membre
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void utiliser(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        FacadeException;

    /**
     * Annule une réservation.
     *
     * @param session La session Hibernate à utiliser
     * @param reservationDTO Le reservation à annuler
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si la réservation est <code>null</code>
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void annuler(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException;
}
