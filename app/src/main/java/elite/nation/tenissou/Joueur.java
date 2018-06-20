package elite.nation.tenissou;

public class Joueur {

    private String nom;
    private String prenom;
    private String pays;
    private int age;
    private int classement;


    public Joueur(String nom, String prenom, String pays, int age, int classement){

        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.classement = classement;

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






}
