package networks.impl;

import networks.impl.File.FileUtility;
import networks.models.Message;
import networks.models.Request;
import networks.utilities.LogHelper;
import networks.utilities.PayloadWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.TimerTask;

public class RequestTimer extends TimerTask{
    private final Request request;
    private final FileUtility fileManager;
    private final PayloadWriter outStream;
    private final int remotePeerId;
    private final Message message;

    public RequestTimer (Request request1, FileUtility fileManager1, PayloadWriter out1, Message message1, int remotePeerId1) {
        super();
        request = request1;
        fileManager = fileManager1;
        outStream = out1;
        remotePeerId = remotePeerId1;
        message = message1;
    }

    public void run() {
        if (fileManager.hasChunk(ByteBuffer.wrap(Arrays.copyOfRange(request.getPayload(), 0, 4)).order(ByteOrder.BIG_ENDIAN).getInt())){
            LogHelper.getLogger().debug("Not rerequesting piece " + ByteBuffer.wrap(Arrays.copyOfRange(request.getPayload(), 0, 4)).order(ByteOrder.BIG_ENDIAN).getInt()
                    + " to peer " + remotePeerId);
    }
        else {
            LogHelper.getLogger().debug("Rerequesting piece " + ByteBuffer.wrap(Arrays.copyOfRange(request.getPayload(), 0, 4)).order(ByteOrder.BIG_ENDIAN).getInt()
                    + " to peer " + remotePeerId);
            try {
                outStream.writeObject(message);
            } catch (IOException e) {
                LogHelper.getLogger().warning("IOEXception Occurred");
            }

        }
    }
}
