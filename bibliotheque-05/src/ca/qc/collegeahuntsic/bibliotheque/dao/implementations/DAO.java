// Fichier DAO.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;

/**
 * Classe de base pour tous les DAOs.<br />
 * Tous les DAOs devraient en hériter.
 *
 * @author Gilles Bénichou
 */
public class DAO {
    private Class<? extends DTO> dtoClass;

    /**
     * Crée un DAO.
     *
     * @param dtoClass La classe de DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    protected DAO(Class<? extends DTO> dtoClass) throws InvalidDTOClassException {
        super();
        if(dtoClass == null) {
            throw new InvalidDTOClassException("La classe de DTO ne peut être null");
        }
        setDtoClass(dtoClass);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.dtoClass</code>.
     *
     * @return La variable d'instance <code>this.dtoClass</code>
     */
    protected Class<? extends DTO> getDtoClass() {
        return this.dtoClass;
    }

    /**
     * Setter de la variable d'instance <code>this.dtoClass</code>.
     *
     * @param dtoClass La valeur à utiliser pour la variable d'instance <code>this.dtoClass</code>
     */
    private void setDtoClass(Class<? extends DTO> dtoClass) {
        this.dtoClass = dtoClass;
    }
    // EndRegion Getters and Setters
}
