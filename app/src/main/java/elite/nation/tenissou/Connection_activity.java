package elite.nation.tenissou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Connection_activity extends AppCompatActivity {

    private Button connect;
    private EditText login;
    private  EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        connect = (Button) findViewById(R.id.connection_connect_btn);
        login = (EditText) findViewById(R.id.connection_login_etxt);
        password = (EditText) findViewById(R.id.connection_password_etxt);

    }


    public void connect (View v){

        Intent intent = new Intent(Connection_activity.this,Choose_match_activity.class );
        startActivity(intent);

    }
}
