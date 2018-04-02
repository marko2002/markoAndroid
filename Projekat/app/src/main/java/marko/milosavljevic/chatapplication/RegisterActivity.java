package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    boolean usernameVer = false;
    boolean passwordVer = false;
    boolean emailVer = false;
    private EditText username;
    private EditText password;
    private EditText email;
    private Button register;
    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.usernameRegID);
        password = findViewById(R.id.passwordRegID);
        email = findViewById(R.id.emailRegID);
        datePicker = findViewById(R.id.datePickerID);


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

        if (view.getId() == R.id.registerActvButtonID) {

            Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
            startActivity(intent);
        }
    }
}
