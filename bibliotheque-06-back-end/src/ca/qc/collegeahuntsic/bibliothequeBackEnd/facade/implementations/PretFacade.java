// Fichier PretFacade.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IPretFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de prêts.
 *
 * @author Gilles Benichou
 */
public class PretFacade extends Facade implements IPretFacade {
    private IPretService pretService;

    /**
     * Crée la façade de la table <code>pret</code>.
     *
     * @param pretService Le service de la table <code>pret</code>
     * @throws InvalidServiceException Si le service de pret est <code>null</code>
     */
    PretFacade(IPretService pretService) throws InvalidServiceException {
        super(pretService);
        if(pretService == null) {
            throw new InvalidServiceException("Le service de prets ne peut être null");
        }
        setPretService(pretService);
    }

    /**
     * Getter de la variable d'instance <code>this.pretService</code>.
     *
     * @return La variable d'instance <code>this.pretService</code>
     */
    private IPretService getPretService() {
        return this.pretService;
    }

    /**
     * Setter de la variable d'instance <code>this.membreService</code>.
     *
     * @param pretService La valeur à utiliser pour la variable d'instance <code>this.pretService</code>
     */
    private void setPretService(IPretService pretService) {
        this.pretService = pretService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        FacadeException {
        try {
            getPretService().commencer(session,
                pretDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            getPretService().renouveler(session,
                pretDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        FacadeException {
        try {
            getPretService().terminer(session,
                pretDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

}
