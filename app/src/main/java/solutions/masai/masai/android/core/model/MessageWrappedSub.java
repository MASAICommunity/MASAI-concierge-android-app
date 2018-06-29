package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-02-22.
 */

public class MessageWrappedSub {
    private String eventName;
    private Message args[];

    public Message[] getArgs() {
        return args;
    }

    public void setArgs(Message[] args) {
        this.args = args;
    }
}
