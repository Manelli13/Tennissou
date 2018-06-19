package elite.nation.tenissou;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Choose_match_activity extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_choose_match);
        listView = (ListView) findViewById(R.id.choose_list_lv);
        ArrayList<Match> aMatch = new ArrayList<>();
        MatchAdapter adapMatch;

        adapMatch = new MatchAdapter(this,aMatch );


        aMatch.add(new Match( BitmapFactory.decodeResource(getResources(), R.drawable.stade), "FEDERER VS RISITAS"));
        aMatch.add(new Match( BitmapFactory.decodeResource(getResources(), R.drawable.stade), "YATANGAKI VS RISITAS"));
        aMatch.add(new Match( BitmapFactory.decodeResource(getResources(), R.drawable.stade), "ISSOU VS RISITAS"));
        aMatch.add(new Match( BitmapFactory.decodeResource(getResources(), R.drawable.stade), "GPASLU VS RISITAS"));

        listView.setAdapter(adapMatch);

        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Intent intent = new Intent(Choose_match_activity.this,MatchActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }

        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent( Choose_match_activity.this, Connection_activity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
