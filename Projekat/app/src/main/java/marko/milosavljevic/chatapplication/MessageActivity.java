package marko.milosavljevic.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logOut;
    private Button send;
    private EditText message;
    private Button refresh;

    private ListView list;
    final MessageAdapter messageAdapter = new MessageAdapter(this);
    // private DbHelper db;
    private MessageModel[] bufferedMessages;

    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private String senderID;
    private String receiverID;

    private static final String TAG = "MessageActivity";

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private HttpHelper httpHelper;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        refresh = findViewById(R.id.refresh_messages);
        refresh.setOnClickListener(this);

        logOut = findViewById(R.id.messageActvLogOutBtnID);
        logOut.setOnClickListener(this);

        send = findViewById(R.id.sendBtnID);
        send.setOnClickListener(this);

        message = findViewById(R.id.massageTextID);

        list = findViewById(R.id.messageListID);
        TextView contact = findViewById(R.id.contactNameID);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("textNameID");
        contact.setText(name);



       /* messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message1).toString(), true));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message2).toString(), false));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message3).toString(), true));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message4).toString(), false));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message5).toString(), true));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message6).toString(), false));
        messageAdapter.AddMessage(new MessageModel(getResources().getString(R.string.message7).toString(), true));
    */

        SharedPreferences  preferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        senderID = preferences.getString("sender_id1",null);
        receiverID = preferences.getString("receiver_id1",null);

        // db = new DbHelper(this);
        httpHelper = new HttpHelper();
        handler = new Handler();
        list.setAdapter(messageAdapter);


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

           /* @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                final int deletePos = position;
                final MessageModel message = (MessageModel) messageAdapter.getItem(deletePos);



                if (bufferedMessages!=null) {
                    for (int i = 0; i < bufferedMessages.length; i++) {
                        if (bufferedMessages[i].getmMessageId().compareTo(message.getmMessageId()) == 0) {
                          //  db.deleteMessage(message.getmMessageId());
                            Context context = getApplicationContext();
                            CharSequence text = getResources().getString(R.string.messageDeleted);
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            break;
                        }
                    }
                }
                updateList();
                return true;
            }*/

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        MessageModel model  = (MessageModel) messageAdapter.getItem(position);
                        String message_for_del = model.getmMessage();



                        try {

                            if(model.getmSenderID().toString().compareTo(senderID)==0) {
                                jsonObject.put("sender", senderID);
                                jsonObject.put("receiver", receiverID);
                                jsonObject.put("data", message_for_del);
                            }else{
                                jsonObject.put("sender", receiverID);
                                jsonObject.put("receiver", senderID);
                                jsonObject.put("data", message_for_del);

                            }
                            Log.d(TAG, "run: " + jsonObject.toString());

                            final boolean success = httpHelper.httpDelete(MessageActivity.this, POST_MESSAGE_URL, jsonObject);



                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {

                                        Toast.makeText(MessageActivity.this, getText(R.string.messageDeleted), Toast.LENGTH_SHORT).show();
                                        message.getText().clear();
                                        updateList();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                        String sendMsgErr = prefs.getString("sendMsgErr", null);
                                        Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
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
                return true;
            }




        });


        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text1 = message.getText().toString();

                if (text1.length() != 0) {
                    send.setEnabled(true);
                } else {
                    send.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.messageActvLogOutBtnID) {
            /*
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intent);
            */
            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httpHelper.logOutUser(MessageActivity.this, LOGOUT_URL);
                        handler.post(new Runnable(){
                            public void run() {
                                if (response) {
                                    startActivity(new Intent(MessageActivity.this, MainActivity.class));
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                    String logoutErr = prefs.getString("logoutError", null);
                                    Toast.makeText(MessageActivity.this, logoutErr, Toast.LENGTH_SHORT).show();
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



        }if (view.getId() == R.id.sendBtnID) {

          /*  Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.messageSent);
            int duration = Toast.LENGTH_SHORT;

            String messageForSend = message.getText().toString();
            MessageModel mess = new MessageModel(null,senderID,receiverID,messageForSend);
            //db.insertMessage(mess);
            updateList();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            message.setText("");

        */
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("receiver", receiverID);
                        jsonObject.put("data", message.getText().toString());

                        final boolean success = httpHelper.sendMessage(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                        handler.post(new Runnable(){
                            public void run() {
                                if (success) {
                                    Toast.makeText(MessageActivity.this, getText(R.string.messageSent), Toast.LENGTH_SHORT).show();
                                    message.getText().clear();
                                    updateList();
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                    String sendMsgErr = prefs.getString("sendMsgErr", null);
                                    Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
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

        }  if(view.getId() == R.id.refresh_messages) {
            updateList();
        }

    }

    public void updateList(){

        // bufferedMessages = db.readMessages(senderID,receiverID);
        // messageAdapter.AddMessage1(bufferedMessages);

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = httpHelper.getMessages(MessageActivity.this, GET_MESSAGE_URL+receiverID);

                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {

                                JSONObject json_message;
                                bufferedMessages = new MessageModel[messages.length()];

                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        json_message = messages.getJSONObject(i);
                                        bufferedMessages[i] = new MessageModel(json_message.getString("sender"),json_message.getString("data"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                messageAdapter.AddMessage1(bufferedMessages);
                            } else {
                                SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                                String getMessagesErr = prefs.getString("getMessagesErr", null);
                                Toast.makeText(MessageActivity.this, getMessagesErr, Toast.LENGTH_SHORT).show();
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
