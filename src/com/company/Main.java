package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class Main {
    static Calendar now = Calendar.getInstance();
    public static void main(String[] args) throws IOException {
        ServerSocket myserverSocket = new ServerSocket(6698);
   while(true)
   { Socket clientSocket=null;

       try{
           clientSocket =myserverSocket.accept();
           DataInputStream ournewDataInputstream = new DataInputStream(clientSocket.getInputStream());
           DataOutputStream ournewDataOutputstream = new DataOutputStream(clientSocket.getOutputStream());
           Thread myThread = new ClientHandler(clientSocket, ournewDataInputstream, ournewDataOutputstream,System.currentTimeMillis(),ournewDataInputstream.readUTF());
           // starting
           myThread.start();
       }
       catch (Exception e){
           assert clientSocket != null;
           clientSocket.close();
           e.printStackTrace();
       }
   }
    }
}
