package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logOut;
    private TextView contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logOut = findViewById(R.id.LogOutID);
        logOut.setOnClickListener(this);

        contact = findViewById(R.id.MyFriendTextClickID);
        contact.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.LogOutID){

            Intent intent = new Intent(ContactsActivity.this,MainActivity.class);
            startActivity(intent);

        }else if(view.getId()==R.id.MyFriendTextClickID){

            Intent intent1 = new Intent(ContactsActivity.this,MessageActivity.class);
            startActivity(intent1);

        }

    }
}
