// Fichier PretService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Gilles Bénichou
 */
public class PretService extends Service implements IPretService {
    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO est <code>null</code>
     */
    public PretService(IPretDAO pretDAO) throws InvalidDAOException {
        super(pretDAO);
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        setPretDAO(pretDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByLivre(session,
                idLivre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionValueException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDatePret(session,
                datePret,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDateRetour(session,
                dateRetour,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
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
        ServiceException {
        final MembreDTO unMembreDTO = pretDTO.getMembreDTO();
        final LivreDTO unLivreDTO = pretDTO.getLivreDTO();
        List<PretDTO> prets = new ArrayList<>(unLivreDTO.getPrets());
        if(!prets.isEmpty()) {
            final PretDTO unPretDTO = prets.get(0);
            final MembreDTO emprunteur = unPretDTO.getMembreDTO();
            throw new ExistingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") a été prêté à "
                + emprunteur.getNom()
                + " (ID de membre : "
                + emprunteur.getIdMembre()
                + ")");
        }
        prets = new ArrayList<>(unMembreDTO.getPrets());
        if(prets.size() >= Integer.parseInt(unMembreDTO.getLimitePret())) {
            throw new InvalidLoanLimitException("Le membre "
                + unMembreDTO.getNom()
                + " (ID de membre : "
                + unMembreDTO.getIdMembre()
                + ") a atteint sa limite de prêt ("
                + unMembreDTO.getLimitePret()
                + " emprunt(s) maximum)");
        }
        final List<ReservationDTO> reservations = new ArrayList<>(pretDTO.getLivreDTO().getReservations());
        if(!reservations.isEmpty()) {
            final ReservationDTO uneReservationDTO = reservations.get(0);
            final MembreDTO booker = uneReservationDTO.getMembreDTO();
            throw new ExistingReservationException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") est réservé pour "
                + booker.getNom()
                + " (ID de membre : "
                + booker.getIdMembre()
                + ")");
        }
        pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        add(session,
            pretDTO);
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
        ServiceException {
        final MembreDTO unMembreDTO = pretDTO.getMembreDTO();
        final LivreDTO unLivreDTO = pretDTO.getLivreDTO();
        //Avant : MembreDTO.getPrets
        final List<PretDTO> prets = new ArrayList<>(unLivreDTO.getPrets());
        if(prets.isEmpty()) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'est pas encore prêté");
        }
        //TODO vérifier
        boolean aEteEmprunteParMembre = false;
        for(PretDTO unAutrePretDTO : prets) {
            aEteEmprunteParMembre = unMembreDTO.equals(unAutrePretDTO.getMembreDTO())
                && unLivreDTO.equals(unAutrePretDTO.getLivreDTO());
        }
        if(!aEteEmprunteParMembre) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'a pas été prêté à "
                + unMembreDTO.getNom()
                + " (ID de membre : "
                + unMembreDTO.getIdMembre()
                + ")");
        }
        final List<ReservationDTO> reservations = new ArrayList<>(pretDTO.getLivreDTO().getReservations());
        if(!reservations.isEmpty()) {
            final ReservationDTO uneReservationDTO = reservations.get(0);
            final MembreDTO booker = uneReservationDTO.getMembreDTO();
            throw new ExistingReservationException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") est réservé pour "
                + booker.getNom()
                + " (ID de membre : "
                + booker.getIdMembre()
                + ")");
        }
        final PretDTO unPretDTO = pretDTO;
        unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        update(session,
            unPretDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException {
        final MembreDTO unMembreDTO = pretDTO.getMembreDTO();
        final LivreDTO unLivreDTO = pretDTO.getLivreDTO();
        //Avant : MembreDTO.getPrets
        final List<PretDTO> prets = new ArrayList<>(unLivreDTO.getPrets());
        if(prets.isEmpty()) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'est pas encore prêté");
        }
        //TODO vérifier
        boolean aEteEmprunteParMembre = false;
        for(PretDTO unAutrePretDTO : prets) {
            aEteEmprunteParMembre = unMembreDTO.equals(unAutrePretDTO.getMembreDTO())
                && unLivreDTO.equals(unAutrePretDTO.getLivreDTO());
        }
        if(!aEteEmprunteParMembre) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'a pas été prêté à "
                + unMembreDTO.getNom()
                + " (ID de membre : "
                + unMembreDTO.getIdMembre()
                + ")");
        }
        final PretDTO unPretDTO = pretDTO;
        unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
        update(session,
            unPretDTO);
    }
}
