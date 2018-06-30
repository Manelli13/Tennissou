package elite.nation.tenissou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

public class Connection_activity extends AppCompatActivity {

    private Button connect;
    private EditText login;
    private EditText password;
    Arbitre currentArbitre;
    ImageView imV;
    GifTextView gifFromResource;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        connect = (Button) findViewById(R.id.connection_connect_btn);
        login = (EditText) findViewById(R.id.connection_login_etxt);
        password = (EditText) findViewById(R.id.connection_password_etxt);
        imV = (ImageView) findViewById(R.id.connection_image_imb);
        gifFromResource = (GifTextView) findViewById(R.id.connection_gif_imb);



    }

    @Override
    protected void onResume() {
        super.onResume();

        imV.setVisibility(View.VISIBLE);
        gifFromResource.setVisibility(View.INVISIBLE);

        sp = getApplicationContext().getSharedPreferences("tennissou", 0);

        String val = "";

        //TODO recup max set

        if (sp.contains("arbitre")) {
            Gson gson = new Gson();
            String json = sp.getString("arbitre", "");


            currentArbitre = gson.fromJson(json, Arbitre.class);
            val = currentArbitre.getMailArbitre();
            login.setText(val);
        } else
            val = "";

    }


    public void connect (View v){

        if(login.getText().length() !=0)
        requestArbitre();
        else
            Toast.makeText(getApplicationContext(), "Veuillez indiquer vos identifiants de connexion", Toast.LENGTH_SHORT).show();

    }


    public void requestArbitre() {

        String pwd = password.getText().toString();
        String mail = login.getText().toString();


        imV.setVisibility(View.INVISIBLE);
        gifFromResource.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConfigAppParameters.URL + "/arbitre/"+mail+"/"+pwd;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            currentArbitre = Arbitre.fromJson(jsonObject);

                            if (currentArbitre == null) {
                                Toast.makeText(getApplicationContext(), "Mauvais mail ou mots de passe", Toast.LENGTH_SHORT).show();
                                imV.setVisibility(View.VISIBLE);
                                gifFromResource.setVisibility(View.INVISIBLE);

                            }
                            else {


                                SharedPreferences.Editor edit = sp.edit();

                                Gson gson = new Gson();
                                String json = gson.toJson(currentArbitre);
                                edit.putString("arbitre", json);

                                Log.i("json", json.toString());

                                edit.commit();


                                Intent intent = new Intent(Connection_activity.this, Choose_match_activity.class);
                                //based on item add info to intent
                                startActivity(intent);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
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
}
