package elite.nation.tenissou;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ServerCallBack {

        void onSuccess(long matchId, ArrayList<Joueur> result);

}
