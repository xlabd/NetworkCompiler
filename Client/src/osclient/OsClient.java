/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Umesh Ramchandani
 */
public class OsClient {
    
    public final static int SOCKET_PORT = 3000;      // you may change this
    public final static String SERVER = "127.0.0.1";  // localhost
    public final static String FILE_TO_SEND = "C:\\Users\\Umesh Ramchandani\\Desktop\\OS\\"; 
    private static Socket socket;
    public final static int FILE_SIZE = 6022386;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        try{
            String host = "localhost";
            int port = 3000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            System.out.println("Enter the filename");
            Scanner sc = new Scanner(System.in);
            String fileName = sc.next();
            File myFile = new File (FILE_TO_SEND+fileName);
            byte mybytearray[]  = new byte [(int)myFile.length()];
            String sendMessage = fileName+" "+myFile.length()+"\n";
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(sendMessage);
            bw.flush();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            Scanner br = new Scanner(isr);
            int status = br.nextInt();
            if( status == 200){
                System.out.println("Successfull");
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream osf = null;
                try{
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    osf = socket.getOutputStream();
                    System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                    osf.write(mybytearray,0,mybytearray.length);
                    osf.flush();
                    System.out.println("Done.");
                    System.out.println(socket.isClosed());
                    //New Class file
                System.out.println(socket.isClosed());
                int bytesRead;
                int current = 0;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                byte [] classarray  = new byte [FILE_SIZE];
                fos = new FileOutputStream(FILE_TO_SEND+"Test.class");
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(classarray,0,classarray.length);
                current = bytesRead;
                System.out.println(bytesRead);
                do {
                   bytesRead =
                      is.read(classarray, current, (classarray.length-current));
                   if(bytesRead >= 0) current += bytesRead;
                   System.out.println(bytesRead);
                } while(bytesRead > -1);

                bos.write(classarray, 0 , current);
                bos.flush();
                System.out.println("File " + FILE_TO_SEND+fileName
                    + " downloaded (" + current + " bytes read)");
                //New ends
                }
                finally{
                    
                    if (bis != null) bis.close();
                    if (osf != null) osf.close();
//                    if (socket!=null) socket.close();
                }
                
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        finally{
            try
            {
                System.out.println("Here");
                socket.close();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }
    }
    
}
