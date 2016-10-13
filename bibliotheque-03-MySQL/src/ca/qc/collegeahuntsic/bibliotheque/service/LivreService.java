// Fichier LivreService.java
// Auteur : Mathieu Lafond
// Date de création : 2016-05-18

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
 * Service de la table <code>livre</code>.
 *
 * @author Mathieu Lafond
 */
public class LivreService extends Service {
    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public LivreService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        super();
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters
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

    // EndRegion Getters and Setters

    /**
     * Ajoute un nouveau livre.
     *
     * @param livreDTO Le livre à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().add(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit un livre. Si aucun livre n'est trouvé, <code>null</code> est retourné.
     *
     * @param idLivre L'ID du livre à lire
     * @return Le livre lu ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public LivreDTO read(int idLivre) throws ServiceException {
        try {
            return getLivreDAO().read(idLivre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour un livre.
     *
     * @param livreDTO Le livre à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().update(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime un livre.
     *
     * @param livreDTO Le livre à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().delete(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve tous les livres.
     *
     * @return La liste des livres ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> getAll() throws ServiceException {
        try {
            return getLivreDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les livres à partir d'un titre.
     *
     * @param titre Le titre à utiliser
     * @return La liste des livres correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> findByTitre(String titre) throws ServiceException {
        try {
            return getLivreDAO().findByTitre(titre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les livres à partir d'un membre.
     *
     * @param idMembre L'ID du membre à utiliser
     * @return La liste des livres correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> findByMembre(int idMembre) throws ServiceException {
        try {
            return getLivreDAO().findByMembre(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Acquiert un livre.
     *
     * @param livreDTO Le livre à ajouter
     * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        if(read(livreDTO.getIdLivre()) != null) {
            throw new ServiceException("Le livre "
                + livreDTO.getIdLivre()
                + " existe déjà");
        }
        add(livreDTO);
    }

    /**
     * Emprunte un livre.
     *
     * @param livreDTO Le livre à emprunter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void emprunter(LivreDTO livreDTO) throws ServiceException {
        // On voit le manque de la table prêt avec le décalage illogique (bancal) entre MembreService.emprunte et cette méthode
        try {
            getLivreDAO().emprunter(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param livreDTO Le livre à retourner
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void retourner(LivreDTO livreDTO) throws ServiceException {
        // On voit le manque de la table prêt avec le décalage illogique (bancal) entre MembreService.emprunte et cette méthode
        try {
            getLivreDAO().retourner(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Vendre un livre.
     *
     * @param livreDTO Le livre à vendre
     * @throws ServiceException Si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé ou s'il y a une erreur avec la base
     *         de données
     */
    public void vendre(LivreDTO livreDTO) throws ServiceException {
        try {
            final LivreDTO unLivreDTO = read(livreDTO.getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            final MembreDTO membreDTO = getMembreDAO().read(unLivreDTO.getIdMembre());
            if(!getLivreDAO().findByMembre(membreDTO.getIdMembre()).isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a été prêté à "
                    + membreDTO.getNom()
                    + " (ID de membre : "
                    + membreDTO.getIdMembre()
                    + ")");
            }
            if(!getReservationDAO().findByLivre(unLivreDTO.getIdLivre()).isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a des réservations");
            }
            delete(unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
