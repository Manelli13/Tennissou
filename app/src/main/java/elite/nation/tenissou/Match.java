package elite.nation.tenissou;

import android.graphics.Bitmap;

/**
 * Created by nicolas on 21/02/2018.
 */

public class Match {

    Bitmap mStade;
    Joueur joueur;
    Joueur joueur2;


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

}
