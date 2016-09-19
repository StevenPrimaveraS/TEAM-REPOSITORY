// Fichier BDCreateurException.java
// Auteur : Anthony Chan
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.exception;

/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple -- livre inexistant
 *
 * @author Anthony Chan
 */

public final class BibliothequeException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * .
     * @param message -
     */
    public BibliothequeException(String message) {

        super(message);
    }
}
