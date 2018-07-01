package elite.nation.tenissou;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static elite.nation.tenissou.ConfigAppParameters.ENCOURS;
import static elite.nation.tenissou.ConfigAppParameters.PAUSE;
import static elite.nation.tenissou.ConfigAppParameters.SOIN;
import static elite.nation.tenissou.ConfigAppParameters.TERMINE;
import static elite.nation.tenissou.ConfigAppParameters.isTest;
import static java.lang.Math.round;

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
    LinearLayout ln;

    TextView toolbarTxt;
    ImageView toolbarImb;

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

    int countService = 1;

    int serviceMiss1 = 0;
    int serviceMiss2 = 0;

    final int playerOne = 1;
    final int playerTwo = 2;

    Match match;

    Boolean isFirst = false;

    LovelyChoiceDialog choiceDialog;
    long elapsedMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        if (isTest) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("tennissou", 0);
            String val = "";

            //TODO recup max set

            maxSet = 3;

            if (pref.contains("match")) {
                Gson gson = new Gson();
                String json = pref.getString("match", "");

                Log.i("Match", "json : " + json);


                match = gson.fromJson(json, Match.class);
                val = match.mNameMatch;
            } else
                val = "";


            setTitle("Match : " + val);
        } else {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("tennissou", 0);
            String val = "";

            //TODO recup max set


            if (pref.contains("match")) {
                Gson gson = new Gson();
                String json = pref.getString("match", "");

                Log.i("Match", "json : " + json);

                match = gson.fromJson(json, Match.class);

                val = match.ejoueur1.get(0).getNom() + " VS " + match.ejoueur2.get(0).getNom();

                addWebJeux((int) match.getMatch().idMatch, 1, 1, 0, false);

                getSet();

            } else
                val = "";

            toolbarTxt = (TextView) findViewById(R.id.toolbar_title);
            toolbarImb = (ImageView) findViewById(R.id.toolbar_button);
            toolbarTxt.setText("Match : " + val);
        }

        init();

        if (match.getMatch().getTerrainMatch().equals("Terre battue"))
            ln.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.terre2));
        if (match.getMatch().getTerrainMatch().equals("Gazon"))
            ln.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stade));
        if (match.getMatch().getTerrainMatch().equals("Dure"))
            ln.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.surfacedur));
    }

    public void init() {


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
        ln = (LinearLayout) findViewById(R.id.match_ln);


        lnP1Tie.setVisibility(View.INVISIBLE);
        lnP2Tie.setVisibility(View.INVISIBLE);

        if (isTest) {

            topNameTxt.setText(match.joueur.getNom());
            topNameP2Txt.setText(match.joueur2.getNom());

            bottomNameTxt.setText(match.joueur.getNom());
            bottomP2NameTxt.setText(match.joueur2.getNom());
        } else {

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

    public void OnClickPlay(View v) {

        if (runMatch == false) {

            changeEtatMatch(ENCOURS);
            mainChrono.start();
            mainChrono.setBase(SystemClock.elapsedRealtime());
            mainChrono.start();
            playButton.setImageResource(R.drawable.ic_shortcut_pause);
            winP1.setClickable(true);
            winP2.setClickable(true);

            faultP1.setClickable(true);
            faultP2.setClickable(true);
        }

        if (playMatch && runMatch) {
            playMatch = false;
            changeEtatMatch(PAUSE);
            playButton.setImageResource(R.drawable.ic_shortcut_play_arrow);
            pauseChrono.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
            pauseChrono.start();

            winP1.setClickable(false);
            winP2.setClickable(false);

            faultP1.setClickable(false);
            faultP2.setClickable(false);

        } else if (!playMatch && runMatch) {
            playMatch = true;
            pauseChrono.stop();
            changeEtatMatch(ENCOURS);
            elapsedMillis = SystemClock.elapsedRealtime() - pauseChrono.getBase();

            playButton.setImageResource(R.drawable.ic_shortcut_pause);

            winP1.setClickable(true);
            winP2.setClickable(true);

            faultP1.setClickable(true);
            faultP2.setClickable(true);

        }

        runMatch = true;


    }

    public void OnClickHelp(View v) {

        if (playMatch && runMatch) {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.orange)
                    .setButtonsColorRes(R.color.orangel)
                    .setIcon(R.drawable.ic_action_check)
                    .setTitle("Demande d'intervention soigneur" + '\n')
                    .setMessage("Voulez vous appeler un soigneur et mettre le match en pause ?")
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            changeEtatMatch(SOIN);

                            playMatch = false;
                            playButton.setImageResource(R.drawable.ic_shortcut_play_arrow);
                            pauseChrono.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                            pauseChrono.start();

                            winP1.setClickable(false);
                            winP2.setClickable(false);

                            faultP1.setClickable(false);
                            faultP2.setClickable(false);

                        }


                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }

    }

    // set addition les 2 + 1
    public void OnClickWinP1(View v) {

        scoreCount(playerOne);

        countService++;


    }

    public void OnClickWinP2(View v) {

        scoreCount(playerTwo);

        countService++;
    }

    public void OnClickFaultP1(View v) {

        String str = (String) topNameTxt.getText();
        popup(str, 1);

    }

    public void OnClickFaultP2(View v) {

        String str = (String) topNameP2Txt.getText();
        popup(str, 2);
    }

    public void popup(String str, final int i) {


        ArrayList<String> aList = new ArrayList<String>();
        aList.add("Directe");
        aList.add("Service raté");
        aList.add("Filet");
        aList.add("Double fautes");
        aList.add("Faute de pied");

        int idJoueur;
        if (i == 1)
            idJoueur = (int) match.ejoueur1.get(0).getIdJoueur();
        else
            idJoueur = (int) match.ejoueur2.get(0).getIdJoueur();

        final int finalIdJoueur = idJoueur;

        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.orangel)
                .setTitle("Faute du joueur : " + str + '\n')
                .setIcon(R.drawable.ic_shortcut_clear)
                .setMessage("Choisissez la faute commise : ")
                .setItems(aList, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int positions, String t) {

                        if (positions == 0)
                            addStat(finalIdJoueur, "fauteDirecte");

                        if (positions == 1) {
                            if(i ==1) {
                                serviceMiss1++;
                                int is = ( (1-(serviceMiss1 / (countService / 2 ))) *1000);
                                String service = is+"";
                                addStat(finalIdJoueur, service);
                            }
                            else{
                                serviceMiss2++;
                                int is = ( (1-(serviceMiss2 / (countService / 2 ))) *1000);
                                String service = is +"";
                                addStat(finalIdJoueur, service);
                            }

                        }

                        if (positions == 2)
                            addStat(finalIdJoueur, "fillet");

                        if (positions == 3)
                            addStat(finalIdJoueur, "dfaute");

                        if (positions == 4)
                            addStat(finalIdJoueur, "pied");

                    }
                })
                .show();
    }


    public void scoreCount(int player) {

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


        if (scoreValue == "A")
            scoreValue = "" + 41;

        jeuxValue = topJeuxTxt.getText().toString();

        setP2Value = topSetScoreP2Txt.getText().toString();
        scoreP2Value = topScoreP2Txt.getText().toString();

        if (scoreP2Value == "A")
            scoreP2Value = "" + 41;


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
            if (score < 30) {
                score += 15;

                //+1 -> set 1 = 0 +1
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerOne, "" + score);
            } else if (score == 30) {
                score += 10;
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerOne, "" + score);
            }
