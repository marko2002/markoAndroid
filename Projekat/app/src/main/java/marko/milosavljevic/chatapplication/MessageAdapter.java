package marko.milosavljevic.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Win10 on 3/30/2018.
 */

public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MessageModel> mMessages;

    private static final String SHARED_PREFERENCES = "SharedPreferences";

    public MessageAdapter(Context context) {
        mContext = context;
        mMessages = new ArrayList<MessageModel>();
    }

    public void AddMessage(MessageModel model) {
        mMessages.add(model);
        notifyDataSetChanged();
    }

    public void AddMessage1(MessageModel[] mess){
        mMessages.clear();
        if(mess!=null){
            for(MessageModel mess1 : mess){
                mMessages.add(mess1);
            }
        }
        notifyDataSetChanged();
    }

    public void RemoveMessage(MessageModel model) {
        mMessages.remove(model);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv = null;
        try {
            rv = mMessages.get(i);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_messages, null);
            ViewHolder holder = new ViewHolder();
            holder.message = (TextView) convertView.findViewById(R.id.rowItemMessageID);
            convertView.setTag(holder);
        }

        MessageModel model = (MessageModel) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.message.setText(model.getmMessage());

        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES,mContext.MODE_PRIVATE);
        String senderID = preferences.getString("sender_id1",null);

        if(model.getmSenderID().toString().compareTo(senderID)==0) {

            holder.message.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        }else {
            holder.message.setBackgroundColor(Color.parseColor("#b7b3b3"));
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        }


        return convertView;
    }


    public class ViewHolder {

        public TextView message = null;
    }
}
