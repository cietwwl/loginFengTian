package fileUpload;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
  
  
/** 
 * 接收文件服务 
 * @author admin_Hzw 
 * 
 */  
public class Server {  
      
    /** 
     * 工程main方法 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            final ServerSocket server = new ServerSocket(8080);  
            Socket socket = server.accept();  
            receiveFile(socket);
        } catch (Exception e) {  
            e.printStackTrace();  
        }       
    }  
  
    public void run() {  
    }  
  
    /** 
     * 接收文件方法 
     * @param socket 
     * @throws IOException 
     */  
    public static void receiveFile(Socket socket) throws IOException {  
        try {  
            try {  
             // 创建一个输入流和输出流
        		InputStream fis = socket.getInputStream();
        		OutputStream fos = new FileOutputStream("d:/iocopy3.jpg");	
        		BufferedInputStream bis = new BufferedInputStream(fis);
        		BufferedOutputStream bos = new BufferedOutputStream(fos);
        		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        		// 使用输入流和输出流完成文件复制
        		// 中转站
        		byte[] buf = new byte[4096];
        		// 通过循环完成文件复制
        		int len = bis.read(buf);
        		dos.writeUTF("写入成功!");
        		while (len != -1) {
        			// 写
        			bos.write(buf, 0, len);
        			// 继续读
        			len = bis.read(buf);
        		}
        		
        		System.out.println("dddd");
        		//bos.flush();//手动的刷新
        		// 关闭输入流和输出流
        		bis.close();
        		bos.close();
        		fis.close();
        		fos.close();
            } finally {
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  