package marko.milosavljevic.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Win10 on 5/14/2018.
 */

public class HttpHelper {

    private static final int SUCCESS = 200;
    public static final String MY_PREFS_NAME = "MyPrefs";

    public boolean registerNewUser(Context context, String urlString, JSONObject jsonObject)throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        }catch (IOException e){
            return  false;
        }

        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        /*write json object*/
        outputStream.writeBytes(jsonObject.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = urlConnection.getResponseCode();

        if(responseCode!=SUCCESS){
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();
            String error_message = urlConnection.getResponseMessage();
            editor.putString("error_message_register",error_message);
            editor.apply();
        }
        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public boolean logInUser(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        outputStream.writeBytes(jsonObject.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode =  urlConnection.getResponseCode();
        String sessionid = urlConnection.getHeaderField("sessionid");
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode==SUCCESS) {
            editor.putString("sessionId", sessionid);
            editor.apply();
        } else {
            String responseMsg = urlConnection.getResponseMessage();
            String loginErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("loginErr", loginErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getContacts(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        int responseCode =  urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    public boolean logOutUser(Context contacts_context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();
        SharedPreferences.Editor editor = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String logoutErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("logoutError", logoutErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean sendMessage(Context message_context, String urlString, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = message_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        SharedPreferences.Editor editor = message_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String sendMsgErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("sendMsgErr", sendMsgErr);
            editor.apply();
        }

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public JSONArray getMessages(Context contacts_context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();
        SharedPreferences.Editor editor = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();



        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getMessagesErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getMessagesErr", getMessagesErr);
            editor.apply();
            return null;
        }


    }
}




