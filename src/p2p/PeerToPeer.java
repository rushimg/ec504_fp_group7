package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PeerToPeer {

      private int numPeers;
      private ArrayList<String> peerUrls;

      public static void main(String args[]) throws Exception {
          InetAddress IP=InetAddress.getLocalHost();
          System.out.println("IP of my system is := "+IP.getHostAddress());
          server();
          client(IP.getHostAddress());
      }

      public static void client(String ipAddress) throws UnknownHostException, IOException{
            Socket socket1;
            int portNumber = 1778;
            String str = "initialize";
            socket1 = new Socket(ipAddress, portNumber);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
            pw.println(str);
            while ((str = br.readLine()) != null) {
              System.out.println(str);
              pw.println("bye");
              if (str.equals("bye"))
                break;
            }
            br.close();
            pw.close();
            socket1.close();
      }
      
      public static void server() throws IOException{
          int cTosPortNumber = 1778;
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
            pw.close();
            br.close();

            fromClientSocket.close();
      }
      
    }
