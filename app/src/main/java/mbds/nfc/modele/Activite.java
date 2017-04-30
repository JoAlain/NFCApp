package mbds.nfc.modele;

/**
 * Created by Alain on 26/04/2017.
 */

public class Activite {

    private String id;
    private String libelle;
    private String description;
    private double prix;

    public Activite() {
    }

    public Activite(String id, String libelle, String description, double prix) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
        this.prix = prix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
