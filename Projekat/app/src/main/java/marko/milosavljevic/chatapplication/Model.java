package marko.milosavljevic.chatapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by Win10 on 3/30/2018.
 */

public class Model {

    private String mmId;
    private String mmFirst_name;
    private String mmLast_name;
    private String mmUsername;

    public Model(String mId, String mFirst_name, String mLast_name, String mUsername) {
        this.mmId = mId;
        this.mmFirst_name = mFirst_name;
        this.mmLast_name = mLast_name;
        this.mmUsername = mUsername;
    }

    public String getmId() {
        return mmId;
    }

    public String getmFirst_name() {
        return mmFirst_name;
    }

    public String getmLast_name() {
        return mmLast_name;
    }

    public String getmUsername() {
        return mmUsername;
    }
}


