// Fichier LivreDTO.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO de la table livre.
 *
 * @author Leroux Dominic
 */

public class LivreDTO extends DTO {
    private static final long serialVersionUID = 1L;

    private int idLivre;

    private int idMembre;

    private String titre;

    private String auteur;

    private Timestamp dateAcquisition;

    private Timestamp datePret;

    /**
     * Constructeur de LivreDTO par défaut.
     */

    public LivreDTO() {

    }

    /**
     * Getter de la variable d'instance <code>this.idLivre</code>.
     *
     * @return La variable d'instance <code>this.idLivre</code>
     */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.idLivre</code>.
     *
     * @param idLivre La valeur à utiliser pour la variable d'instance <code>this.idLivre</code>
     */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.titre</code>.
     *
     * @return La variable d'instance <code>this.titre</code>
     */
    public String getTitre() {
        return this.titre;
    }

    /**
     * Setter de la variable d'instance <code>this.titre</code>.
     *
     * @param titre La valeur à utiliser pour la variable d'instance <code>this.titre</code>
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Getter de la variable d'instance <code>this.auteur</code>.
     *
     * @return La variable d'instance <code>this.auteur</code>
     */
    public String getAuteur() {
        return this.auteur;
    }

    /**
     * Setter de la variable d'instance <code>this.auteur</code>.
     *
     * @param auteur La valeur à utiliser pour la variable d'instance <code>this.auteur</code>
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Getter de la variable d'instance <code>this.dateAcquisition</code>.
     *
     * @return La variable d'instance <code>this.dateAcquisition</code>
     */
    public Timestamp getDateAcquisition() {
        return this.dateAcquisition;
    }

    /**
     * Setter de la variable d'instance <code>this.dateAcquisition</code>.
     *
     * @param dateAcquisition La valeur à utiliser pour la variable d'instance <code>this.dateAcquisition</code>
     */
    public void setDateAcquisition(Timestamp dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.datePret</code>.
     *
     * @return La variable d'instance <code>this.datePret</code>
     */
    public Timestamp getDatePret() {
        return this.datePret;
    }

    /**
     * Setter de la variable d'instance <code>this.datePret</code>.
     *
     * @param datePret La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
     */
    public void setDatePret(Timestamp datePret) {
        this.datePret = datePret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof LivreDTO;
            if(equals) {
                final LivreDTO livreDTO = (LivreDTO) obj;
                final EqualsBuilder equalsBuilder = new EqualsBuilder();
                equalsBuilder.appendSuper(super.equals(livreDTO));
                equalsBuilder.append(getIdLivre(),
                    livreDTO.getIdLivre());
                equals = equalsBuilder.isEquals();
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(461,
            451);
        hashCodeBuilder.appendSuper(super.hashCode());
        hashCodeBuilder.append(getIdLivre());
        return hashCodeBuilder.toHashCode();
    }

}
