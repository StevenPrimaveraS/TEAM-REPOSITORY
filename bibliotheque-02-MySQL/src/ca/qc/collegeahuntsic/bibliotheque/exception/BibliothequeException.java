
package ca.qc.collegeahuntsic.bibliotheque.exception;

/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple -- livre inexistant
 */

public final class BibliothequeException extends Exception {
	private static final long serialVersionUID = 1L;

	public BibliothequeException(String message) {
        super(message);
    }
}