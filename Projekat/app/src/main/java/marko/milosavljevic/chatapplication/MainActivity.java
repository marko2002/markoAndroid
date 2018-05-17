package marko.milosavljevic.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    boolean passwordEnt = false;
    boolean usernameEnt = false;

   // private DbHelper db;
    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private Context context;

    private HttpHelper httpHelper;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";

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
        //db = new DbHelper(this);
        context = this;
        httpHelper = new HttpHelper();
        handler = new Handler();

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
           /* int found=0;

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
            */
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());

                        final boolean response = httpHelper.logInUser(context, LOGIN_URL, jsonObject);

                        handler.post(new Runnable(){
                            public void run() {
                                if (response) {
                                    SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit();
                                    editor.putString("sender_id1", username.getText().toString());
                                    editor.apply();

                                    Intent intent2 = new Intent(MainActivity.this, ContactsActivity.class);
                                    startActivity(intent2);
                                } else {
                                    SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                    String error_message = prefs.getString("loginErr", null);
                                    Toast.makeText(MainActivity.this, R.string.CONFLICT,Toast.LENGTH_LONG).show();
                                 //   Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        }
    }
}
