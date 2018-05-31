// INotificationBinder.aidl
package marko.milosavljevic.chatapplication;

// Declare any non-default types here with import statements
import marko.milosavljevic.chatapplication.INotificationCallback;

interface INotificationBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void setCallback(in INotificationCallback callback);
}
