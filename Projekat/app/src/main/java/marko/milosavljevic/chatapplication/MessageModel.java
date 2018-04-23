package marko.milosavljevic.chatapplication;


/**
 * Created by Win10 on 3/30/2018.
 */

public class MessageModel {

    private String mMessageId;
    private String mSenderID;
    private String mReceiverId;
    private String mMessage;
    private boolean mColor = false;

    public MessageModel(String mMessageId, String mSenderID, String mReceiverId, String mMessage, boolean mColor) {
        this.mMessageId = mMessageId;
        this.mSenderID = mSenderID;
        this.mReceiverId = mReceiverId;
        this.mMessage = mMessage;
        this.mColor = mColor;
    }

    public String getmMessageId() {
        return mMessageId;
    }

    public String getmSenderID() {
        return mSenderID;
    }

    public String getmReceiverId() {
        return mReceiverId;
    }

    public String getmMessage() {
        return mMessage;
    }


}
