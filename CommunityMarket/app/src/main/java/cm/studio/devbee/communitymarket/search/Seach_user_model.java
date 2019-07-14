package cm.studio.devbee.communitymarket.search;

public class Seach_user_model {
    String user_name;
    String user_prenom;
    String user_telephone;
    String user_residence;
    String user_mail;
    String user_profil_image;
    String id_utilisateur;
    String status;
    String search;
    String message;
    String derniere_conection;

    public Seach_user_model() {
    }

    public Seach_user_model(String user_name, String user_prenom, String user_telephone, String user_residence, String user_mail, String user_profil_image, String id_utilisateur, String status, String search, String message, String derniere_conection) {
        this.user_name = user_name;
        this.user_prenom = user_prenom;
        this.user_telephone = user_telephone;
        this.user_residence = user_residence;
        this.user_mail = user_mail;
        this.user_profil_image = user_profil_image;
        this.id_utilisateur = id_utilisateur;
        this.status = status;
        this.search = search;
        this.message = message;
        this.derniere_conection = derniere_conection;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_prenom() {
        return user_prenom;
    }

    public void setUser_prenom(String user_prenom) {
        this.user_prenom = user_prenom;
    }

    public String getUser_telephone() {
        return user_telephone;
    }

    public void setUser_telephone(String user_telephone) {
        this.user_telephone = user_telephone;
    }

    public String getUser_residence() {
        return user_residence;
    }

    public void setUser_residence(String user_residence) {
        this.user_residence = user_residence;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_profil_image() {
        return user_profil_image;
    }

    public void setUser_profil_image(String user_profil_image) {
        this.user_profil_image = user_profil_image;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDerniere_conection() {
        return derniere_conection;
    }

    public void setDerniere_conection(String derniere_conection) {
        this.derniere_conection = derniere_conection;
    }
}
