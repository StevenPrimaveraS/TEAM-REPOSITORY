// Fichier ReservationService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IReservationDAO;
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
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IReservationService;
import org.hibernate.Session;

/**
 * Service de la table reservation.
 *
 * @author Primavera Sequeira Steven
 */

public class ReservationService extends Service implements IReservationService {
    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO de réservation est <code>null</code>, Si le DAO de membre est <code>null</code>, Si le DAO de livre est <code>null</code>, Si le DAO de prêt est <code>null</code>
     */
    public ReservationService(IReservationDAO reservationDAO,
        IPretDAO pretDAO) throws InvalidDAOException {
        super(reservationDAO);
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setPretDAO(pretDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

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
    public List<ReservationDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getReservationDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(
            DAOException
            | InvalidCriterionValueException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getReservationDAO().findByLivre(session,
                idLivre,
                sortByPropertyName);
        } catch(
            DAOException
            | InvalidCriterionValueException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placer(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {
        final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();
        final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
        MembreDTO emprunteur = null;
        if(new ArrayList<>(unLivreDTO.getPrets()).size() > 0) {
            emprunteur = new ArrayList<>(unLivreDTO.getPrets()).get(0).getMembreDTO();
        }
        if(emprunteur == null) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'est pas encore prêté");
        }
        if(unMembreDTO.getIdMembre() == emprunteur.getIdMembre()) {
            throw new ExistingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") est déjà prêté à "
                + emprunteur.getNom()
                + " (ID de membre : "
                + emprunteur.getIdMembre()
                + ")");
        }

        // Cas éliminé en utilisant la date de réservation comme étant la date système de la base de données

        /* Verifier si date reservation >= datePret */
        // if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
        //     throw new BibliothequeException("Date de réservation inférieure à la date de prêt");
        // }

        final List<ReservationDTO> reservations = new ArrayList<>(unMembreDTO.getReservations());
        for(ReservationDTO uneAutreReservationDTO : reservations) {
            if(uneAutreReservationDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est déjà réservé à "
                    + emprunteur.getNom()
                    + " (ID de membre : "
                    + emprunteur.getIdMembre()
                    + ")");
            }
        }
        reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));
        add(session,
            reservationDTO);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliser(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ServiceException {
        try {
            final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();
            final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
            final List<ReservationDTO> reservationsLivre = new ArrayList<>(unLivreDTO.getReservations());
            if(!reservationsLivre.isEmpty()) {
                final ReservationDTO uneReservationDTO = reservationsLivre.get(0);
                if(uneReservationDTO.getMembreDTO().getIdMembre() != unMembreDTO.getIdMembre()) {
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
            }
            final List<PretDTO> pretsLivre = new ArrayList<>(unLivreDTO.getPrets());
            if(!pretsLivre.isEmpty()) {
                final MembreDTO emprunteur = pretsLivre.get(0).getMembreDTO();
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
            final List<PretDTO> empruntsMembre = new ArrayList<>(unMembreDTO.getPrets());
            if(empruntsMembre.size() >= Integer.parseInt(unMembreDTO.getLimitePret())) {
                throw new InvalidLoanLimitException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }

            // Cas éliminé en utilisant la date de prêt et de réservation comme étant la date système de la base de données

            /* Verifier si datePret >= tupleReservation.dateReservation */
            // if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
            //     throw new BibliothequeException("Date de prêt inférieure à la date de réservation");
            // }
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(unLivreDTO);
            pretDTO.setMembreDTO(unMembreDTO);
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            annuler(session,
                reservationDTO);
            getPretDAO().add(session,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        delete(session,
            reservationDTO);
    }
}
