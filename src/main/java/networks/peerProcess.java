package networks;

import networks.impl.PeerImplementation;
import networks.impl.Process;
import networks.models.RemotePeerInfo;
import networks.utilities.LogHelper;
import networks.utilities.PropertyFileUtility;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class peerProcess {
    public static void main (String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        PeerImplementation peerImplementation = new PeerImplementation();
        if (args.length != 1) {
            LogHelper.getLogger().severe("the number of arguments passed to the program is " + args.length + " while it should be 1.\nUsage: java networks.peerProcess peerId");
        }
        final int peerId = Integer.parseInt(args[0]);
        LogHelper.configure(peerId);
        String address = "localhost";
        int port = 6008;
        boolean hasFile = false;
        PropertyFileUtility commProp = new PropertyFileUtility("Common.cfg");
        Collection<RemotePeerInfo> peersToConnectTo = new LinkedList<RemotePeerInfo>();
        Iterator<RemotePeerInfo> iter = peerImplementation.getAllPeerInfo().iterator();
        while (iter.hasNext()){
            RemotePeerInfo currPeer = iter.next();
            if(peerId == currPeer.getPeerId()) {
                address = currPeer.getPeerAddress();
                port = currPeer.getPeerPort();
                hasFile = currPeer.isHasFile();
                break;
            }
            else {
                peersToConnectTo.add(currPeer);
                LogHelper.getLogger().conf ("Read configuration for peer: " + peerId);
                System.out.println("Read configuration for peer : " + peerId);
            }
        }

        Process peerProc = new Process(peerId, address, port, hasFile, peerImplementation.getAllPeerInfo(), commProp);
        peerProc.init();
        Thread t = new Thread (peerProc);
        t.setName ("networks.peerProcess-" + peerId);
        t.start();

        LogHelper.getLogger().debug ("Connecting to " + peersToConnectTo.size() + " peers.");
        peerProc.connectToPeers (peersToConnectTo);

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

