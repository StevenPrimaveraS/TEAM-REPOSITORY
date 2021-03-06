// Fichier LivreService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.ILivreService;
import org.hibernate.Session;

/**
 * Service de la table <code>livre</code>.
 *
 * @author Gilles Bénichou
 */
public class LivreService extends Service implements ILivreService {
    private ILivreDAO livreDAO;

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    LivreService(ILivreDAO livreDAO) throws InvalidDAOException {
        super(livreDAO);
        setLivreDAO(livreDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LivreDTO> findByTitre(Session session,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException,
        InvalidCriterionValueException {
        try {
            return getLivreDAO().findByTitre(session,
                titre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquerir(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        add(session,
            livreDTO);
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
        ServiceException {
        final List<PretDTO> prets = new ArrayList<>(livreDTO.getPrets());
        if(!prets.isEmpty()) {
            for(PretDTO pretDTO : prets) {
                if(pretDTO.getDateRetour() == null) {
                    final MembreDTO emprunteur = pretDTO.getMembreDTO();
                    throw new ExistingLoanException("Le livre "
                        + livreDTO.getTitre()
                        + " (ID de livre : "
                        + livreDTO.getIdLivre()
                        + ") a été prêté à "
                        + emprunteur.getNom()
                        + " (ID de membre : "
                        + emprunteur.getIdMembre()
                        + ")");
                }
            }
        }
        final List<ReservationDTO> reservations = new ArrayList<>(livreDTO.getReservations());
        if(!reservations.isEmpty()) {
            final ReservationDTO reservationDTO = reservations.get(0);
            final MembreDTO booker = reservationDTO.getMembreDTO();
            throw new ExistingReservationException("Le livre "
                + livreDTO.getTitre()
                + " (ID de livre : "
                + livreDTO.getIdLivre()
                + ") est réservé pour "
                + booker.getNom()
                + " (ID de membre : "
                + booker.getIdMembre()
                + ")");
        }
        delete(session,
            livreDTO);
    }

}
