package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    boolean passwordEnt = false;
    boolean usernameEnt = false;

    private DbHelper db;
    private static final String SHARED_PREFERENCES = "SharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);

        login = findViewById(R.id.loginID);
        login.setOnClickListener(this);
        register = findViewById(R.id.registerID);
        register.setOnClickListener(this);

        db = new DbHelper(this);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = username.getText().toString();

                if (text.length() != 0) {
                    usernameEnt = true;
                    if (passwordEnt == true) {

                        login.setEnabled(true);
                    }
                } else {
                    usernameEnt = false;
                    login.setEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = password.getText().toString();

                if (text.length() >= 6) {

                    passwordEnt = true;
                    if (usernameEnt == true) {
                        login.setEnabled(true);
                    }
                } else {
                    passwordEnt = false;
                    login.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.registerID) {

            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.loginID) {
            int found=0;

            Intent intent1 = new Intent(MainActivity.this, ContactsActivity.class);
            //startActivity(intent1);
            Model[] contacts = db.ContactRead();
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE).edit();

            if(contacts!=null){
                for(int i = 0 ; i<contacts.length;i++){
                    if((contacts[i].getmUsername().compareTo(username.getText().toString()))==0){
                        editor.putString("sender_id1",contacts[i].getmId());
                        editor.apply();
                        found=1;
                    }
                }
            }
            if(found==1){
                startActivity(intent1);
            }else {
                Toast.makeText(this,R.string.usernameDosentExist,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
