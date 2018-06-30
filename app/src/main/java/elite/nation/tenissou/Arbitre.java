package elite.nation.tenissou;

import org.json.JSONException;
import org.json.JSONObject;

public class Arbitre {




    private long idArbitre;

    private String nom;

    private String prenom;

    private String pays;

    private Integer age;

    private String mailArbitre;

    private String passwordArbitre;


    public Arbitre(long idArbitre,  String nom,  String prenom,  String pays, Integer age,  String mailArbitre,  String passwordArbitre) {

        this.idArbitre = idArbitre;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.mailArbitre = mailArbitre;
        this.passwordArbitre = passwordArbitre;
    }

    public long getIdArbitre() {
        return idArbitre;
    }

    public void setIdArbitre(long idArbitre) {
        this.idArbitre = idArbitre;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMailArbitre() {
        return mailArbitre;
    }

    public void setMailArbitre(String mailArbitre) {
        this.mailArbitre = mailArbitre;
    }

    public String getPasswordArbitre() {
        return passwordArbitre;
    }

    public void setPasswordArbitre(String passwordArbitre) {
        this.passwordArbitre = passwordArbitre;
    }

    public static Arbitre fromJson(JSONObject jsonObject) {


        try {
            long idArbitre = jsonObject.getLong("idArbitre");
            String nom = jsonObject.getString("nom");
            String prenom = jsonObject.getString("prenom");
            String pays = jsonObject.getString("pays");
            int age = jsonObject.getInt("age");
            String mailArbitre = jsonObject.getString("mailArbitre");
            String password = jsonObject.getString("passwordArbitre");

            Arbitre a = new Arbitre(idArbitre, nom, prenom, pays, age, mailArbitre, password);

            return a;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
