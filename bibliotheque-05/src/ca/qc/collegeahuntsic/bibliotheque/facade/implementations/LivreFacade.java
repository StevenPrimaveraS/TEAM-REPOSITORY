// Fichier LivreFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.facade.implementations;

import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.ILivreFacade;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.ILivreService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de livres.
 *
 * @author Gilles Bénichou
 */
public class LivreFacade extends Facade implements ILivreFacade {
    private ILivreService livreService;

    /**
     * Crée la façade de la table <code>livre</code>.
     *
     * @param livreService Le service de la table <code>livre</code>
     * @throws InvalidServiceException Si le service de livres est <code>null</code>
     */
    LivreFacade(ILivreService livreService) throws InvalidServiceException {
        super(livreService);
        if(livreService == null) {
            throw new InvalidServiceException("Le service de livres ne peut être null");
        }
        setLivreService(livreService);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.livreService</code>.
     *
     * @return La variable d'instance <code>this.livreService</code>
     */
    private ILivreService getLivreService() {
        return this.livreService;
    }

    /**
     * Setter de la variable d'instance <code>this.livreService</code>.
     *
     * @param livreService La valeur à utiliser pour la variable d'instance <code>this.livreService</code>
     */
    private void setLivreService(ILivreService livreService) {
        this.livreService = livreService;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquerir(Session connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException {
        try {
            getLivreService().acquerir(connexion,
                livreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void vendre(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            getLivreService().vendre(session,
                livreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }
}
