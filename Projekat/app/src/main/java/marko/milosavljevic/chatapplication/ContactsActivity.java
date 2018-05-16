package marko.milosavljevic.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logOut;
    private DbHelper db;
    private ContactsAdapter contactsAdapter;
    private Model[] contacts;
    private Button refreshButton;

    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private String userID;

    private HttpHelper httpHelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logOut = findViewById(R.id.LogOutID);
        logOut.setOnClickListener(this);
        refreshButton = findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);


         contactsAdapter = new ContactsAdapter(this);
      /*  contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact1).toString().charAt(0)), getResources().getString(R.string.contact1).toString(), getResources().getDrawable(R.drawable.send_button)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact2).toString().charAt(0)), getResources().getString(R.string.contact2).toString(), getResources().getDrawable(R.drawable.send_button)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact3).toString().charAt(0)), getResources().getString(R.string.contact3).toString(), getResources().getDrawable(R.drawable.send_button)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact4).toString().charAt(0)), getResources().getString(R.string.contact4).toString(), getResources().getDrawable(R.drawable.send_button)));
        contactsAdapter.AddContacts(new Model(("" + getResources().getString(R.string.contact5).toString().charAt(0)), getResources().getString(R.string.contact5).toString(), getResources().getDrawable(R.drawable.send_button)));
*/
        ListView list = findViewById(R.id.contactListID);
        list.setAdapter(contactsAdapter);
        //db = new DbHelper(this);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        userID = preferences.getString("sender_id1",null);

        httpHelper = new HttpHelper();
        handler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();

       /* contacts = db.ContactRead();
        contactsAdapter.AddContacts(contacts);

        if(contacts != null){
            for(int i =0 ; i< contacts.length;i++){
                if(contacts[i].getmId().compareTo(userID)==0){
                    contactsAdapter.removeContact(i);
                    break;
                }
            }
        }
        */
    }



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.LogOutID) {

         /*   Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
            */
            new Thread(new Runnable() {
                public void run() {
                    try {

                        final boolean success = httpHelper.logOutUser(ContactsActivity.this, LOGOUT_URL);

                        handler.post(new Runnable(){
                            public void run() {
                                if (success) {
                                    startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                    String logout_error = prefs.getString("logoutError", null);
                                    Toast.makeText(ContactsActivity.this, logout_error, Toast.LENGTH_SHORT).show();}
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }        if(view.getId() == R.id.refresh) {
            updateContactList();
    }}

    public void updateContactList() {

        new Thread(new Runnable() {
            Model[] contacts_class;
            public void run() {
                try {
                    final JSONArray contacts = httpHelper.getContacts(ContactsActivity.this, CONTACTS_URL);
                    handler.post(new Runnable(){
                        public void run() {
                            if (contacts != null) {

                                JSONObject json_contact;
                                contacts_class = new Model[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++) {
                                    try {
                                        json_contact = contacts.getJSONObject(i);
                                        contacts_class[i] = new Model(json_contact.getString("username"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                contactsAdapter.AddContacts(contacts_class);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }




}


