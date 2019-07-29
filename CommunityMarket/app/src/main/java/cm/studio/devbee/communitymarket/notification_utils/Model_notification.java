package cm.studio.devbee.communitymarket.notification_utils;

public class Model_notification {

    String nom_du_produit;
    String decription_du_produit;
    String prix_du_produit;
    String date_du_like;
    String image_du_produit;
    String categories;
    String id_de_utilisateur;
    String id_du_post;
    String post_id;
    String action;
    String commantaire;
    private String collection;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Model_notification() {

    }

    public Model_notification(String nom_du_produit, String decription_du_produit, String prix_du_produit, String date_du_like, String image_du_produit, String categories, String id_de_utilisateur, String id_du_post, String post_id, String action, String commantaire, String collection) {
        this.nom_du_produit = nom_du_produit;
        this.decription_du_produit = decription_du_produit;
        this.prix_du_produit = prix_du_produit;
        this.date_du_like = date_du_like;
        this.image_du_produit = image_du_produit;
        this.categories = categories;
        this.id_de_utilisateur = id_de_utilisateur;
        this.id_du_post = id_du_post;
        this.post_id = post_id;
        this.action = action;
        this.commantaire = commantaire;
        this.collection = collection;
    }

    public String getNom_du_produit() {
        return nom_du_produit;
    }

    public void setNom_du_produit(String nom_du_produit) {
        this.nom_du_produit = nom_du_produit;
    }

    public String getDecription_du_produit() {
        return decription_du_produit;
    }

    public void setDecription_du_produit(String decription_du_produit) {
        this.decription_du_produit = decription_du_produit;
    }

    public String getPrix_du_produit() {
        return prix_du_produit;
    }

    public void setPrix_du_produit(String prix_du_produit) {
        this.prix_du_produit = prix_du_produit;
    }

    public String getDate_du_like() {
        return date_du_like;
    }

    public void setDate_du_like(String date_du_like) {
        this.date_du_like = date_du_like;
    }

    public String getImage_du_produit() {
        return image_du_produit;
    }

    public void setImage_du_produit(String image_du_produit) {
        this.image_du_produit = image_du_produit;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getId_de_utilisateur() {
        return id_de_utilisateur;
    }

    public void setId_de_utilisateur(String id_de_utilisateur) {
        this.id_de_utilisateur = id_de_utilisateur;
    }

    public String getId_du_post() {
        return id_du_post;
    }

    public void setId_du_post(String id_du_post) {
        this.id_du_post = id_du_post;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCommantaire() {
        return commantaire;
    }

    public void setCommantaire(String commantaire) {
        this.commantaire = commantaire;
    }
}

