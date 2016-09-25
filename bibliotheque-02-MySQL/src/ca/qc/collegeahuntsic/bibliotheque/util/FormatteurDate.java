// Fichier FormatteurDate.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un
 * objet Date.
 *
 * @author Mathieu Lafond
 */
public final class FormatteurDate {
    private static SimpleDateFormat formatAMJ;
    static {
        formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
        formatAMJ.setLenient(false);
    }

    /**
     * Constructeur par défaut.
     *
     * @author Primavera Sequeira Steven
     */
    private FormatteurDate() {
        super();
    }

    /**
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     *
     * @param dateString - la string à convertir en date
     * @return - retourne le resultat de la conversion de la string en date
     * @throws ParseException - Si une erreur survient
     */
    public static Date convertirDate(String dateString) throws ParseException {
        return formatAMJ.parse(dateString);
    }

    /**
     * Affiche la date en string.
     *
     * @param date - une date passée en paramètre
     * @return - retourne la date passée en string 
     */
    public static String toString(Date date) {
        return formatAMJ.format(date);
    }
}
