package elite.nation.tenissou;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicolas on 21/02/2018.
 */

public class Match {

    Bitmap mStade;
    Joueur joueur;
    Joueur joueur2;

    long idMatch;
    int idTournoi;
    String lieuMatch;


    String date;
    String etatMatch;
    String terrainMatch;
    long idScore;



    Match Match;
    ArrayList<Joueur> ejoueur1;
    ArrayList<Joueur> ejoueur2;


    String mNameMatch;


    public Match(Bitmap stade, String name){

        mStade = stade;
        mNameMatch = name;
    }

    public Match(Bitmap stade, String name, Joueur joueur, Joueur joueur2 ){

        mStade = stade;
        mNameMatch = name;
        this.joueur = joueur;
        this.joueur2 = joueur2;
    }


    public Match(long idMatch, String lieuMatch, int idTournoi, String date, String etatMatch, String terrainMatch){

        this.idMatch = idMatch;
        this.idTournoi = idTournoi;
        this.lieuMatch = lieuMatch;
        this.date = date;
        this.etatMatch = etatMatch;
        this.terrainMatch = terrainMatch;
        this.idScore = idScore;

    }


    public Match(Match match, ArrayList<Joueur> joueur1, ArrayList<Joueur> joueur2 ){

        this.Match = match;
        this.ejoueur1 = joueur1;
        this.ejoueur2 = joueur2;
    }



    public long getIdMatch() {
        return idMatch;
    }


    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public void setJoueur2(Joueur joueur2) {
        this.joueur2 = joueur2;
    }

    public elite.nation.tenissou.Match getMatch() {
        return Match;
    }

    public void setMatch(elite.nation.tenissou.Match match) {
        Match = match;
    }
    public void setIdMatch(long idMatch) {
        this.idMatch = idMatch;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    public void setIdTournoi(int idTournoi) {
        this.idTournoi = idTournoi;
    }

    public String getLieuMatch() {
        return lieuMatch;
    }

    public void setLieuMatch(String lieuMatch) {
        this.lieuMatch = lieuMatch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtatMatch() {
        return etatMatch;
    }

    public void setEtatMatch(String etatMatch) {
        this.etatMatch = etatMatch;
    }

    public String getTerrainMatch() {
        return terrainMatch;
    }

    public void setTerrainMatch(String terrainMatch) {
        this.terrainMatch = terrainMatch;
    }

    public Bitmap getmStade() {
        return mStade;
    }

    public void setmStade(Bitmap mStade) {
        this.mStade = mStade;
    }

    public String getmNameMatch() {
        return mNameMatch;
    }

    public void setmNameMatch(String mNameMatch) {
        this.mNameMatch = mNameMatch;
    }


   public static Match fromJson(JSONObject jsonObject) {


       try {
           long idMatch = jsonObject.getInt("idMatch");
           String lieuMatch = jsonObject.getString("lieuMatch");
           int idTournoi = jsonObject.getInt("idTournoi");
           String date = jsonObject.getString("dateDebut");
           String etatMatch = jsonObject.getString("etatMatch");
           String terrainMatch = jsonObject.getString("terrainMatch");

           Match m = new Match(idMatch, lieuMatch, idTournoi, date, etatMatch, terrainMatch);

           return m;

       } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object

    }
}
