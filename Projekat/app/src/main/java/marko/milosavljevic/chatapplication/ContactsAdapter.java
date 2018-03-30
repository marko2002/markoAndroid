package marko.milosavljevic.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Win10 on 3/30/2018.
 */

public class ContactsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Model> mContacts;

    public ContactsAdapter(Context context){
        mContext = context;
        mContacts = new ArrayList<Model>();
    }

    public void AddContacts(Model model){
        mContacts.add(model);
        notifyDataSetChanged();

    }



    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv =null;
        try {
            rv=mContacts.get(i);
        }catch (IndexOutOfBoundsException e){
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

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item,null);
            ImageView send = (ImageView) convertView.findViewById(R.id.imageViewID);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext,MessageActivity.class);
                    mContext.startActivity(intent);

                }
            });

            ViewHolder holder = new ViewHolder();

            holder.letter=(TextView) convertView.findViewById(R.id.letterID);
            holder.name = (TextView) convertView.findViewById(R.id.textNameID);
            holder.image = (ImageView) convertView.findViewById(R.id.imageViewID);
            holder.letter.setBackgroundColor(randomColor());

            convertView.setTag(holder);

        }

        Model model = (Model) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.letter.setText(model.mLetter);
        holder.name.setText(model.mName);
        holder.image.setImageDrawable(model.mImage);

        return convertView;

    }


    public int randomColor(){

        Random rnd = new Random();

        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

    }


    public class ViewHolder{

        public TextView letter = null;
        public TextView name = null;
        public ImageView image =null;

    }




}