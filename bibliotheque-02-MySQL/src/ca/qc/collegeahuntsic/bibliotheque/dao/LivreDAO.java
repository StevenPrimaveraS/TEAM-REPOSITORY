// Fichier LivreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;

/**
 * DAO pour effectuer des CRUDs avec la table livre.
 *
 * @author Mathieu Lafond
 */

public class LivreDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param cx - La connexion à utiliser
     * @throws SQLException si une erreur survient
     */
    public LivreDAO(Connexion cx) throws SQLException {
        super(cx);
        this.stmtExiste = cx.getConnection()
            .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
            + "values (?,?,?,?,null,null)");
        this.stmtUpdate = cx.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
            + "where idLivre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from livre where idlivre = ?");
    }

    /**
      * Verifie si un livre existe.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return boolean si le livre existe ou pas
      */
    public boolean existe(int idLivre) throws SQLException {

        this.stmtExiste.setInt(1,
            idLivre);
        final ResultSet rset = this.stmtExiste.executeQuery();
        final boolean livreExiste = rset.next();
        rset.close();
        return livreExiste;
    }

    /**
      * Lecture d'un livre.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return LivreDTO retourne un DTO de livre
      */
    public LivreDTO getLivre(int idLivre) throws SQLException {

        this.stmtExiste.setInt(1,
            idLivre);
        final ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            final LivreDTO tupleLivre = new LivreDTO();
            tupleLivre.setIdLivre(idLivre);
            tupleLivre.setTitre(rset.getString(2));
            tupleLivre.setAuteur(rset.getString(3));
            tupleLivre.setDateAcquisition(rset.getDate(4));
            tupleLivre.setIdMembre(rset.getInt(5));
            tupleLivre.setDatePret(rset.getDate(6));
            rset.close();
            return tupleLivre;
        }
        rset.close();
        return null;
    }

    /**
      * Ajout d'un nouveau livre dans la base de donnees.
      * @param idLivre identifiant du livre
      * @param titre titre du livre
      * @param auteur auteur du livre
      * @param dateAcquisition date d'acquisition
      * @throws SQLException si une erreur survient
      */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException {
        /* Ajout du livre. */
        this.stmtInsert.setInt(1,
            idLivre);
        this.stmtInsert.setString(2,
            titre);
        this.stmtInsert.setString(3,
            auteur);
        this.stmtInsert.setDate(4,
            Date.valueOf(dateAcquisition));
        this.stmtInsert.executeUpdate();
    }

    /**
      * Enregistrement de l'emprunteur d'un livre.
      * @param idLivre identifiant du livre
      * @param idMembre identifiant du membre
      * @param datePret date du Pret
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException {
        /* Enregistrement du pret. */
        this.stmtUpdate.setInt(1,
            idMembre);
        this.stmtUpdate.setDate(2,
            Date.valueOf(datePret));
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
      * Rendre le livre disponible (non-prete).
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      */
    public int retourner(int idLivre) throws SQLException {
        /* Enregistrement du pret. */
        this.stmtUpdate.setNull(1,
            Types.INTEGER);
        this.stmtUpdate.setNull(2,
            Types.DATE);
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
      * Suppression d'un livre.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      */
    public int vendre(int idLivre) throws SQLException {
        /* Suppression du livre. */
        this.stmtDelete.setInt(1,
            idLivre);
        return this.stmtDelete.executeUpdate();
    }
}