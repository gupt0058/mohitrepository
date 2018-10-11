package com.mohitgupta.android.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private static ArrayList<String> chatLog;
    private static ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatLog = new ArrayList<>();
        messageAdapter = new ChatAdapter(this);
        ListView chatList = findViewById(R.id.chatListView);
        chatList.setAdapter(messageAdapter);
    }

    public void sendButton(View view) {
        EditText input = findViewById(R.id.chatEditText);
        chatLog.add(input.getText().toString());
        messageAdapter.notifyDataSetChanged();
        input.setText("");
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return chatLog.size();
        }

        @Override
        public String getItem(int position) {
            return chatLog.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

}