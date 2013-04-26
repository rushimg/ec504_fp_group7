package p2p;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import webGraph.Graph;

import javax.swing.JOptionPane;

public class PeerToPeer {

    private int numPeers;
    private ArrayList<String> peerUrls;

    public static void main(String[] args) throws Exception {
        InetAddress IP = InetAddress.getLocalHost();
        System.out.println("IP of my system is := " + IP.getHostAddress());
        getGraph();


        Graph graphNetGraph = new Graph();
        graphNetGraph.addNode("HElLo", "By");
        sendGraph("128.197.127.66", graphNetGraph,1774);
    }
    
    public static Graph intialize(String destinationIP) throws IOException{
        InetAddress IP = InetAddress.getLocalHost();
        String ip = IP.getHostAddress();
        requestGraph(destinationIP, 1774);
        try {
            return getGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;    
    }
    
    public static void requestGraph(String ipAddress,int portNumber) throws IOException{
        Socket socket1;

        socket1 = new Socket(ipAddress, portNumber);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socket1.getInputStream()));
        PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
        ObjectOutputStream outStream1 = new ObjectOutputStream(
                socket1.getOutputStream());

        outStream1.writeObject(true);

        br.close();
        pw.close();
        outStream1.close();
        socket1.close();
    }

    public static void sendGraph(String ipAddress, Graph graph, int portNumber)

            throws UnknownHostException, IOException {
        Socket socket1;
        socket1 = new Socket(ipAddress, portNumber);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socket1.getInputStream()));
        PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
        ObjectOutputStream outStream1 = new ObjectOutputStream(
                socket1.getOutputStream());

        outStream1.writeObject(graph);

        br.close();
        pw.close();
        outStream1.close();
        socket1.close();

    }

    public static void sendQuery(String ipAddress, String Query, int portNumber)

            throws UnknownHostException, IOException {
        Socket socket1;
        socket1 = new Socket(ipAddress, portNumber);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socket1.getInputStream()));
        PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
        ObjectOutputStream outStream1 = new ObjectOutputStream(
                socket1.getOutputStream());

        outStream1.writeObject(Query);

        br.close();
        pw.close();
        outStream1.close();
        socket1.close();

    }

    public static Graph getGraph() throws IOException {
        int cTosPortNumber = 1774;
        Graph str;

        ServerSocket servSocket = null;
        try {
            servSocket = new ServerSocket(cTosPortNumber);
        } catch (IOException e) {
            e.printStackTrace();

        }

        Socket fromClientSocket = servSocket.accept();
        PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(),
                true);

        ObjectInputStream inStream1 = new ObjectInputStream(
                fromClientSocket.getInputStream());

        while (true) {
            try {
                while ((str = (Graph) inStream1.readObject()) != null) {
                    str.printGraph();
                    return str;
                            
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static void getQuery() throws IOException {
        int cTosPortNumber = 1774;

        String str;
        ServerSocket servSocket = null;
        try {
            servSocket = new ServerSocket(cTosPortNumber);
        } catch (IOException e) {
            e.printStackTrace();

        }

        Socket fromClientSocket = servSocket.accept();
        PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(),
                true);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                fromClientSocket.getInputStream()));


        while (true) {
            while ((str = br.readLine()) != null) {
                // do query stuff
            }
        }

    }
    
    public static void sendUrl(String ipAddress){
        
    }

}