package marko.milosavljevic.chatapplication;


/**
 * Created by Win10 on 3/30/2018.
 */

public class MessageModel {

    public String mMessage;
    public boolean mColor = false;


    public MessageModel(String message, boolean color) {
        this.mMessage = message;
        this.mColor = color;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String message) {
        this.mMessage = message;
    }
}
