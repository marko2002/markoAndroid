package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    boolean usernameVer = false;
    boolean passwordVer = false;
    boolean emailVer = false;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText firstname;
    private EditText lastname;
    private Button register;
    private DatePicker datePicker;
   // private DbHelper db;
    //private int existing = 1;

    private HttpHelper httpHelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String REGISTER_URL = BASE_URL + "/register";

    public static final String MY_PREFS_NAME = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.usernameRegID);
        password = findViewById(R.id.passwordRegID);
        firstname = findViewById(R.id.firstNameID);
        lastname = findViewById(R.id.lastNameID);
        email = findViewById(R.id.emailRegID);
        datePicker = findViewById(R.id.datePickerID);

       // db = new DbHelper(this);

        httpHelper = new HttpHelper();
        handler = new Handler();

        register = findViewById(R.id.registerActvButtonID);
        register.setOnClickListener(this);


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
                    usernameVer = true;
                    if (passwordVer == true && emailVer == true) {
                        register.setEnabled(true);
                    }
                } else {
                    usernameVer = false;
                    register.setEnabled(false);
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
                    passwordVer = true;
                    if (usernameVer == true && emailVer == true) {
                        register.setEnabled(true);
                    }
                } else {
                    passwordVer = false;
                    register.setEnabled(false);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = email.getText().toString();

                if (text.length() != 0 && Patterns.EMAIL_ADDRESS.matcher(text).matches()) {

                    emailVer = true;

                    if (usernameVer == true && passwordVer == true) {
                        register.setEnabled(true);
                    }

                } else {
                    register.setEnabled(false);
                }
            }
        });

        Date c = Calendar.getInstance().getTime();

        datePicker.setMaxDate(c.getTime());
    }

    @Override
    public void onClick(View view) {

       /* if (view.getId() == R.id.registerActvButtonID) {
            Model[] contact = db.ContactRead();
            if(contact!=null){
                for(int i = 0 ; i<contact.length;i++){
                    existing=contact[i].getmUsername().compareTo(username.getText().toString());
                    if(existing==0){
                        break;
                    }
                }
            }
            if(existing != 0){
                existing=1;
                Model model = new Model(null,firstname.getText().toString(),lastname.getText().toString(),username.getText().toString());
                db.ContactInsert(model);
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), R.string.existingUsername, Toast.LENGTH_LONG).show();
            }

        }*/

       if(view.getId()==R.id.registerActvButtonID){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   JSONObject jsonObject = new JSONObject();
                   try {
                       jsonObject.put("username",username.getText().toString());
                       jsonObject.put("password",password.getText().toString());
                       jsonObject.put("email",email.getText().toString());

                       final boolean check = httpHelper.registerNewUser(RegisterActivity.this,REGISTER_URL,jsonObject);

                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               if(check){
                                   Toast.makeText(RegisterActivity.this,getText(R.string.userRegistration),Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                   startActivity(intent);
                               }else{
                                   SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                                   String error_message = preferences.getString("error_message_register",null);
                                   Toast.makeText(RegisterActivity.this,error_message,Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }catch (JSONException e){
                       e.printStackTrace();
                   }catch (IOException e){
                       e.printStackTrace();
                   }
               }
           }).start();
       }
    }
}
