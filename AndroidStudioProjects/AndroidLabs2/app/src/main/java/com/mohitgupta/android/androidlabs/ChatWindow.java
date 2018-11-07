package com.mohitgupta.android.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    public static final String ACTIVITY_NAME = "ChatWindow";
    public static final String SQL_MESSAGE = "SQL MESSAGE:";
   // public static final String COLUMN_COUNT = "Cursor\'s  column count= ";
    Cursor cursor;

    //ArrayList to hold the messages of the chat.
    private ArrayList<String> msgs = new ArrayList<>();

    SQLiteDatabase database;

    ChatDatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        helper = new ChatDatabaseHelper(this);
        database = helper.getWritableDatabase();

        ListView listView = (ListView) findViewById(R.id.chatListView);
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


        Button button = (Button) findViewById(R.id.sendButton);
        final EditText editText = (EditText) findViewById(R.id.chatEditText);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mymessage = editText.getText().toString();

                int messageCharLength = mymessage.trim().length();


                if (TextUtils.isEmpty(mymessage)) {
                    editText.setError("Empty message ignored");
                } else {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ChatDatabaseHelper.COLUMN_MESSAGE, editText.getText().toString());
                    database.insert(ChatDatabaseHelper.TABLE_NAME, "null", contentValues);
                    getMsgs().add(mymessage);
                    editText.setText("");
                    editText.setHint("So far " + getMsgs().size() + " messages");
                    messageAdapter.notifyDataSetChanged();

                }

            }
        });
        //This creates a string array used for the Cursor object and its query(..) method signature.
        String[] allColumns = {ChatDatabaseHelper.COLUMN_ID, ChatDatabaseHelper.COLUMN_MESSAGE};

        cursor = database.query(helper.TABLE_NAME, allColumns, null, null, null, null, null);

        //move the cursor to the first row
        cursor.moveToFirst();
        Log.i(ACTIVITY_NAME,  "cursor column count " + cursor.getColumnCount() );
        while(!cursor.isAfterLast() ){
            String newMessage = cursor.getString(cursor.getColumnIndex(
                    ChatDatabaseHelper.COLUMN_MESSAGE));
            msgs.add(newMessage);
            Log.i(ACTIVITY_NAME, SQL_MESSAGE +":" + cursor.getString
                    ( cursor.getColumnIndex( ChatDatabaseHelper.COLUMN_MESSAGE) ) );
            //Log.i(ACTIVITY_NAME,  "cursor column count " + cursor.getColumnCount() );

            cursor.moveToNext();
        }

        for(int i = 0;i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"Cursor Column " + cursor.getColumnName(i));
        }
    }
    public ArrayList<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(ArrayList<String> msgs) {
        this.msgs = msgs;
    }



    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return getMsgs().size();
        }

        public String getItem(int position) {
            return getMsgs().get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }


            TextView message = (TextView) result.findViewById(R.id.message_text);  //this is the msg from the chat_row_incoming/outing
            message.setText(getItem(position));
            return result;

        }


    }//end class ChatAdapter




    /**
     * Created by ARSIA on 11/13/2016.
     */


    public static class ChatDatabaseHelper extends SQLiteOpenHelper {
        private static final String TAG = ChatDatabaseHelper.class.getSimpleName();
        public static  final String TABLE_NAME = "CHATS";
        private static final String DATABASE_NAME = "Chats.db";
        private static final int VERSION_NUM = 4;
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MESSAGE = "_msg";

        //Database creation sql statement:
        private static final String CREATE_DATABASE = "create table "
                + TABLE_NAME
                + "( "
                + COLUMN_ID
                + " integer primary key autoincrement, "
                + COLUMN_MESSAGE
                + " VARCHAR(50));";



        public ChatDatabaseHelper(Context ctx){
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATABASE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.i(TAG,"Downgrading database from version " + newVersion
                    + " to " + oldVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }



}//end class ChatWindow
