package cm.studio.devbee.communitymarket.commentaires;

public class Commentaires_Model {
    private String contenu,heure,id_user;

    public Commentaires_Model() {
    }

    public Commentaires_Model(String contenu, String heure, String id_user) {
        this.contenu = contenu;
        this.heure = heure;
        this.id_user = id_user;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
