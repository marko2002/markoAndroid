package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logOut = findViewById(R.id.LogOutID);
        logOut.setOnClickListener(this);


        ContactsAdapter contactsAdapter = new ContactsAdapter(this);
        // adapter.addCharacter(new Character(getString(R.string.stan),getResources().getDrawable(R.drawable.stan_marsh)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact1).toString().charAt(0)), getResources().getString(R.string.contact1).toString(), getResources().getDrawable(R.drawable.ic_action_name)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact2).toString().charAt(0)), getResources().getString(R.string.contact2).toString(), getResources().getDrawable(R.drawable.ic_action_name)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact3).toString().charAt(0)), getResources().getString(R.string.contact3).toString(), getResources().getDrawable(R.drawable.ic_action_name)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact4).toString().charAt(0)), getResources().getString(R.string.contact4).toString(), getResources().getDrawable(R.drawable.ic_action_name)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact5).toString().charAt(0)), getResources().getString(R.string.contact5).toString(), getResources().getDrawable(R.drawable.ic_action_name)));

        ListView list = findViewById(R.id.contactListID);
        list.setAdapter(contactsAdapter);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.LogOutID) {

            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}


