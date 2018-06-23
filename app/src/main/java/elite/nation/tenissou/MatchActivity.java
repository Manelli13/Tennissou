package elite.nation.tenissou;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;

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
    TextView topJeuxTxt;
    TextView topJeuxP2Txt;

    TextView bottomNameTxt;
    TextView bottomP2NameTxt;

    ImageView service;
    ImageView serviceP2;
    TextView scoreMainTxt;

    LinearLayout lnP1Score;
    LinearLayout lnP2Score;

    LinearLayout lnP1Tie;
    LinearLayout lnP2Tie;

    TextView tieBreakP1;
    TextView tieBreakP2;

    ImageButton winP1;
    ImageButton winP2;

    ImageButton faultP1;
    ImageButton faultP2;

    Boolean playMatch = true;
    Boolean runMatch = false;

    Boolean isTieB = false;
    Boolean isServiceP1 = true;
    int maxSet = 0;
    int count = -1;

    final  int playerOne = 1;
    final  int playerTwo = 2;

    Match match;

    Boolean isFirst = false;

    LovelyChoiceDialog choiceDialog;
    long elapsedMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        if(isTest) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("tennissou", 0);
            String val = "";

            //TODO recup max set

            maxSet=3;

            if (pref.contains("match")) {
                Gson gson = new Gson();
                String json = pref.getString("match", "");

                Log.i("Match","json : "+ json);


                match = gson.fromJson(json, Match.class);
                val = match.mNameMatch;
            } else
                val = "";


            setTitle("Match : " + val);
        }else{

            SharedPreferences pref = getApplicationContext().getSharedPreferences("tennissou", 0);
            String val = "";

            //TODO recup max set

            maxSet=3;

            if (pref.contains("match")) {
                Gson gson = new Gson();
                String json = pref.getString("match", "");

                Log.i("Match","json : "+ json);


                match = gson.fromJson(json, Match.class);


                val = match.ejoueur1.get(0).getNom() +" VS "+ match.ejoueur2.get(0).getNom() ;


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

        topJeuxTxt = (TextView) findViewById(R.id.match_jeuxscore_txt);
        topJeuxP2Txt = (TextView) findViewById(R.id.match_jeuxscore2_txt);

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

        lnP1Score = (LinearLayout) findViewById(R.id.match_lnlScore_ly);
        lnP2Score = (LinearLayout) findViewById(R.id.match_lnlScore_ly);

        lnP1Tie = (LinearLayout) findViewById(R.id.match_lnlTie_ly);
        lnP2Tie = (LinearLayout) findViewById(R.id.match_lnlTieP2_ly);

        tieBreakP1 = (TextView) findViewById(R.id.match_tieBreak_txt);
        tieBreakP2 = (TextView) findViewById(R.id.match_tieBreakP2_txt);

        lnP1Tie.setVisibility(View.INVISIBLE);
        lnP2Tie.setVisibility(View.INVISIBLE);

        if(isTest){

            topNameTxt.setText(match.joueur.getNom());
            topNameP2Txt.setText(match.joueur2.getNom());

            bottomNameTxt.setText(match.joueur.getNom());
            bottomP2NameTxt.setText(match.joueur2.getNom());
        }
        else{

            topNameTxt.setText(match.ejoueur1.get(0).getNom());
            topNameP2Txt.setText(match.ejoueur2.get(0).getNom());

            bottomNameTxt.setText(match.ejoueur1.get(0).getNom());
            bottomP2NameTxt.setText(match.ejoueur2.get(0).getNom());
        }

        serviceP2.setVisibility(View.INVISIBLE);

        winP1.setClickable(false);
        winP2.setClickable(false);

        faultP1.setClickable(false);
        faultP2.setClickable(false);

    }

    public void OnClickPlay(View v){

        if(runMatch == false){
            mainChrono.start();
            mainChrono.setBase(SystemClock.elapsedRealtime());
            mainChrono.start();
            playButton.setImageResource(R.drawable.ic_shortcut_pause);
            winP1.setClickable(true);
            winP2.setClickable(true);

            faultP1.setClickable(true);
            faultP2.setClickable(true);
        }

        if(playMatch && runMatch){
            playMatch = false;
            playButton.setImageResource(R.drawable.ic_shortcut_play_arrow);
            pauseChrono.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
            pauseChrono.start();

            winP1.setClickable(false);
            winP2.setClickable(false);

            faultP1.setClickable(false);
            faultP2.setClickable(false);

        }else if (!playMatch && runMatch){
            playMatch = true;
            pauseChrono.stop();

            elapsedMillis = SystemClock.elapsedRealtime() - pauseChrono.getBase();

            playButton.setImageResource(R.drawable.ic_shortcut_pause);

            winP1.setClickable(true);
            winP2.setClickable(true);

            faultP1.setClickable(true);
            faultP2.setClickable(true);

        }

        runMatch = true;


    }

    public void OnClickWinP1(View v){

    scoreCount(playerOne);

    }

    public void OnClickWinP2(View v){

        scoreCount(playerTwo);
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

    public void scoreCount(int player){

        String setValuue;
        String scoreValue;
        String jeuxValue;

        String setP2Value;
        String scoreP2Value;
        String jeuxP2Value;

        String scoreTieP1;
        String scoreTieP2;


        scoreTieP1 = tieBreakP1.getText().toString();
        scoreTieP2 = tieBreakP2.getText().toString();


        setValuue = topSetScoreTxt.getText().toString();


        scoreValue = topScoreTxt.getText().toString();

        if(scoreValue == "A")
            scoreValue = ""+41;

        jeuxValue = topJeuxTxt.getText().toString();

        setP2Value = topSetScoreP2Txt.getText().toString();
        scoreP2Value = topScoreP2Txt.getText().toString();

        if(scoreP2Value == "A")
            scoreP2Value = ""+41;


        jeuxP2Value = topJeuxP2Txt.getText().toString();


        int set = Integer.parseInt(setValuue);
        int score = Integer.parseInt(scoreValue);
        int jeux = Integer.parseInt(jeuxValue);

        int setP2 = Integer.parseInt(setP2Value);
        int scoreP2 = Integer.parseInt(scoreP2Value);
        int jeuxP2 = Integer.parseInt(jeuxP2Value);

        int tieP1 = Integer.parseInt(scoreTieP1);
        int tieP2 = Integer.parseInt(scoreTieP2);

        //TODO : gerer la premiere iteration da tie auto increm && avantage


        //score : 0-0 0-15 0-30 0-40  -> win -- 40-40 -> avantage : 40+ - 40 -> A - 40 -> A - 40+ -> 40 - 40 -> a+ - 40 -> win play1
        //jeux : max 6 -> win set -- 6-6 -> tie break = nouveau jeux : avoir deux points d'écart ex -> 5-5 ; 7 - 5 -> win ; 10 - 12 -> win p2
        //Set : 3 set = gagnant homme ; 2 set = gagnant femme dernier


        if (player == playerOne) {
            if (score < 30)
                score += 15;

            else if (score == 30)
                score += 10;
//Tie break
            else if (isTieB || (
                    jeux == 5
                    && jeuxP2 == 6
                    && ((score == 40 && scoreP2 < 40) || score == 41)
                    && !(set == maxSet-1 && setP2 == maxSet-1 )
                    )
                    )

                    /*(((score == 40 && scoreP2 < 40) || score == 41 )
                    && jeux == 5
                    && jeuxP2 == 6
                    && ((set != maxSet-1 && setP2 < maxSet-1 ) || (setP2 != maxSet-1 && set < maxSet-1) ) )
                    ) */
            {

                isTieB = true;

                //eviter lincrement des scores
                score = 40;
                scoreP2 = 40;

                lnP1Tie.setVisibility(View.VISIBLE);
                lnP1Score.setVisibility(View.INVISIBLE);

                lnP2Tie.setVisibility(View.VISIBLE);
                lnP2Score.setVisibility(View.INVISIBLE);

               if((tieP1 - tieP2)  >= 1 && tieP1 > 5  && isFirst){

                   score = 0;
                   scoreP2 = 0;
                   jeux = 0;
                   jeuxP2 = 0;
                   tieP1 = 0;
                   tieP2 = 0;
                   count = -1;
                   set++;
                   isFirst = false;
                   isTieB = false;
                   lnP1Tie.setVisibility(View.INVISIBLE);
                   lnP1Score.setVisibility(View.VISIBLE);

                   lnP2Tie.setVisibility(View.INVISIBLE);
                   lnP2Score.setVisibility(View.VISIBLE);
               }

                else if (isFirst){
                   count++;
                   tieP1++;

                   if(count % 2 == 0){

                       if(serviceP2.getVisibility() == View.VISIBLE) {
                           serviceP2.setVisibility(View.INVISIBLE);
                           service.setVisibility(View.VISIBLE);
                           isServiceP1 = true;
                       }
                       else{

                           serviceP2.setVisibility(View.VISIBLE);
                           service.setVisibility(View.INVISIBLE);
                           isServiceP1 = false;
                       }
                   }
                }else{
                   isFirst = true;
                   isServiceP1 = true;
                   serviceP2.setVisibility(View.INVISIBLE);
                   service.setVisibility(View.VISIBLE);

               }

            }
            // cas gagnant set p1
            else if (((score == 40 && scoreP2 < 40) || score == 41) && jeux >= 5 && (jeux - jeuxP2) >= 1 ) {
                score = 0;
                scoreP2 = 0;
                jeux = 0;
                jeuxP2 = 0;
                set++;

               if(isServiceP1 == true){
                   isServiceP1 = false;
                   service.setVisibility(View.INVISIBLE);
                   serviceP2.setVisibility(View.VISIBLE);
               }
               else{

                   isServiceP1 = true;
                   service.setVisibility(View.VISIBLE);
                   serviceP2.setVisibility(View.INVISIBLE);
               }

            }else if (score == 40 && scoreP2 < 40 /*&& jeux < 6 */ ) {
                score = 0;
                scoreP2 = 0;

                if(isServiceP1 == true){
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                }
                else{

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

                jeux++;
            }else if(score == 40 && scoreP2 == 40){
                score = 41;
            }else if(score == 40 && scoreP2 == 41){
                scoreP2 = 40;
            }
            else if(score == 41 && scoreP2 == 40){
                score = 0;
                scoreP2 = 0;

                if(isServiceP1 == true){
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                }
                else{

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

                jeux++;
            }

// Cas gagnant p2
        } else if (player == playerTwo) {

            if (scoreP2 < 30)
                scoreP2 += 15;

            else if (scoreP2 == 30)
                scoreP2 += 10;


//Tie break

            else if (isTieB || (
                    jeuxP2 == 5
                            && jeux == 6
                            && ((scoreP2 == 40 && score < 40) || scoreP2 == 41)
                            && !(set == maxSet - 1 && setP2 == maxSet - 1)
            )

                    ) {
                isTieB = true;

//eviter lincrement des scores
                score = 40;
                scoreP2 = 40;
                lnP1Tie.setVisibility(View.VISIBLE);
                lnP1Score.setVisibility(View.INVISIBLE);

                lnP2Tie.setVisibility(View.VISIBLE);
                lnP2Score.setVisibility(View.INVISIBLE);

                if ((tieP2 - tieP1) >= 1 && tieP2 > 5 && isFirst) {

                    score = 0;
                    scoreP2 = 0;
                    jeux = 0;
                    jeuxP2 = 0;
                    tieP1 = 0;
                    tieP2 = 0;
                    count= -1;
                    setP2++;
                    isFirst = false;
                    isTieB = false;

                    if(isServiceP1 == true){
                        isServiceP1 = false;
                        service.setVisibility(View.INVISIBLE);
                        serviceP2.setVisibility(View.VISIBLE);
                    }
                    else{

                        isServiceP1 = true;
                        service.setVisibility(View.VISIBLE);
                        serviceP2.setVisibility(View.INVISIBLE);
                    }

                    lnP1Tie.setVisibility(View.INVISIBLE);
                    lnP1Score.setVisibility(View.VISIBLE);

                    lnP2Tie.setVisibility(View.INVISIBLE);
                    lnP2Score.setVisibility(View.VISIBLE);
                }

                else if (isFirst){

                    tieP2++;
                    count++;
                    if(count % 2 == 0){

                        if(serviceP2.getVisibility() == View.VISIBLE) {
                            serviceP2.setVisibility(View.INVISIBLE);
                            service.setVisibility(View.VISIBLE);
                            isServiceP1=true;
                        }
                        else{

                            serviceP2.setVisibility(View.VISIBLE);
                            service.setVisibility(View.INVISIBLE);
                            isServiceP1=false;
                        }
                    }

                }else{
                    isFirst = true;

                    isServiceP1 = false;
                    serviceP2.setVisibility(View.VISIBLE);
                    service.setVisibility(View.INVISIBLE);
                }

            }
            //gagnant set
            else if (((scoreP2 == 40 && score < 40) || scoreP2 == 41) &&  jeuxP2 >= 5 && (jeuxP2 - jeux) >= 1) {
                score = 0;
                scoreP2 = 0;
                jeuxP2 = 0;
                jeux = 0;
                setP2++;

                if(isServiceP1 == true){
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                }
                else{

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

            } else if (scoreP2 == 40 && score < 40 /*&& jeuxP2 < 6*/) {
                score = 0;
                scoreP2 = 0;
                jeuxP2++;

                if(isServiceP1 == true){
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                }
                else{

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

            }else if(score == 40 && scoreP2 == 40){
                scoreP2 = 41;
            }else if(scoreP2 == 40 && score == 41){
                score = 40;
            }
            else if(score == 40 && scoreP2 == 41){
                score = 0;
                scoreP2 = 0;

                if(isServiceP1 == true){
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                }
                else{

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }
                jeuxP2++;
            }
        }
//fin du match a config
        if(set == 3){

            // Read your drawable from somewhere
            Drawable dr = getResources().getDrawable(R.drawable.win);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.orange)
                    .setButtonsColorRes(R.color.orangel)
                    .setIcon(d)
                    .setTitle("Match gagné !" + '\n')
                    .setMessage("Gagnant : " + topNameTxt.getText().toString() )
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })

                    .show();

        }
        else if (setP2 == maxSet)
        {
            Drawable dr = getResources().getDrawable(R.drawable.win);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.orange)
                    .setButtonsColorRes(R.color.orangel)
                    .setIcon(d)
                    .setTitle("Match gagné !" + '\n')
                    .setMessage("Gagnant : " + topNameP2Txt.getText().toString() )
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })

                    .show();

        }


        if (score == 41) {
            topScoreTxt.setText("A");
            scoreMainTxt.setText("A" +" : "+ 40);
        }
        else {
            topScoreTxt.setText("" + score);
            scoreMainTxt.setText(score +" : "+ scoreP2);
        }

        if (scoreP2 == 41) {
            topScoreP2Txt.setText("A");
            scoreMainTxt.setText(40 +" : "+ "A");
        }
        else if(score < 41  ) {
            topScoreP2Txt.setText("" + scoreP2);
            scoreMainTxt.setText(score +" : "+ scoreP2);
        }


        tieBreakP1.setText(""+tieP1);
        tieBreakP2.setText(""+tieP2);

        topJeuxTxt.setText(""+jeux);
        topJeuxP2Txt.setText(""+jeuxP2);

        topSetScoreTxt.setText(""+set);
        topSetScoreP2Txt.setText(""+setP2);



    }

}
