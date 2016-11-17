// Fichier IFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import org.hibernate.Session;

/**
 * Interface de base pour les façades.<br />
 * Toutes les interfaces de façade devraient en hériter.
 *
 * @author Gilles Bénichou
 */
public interface IFacade {
    /**
     * Lit un DTO à partir de la base de données. Si aucun DTO n'est trouvé, <code>null</code> est retourné.
     *
     * @param session La session Hibernate à utiliser
     * @param primaryKey La clef primaire du DTO à lire
     * @return Le DTO lu ; <code>null</code> sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire du DTO est <code>null</code>
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    DTO get(Session session,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        FacadeException;
}
