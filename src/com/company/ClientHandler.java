package com.company;

import java.io.*;
import java.io.File;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread {
    final DataInputStream ournewDataInputstream;
    final DataOutputStream ournewDataOutputstream;
    final Socket mynewSocket;
    static Calendar now = Calendar.getInstance();
    long Time = 0;
    static int acceptedFilesWthServer = 0;
    static int refusedFilesWthServer = 0;
    String path = "";
    int fileNumber = 0;
    Map<String, Long> fileTime = new HashMap<String, Long>();
    public ClientHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream, long time, String path) throws IOException {
        this.ournewDataInputstream = ournewDataInputstream;
        this.ournewDataOutputstream = ournewDataOutputstream;
        this.mynewSocket = mynewSocket;
        this.path = path;
        this.Time = time;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long startTime = System.currentTimeMillis();
                recieveFile(path, mynewSocket, ournewDataInputstream, ournewDataOutputstream,startTime);
                compareFiles(path, mynewSocket);
                printTimeDetails(fileNumber);
            } catch (Exception e) {
            }
        }

    }
    public void recieveFile(String path, Socket socket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream,long startTime) throws IOException {
        FileOutputStream fos=null;
        BufferedOutputStream bos=null;
        try {
            // receive file
            byte[] mybytearray = new byte[6022386];
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream(path);
            bos = new BufferedOutputStream(fos);
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);
            int current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) current += bytesRead;
            } while (bytesRead > -1);
//printTimeDetails();
            bos.write(mybytearray, 0, current);
            bos.flush();
        } finally {
            fileTime.put(String.format("file number %s", fileNumber + 1),System.currentTimeMillis() - startTime);
            fileNumber++;
        }
    }
  public  void compareFiles(String path, Socket client) throws IOException {
      List<Path>s=Files.list(Paths.get("")).toList();
      File serverfile;
      File exist = new File(path);
      for (Path e:s) {
          serverfile=new File(e.toString());
          if(serverfile.compareTo(exist)==0)
          {
              if(Files.mismatch(serverfile.toPath(),exist.toPath())==-1)
              {
                  System.out.println("read successfully");
               System.out.println(Files.mismatch(e, exist.toPath()));
                  acceptedFilesWthServer++;}
          }
          else
          {refusedFilesWthServer++;}

      }
    }
  public  void printTimeDetails(int num) {
        long tim = System.currentTimeMillis() - this.Time;
        System.out.println("total time for " + num + " is = " + tim);
        System.out.println("average time for " + num + " is = " + tim / num);
        System.out.println("number of refused files is = " + refusedFilesWthServer);
        System.out.println("number of accepted files is = " + acceptedFilesWthServer);
      for (Map.Entry<String, Long> me :
              fileTime.entrySet()) {
          System.out.print(me.getKey() + ":");
          System.out.println(me.getValue());
      }
    }
}
