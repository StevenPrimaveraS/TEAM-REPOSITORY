// Fichier Bibliotheque.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;

/**
 * Interface du système de gestion d'une bibliothèque.<br /><br />
 *
 * Ce programme permet d'appeler les transactions de base d'une bibliothèque.  Il gère des livres, des membres et des réservations. Les données
 * sont conservées dans une base de données relationnelles accédée avec JDBC. Pour une liste des transactions traitées, voir la méthode
 * {@link Bibliotheque#afficherAide() afficherAide()}.<br /><br />
 *
 * Paramètres :<br />
 * 0- site du serveur SQL ("local", "distant" ou "postgres")<br />
 * 1- nom de la BD<br />
 * 2- user id pour établir une connexion avec le serveur SQL<br />
 * 3- mot de passe pour le user id<br />
 * 4- fichier de transaction<br /><br />
 *
 * Pré-condition :<br />
 *   La base de données de la bibliothèque doit exister<br /><br />
 *
 * Post-condition :<br />
 *   Le programme effectue les maj associées à chaque transaction
 *
 * @author Gilles Bénichou
 */
public final class Bibliotheque {
    private static BibliothequeCreateur gestionnaireBibliotheque;

    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private Bibliotheque() {
        super();
    }

    /**
     * Crée une connexion sur la base de données, traite toutes les transactions et détruit la connexion.
     *
     * @param arguments Les arguments du main
     * @throws Exception Si une erreur survient
     */
    public static void main(String[] arguments) throws Exception {
        // Validation du nombre de paramètres
        if(arguments.length < 5) {
            System.out.println("Usage: java Bibliotheque <serveur> <bd> <user> <password> <fichier-transactions>");
            System.out.println(Connexion.getServeursSupportes());
            return;
        }

        try {
            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[4]);
            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionnaireBibliotheque = new BibliothequeCreateur(arguments[0],
                    arguments[1],
                    arguments[2],
                    arguments[3]);
                Bibliotheque.traiterTransactions(reader);
            }
        } catch(Exception exception) {
            Bibliotheque.gestionnaireBibliotheque.rollback();
            exception.printStackTrace(System.out);
        } finally {
            Bibliotheque.gestionnaireBibliotheque.close();
        }
    }

    /**
     * Traite le fichier de transactions.
     *
     * @param reader Le flux d'entrée à lire
     * @throws Exception Si une erreur survient
     */
    private static void traiterTransactions(BufferedReader reader) throws Exception {
        Bibliotheque.afficherAide();
        System.out.println("\n\n\n");
        String transaction = Bibliotheque.lireTransaction(reader);
        while(!Bibliotheque.finTransaction(transaction)) {
            // Découpage de la transaction en mots
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                Bibliotheque.executerTransaction(tokenizer);
            }
            transaction = Bibliotheque.lireTransaction(reader);
        }
    }

    /**
     * Lit une transaction.
     *
     * @param reader Le flux d'entrée à lire
     * @return La transaction lue
     * @throws IOException Si une erreur de lecture survient
     */
    private static String lireTransaction(BufferedReader reader) throws IOException {
        final String transaction = reader.readLine();
        if(transaction != null) {
            System.out.println("> "
                + transaction);
        }
        return transaction;
    }

    /**
     * Décode et traite une transaction.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void executerTransaction(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            final String command = tokenizer.nextToken();

            switch(command) {
                case "aide":
                    Bibliotheque.afficherAide();
                    break;
                case "acquerir":
                    Bibliotheque.acquerir(tokenizer);
                    break;
                case "vendre":
                    Bibliotheque.vendre(tokenizer);
                    break;
                case "preter":
                    Bibliotheque.preter(tokenizer);
                    break;
                case "renouveler":
                    Bibliotheque.renouveler(tokenizer);
                    break;
                case "retourner":
                    Bibliotheque.retourner(tokenizer);
                    break;
                case "inscrire":
                    Bibliotheque.inscrire(tokenizer);
                    break;
                case "desinscrire":
                    Bibliotheque.desinscrire(tokenizer);
                    break;
                case "reserver":
                    Bibliotheque.reserver(tokenizer);
                    break;
                case "utiliser":
                    Bibliotheque.utiliser(tokenizer);
                    break;
                case "annuler":
                    Bibliotheque.annuler(tokenizer);
                    break;
                case "--":
                    break;
                default:
                    System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
                    break;
            }
            //            //******************* ANCIEN ***********************
            //            if("aide".equals(command)) {
            //                Bibliotheque.afficherAide();
            //            } else if("acquerir".equals(command)) {
            //                Bibliotheque.acquerir(tokenizer,
            //                    connexion);
            //            } else if("vendre".equals(command)) {
            //                Bibliotheque.vendre(tokenizer,
            //                    connexion);
            //            } else if("preter".equals(command)) {
            //                Bibliotheque.preter(tokenizer,
            //                    connexion);
            //            } else if("renouveler".equals(command)) {
            //                Bibliotheque.renouveler(tokenizer,
            //                    connexion);
            //            } else if("retourner".equals(command)) {
            //                Bibliotheque.retourner(tokenizer,
            //                    connexion);
            //            } else if("inscrire".equals(command)) {
            //                Bibliotheque.inscrire(tokenizer,
            //                    connexion);
            //            } else if("desinscrire".equals(command)) {
            //                Bibliotheque.desinscrire(tokenizer,
            //                    connexion);
            //            } else if("reserver".equals(command)) {
            //                Bibliotheque.reserver(tokenizer,
            //                    connexion);
            //            } else if("utiliser".equals(command)) {
            //                Bibliotheque.utiliser(tokenizer,
            //                    connexion);
            //            } else if("annuler".equals(command)) {
            //                Bibliotheque.annuler(tokenizer,
            //                    connexion);
            //                // } else if("listerLivres".equals(command)) {
            //                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivres();
            //                // } else if("listerLivresRetard".equals(command)) {
            //                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivresRetard(readString(tokenizer) /* date courante */);
            //                // } else if("listerLivresTitre".equals(command)) {
            //                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivresTitre(readString(tokenizer) /* mot */);
            //            } else if(!"--".equals(command)) {
            //                System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            //            }
        } catch(BibliothequeException bibliothequeException) {
            System.out.println("** "
                + bibliothequeException.toString());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("séparés par des espaces. La liste peut être vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.");
        System.out.println("");
        System.out.println("Les transactions sont :");
        System.out.println("  aide");
        System.out.println("  exit");
        System.out.println("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        System.out.println("  preter <idMembre> <idLivre>");
        System.out.println("  renouveler <idLivre>");
        System.out.println("  retourner <idLivre>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  reserver <idReservation> <idMembre> <idLivre>");
        System.out.println("  utiliser <idReservation>");
        System.out.println("  annuler <idReservation>");
        // System.out.println("  listerLivresRetard <dateCourante>");
        // System.out.println("  listerLivresTitre <mot>");
        // System.out.println("  listerLivres");
    }

    /**
     * Transaction pour acquerir un livre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void acquerir(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setTitre(Bibliotheque.readString(tokenizer));
            livreDTO.setAuteur(Bibliotheque.readString(tokenizer));
            livreDTO.setDateAcquisition(Bibliotheque.readDate(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().acquerir(connexion,
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour vendre un livre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void vendre(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().vendre(connexion,
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidDTOClassException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour prêter un livre à un membre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void preter(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(livreDTO);
            pretDTO.setMembreDTO(membreDTO);
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(connexion,
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | InvalidLoanLimitException
            | ExistingReservationException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour renouveler le prêt d'un livre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void renouveler(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(livreDTO);
            pretDTO.setMembreDTO(membreDTO);
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().renouveler(connexion,
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | InvalidLoanLimitException
            | ExistingReservationException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour retourner un livre suite à un prêt.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void retourner(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(livreDTO);
            pretDTO.setMembreDTO(membreDTO);
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().terminer(connexion,
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | InvalidLoanLimitException
            | ExistingReservationException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour inscrire un membre.
     *
     * @param tokenizer Données de la transaction
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void inscrire(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setNom(Bibliotheque.readString(tokenizer));
            membreDTO.setTelephone(Bibliotheque.readString(tokenizer));
            membreDTO.setLimitePret(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().inscrire(Bibliotheque.gestionnaireBibliotheque.getSession(),
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour désinscrire un livre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void desinscrire(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().desinscrire(connexion,
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidDTOClassException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour réserver un livre.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void reserver(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            // Juste pour éviter deux dates de réservation strictement identiques
            Thread.sleep(1);
            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            reservationDTO.setMembreDTO(membreDTO);
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            reservationDTO.setLivreDTO(livreDTO);
            membreDTO.setIdMembre(reservationDTO.getMembreDTO().getIdMembre());
            livreDTO.setIdLivre(reservationDTO.getLivreDTO().getIdLivre());
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().placer(connexion,
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InterruptedException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | MissingLoanException
            | ExistingLoanException
            | ExistingReservationException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour utiliser une réservation.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void utiliser(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            reservationDTO.setMembreDTO(membreDTO);
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            reservationDTO.setLivreDTO(livreDTO);
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().utiliser(connexion,
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Transaction pour annuler une réservation.
     *
     * @param tokenizer Données de la transaction
     * @param connexion La connexion à utiliser
     * @throws BibliothequeException Si une erreur survient au cours du transactionnel
     */
    private static void annuler(StringTokenizer tokenizer,
        Connexion connexion) throws BibliothequeException {
        try {
            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().annuler(connexion,
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidDTOClassException
            | FacadeException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     *
     * @param transaction La transaction à traiter
     * @return <code>true</code> Si la fin du fichier est atteinte, <code>false</code> sinon
     */
    private static boolean finTransaction(String transaction) {
        boolean finDeFichier = transaction == null;
        if(!finDeFichier) {
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            finDeFichier = !tokenizer.hasMoreTokens();
            if(!finDeFichier) {
                final String commande = tokenizer.nextToken();
                finDeFichier = "exit".equals(commande);
            }
        }

        return finDeFichier;
    }

    /**
     * Lit une chaîne de caractères de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La chaîne de caractères lue
     * @throws BibliothequeException Si l'élément lu est manquant
     */
    private static String readString(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

    //      ****** readInt et readLong ******
    //    /**
    //     * Lit un integer de la transaction.
    //     *
    //     * @param tokenizer La transaction à décoder
    //     * @return Le integer lu
    //     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas un integer
    //     */
    //    private static int readInt(StringTokenizer tokenizer) throws BibliothequeException {
    //        if(tokenizer.hasMoreElements()) {
    //            final String token = tokenizer.nextToken();
    //            try {
    //                return Integer.valueOf(token).intValue();
    //            } catch(NumberFormatException numberFormatException) {
    //                throw new BibliothequeException("Nombre attendu à la place de \""
    //                    + token
    //                    + "\"");
    //            }
    //        }
    //        throw new BibliothequeException("Autre paramètre attendu");
    //    }
    //
    //    /**
    //     * Lit un long de la transaction.
    //     *
    //     * @param tokenizer La transaction à décoder
    //     * @return Le long lu
    //     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas un long
    //     */
    //    private static long readLong(StringTokenizer tokenizer) throws BibliothequeException {
    //        if(tokenizer.hasMoreElements()) {
    //            final String token = tokenizer.nextToken();
    //            try {
    //                return Long.valueOf(token).longValue();
    //            } catch(NumberFormatException numberFormatException) {
    //                throw new BibliothequeException("Nombre attendu à la place de \""
    //                    + token
    //                    + "\"");
    //            }
    //        }
    //        throw new BibliothequeException("Autre paramètre attendu");
    //    }

    /**
     * Lit une date au format YYYY-MM-DD de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La date lue
     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas une date correctement formatée
     */
    private static Timestamp readDate(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return FormatteurDate.timestampValue(token);
            } catch(ParseException parseException) {
                throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }
}
