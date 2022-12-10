package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Calendar;

public class ClientHandler extends Thread {
    final DataInputStream ournewDataInputstream;
    final DataOutputStream ournewDataOutputstream;
    final Socket mynewSocket;
    static Calendar now = Calendar.getInstance();
    long Time = 0;
    static int acceptedFiles = 0;
    static int refusedFiles = 0;
    String path = "";
    long res = this.Time;

    public ClientHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream, long time, String path) {
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

            } catch (Exception e) {
                e.printStackTrace();
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

    public void recieveFile(String path, Socket socket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream) throws Exception {
        byte[] contents = new byte[10000];
        //Initialize the FileOutputStream to the output file's full path.
//        if(ournewDataInputstream.readUTF().equals("Start"))
//        {
        FileOutputStream fos = new FileOutputStream(path);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();
        //No of bytes read in one read() call
        int bytesRead = 0;
        while ((bytesRead = is.read(contents)) != -1&&!ournewDataInputstream.readUTF().equals("Exit")) {
            bos.write(contents, 0, bytesRead);
        }
        bos.flush();
    }

//}
  public  void compareFiles(String path, Socket client) throws IOException {
        File serverfile = new File("tcpServer1.txt");
        File exist = new File(path);
        System.out.print(Files.mismatch(serverfile.toPath(), exist.toPath()));
        long res = Files.mismatch(serverfile.toPath(), exist.toPath());
        if (res == -1)
            acceptedFiles++;
         else
            refusedFiles++;
    }
  public  void printTimeDetails(int num) {
        long tim = System.currentTimeMillis() - this.Time;
        System.out.print("total time for " + num + " is = " + tim);
        System.out.print("average time for " + num + " is = " + tim / num);
        System.out.print("number of refused files is = " + refusedFiles);
        System.out.print("number of accepted files is = " + acceptedFiles);
    }
}