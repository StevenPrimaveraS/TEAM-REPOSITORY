package ca.qc.collegeahuntsic.bibliotheque.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un
 * objet Date.
 *
 * @author Math -
 */
public class FormatteurDate {
	private static SimpleDateFormat formatAMJ;
	static {
		formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
		formatAMJ.setLenient(false);
	}

	/**
	 * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
	 *
	 * @param dateString
	 *            -
	 * @return -
	 * @throws ParseException
	 *             -
	 */
	public static Date convertirDate(String dateString) throws ParseException {
		return formatAMJ.parse(dateString);
	}

	/**
	 * Affiche la date.
	 * 
	 * @return -
	 * @param date
	 *            -
	 */
	public static String toString(Date date) {
		return formatAMJ.format(date);
	}
}
