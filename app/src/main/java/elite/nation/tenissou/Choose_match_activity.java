package elite.nation.tenissou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifTextView;

import static elite.nation.tenissou.ConfigAppParameters.isTest;

public class Choose_match_activity extends AppCompatActivity implements ServerCallBack {

    ListView listView;
    ArrayList<Match> listMatch;

    GifTextView gif;

    final ArrayList<Match> aMatch = new ArrayList<>();
    MatchAdapter adapMatch;
    Arbitre currentArbitre;
    MatchWebAdapter adapWMatch;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_choose_match);
        listView = (ListView) findViewById(R.id.choose_list_lv);
        gif = (GifTextView) findViewById(R.id.choose_progressBar);

        listMatch = new ArrayList();

       // if (!ConfigAppParameters.isTest)
          //  requestMatch();

        sp = getApplicationContext().getSharedPreferences("tennissou", 0);

        if (sp.contains("arbitre")) {
            Gson gson = new Gson();
            String json = sp.getString("arbitre", "");


            currentArbitre = gson.fromJson(json, Arbitre.class);
            setTitle("Arbitre :  " +  currentArbitre.getNom() +" "+currentArbitre.getPrenom());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



        if (isTest) {

            gif.setVisibility(View.INVISIBLE);

            aMatch.add(new Match(BitmapFactory.decodeResource(getResources(), R.drawable.stade), "FEDERER VS RISITAS",
                    new Joueur("Federer", "Roger", "Angleterre", 28, 10),
                    new Joueur("Rissitas", "Issou", "Elite", 48, 2)));

            aMatch.add(new Match(BitmapFactory.decodeResource(getResources(), R.drawable.stade), "YATANGAKI VS RISITAS"));
            aMatch.add(new Match(BitmapFactory.decodeResource(getResources(), R.drawable.stade), "ISSOU VS RISITAS"));
            aMatch.add(new Match(BitmapFactory.decodeResource(getResources(), R.drawable.stade), "GPASLU VS RISITAS"));

            adapMatch = new MatchAdapter(this, aMatch);
            listView.setAdapter(adapMatch);
        } else {

        }

        if (isTest) {
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);


                    SharedPreferences sp = getApplicationContext().getSharedPreferences("tennissou", 0);
                    SharedPreferences.Editor edit = sp.edit();

                    Gson gson = new Gson();
                    String json = gson.toJson(aMatch.get((int) item));
                    edit.putString("match", json);
                    edit.commit();


                    Intent intent = new Intent(Choose_match_activity.this, MatchActivity.class);
                    //based on item add info to intent
                    startActivity(intent);
                }

            });
        }




        requestMatch();
    }




    @Override
    protected void onResume() {
        super.onResume();

        if(!isTest) {

            aMatch.clear();
            adapMatch = new MatchAdapter(this, aMatch);
            adapWMatch = new MatchWebAdapter(this, aMatch);

            adapWMatch.notifyDataSetChanged();
            listView.setAdapter(adapWMatch);




            gif.setVisibility(View.VISIBLE);

        }
    }



    public void requestMatch() {


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/matchs";

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

                            listMatch.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Log.i("requestJo", jo.toString());

                                Match match = Match.fromJson(jo);

                                    listMatch.add(Match.fromJson(jo));

                            }

                            for (Match match : listMatch) {



                                requestJoueur(match.idMatch);

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

    public void requestJoueur(final long matchId) {

        final ArrayList<Joueur> lJoueur = new ArrayList<Joueur>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/matchs/"+matchId+"/joueurs";

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
                            onSuccess(matchId, lJoueur);


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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Choose_match_activity.this, Connection_activity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSuccess(long matchId, ArrayList<Joueur> result) {

        gif.setVisibility(View.INVISIBLE);
        ArrayList<Joueur> j1 = new ArrayList<>();
        ArrayList<Joueur> j2 = new ArrayList<>();

        if (result.size() == 4) {

            j1.add(result.get(0));
            j1.add(result.get(1));

            j1.add(result.get(2));
            j1.add(result.get(3));

        } else {

            j1.add(result.get(0));
            j2.add(result.get(1));
        }

        if (listMatch.get((int)matchId -1).getIdArbitre() == currentArbitre.getIdArbitre() && listMatch.get((int)matchId -1).getEtatMatch().equals("En attente")) {
            Match match = new Match(listMatch.get((int) matchId - 1), j1, j2);

            aMatch.add(match);

            adapWMatch = new MatchWebAdapter(this, aMatch);
            adapWMatch.notifyDataSetChanged();

            listView.setAdapter(adapWMatch);
        }


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);


                SharedPreferences sp = getApplicationContext().getSharedPreferences("tennissou", 0);
                SharedPreferences.Editor edit = sp.edit();

                Gson gson = new Gson();
                String json = gson.toJson(aMatch.get((int)item));
                edit.putString("match", json);
                Log.i("json", json.toString());
                edit.commit();


                Intent intent = new Intent(Choose_match_activity.this, MatchActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }
        });

    }


}

