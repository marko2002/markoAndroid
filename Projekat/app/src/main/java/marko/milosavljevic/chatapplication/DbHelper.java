package marko.milosavljevic.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Display;

/**
 * Created by Win10 on 4/23/2018.
 */

public class DbHelper /*extends SQLiteOpenHelper */{}

   /* public static final String DATABASE_NAME = "helperdatabase.db";
    public static final int DATABASE_VERSION = 1;



    public static final String TABLE_NAME_CONTACT = "Contact";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_USERNAME = "userame";
    public static final String COLUMN_FIRST_NAME = "firstname";
    public static final String COLUMN_LAST_NAME = "lastname";

    public static final String TABLE_NAME_MESSAGE = "Message";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECEIVER_ID = "receiver_id";
    public static final String COLUMN_MESSAGE = "message";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACT + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT); " );

        db.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGE + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MESSAGE + " TEXT); " );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void ContactInsert (Model model ){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
       // values.put(COLUMN_CONTACT_ID,model.getmId());
        values.put(COLUMN_USERNAME,model.getmUsername());
       // values.put(COLUMN_FIRST_NAME,model.getmFirst_name());
        ///values.put(COLUMN_LAST_NAME,model.getmLast_name());

        db.insert(TABLE_NAME_CONTACT,null,values);
        close();
    }

    public Model[] ContactRead(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACT,null,null,null,null,null,null,null);

        if(cursor.getCount()<=0){
            return null;
        }

        Model[] contacts = new Model[cursor.getCount()];
        int i=0;

        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            contacts[i++]=createContact(cursor);
        }
        close();
        return contacts;
    }

    private Model createContact(Cursor cursor) {
        String contact_id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String first_name = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String last_name = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
        return new Model(contact_id, first_name, last_name, username);
    }

    public void deleteContact(String index) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CONTACT, COLUMN_CONTACT_ID + "=?", new String[] {index});
        close();
    }


    public void insertMessage(MessageModel model) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID, model.getmMessageId());
        values.put(COLUMN_SENDER_ID, model.getmSenderID());
        values.put(COLUMN_RECEIVER_ID, model.getmReceiverId());
        values.put(COLUMN_MESSAGE, model.getmMessage());

        db.insert(TABLE_NAME_MESSAGE, null, values);
        close();
    }


    public MessageModel[] readMessages(String sender, String receiver) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, "(sender_id =? AND receiver_id =?) OR (sender_id =? AND receiver_id =?)", new String[] {sender,receiver,receiver,sender}, null, null, null, null);

        if(cursor.getCount() <= 0) {
            return null;
        }

        MessageModel[] messages = new MessageModel[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }


    private MessageModel createMessage(Cursor cursor) {
        String message_id = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        String sender_id = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID));
        String receiver_id = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID));
        String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
        return new MessageModel(message_id, sender_id, receiver_id, message);
    }

    public void deleteMessage(String index) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MESSAGE, COLUMN_MESSAGE_ID + "=?", new String[] {index});
        close();
    }

}*/
