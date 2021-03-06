// Fichier MembreService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table membre.
 *
 * @author Primavera Sequeira Steven
 */

public class MembreService extends Service {
    private static final long serialVersionUID = 1L;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>membre</code>.
     *
     * @param membreDAO MembreDAO qu'on reçoit en paramètre.
     * @param livreDAO LivreDAO qu'on reçoit en paramètre.
     * @param reservationDAO ReservationDAO qu'on reçoit en paramètre.
     */
    public MembreService(MembreDAO membreDAO,
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) {
        super();
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, <code>null</code> est retourné.
     *
     * @param idMembre L'ID du membre à lire
     * @return Le membre lu ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public MembreDTO read(int idMembre) throws ServiceException {
        try {
            return getMembreDAO().read(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour un membre.
     *
     * @param membreDTO Le membre à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().update(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO Le membre à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<MembreDTO> getAll() throws ServiceException {
        try {
            return getMembreDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Inscrit un membre.
     *
     * @param membreDTO Le membre qu'on veut ajouter.
     * @throws ServiceException - Si le membre existe déjà ou si
     *         une erreur survient avec la base de données.
     */
    public void inscrire(MembreDTO membreDTO) throws ServiceException {
        if(read(membreDTO.getIdMembre()) != null) {
            throw new ServiceException("Le membre "
                + membreDTO.getIdMembre()
                + " existe déjà");
        }
        add(membreDTO);
    }

    /**
     * Désincrit un membre.
     *
     * @param membreDTO - Le membre à désinscrire
     * @throws ServiceException - Si le membre n'existe pas,
     *         si le membre a encore des prêts,
     *         s'il a des réservations ou
     *         s'il y a une erreur avec la base de données
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unMembreDTO.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " a encore un ou plusieurs prêts en cours");
            }
            if(!getReservationDAO().findByMembre(unMembreDTO).isEmpty()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a des réservations");
            }
            delete(unMembreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Emprunte un livre.
     *
     * @param membreDTO - Le membre qui emprunte
     * @param livreDTO - Le livre à emprunter
     * @throws ServiceException - Si le membre n'existe pas,
     *         si le livre n'existe pas, si le livre a été prêté,
     *         si le livre a été réservé, si le membre a atteint sa limite de prêt
     *         ou s'il y a une erreur avec la base de données
     */
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            if(unLivreDTO.getIdMembre() != 0) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " a déjà été emprunté");
            }
            if(!getReservationDAO().findByLivre(unLivreDTO).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }
            if(unMembreDTO.getNbPret() == unMembreDTO.getLimitePret()) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " a atteint sa limite de prêts");
            }
            //?
            getMembreDAO().emprunter(unMembreDTO);
            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
            getLivreDAO().emprunter(unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Renouvelle le prêt d"un livre.
     *
     * @param membreDTO - Le membre qui renouvelle
     * @param livreDTO - Le livre à renouveler
     * @throws ServiceException - Si le membre n'existe pas,
     *         si le livre n'existe pas, si le livre n'a pas encore été prêté,
     *         si le livre a été prêté à quelqu'un d'autre,
     *         si le livre a été réservé ou
     *         s'il y a une erreur avec la base de données
     */
    public void renouveler(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            if(unLivreDTO.getIdMembre() == 0) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'a pas été emprunté");
            }
            if(unLivreDTO.getIdMembre() != unMembreDTO.getIdMembre()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " a été emprunté par quelqu'un d'autre");
            }
            if(!getReservationDAO().findByLivre(unLivreDTO).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }
            //?
            getLivreDAO().retourner(unLivreDTO);
            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
            getLivreDAO().emprunter(unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO - Le membre qui retourne
     * @param livreDTO - Le livre à retourner
     * @throws ServiceException - Si le membre n'existe pas,
     *         si le livre n'existe pas,
     *         si le livre n'a pas encore été prêté,
     *         si le livre a été prêté à quelqu'un d'autre ou
     *         s'il y a une erreur avec la base de données
     */
    public void retourner(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            if(unLivreDTO.getIdMembre() == 0) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'a pas été emprunté");
            }
            if(unLivreDTO.getIdMembre() != unMembreDTO.getIdMembre()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " a été emprunté par quelqu'un d'autre");
            }
            //?
            getMembreDAO().retourner(unMembreDTO);
            getLivreDAO().retourner(unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
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
        return this.membreDAO;
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
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

} //class