//Tie break
            else if (isTieB || (
                    jeux == 5
                            && jeuxP2 == 6
                            && ((score == 40 && scoreP2 < 40) || score == 41)
                            && !(set == maxSet - 1 && setP2 == maxSet - 1)
            )
                    )

            {

                isTieB = true;

                //eviter lincrement des scores
                score = 40;
                scoreP2 = 40;

                lnP1Tie.setVisibility(View.VISIBLE);
                lnP1Score.setVisibility(View.INVISIBLE);

                lnP2Tie.setVisibility(View.VISIBLE);
                lnP2Score.setVisibility(View.INVISIBLE);

                if ((tieP1 - tieP2) >= 1 && tieP1 > 5 && isFirst) {

                    score = 0;
                    scoreP2 = 0;
                    tieP1++;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, playerOne, tieP1, true);
                    jeux = 0;
                    jeuxP2 = 0;
                    tieP1 = 0;
                    tieP2 = 0;
                    count = -1;
                    set++;

                    updateWebSet(match.getMatch().idMatch, playerOne);
                    addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, 1, playerOne, false);

                    isFirst = false;
                    isTieB = false;
                    lnP1Tie.setVisibility(View.INVISIBLE);
                    lnP1Score.setVisibility(View.VISIBLE);

                    lnP2Tie.setVisibility(View.INVISIBLE);
                    lnP2Score.setVisibility(View.VISIBLE);
                } else if (isFirst) {
                    count++;
                    tieP1++;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, playerOne, tieP1, true);


                    if (count % 2 == 0) {

                        if (serviceP2.getVisibility() == View.VISIBLE) {
                            serviceP2.setVisibility(View.INVISIBLE);
                            service.setVisibility(View.VISIBLE);
                            isServiceP1 = true;
                        } else {

                            serviceP2.setVisibility(View.VISIBLE);
                            service.setVisibility(View.INVISIBLE);
                            isServiceP1 = false;
                        }
                    }
                } else {
                    isFirst = true;
                    isServiceP1 = true;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, 0, 0, false);
                    serviceP2.setVisibility(View.INVISIBLE);
                    service.setVisibility(View.VISIBLE);

                }

            }
            // cas gagnant set p1
            else if (((score == 40 && scoreP2 < 40) || score == 41) && jeux >= 5 && (jeux - jeuxP2) >= 1) {
                score = 0;
                scoreP2 = 0;
                jeux = 0;
                jeuxP2 = 0;
                set++;
                updateWebSet(match.getMatch().idMatch, playerOne);
                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, 1, playerOne, false);

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

            } else if (score == 40 && scoreP2 < 40 /*&& jeux < 6 */) {
                score = 0;
                scoreP2 = 0;

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

                jeux++;

                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, jeux + jeuxP2 + 1, playerOne, true);


            } else if (score == 40 && scoreP2 == 40) {
                score = 41;
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerOne, "" + score);
            } else if (score == 40 && scoreP2 == 41) {
                scoreP2 = 40;
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerTwo, "" + scoreP2);
            } else if (score == 41 && scoreP2 == 40) {
                score = 0;
                scoreP2 = 0;

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

                jeux++;
                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, jeux + jeuxP2 + 1, playerOne, true);
            }

