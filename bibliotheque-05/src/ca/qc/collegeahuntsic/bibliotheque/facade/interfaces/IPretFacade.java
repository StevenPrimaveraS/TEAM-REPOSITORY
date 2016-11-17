// Fichier IPretFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import org.hibernate.Session;

/**
 * Interface de façade pour manipuler les prêts dans la base de données.
 *
 * @author Gilles Bénichou
 */
public interface IPretFacade extends IFacade {
    /**
     * Commence un prêt.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à commencer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws ExistingLoanException Si le livre a été prêté
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        FacadeException;

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à renouveler
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        FacadeException;

    /**
     * Termine un prêt.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à terminer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        FacadeException;
}
