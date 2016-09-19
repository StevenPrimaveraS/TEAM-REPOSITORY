// Fichier ReservationDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;

/**
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Mathieu Lafond
 */

public class ReservationDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtExisteLivre;

    private PreparedStatement stmtExisteMembre;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtDelete;

    /**
      * Crée un DAO à partir d'une connexion à la base de données.
      *
      * @param cx - La connexion à utiliser
      * @throws SQLException si une erreur survient
      */
    public ReservationDAO(Connexion cx) throws SQLException {
        super(cx);
        this.stmtExiste = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idReservation = ?");
        this.stmtExisteLivre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idLivre = ? "
            + "order by dateReservation");
        this.stmtExisteMembre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idMembre = ? ");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
            + "values (?,?,?,to_date(?,'YYYY-MM-DD'))");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from reservation where idReservation = ?");
    }

    /**
      * Verifie si une reservation existe.
      *
      * @param idReservation identifiant de la reservation
      * @return boolean si le livre existe ou pas
      * @throws SQLException si une erreur survient
      */
    public boolean existe(int idReservation) throws SQLException {

        this.stmtExiste.setInt(1,
            idReservation);
        final ResultSet rset = this.stmtExiste.executeQuery();
        final boolean reservationExiste = rset.next();
        rset.close();
        return reservationExiste;
    }

    /**
      * Lecture d'une reservation.
      *
      * @param idReservation identifiant de la reservation.
      * @throws SQLException si une erreur survient
      * @return ReservationDTO retourne un DTO de reservation
      */
    public ReservationDTO getReservation(int idReservation) throws SQLException {

        this.stmtExiste.setInt(1,
            idReservation);
        final ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            final ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));
            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            rset.close();
            return tupleReservation;
        }
        rset.close();
        return null;
    }

    /**
      * Lecture de la premiere reservation d'un livre.
      *
      * @param idLivre identifiant du livre.
      * @throws SQLException si une erreur survient
      * @return ReservationDTO retourne un DTO de reservation
      */
    public ReservationDTO getReservationLivre(int idLivre) throws SQLException {

        this.stmtExisteLivre.setInt(1,
            idLivre);
        final ResultSet rset = this.stmtExisteLivre.executeQuery();
        if(rset.next()) {
            final ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));
            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            rset.close();
            return tupleReservation;
        }
        rset.close();
        return null;
    }

    /**
      * Lecture de la premiere reservation d'un livre.
      *
      * @param idMembre identifiant de la reservation.
      * @throws SQLException si une erreur survient
      * @return ReservationDTO retourne un DTO de reservation
      */
    public ReservationDTO getReservationMembre(int idMembre) throws SQLException {

        this.stmtExisteMembre.setInt(1,
            idMembre);
        final ResultSet rset = this.stmtExisteMembre.executeQuery();
        if(rset.next()) {
            final ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));
            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            rset.close();
            return tupleReservation;
        }
        rset.close();
        return null;
    }

    /**
      * Reservation d'un livre.
      *
      * @param idReservation identifiant de la reservation.
      * @param idLivre identifiant du livre
      * @param idMembre identifiant du membre
      * @param dateReservation date de la reservation
      * @throws SQLException si une erreur survient
      */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException {
        this.stmtInsert.setInt(1,
            idReservation);
        this.stmtInsert.setInt(2,
            idLivre);
        this.stmtInsert.setInt(3,
            idMembre);
        this.stmtInsert.setString(4,
            dateReservation);
        this.stmtInsert.executeUpdate();
    }

    /**
      * Suppression d'une reservation.
      *
      * @param idReservation identifiant de la reservation.
      * @throws SQLException si une erreur survient
      * @return ReservationDTO retourne un DTO de reservation
      */
    public int annulerRes(int idReservation) throws SQLException {
        this.stmtDelete.setInt(1,
            idReservation);
        return this.stmtDelete.executeUpdate();
    }
}