// Cas gagnant p2
        } else if (player == playerTwo) {

            if (scoreP2 < 30) {
                scoreP2 += 15;
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerTwo, "" + scoreP2);
            } else if (scoreP2 == 30) {
                scoreP2 += 10;
                addWebScore(set + setP2 + 1, jeux + jeuxP2 + 1, playerTwo, "" + scoreP2);
            }


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

                    tieP2++;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, playerTwo, tieP2, true);
                    score = 0;
                    scoreP2 = 0;
                    jeux = 0;
                    jeuxP2 = 0;
                    tieP1 = 0;
                    tieP2 = 0;
                    count = -1;
                    setP2++;
                    updateWebSet(match.getMatch().idMatch, playerTwo);
                    addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, 1, playerTwo, false);
                    isFirst = false;
                    isTieB = false;

                    if (isServiceP1 == true) {
                        isServiceP1 = false;
                        service.setVisibility(View.INVISIBLE);
                        serviceP2.setVisibility(View.VISIBLE);
                    } else {

                        isServiceP1 = true;
                        service.setVisibility(View.VISIBLE);
                        serviceP2.setVisibility(View.INVISIBLE);
                    }

                    lnP1Tie.setVisibility(View.INVISIBLE);
                    lnP1Score.setVisibility(View.VISIBLE);

                    lnP2Tie.setVisibility(View.INVISIBLE);
                    lnP2Score.setVisibility(View.VISIBLE);
                } else if (isFirst) {

                    tieP2++;
                    count++;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, playerTwo, tieP2, true);
                    if (count % 2 == 0) {

                        if (serviceP2.getVisibility() == View.VISIBLE) {
                            serviceP2.setVisibility(View.INVISIBLE);
                            service.setVisibility(View.VISIBLE);
                            isServiceP1 = true;
                        } else {

                            serviceP2.setVisibility(View.VISIBLE);
                            service.setVisibility(View.INVISIBLE);
                            isServiceP1 = false;
                        }
                    }

                } else {
                    isFirst = true;
                    addWebTie((int) match.getMatch().idMatch, set + setP2 + 1, playerTwo, tieP2, false);
                    isServiceP1 = false;
                    serviceP2.setVisibility(View.VISIBLE);
                    service.setVisibility(View.INVISIBLE);
                }

            }
            //gagnant set
            else if (((scoreP2 == 40 && score < 40) || scoreP2 == 41) && jeuxP2 >= 5 && (jeuxP2 - jeux) >= 1) {
                score = 0;
                scoreP2 = 0;
                jeuxP2 = 0;
                jeux = 0;
                setP2++;
                updateWebSet(match.getMatch().idMatch, playerTwo);
                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, 1, playerTwo, false);

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

            } else if (scoreP2 == 40 && score < 40 /*&& jeuxP2 < 6*/) {
                score = 0;
                scoreP2 = 0;
                jeuxP2++;
                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, jeux + jeuxP2 + 1, playerTwo, true);

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }

            } else if (score == 40 && scoreP2 == 40) {
                scoreP2 = 41;
                addWebScore(set + setP2 + 1, jeux + 1, playerTwo, "" + scoreP2);
            } else if (scoreP2 == 40 && score == 41) {
                score = 40;
                addWebScore(set + setP2 + 1, jeux + 1, playerOne, "" + score);
            } else if (score == 40 && scoreP2 == 41) {
                score = 0;
                scoreP2 = 0;

                if (isServiceP1 == true) {
                    isServiceP1 = false;
                    service.setVisibility(View.INVISIBLE);
                    serviceP2.setVisibility(View.VISIBLE);
                } else {

                    isServiceP1 = true;
                    service.setVisibility(View.VISIBLE);
                    serviceP2.setVisibility(View.INVISIBLE);
                }
                jeuxP2++;
                addWebJeux((int) match.getMatch().idMatch, set + setP2 + 1, jeux + jeuxP2 + 1, playerTwo, true);
            }
        }
