// Fichier MembreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mathieu Lafond
 *
 */

public class MembreDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param cx - La connexion à utiliser
     * @throws SQLException si une erreur survient
     */
    public MembreDAO(Connexion cx) throws SQLException {
        super(cx);
        this.stmtExiste = cx.getConnection().prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
            + "values (?,?,?,?,0)");
        this.stmtUpdateIncrNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
        this.stmtUpdateDecNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from membre where idmembre = ?");
    }

    /**
      * Verifie si un membre existe.
      * @param idMembre identifiant du membre
      * @return boolean si le livre existe ou pas
      * @throws SQLException si une erreur survient
      */
    public boolean existe(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);
        final ResultSet rset = this.stmtExiste.executeQuery();
        final boolean membreExiste = rset.next();
        rset.close();
        return membreExiste;
    }

    /**
      * Lecture d'un membre.
      * @param idMembre identifiant du membre.
      * @throws SQLException si une erreur survient.
      * @return MembreDTO
      */
    public MembreDTO getMembre(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);
        final ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            final MembreDTO tupleMembre = new MembreDTO();
            tupleMembre.setIdMembre(idMembre);
            tupleMembre.setNom(rset.getString(2));
            tupleMembre.setTelephone(rset.getLong(3));
            tupleMembre.setLimitePret(rset.getInt(4));
            tupleMembre.setNbPret(rset.getInt(5));
            rset.close();
            return tupleMembre;
        }
        rset.close();
        return null;
    }

    /**
      * Ajout d'un nouveau membre.
      * @param idMembre identificateur du membre
      * @param nom nom du membre
      * @param telephone numero de telephone du membre
      * @param limitePret limite de pret du membre
      * @throws SQLException si une erreur survient
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException {
        /* Ajout du membre. */
        this.stmtInsert.setInt(1,
            idMembre);
        this.stmtInsert.setString(2,
            nom);
        this.stmtInsert.setLong(3,
            telephone);
        this.stmtInsert.setInt(4,
            limitePret);
        this.stmtInsert.executeUpdate();
    }

    /**
      * Incrementer le nb de pret d'un membre.
      * @param idMembre identifiant du membre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      */
    public int preter(int idMembre) throws SQLException {
        this.stmtUpdateIncrNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateIncrNbPret.executeUpdate();
    }

    /**
      * Decrementer le nb de pret d'un membre.
      * @param idMembre identifiant du membre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de retour
      */
    public int retourner(int idMembre) throws SQLException {
        this.stmtUpdateDecNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateDecNbPret.executeUpdate();
    }

    /**
      * Suppression d'un membre.
      * @param idMembre identifiant du membre
      * @throws SQLException si une erreur survient
      * @return int resultat de la suppression
      */
    public int desinscrire(int idMembre) throws SQLException {
        this.stmtDelete.setInt(1,
            idMembre);
        return this.stmtDelete.executeUpdate();
    }
}