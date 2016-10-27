// Fichier ReservationService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IReservationService;

/**
 * Service de la table reservation.
 *
 * @author Primavera Sequeira Steven
 */

public class ReservationService extends Service implements IReservationService {
    private IReservationDAO reservationDAO;

    private ILivreDAO livreDAO;

    private IMembreDAO membreDAO;

    private IPretDAO pretDAO;

    //    /**
    //     * Crée le service de la table <code>reservation</code>.
    //     *
    //     * @param reservationDAO Le DAO de la table <code>reservation</code>
    //     * @param membreDAO Le DAO de la table <code>membre</code>
    //     * @param livreDAO Le DAO de la table <code>livre</code>
    //     * @param pretDAO Le DAO de la table <code>pret</code>
    //     */
    //    public ReservationService(ReservationDAO reservationDAO,
    //        LivreDAO livreDAO,
    //        MembreDAO membreDAO,
    //        PretDAO pretDAO) {
    //        super();
    //        setReservationDAO(reservationDAO);
    //        setMembreDAO(membreDAO);
    //        setLivreDAO(livreDAO);
    //        setPretDAO(pretDAO);
    //    }
    //
    //    // Region Getters and Setters
    //    /**
    //     * Getter de la variable d'instance <code>this.reservationDAO</code>.
    //     *
    //     * @return La variable d'instance <code>this.reservationDAO</code>
    //     */
    //    private ReservationDAO getReservationDAO() {
    //        return this.reservationDAO;
    //    }
    //
    //    /**
    //     * Setter de la variable d'instance <code>this.reservationDAO</code>.
    //     *
    //     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
    //     */
    //    private void setReservationDAO(ReservationDAO reservationDAO) {
    //        this.reservationDAO = reservationDAO;
    //    }
    //
    //    /**
    //     * Getter de la variable d'instance <code>this.livreDAO</code>.
    //     *
    //     * @return La variable d'instance <code>this.livreDAO</code>
    //     */
    //    private LivreDAO getLivreDAO() {
    //        return this.livreDAO;
    //    }
    //
    //    /**
    //     * Setter de la variable d'instance <code>this.livreDAO</code>.
    //     *
    //     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
    //     */
    //    private void setLivreDAO(LivreDAO livreDAO) {
    //        this.livreDAO = livreDAO;
    //    }
    //
    //    /**
    //     * Getter de la variable d'instance <code>this.membreDAO</code>.
    //     *
    //     * @return La variable d'instance <code>this.membreDAO</code>
    //     */
    //    private MembreDAO getMembreDAO() {
    //        return this.membreDAO;
    //    }
    //
    //    /**
    //     * Setter de la variable d'instance <code>this.membreDAO</code>.
    //     *
    //     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
    //     */
    //    private void setMembreDAO(MembreDAO membreDAO) {
    //        this.membreDAO = membreDAO;
    //    }
    //
    //    /**
    //     * Getter de la variable d'instance <code>this.pretDAO</code>.
    //     *
    //     * @return La variable d'instance <code>this.pretDAO</code>
    //     */
    //    private PretDAO getPretDAO() {
    //        return this.pretDAO;
    //    }
    //
    //    /**
    //     * Setter de la variable d'instance <code>this.pretDAO</code>.
    //     *
    //     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
    //     */
    //    private void setPretDAO(PretDAO pretDAO) {
    //        this.pretDAO = pretDAO;
    //    }
    //
    //    // EndRegion Getters and Setters
    //
    //    /**
    //     * Ajoute une nouvelle réservation.
    //     *
    //     * @param reservationDTO La réservation à ajouter
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public void add(ReservationDTO reservationDTO) throws ServiceException {
    //        try {
    //            getReservationDAO().add(reservationDTO);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Lit une réservation. Si aucune réservation n'est trouvée, <code>null</code> est retourné.
    //     *
    //     * @param idReservation L'ID de la réservation à lire
    //     * @return La réservation lue ; <code>null</code> sinon
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public ReservationDTO read(int idReservation) throws ServiceException {
    //        try {
    //            return getReservationDAO().read(idReservation);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Met à jour une réservation.
    //     *
    //     * @param reservationDTO La réservation à mettre à jour
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public void update(ReservationDTO reservationDTO) throws ServiceException {
    //        try {
    //            getReservationDAO().update(reservationDTO);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Supprime une réservation.
    //     *
    //     * @param reservationDTO La réservation à supprimer
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public void delete(ReservationDTO reservationDTO) throws ServiceException {
    //        try {
    //            getReservationDAO().delete(reservationDTO);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Trouve toutes les réservations.
    //     *
    //     * @return La liste des réservations ; une liste vide sinon
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public List<ReservationDTO> getAll() throws ServiceException {
    //        try {
    //            return getReservationDAO().getAll();
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Trouve les réservations à partir d'un livre.
    //     *
    //     * @param idLivre L'ID du livre à utiliser
    //     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public List<ReservationDTO> findByLivre(int idLivre) throws ServiceException {
    //        try {
    //            return getReservationDAO().findByLivre(idLivre);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Trouve les réservations à partir d'un membre.
    //     *
    //     * @param idMembre L'ID du membre à utiliser
    //     * @return La liste des réservations correspondantes ; une liste vide sinon
    //     * @throws ServiceException S'il y a une erreur avec la base de données
    //     */
    //    public List<ReservationDTO> findByMembre(int idMembre) throws ServiceException {
    //        try {
    //            return getReservationDAO().findByMembre(idMembre);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Réserve un livre.
    //     *
    //     * @param reservationDTO La réservation à créer
    //     * @param membreDTO Le membre qui réserve
    //     * @param livreDTO Le livre à réserver
    //     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore
    //     *         été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de
    //     *         données
    //     */
    //    public void reserver(ReservationDTO reservationDTO,
    //        MembreDTO membreDTO,
    //        LivreDTO livreDTO) throws ServiceException {
    //        try {
    //            final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
    //            if(uneReservationDTO != null) {
    //                throw new ServiceException("La réservation "
    //                    + reservationDTO.getIdReservation()
    //                    + " existe déjà");
    //            }
    //            final MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
    //            if(unMembreDTO == null) {
    //                throw new ServiceException("Le membre "
    //                    + membreDTO.getIdMembre()
    //                    + " n'existe pas");
    //            }
    //            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
    //            if(unLivreDTO == null) {
    //                throw new ServiceException("Le livre "
    //                    + livreDTO.getIdLivre()
    //                    + " n'existe pas");
    //            }
    //            final MembreDTO emprunteur = getMembreDAO().read(unLivreDTO.getIdMembre());
    //            if(emprunteur == null) {
    //                throw new ServiceException("Le livre "
    //                    + unLivreDTO.getTitre()
    //                    + " (ID de livre : "
    //                    + unLivreDTO.getIdLivre()
    //                    + ") n'est pas encore prêté");
    //            }
    //            if(unMembreDTO.getIdMembre() == emprunteur.getIdMembre()) {
    //                throw new ServiceException("Le livre "
    //                    + unLivreDTO.getTitre()
    //                    + " (ID de livre : "
    //                    + unLivreDTO.getIdLivre()
    //                    + ") est déjà prêté à "
    //                    + emprunteur.getNom()
    //                    + " (ID de membre : "
    //                    + emprunteur.getIdMembre()
    //                    + ")");
    //            }
    //
    //            // Cas éliminé en utilisant la date de réservation comme étant la date système de la base de données
    //
    //            /* Verifier si date reservation >= datePret */
    //            // if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
    //            //     throw new BibliothequeException("Date de réservation inférieure à la date de prêt");
    //            // }
    //
    //            final List<ReservationDTO> reservations = getReservationDAO().findByMembre(unMembreDTO.getIdMembre());
    //            for(ReservationDTO uneAutreReservationDTO : reservations) {
    //                if(uneAutreReservationDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
    //                    throw new ServiceException("Le livre "
    //                        + unLivreDTO.getTitre()
    //                        + " (ID de livre : "
    //                        + unLivreDTO.getIdLivre()
    //                        + ") est déjà réservé à "
    //                        + emprunteur.getNom()
    //                        + " (ID de membre : "
    //                        + emprunteur.getIdMembre()
    //                        + ")");
    //                }
    //            }
    //            reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));
    //            add(reservationDTO);
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Utilise une réservation.
    //     *
    //     * @param reservationDTO La réservation à utiliser
    //     * @param membreDTO Le membre qui utilise sa réservation
    //     * @param livreDTO Le livre à emprunter
    //     * @throws ServiceException Si la réservation n'existe pas, si le membre n'existe pas, si le livre n'existe pas, si la réservation n'est pas
    //     *         la première de la liste, si le livre est déjà prété, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base
    //     *         de données
    //     */
    //    public void utiliser(ReservationDTO reservationDTO,
    //        MembreDTO membreDTO,
    //        LivreDTO livreDTO) throws ServiceException {
    //        try {
    //            ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
    //            if(uneReservationDTO == null) {
    //                throw new ServiceException("La réservation "
    //                    + reservationDTO.getIdReservation()
    //                    + " n'existe pas");
    //            }
    //            final MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
    //            if(unMembreDTO == null) {
    //                throw new ServiceException("Le membre "
    //                    + membreDTO.getIdMembre()
    //                    + " n'existe pas");
    //            }
    //            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
    //            if(unLivreDTO == null) {
    //                throw new ServiceException("Le livre "
    //                    + livreDTO.getIdLivre()
    //                    + " n'existe pas");
    //            }
    //            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(unLivreDTO.getIdLivre());
    //            if(!reservations.isEmpty()) {
    //                uneReservationDTO = reservations.get(0);
    //                if(uneReservationDTO.getMembreDTO().getIdMembre() != unMembreDTO.getIdMembre()) {
    //                    final MembreDTO booker = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());
    //                    throw new ServiceException("Le livre "
    //                        + unLivreDTO.getTitre()
    //                        + " (ID de livre : "
    //                        + unLivreDTO.getIdLivre()
    //                        + ") est réservé pour "
    //                        + booker.getNom()
    //                        + " (ID de membre : "
    //                        + booker.getIdMembre()
    //                        + ")");
    //                }
    //            }
    //            final MembreDTO emprunteur = getMembreDAO().read(livreDTO.getIdMembre());
    //            if(emprunteur != null) {
    //                throw new ServiceException("Le livre "
    //                    + unLivreDTO.getTitre()
    //                    + " (ID de livre : "
    //                    + unLivreDTO.getIdLivre()
    //                    + ") a été prêté à "
    //                    + emprunteur.getNom()
    //                    + " (ID de membre : "
    //                    + emprunteur.getIdMembre()
    //                    + ")");
    //            }
    //            final List<PretDTO> empruntsMembre = getPretDAO().findByMembre(unMembreDTO.getIdMembre());
    //            if(empruntsMembre.size() == unMembreDTO.getLimitePret()) {
    //                throw new ServiceException("Le membre "
    //                    + unMembreDTO.getNom()
    //                    + " (ID de membre : "
    //                    + unMembreDTO.getIdMembre()
    //                    + ") a atteint sa limite de prêt ("
    //                    + unMembreDTO.getLimitePret()
    //                    + " emprunt(s) maximum)");
    //            }
    //
    //            // Cas éliminé en utilisant la date de prêt et de réservation comme étant la date système de la base de données
    //
    //            /* Verifier si datePret >= tupleReservation.dateReservation */
    //            // if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
    //            //     throw new BibliothequeException("Date de prêt inférieure à la date de réservation");
    //            // }
    //
    //            annuler(uneReservationDTO);
    //            // On voit le manque de la table prêt simulée en ce moment par les deux tables
    //            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
    //        } catch(DAOException daoException) {
    //            throw new ServiceException(daoException);
    //        }
    //    }
    //
    //    /**
    //     * Annule une réservation.
    //     *
    //     * @param reservationDTO Le reservation à annuler
    //     * @throws ServiceException Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
    //     */
    //    public void annuler(ReservationDTO reservationDTO) throws ServiceException {
    //        final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
    //        if(uneReservationDTO == null) {
    //            throw new ServiceException("La réservation "
    //                + reservationDTO.getIdReservation()
    //                + " n'existe pas");
    //        }
    //        delete(uneReservationDTO);
    //    }

    //***************NEW***************
    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO de réservation est <code>null</code>, Si le DAO de membre est <code>null</code>, Si le DAO de livre est <code>null</code>, Si le DAO de prêt est <code>null</code>
     */
    public ReservationService(IReservationDAO reservationDAO,
        ILivreDAO livreDAO,
        IMembreDAO membreDAO,
        IPretDAO pretDAO) throws InvalidDAOException {
        super();
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de livre ne peut être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
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
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
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
    public void add(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getReservationDAO().add(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReservationDTO get(Connexion connexion,
        String idReservation) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return (ReservationDTO) getReservationDAO().get(connexion,
                idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getLivreDAO().update(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getLivreDAO().delete(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return (List<ReservationDTO>) getReservationDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getReservationDAO().findByMembre(connexion,
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
    public List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getReservationDAO().findByLivre(connexion,
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
    public void placer(Connexion connexion,
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
        ServiceException {
        try {
            //TODO : Mettre en string le ID de reservationDTO
            //TODO : Check si bon type exception existe
            final ReservationDTO uneReservationDTO = get(connexion,
                reservationDTO.getIdReservation());
            if(uneReservationDTO != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }
            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                reservationDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                reservationDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }
            //TODO : refaire autrement
            final MembreDTO emprunteur = getMembreDAO().get(connexion,
                unLivreDTO.getIdMembre());
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

            //TODO : ajouter les constantes statiques COLUMN_NAME dans MembreDTO
            //TODO : vérifier orderby voulu
            //TODO : vérifier la condition
            final List<ReservationDTO> reservations = getReservationDAO().findByMembre(connexion,
                unMembreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);
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
            add(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliser(Connexion connexion,
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
        ServiceException {
        try {
            ReservationDTO uneReservationDTO = get(connexion,
                reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }
            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                uneReservationDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + uneReservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                uneReservationDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + uneReservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                LivreDTO.ID_LIVRE_COLUMN_NAME);
            if(!reservations.isEmpty()) {
                uneReservationDTO = reservations.get(0);
                if(uneReservationDTO.getMembreDTO().getIdMembre() != unMembreDTO.getIdMembre()) {
                    final MembreDTO booker = (MembreDTO) getMembreDAO().get(connexion,
                        uneReservationDTO.getMembreDTO().getIdMembre());
                    throw new ServiceException("Le livre "
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
            //TODO : refaire autrement
            final MembreDTO emprunteur = getMembreDAO().get(connexion,
                livreDTO.getIdMembre());
            if(emprunteur != null) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a été prêté à "
                    + emprunteur.getNom()
                    + " (ID de membre : "
                    + emprunteur.getIdMembre()
                    + ")");
            }
            //TODO : changer findByMembre dans PretDAO
            final List<PretDTO> empruntsMembre = getPretDAO().findByMembre(connexion,
                unMembreDTO.getIdMembre());
            if(empruntsMembre.size() == unMembreDTO.getLimitePret()) {
                throw new ServiceException("Le membre "
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

            annuler(connexion,
                uneReservationDTO);
            // On voit le manque de la table prêt simulée en ce moment par les deux tables
            //TODO : refaire autrement (si nécessaire)
            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            final ReservationDTO uneReservationDTO = get(connexion,
                reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }
            delete(connexion,
                uneReservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