//fin du match a config
        if (set == 3) {

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
                    .setMessage("Gagnant : " + topNameTxt.getText().toString())
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeEtatMatch(TERMINE);
                            finish();
                        }
                    })

                    .show();

        } else if (setP2 == maxSet) {
            Drawable dr = getResources().getDrawable(R.drawable.win);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.orange)
                    .setButtonsColorRes(R.color.orangel)
                    .setIcon(d)
                    .setTitle("Match gagné !" + '\n')
                    .setMessage("Gagnant : " + topNameP2Txt.getText().toString())
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeEtatMatch(TERMINE);
                            finish();
                        }
                    })

                    .show();

        }


        if (score == 41) {
            topScoreTxt.setText("A");
            scoreMainTxt.setText("A" + " : " + 40);
        } else {
            topScoreTxt.setText("" + score);
            scoreMainTxt.setText(score + " : " + scoreP2);
        }

        if (scoreP2 == 41) {
            topScoreP2Txt.setText("A");
            scoreMainTxt.setText(40 + " : " + "A");
        } else if (score < 41) {
            topScoreP2Txt.setText("" + scoreP2);
            scoreMainTxt.setText(score + " : " + scoreP2);
        }


        tieBreakP1.setText("" + tieP1);
        tieBreakP2.setText("" + tieP2);

        topJeuxTxt.setText("" + jeux);
        topJeuxP2Txt.setText("" + jeuxP2);

        topSetScoreTxt.setText("" + set);
        topSetScoreP2Txt.setText("" + setP2);


    }

//TODO WEBSERVICE

    public void updWebTie(int matchid, int set, int joueur, int score) {


        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + matchid + "/" + set + "/joueur" + joueur + "/" + score;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void addWebTie(final int matchid, final int set, final int player, final int score, final boolean isWin) {


        if (isWin)
            updWebTie(matchid, set, player, score);
        else {
            final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = ConfigAppParameters.URL + "/match/" + matchid + "/" + set + "/createTiebreak";


            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Anything you want
                    Log.i("requestmatchError", error.getMessage());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }


    public void updateWebSet(long match, int joueur) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + match + "/joueur" + joueur;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void updWebJeux(int matchid, int set, int joueur, int jeux) {


        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + matchid + "/" + set + "/joueur" + joueur;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void addWebJeux(final int matchid, final int set, final int idJeu, final int player, final boolean isWin) {


        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + matchid + "/" + set + "/" + idJeu + "/createJeu";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (isWin)
                            updWebJeux(matchid, set, player, idJeu);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    // etat : En pause; Pause soin; En cours; En attente; Terminé
    public void changeEtatMatch(String etatMatch) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + match.getMatch().idMatch + "/etat/" + etatMatch;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void addWebScore(int set, int idJeu, int joueur, String score) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/match/" + match.getMatch().idMatch + "/" + set + "/" + idJeu + "/joueur" + joueur + "/" + score;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getSet() {

        final int[] set = new int[1];
        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/tournoi/match/" + match.getMatch().idMatch;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        maxSet = Integer.parseInt(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void requestJoueur(final long matchId) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/matchs/" + matchId + "/joueurs";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            // JSONObject jsonObject = new JSONObject(response);
                            // Log.i("requestmatch", jsonObject.toString());

                            JSONArray jsonArray = new JSONArray(response);
                            Log.i("requestmatch", jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);


                                lJoueur.add(Joueur.fromJson(jo));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("requestmatchJsonError", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void addStat(int joueur, final String stat) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/stat/" + match.getMatch().idMatch + "/" + joueur + "/" + stat;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.i("requestmatchError", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

}
