// Fichier ReservationService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table reservation.
 *
 * @author Primavera Sequeira Steven
 */

public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private ReservationDAO reservationDAO;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO - reservation qu'on reçoit en paramètre dans la méthode
     * @param livreDAO - livre qu'on recoit en paramètre dans la méthode
     * @param membreDAO - membre qu'on recoit en paramètre dans la méthode
     */
    public ReservationService(ReservationDAO reservationDAO,
        LivreDAO livreDAO,
        MembreDAO membreDAO) {
        setReservationDAO(reservationDAO);
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
    }

    /**
     * Ajoute une nouvelle reservation.
     *
     * @param reservationDTO La reservation à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit une reservation. Si aucune reservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param idReservation L'ID du reservation à lire
     * @return La reservation lue ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws ServiceException {
        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour une reservation.
     *
     * @param reservationDTO La reservation à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime une reservation.
     *
     * @param reservationDTO La reservation à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve toutes les reservations.
     *
     * @return La liste des reservations ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws ServiceException {
        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Réservation d'un livre par un membre. Le livre doit être prété.
     *
     * @param idReservation - id de la réservation qu'on veut réserver.
     * @param idLivre - id du livre qu'on veut réserver.
     * @param idMembre - id du livre qu'on veut réserver
     * @param dateReservation - date de la réservation.
     * @throws ServiceException - Si la réservation existe déjà,
     *         si le membre n'existe pas, si le livre n'existe pas, 
     *         si le livre n'a pas encore été prêté, 
     *         si le livre est déjà prêté au membre, 
     *         si le membre a déjà réservé ce livre ou 
     *         s'il y a une erreur avec la base de données
     */
    public void reserver(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            final MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdMembre());
            if(uneReservationDTO != null) {
                throw new ServiceException("La reservation: "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre: "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre: "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unLivreDTO.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " n'est pas prêté");
            }
            if(unLivreDTO.getIdMembre() == unMembreDTO.getIdMembre()) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " déjà prêté à ce membre");
            }
            //?
            for(ReservationDTO reservations : getReservationDAO().findByMembre(unMembreDTO)) {
                if(reservations.getIdLivre() == unLivreDTO.getIdLivre()) {
                    throw new ServiceException("Le membre "
                        + membreDTO.getIdMembre()
                        + " a déjà réservé le livre "
                        + livreDTO.getIdLivre());
                }
            }
            //?
            uneReservationDTO = new ReservationDTO();
            uneReservationDTO.setIdLivre(unLivreDTO.getIdLivre());
            uneReservationDTO.setIdMembre(unMembreDTO.getIdMembre());
            add(uneReservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Prise d'une réservation. Le livre ne doit pas être prété. Le membre ne
     * doit pas avoir dépassé sa limite de pret. La réservation doit la être la
     * première en liste.
     *
     * @param idReservation - id de la réservation.
     * @param datePret - date du prêt de la réservation.
     * @throws ServiceException -  Si la réservation n'existe pas, 
     *         si le membre n'existe pas, si le livre n'existe pas, 
     *         si la réservation n'est pas la première de la liste, 
     *         si le livre est déjà prété, 
     *         si le membre a atteint sa limite de prêt ou 
     *         s'il y a une erreur avec la base de données
     */
    public void utiliser(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            final MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            if(uneReservationDTO == null) {
                throw new ServiceException("Réservation inexistante : "
                    + reservationDTO.getIdReservation());
            }
            if(unMembreDTO == null) {
                throw new ServiceException("Membre inexistant : "
                    + membreDTO.getIdMembre());
            }
            if(unLivreDTO == null) {
                throw new ServiceException("Livre inexistant : "
                    + livreDTO.getIdLivre());
            }
            if(uneReservationDTO.getIdReservation() == getReservationDAO().findByLivre(unLivreDTO).get(0).getIdReservation()) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'est pas la première dans la file d'attente");
            }
            if(unLivreDTO.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " deja prêté à "
                    + livreDTO.getIdMembre());
            }
            if(unMembreDTO.getNbPret() >= unMembreDTO.getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + membreDTO.getIdMembre()
                    + " atteinte");
            }
            //?
            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
            getLivreDAO().emprunter(unLivreDTO);
            getMembreDAO().emprunter(unMembreDTO);
            delete(uneReservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Annulation d'une réservation. La réservation doit exister.
     *
     * @param idReservation - id de la réservation qu'on veux annuler.
     * @throws ServiceException - Si la réservation n'existe pas ou 
     *         s'il y a une erreur avec la base de données
     */
    public void annuler(ReservationDTO reservationDTO) throws ServiceException {
        final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
        if(uneReservationDTO == null) {
            throw new ServiceException("La réservation "
                + reservationDTO.getIdReservation()
                + " n'existe pas");
        }
        delete(uneReservationDTO);
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private MembreDAO getMembreDAO() {
        return membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private ReservationDAO getReservationDAO() {
        return reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }
}
