package elite.nation.tenissou;

import org.json.JSONException;
import org.json.JSONObject;

public class Joueur {



    private long idJoueur;
    private int idLicence;
    private String nom;
    private String prenom;
    private String pays;
    private int age;
    private int classement;


    public Joueur(long idJoueur, int idLicence, String nom, String prenom, String pays, int age, int classement){

        this.idJoueur = idJoueur;
        this.idLicence = idLicence;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.classement = classement;

    }

    public Joueur( String nom, String prenom, String pays, int age, int classement){

        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.classement = classement;

    }




    public long getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(long idJoueur) {
        this.idJoueur = idJoueur;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getClassement() {
        return classement;
    }

    public void setClassement(int classement) {
        this.classement = classement;
    }


    public static Joueur fromJson(JSONObject jsonObject) {


        try {

             long idJoueur  = jsonObject.getLong("idJoueur");
             int idLicence = jsonObject.getInt("idLicence");
             String nom = jsonObject.getString("nom");
             String prenom = jsonObject.getString("prenom");
             String pays = jsonObject.getString("pays");
             int age  = jsonObject.getInt("age");
             int classement = jsonObject.getInt("classement");

            Joueur j = new Joueur(idJoueur, idLicence, nom, prenom, pays, age, classement);

            return j;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object

    }





}
