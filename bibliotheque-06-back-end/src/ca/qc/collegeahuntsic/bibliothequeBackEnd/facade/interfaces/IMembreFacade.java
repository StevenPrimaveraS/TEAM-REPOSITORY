// Fichier IMembreFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import org.hibernate.Session;

/**
 * Interface de façade pour manipuler les membres dans la base de données.
 *
 * @author Gilles Bénichou
 */
public interface IMembreFacade extends IFacade {
    /**
     * Inscrit un membre.
     *
     * @param session La session Hibernate à utiliser
     * @param membreDTO Le membre à inscrire
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le membre est <code>null</code>
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void inscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException;

    /**
     * Désincrit un membre.
     *
     * @param session La session Hibernate à utiliser
     * @param membreDTO Le membre à désinscrire
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le livre est <code>null</code>
     * @throws ExistingLoanException Si le membre a encore des prêts
     * @throws ExistingReservationException Si le membre a des réservations
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void desinscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException;
}
