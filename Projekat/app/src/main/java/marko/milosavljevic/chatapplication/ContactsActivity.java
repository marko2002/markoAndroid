package marko.milosavljevic.chatapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener,ServiceConnection {

    private Button logOut;
   // private DbHelper db;
    private ContactsAdapter contactsAdapter;
    private Model[] contacts_class;;
    private Button refreshButton;

    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private String userID;

    private HttpHelper httpHelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private INotificationBinder m_service = null;

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

        bindService(new Intent(ContactsActivity.this, ServiceNotification.class), this , Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContactList();

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
    protected void onDestroy() {
        super.onDestroy();

        if (m_service != null) {
            unbindService(this);
        }
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
                                        contacts_class[i] = new Model(json_contact.getString("username"),getText(R.string.clickForMessage).toString());
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
    public void getLastMsg(Model oldContact){
        final String contact = oldContact.getmUsername();
        final Model oldContactTemp = oldContact;

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = httpHelper.getMessages(ContactsActivity.this, contact);
                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {
                                String last_message = getText(R.string.noNewMessages).toString();
                                JSONObject json_message;
                                if (messages.length()>0){
                                    int lastMsgIndex = messages.length()-1;
                                    try {
                                        json_message = messages.getJSONObject(lastMsgIndex);
                                        last_message = json_message.getString("sender") +": " + json_message.getString("data");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                Model newContact = new Model(contact, last_message);
                                contactsAdapter.addContact(oldContactTemp, newContact);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        m_service = INotificationBinder.Stub.asInterface(service);
        try {
            m_service.setCallback(new NotificationCallback());
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        m_service = null;
    }

    private class NotificationCallback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {

            final HttpHelper http = new HttpHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.send_button)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.ic_launcher))
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.newMessages))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httpHelper.getNotification(ContactsActivity.this);

                        handler.post(new Runnable() {
                            public void run() {
                                if (response) {
                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(2, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}







