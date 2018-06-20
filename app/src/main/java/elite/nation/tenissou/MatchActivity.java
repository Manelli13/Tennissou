package elite.nation.tenissou;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.List;


import static elite.nation.tenissou.ConfigAppParameters.isTest;

public class MatchActivity extends AppCompatActivity {

    ListView listView;
    Chronometer mainChrono;
    ImageButton playButton;
    Chronometer pauseChrono;
    TextView topNameTxt;
    TextView topSetScoreTxt;
    TextView topScoreTxt;
    TextView topNameP2Txt;
    TextView topSetScoreP2Txt;
    TextView topScoreP2Txt;

    TextView bottomNameTxt;
    TextView bottomP2NameTxt;

    ImageView service;
    ImageView serviceP2;
    TextView scoreMainTxt;

    ImageButton winP1;
    ImageButton winP2;

    ImageButton faultP1;
    ImageButton faultP2;

    Boolean playMatch = true;
    Boolean runMatch = false;

    Match match;

    LovelyChoiceDialog choiceDialog;
    long elapsedMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        if(isTest) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("tennissou", 0);
            String val = "";

            if (pref.contains("match")) {
                Gson gson = new Gson();
                String json = pref.getString("match", "");

                Log.i("Match","json : "+ json);


                match = gson.fromJson(json, Match.class);
                val = match.mNameMatch;
            } else
                val = "";


            setTitle("Match : " + val);
        }

        init();

    }

    public void  init(){


        mainChrono = (Chronometer) findViewById(R.id.match_chronometer);
        pauseChrono = (Chronometer) findViewById(R.id.match_chronometer3);

        topNameTxt = (TextView) findViewById(R.id.match_topname_txt);
        topSetScoreTxt = (TextView) findViewById(R.id.match_setscore_txt);
        topScoreTxt = (TextView) findViewById(R.id.match_score_txt);

        topNameP2Txt = (TextView) findViewById(R.id.match_topname2_txt);
        topSetScoreP2Txt = (TextView) findViewById(R.id.match_setscore2_txt);
        topScoreP2Txt = (TextView) findViewById(R.id.match_score2_txt);

        bottomNameTxt = (TextView) findViewById(R.id.match_bottomname_txt);
        bottomP2NameTxt = (TextView) findViewById(R.id.match_bottomname2_txt);
        scoreMainTxt = (TextView) findViewById(R.id.match_scoremain_txt);

        playButton = (ImageButton) findViewById(R.id.match_play_imb);
        winP1 = (ImageButton) findViewById(R.id.match_winplayer1_imb);
        winP2 = (ImageButton) findViewById(R.id.match_winplayer2_imb);

        faultP1 = (ImageButton) findViewById(R.id.match_fault_imv);
        faultP2 = (ImageButton) findViewById(R.id.match_fault2_imv);

        service = (ImageView) findViewById(R.id.match_serviceleft_imv);
        serviceP2 = (ImageView) findViewById(R.id.match_serviceright_imv);

        if(isTest){

            topNameTxt.setText(match.joueur.getNom());
            topNameP2Txt.setText(match.joueur2.getNom());

            bottomNameTxt.setText(match.joueur.getNom());
            bottomP2NameTxt.setText(match.joueur2.getNom());
        }

    }

    public void OnClickPlay(View v){

        if(runMatch == false){
            mainChrono.start();
            mainChrono.setBase(SystemClock.elapsedRealtime());
            mainChrono.start();
            playButton.setImageResource(R.drawable.ic_shortcut_pause);

        }

        if(playMatch && runMatch){
            playMatch = false;
            playButton.setImageResource(R.drawable.ic_shortcut_play_arrow);
            pauseChrono.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
            pauseChrono.start();

        }else if (!playMatch && runMatch){
            playMatch = true;
            pauseChrono.stop();

            elapsedMillis = SystemClock.elapsedRealtime() - pauseChrono.getBase();

            playButton.setImageResource(R.drawable.ic_shortcut_pause);

        }

        runMatch = true;


    }

    public void OnClickWinP1(View v){


    }

    public void OnClickWinP2(View v){


    }

    public void OnClickFaultP1(View v){

        String str = (String) topNameTxt.getText();
        popup(str);

    }

    public void OnClickFaultP2(View v){

        String str = (String) topNameP2Txt.getText();
        popup(str);
    }

    public void popup(String str) {

        ArrayList<String> aList = new ArrayList<String>();
        aList.add("Directe");
        aList.add("Filet");
        aList.add("Double fautes");
        aList.add("Faute de pied");

        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.orangel)
                .setTitle("Faute du joueur : " + str + '\n')
                .setIcon(R.drawable.ic_shortcut_clear)
                .setMessage("Choisissez la faute commise : ")
                .setItems(aList, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int positions, String t) {
                        Toast.makeText(getApplicationContext(), "positive clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}
