package networks.models;

/**
 * Created by sank on 4/19/16.
 */
public class Request extends Message {
    public Request(byte[] partIndex) {
        super (MessageType.Request, partIndex);
    }
}
