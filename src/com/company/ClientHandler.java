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
//                String createFilePath=ournewDataInputstream.readUTF();
                recieveFile(path, mynewSocket, ournewDataInputstream, ournewDataOutputstream);
                compareFiles(path, mynewSocket);
                printTimeDetails(fileNumber);
                long startTime = System.currentTimeMillis();
                fileTime.put(String.format("file number %s", fileNumber + 1),System.currentTimeMillis() - startTime);
                fileNumber++;

            } catch (Exception e) {
//                e.printStackTrace();
            }
//            try {
//                // closing resources
//                this.ournewDataInputstream.close();
//                this.ournewDataOutputstream.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

    public void recieveFile(String path, Socket socket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream) throws IOException {
//        byte[] contents = new byte[10000];
//        //Initialize the FileOutputStream to the output file's full path.
////        if(ournewDataInputstream.readUTF().equals("Start"))
////        {
//        FileOutputStream fos = new FileOutputStream(path);
//        BufferedOutputStream bos = new BufferedOutputStream(fos);
//        InputStream is = socket.getInputStream();
//        //No of bytes read in one read() call
//        int bytesRead = 0;
//        while ((bytesRead = is.read(contents)) != -1&&!ournewDataInputstream.readUTF().equals("Exit")) {
//            bos.write(contents, 0, bytesRead);
//        }
//        bos.flush();
//    }
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
        fileNumber++;
//            System.out.println("File " + filename
//                    + " downloaded (" + current + " bytes read)");
        } finally {

        }
//        finally {
//            if (fos != null) fos.close();
//            if (bos != null) bos.close();
//            if (socket != null) socket.close();
//        }
    }
  public  void compareFiles(String path, Socket client) throws IOException {
        //ارجع كل الفايلات الي عندي
    /*  List<Path>s=Files.list(Paths.get("")).toList();
      for (Path e:s) {
          System.out.println(e);
      }*/
      List<Path>s=Files.list(Paths.get("")).toList();
      File serverfile =null;
      File exist = new File(path);
      for (Path e:s) {
          serverfile=new File(e.toString());
          if(serverfile.compareTo(exist)==0)
          {
              if(Files.mismatch(serverfile.toPath(),exist.toPath())==-1)
              {
               System.out.println(Files.mismatch(e, exist.toPath()));
                  acceptedFilesWthServer++;}
          }
          else
              refusedFilesWthServer++;
//          if(Files.mismatch(serverfile.toPath(), exist.toPath())==-1)
//          {  System.out.println(Files.mismatch(e, exist.toPath()));
//         acceptedFilesWthServer++;
//          break;
//          }
//          else
//          { refusedFilesWthServer++;}

      }
//      File serverfile =new File("tcpServer1.txt");
//      File exist = new File(path);
//        System.out.println(Files.mismatch(serverfile.toPath(), exist.toPath()));
//        long res = Files.mismatch(serverfile.toPath(), exist.toPath());
//        if (res == -1)
//            acceptedFilesWthServer++;
//         else
//            refusedFilesWthServer++;
    }
  public  void printTimeDetails(int num) {
        long tim = System.currentTimeMillis() - this.Time;
        System.out.println("total time for " + num + " is = " + tim);
        System.out.println("average time for " + num + " is = " + tim / num);
        System.out.println("number of refused files is = " + refusedFilesWthServer);
        System.out.println("number of accepted files is = " + acceptedFilesWthServer);
    }
}
