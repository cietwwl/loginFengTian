package fileUpload;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
  
/** 
 * 鏂囦欢鍙戦�瀹㈡埛绔富绋嬪簭 
 * @author admin_Hzw 
 * 
 */  
public class Client {  
      
    /** 
     * 绋嬪簭main鏂规硶 
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
        int length = 0;  
        double sumL = 0 ;  
        Socket socket = null;  
        DataOutputStream dos = null;  
        FileInputStream fis = null;  
        DataInputStream dis = null;
        boolean bool = false;  
        try {  
            File file = new File("D:/copy.jpg"); //瑕佷紶杈撶殑鏂囦欢璺緞  
            long fileLength = file.length();   
            socket = new Socket();    
            socket.connect(new InetSocketAddress("127.0.0.1", 8080));  
            dos = new DataOutputStream(socket.getOutputStream());  
            fis = new FileInputStream(file);        
            byte[] sendBytes = new byte[1024];    
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
                sumL += length;    
                System.out.println("宸蹭紶杈擄細"+((sumL/fileLength)*100)+"%");  
                dos.write(sendBytes, 0, length);  
                dos.flush();  
            }   
            
            dis = new DataInputStream(socket.getInputStream());
            String readUTF = dis.readUTF();
            System.out.println("鏈嶅姟鍣ㄨ繑鍥炵殑淇℃伅鏄�"+readUTF);
            
            if(sumL==fileLength){  
                bool = true;  
            }  
        }catch (Exception e) {  
            System.out.println("瀹㈡埛绔枃浠朵紶杈撳紓甯�");  
            bool = false;  
            e.printStackTrace();    
        } finally{    
            if (dos != null)  
                dos.close();  
            if (fis != null)  
                fis.close();     
            if (socket != null)  
                socket.close();      
        }  
        System.out.println(bool?"鎴愬姛":"澶辫触");  
    }  
}  