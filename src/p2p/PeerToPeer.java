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
          InetAddress IP=InetAddress.getLocalHost();
          System.out.println("IP of my system is := "+IP.getHostAddress());
          server();
 
      	Graph graphNetGraph = new Graph();
      	graphNetGraph.addNode("HElLo", "By");
		//outStream1.writeObject(graphNetGraph);
//          client("128.197.127.66", graphNetGraph );
      }

      public static void client(String ipAddress, Graph graph) throws UnknownHostException, IOException{
            Socket socket1;
            int portNumber = 1774;
            socket1 = new Socket(ipAddress, portNumber);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
           String read = "";
           while(true) 
           pw.println("sdahbfasd");
    
      }
      
      public static void server() throws IOException{
          int cTosPortNumber = 1775;
            String str;

            ServerSocket servSocket = null;
            try {
                servSocket = new ServerSocket(cTosPortNumber);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Waiting for a connection on " + cTosPortNumber);

            Socket fromClientSocket = servSocket.accept();
            PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);

            BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));
            while(true){
            while ((str = br.readLine()) != null) {
              System.out.println("The message: " + str);

              if (str.equals("bye")) {
                pw.println("bye");
                break;
              } else {
                str = "Server returns " + str;
                pw.println(str);
              }
            }
            }
          //  pw.close();
          //  br.close();

          //  fromClientSocket.close();
      }
      
    }
