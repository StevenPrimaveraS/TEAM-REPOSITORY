// Fichier IService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import java.io.Serializable;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import org.hibernate.Session;

/**
 * Interface de base pour les services.<br />
 * Toutes les interfaces de service devraient en hériter.
 *
 * @author Gilles Bénichou
 */
public interface IService {
    /**
     * Ajoute un nouveau DTO dans la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param dto Le DTO à ajouter
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le DTO est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void add(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException;

    /**
     * Lit un DTO à partir de la base de données. Si aucun DTO n'est trouvé, <code>null</code> est retourné.
     *
     * @param session La session Hibernate à utiliser
     * @param primaryKey La clef primaire du DTO à lire
     * @return Le DTO lu ; <code>null</code> sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire du DTO est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    DTO get(Session session,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException;

    /**
     * Met à jour un DTO dans la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param dto Le DTO à mettre à jour
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le DTO est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void update(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException;

    /**
     * Ajoute un nouveau DTO dans la base de données s'il n'existe pas ; le met à jour sinon.
     *
     * @param session La session Hibernate à utiliser
     * @param dto Le DTO à ajouter ou à mettre à jour
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le DTO est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void save(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException;

    /**
     * Supprime un DTO de la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param dto Le DTO à supprimer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le DTO est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void delete(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException;

    /**
     * Trouve tous les DTOs de la base de données. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun
     * DTO n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste de tous les DTOs ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<? extends DTO> getAll(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException;
}
