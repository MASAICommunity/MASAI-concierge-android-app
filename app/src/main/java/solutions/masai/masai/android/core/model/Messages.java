package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2016-12-07.
 */

public class Messages {
    private Message messages[];
    private int unreadNotLoaded = 0;

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public int getUnreadNotLoaded() {
        return unreadNotLoaded;
    }

    public void setUnreadNotLoaded(int unreadNotLoaded) {
        this.unreadNotLoaded = unreadNotLoaded;
    }
}
