
package osserver;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Umesh Ramchandani
 */
public class OsServer {

    /**
     * @param args the command line arguments
     */
    
    public final static int SOCKET_PORT = 3000;
    private static Socket socket;
    public final static String FILE_TO_RECEIVED = "C:\\Users\\Umesh Ramchandani\\Documents\\NetBeansProjects\\OsServer\\"; 
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        try{
            ServerSocket serverSocket = new ServerSocket(SOCKET_PORT);
            System.out.println("Server Started and listening to the port "+SOCKET_PORT);
            while(true)
            {
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
                Scanner sc = new Scanner(isr);
                String fileName = sc.next();
                int size = sc.nextInt();
                System.out.println("Message received from client is "+fileName+size);
                String returnMessage = 200 + "\n";
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage);
                System.out.println("Message sent to the client is "+returnMessage);
                bw.flush();
                while(true){
                    int bytesRead;
                    int current = 0;
                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    System.out.println("Deadlock");
                    System.out.println(socket.isClosed());
                    try{
                        byte [] mybytearray  = new byte [size];
                        fos = new FileOutputStream(FILE_TO_RECEIVED+fileName);
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        current = bytesRead;
//                        System.out.println(bytesRead);
                        do {
                           bytesRead =
                              is.read(mybytearray, current, (mybytearray.length-current));
                           if(bytesRead >= 0) current += bytesRead;
//                           System.out.println(bytesRead);
                        } while(bytesRead > 0);
                        
                        bos.write(mybytearray, 0 , current);
                        bos.flush();
                        System.out.println("File " + FILE_TO_RECEIVED+fileName
                            + " downloaded (" + current + " bytes read)");
                        Process p=Runtime.getRuntime().exec("cmd /c javac "+fileName); 
                        p.waitFor(); 
                        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
                        String line=reader.readLine(); 
                        while(line!=null) 
                        { 
                        System.out.println(line); 
                        line=reader.readLine(); 
                        }
                        //Send class file back
                        while(true){
                            System.out.println(socket.isClosed());
                            FileInputStream fis = null;
                            BufferedInputStream bis = null;
                            OutputStream osf = null;
                            try{
                                File myFile = new File (FILE_TO_RECEIVED+"Test.class");
                                fis = new FileInputStream(myFile);
                                byte mybytearray1[]  = new byte [(int)myFile.length()];
                                bis = new BufferedInputStream(fis);
                                bis.read(mybytearray1,0,mybytearray1.length);
                                osf = socket.getOutputStream();
                                System.out.println("Sending " + FILE_TO_RECEIVED + "(" + mybytearray1.length + " bytes)");
                                osf.write(mybytearray1,0,mybytearray1.length);
                                osf.flush();
                                System.out.println("Done.");
                            }
                            finally{
                                if (bis != null) bis.close();
                                if (osf != null) osf.close();
    //                            if (socket!=null) socket.close();
                            }
                            break;
                        }
                        
                        //Sending Ends
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    finally{
                        if (fos != null) fos.close();
                        if (bos != null) bos.close();
                    }
                    break;
                }
                
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        finally{
            try
            {
                socket.close();
            }
            catch(IOException e){}
        }
        
    }
    
}
