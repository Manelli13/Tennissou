package elite.nation.tenissou;

import android.graphics.Bitmap;

/**
 * Created by nicolas on 21/02/2018.
 */

public class Match {

    Bitmap mStade;



    String mNameMatch;

    public Match(Bitmap stade, String name){

        mStade = stade;
        mNameMatch = name;
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
