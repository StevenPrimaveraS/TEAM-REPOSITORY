// Fichier Facade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.DTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IService;
import org.hibernate.Session;

/**
 * Classe de base pour toutes les façades.
 *
 * @author Gilles Bénichou
 */
public class Facade implements IFacade {
    private IService service;

    /**
     * Crée une façade.
     *
     * @param service Le service à utiliser
     * @throws InvalidServiceException Si le service est <code>null</code>
     */
    protected Facade(IService service) throws InvalidServiceException {
        super();
        if(service == null) {
            throw new InvalidServiceException("Le service ne peut être null");
        }
        setService(service);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.service</code>.
     *
     * @return La variable d'instance <code>this.service</code>
     */
    protected IService getService() {
        return this.service;
    }

    /**
     * Setter de la variable d'instance <code>this.service</code>.
     *
     * @param service La valeur à utiliser pour la variable d'instance <code>this.service</code>
     */
    private void setService(IService service) {
        this.service = service;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO get(Session session,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        FacadeException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }
        try {
            final DTO dto = getService().get(session,
                primaryKey);
            return dto;
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }
}
