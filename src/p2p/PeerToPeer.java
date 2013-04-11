package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PeerToPeer {
	private int numPeers;
	private ArrayList<String> peerUrls;
	
	
	public void partitionGraph(int numPeers){
		//TODO: Fill in with code to partition graph
	}
	
	//The main load balancer will talk to all the peers 
	public void startContoller() throws IOException{
		ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    out.println(this.numPeers);
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
	}
	
	// Each peer will have client side code
	public void startPeer(String serverAddress) throws UnknownHostException, IOException{
        Socket s = new Socket(serverAddress, 9090);
        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        System.exit(0);
    }
	
	public void addPeer(String peerUrl){
		this.setNumPeers(this.getNumPeers()+1);
		this.peerUrls.add(peerUrl);
	}
	
	public void dropPeer(String peerUrl){
		this.setNumPeers(this.getNumPeers()-1);
		this.peerUrls.remove(peerUrl);
	}
	
	public ArrayList<String> readPeerUrls(){
		return this.peerUrls;
	}
	
	public int getNumPeers(){
		return this.numPeers;
	}
	
	public void setNumPeers(int num){
		this.numPeers = num;
	}
	

}
