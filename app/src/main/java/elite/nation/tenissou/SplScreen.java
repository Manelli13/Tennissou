package elite.nation.tenissou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class SplScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spl_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    Intent openstartingpoint = new Intent(SplScreen.this, Connection_activity.class);
                    startActivity(openstartingpoint);
                }
            }
        };
        timer.start();
    }
}