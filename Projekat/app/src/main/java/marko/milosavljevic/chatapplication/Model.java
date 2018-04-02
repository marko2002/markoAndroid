package marko.milosavljevic.chatapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by Win10 on 3/30/2018.
 */

public class Model {

    public String mLetter;
    public String mName;
    public Drawable mImage;

    public Model(String mLetter, String mName, Drawable mImage) {
        this.mLetter = mLetter;
        this.mName = mName;
        this.mImage = mImage;
    }

    public String getmLetter() {
        return mLetter;
    }

    public void setmLetter(String mLetter) {
        this.mLetter = mLetter;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Drawable getmImage() {
        return mImage;
    }

    public void setmImage(Drawable mImage) {
        this.mImage = mImage;
    }
}
