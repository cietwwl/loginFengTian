package com.zxz.rpcclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;


public class RpcClient2 {

	
//	public static final String HOST = "127.0.0.1";
	public static final String HOST = "123.57.185.23";
	public static final int PORT = 9999;
	
	
	
	public static <T> T getService(final Class<T> interfaceClass,final String packagePath){
		try {
			return refer(interfaceClass, packagePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * @param interfaceClass
     * @param packagePath
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T refer(final Class<T> interfaceClass,final String packagePath) throws Exception {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
            	final Socket socket = new Socket(HOST, PORT);
//            	System.out.println("..............");
                try {
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    try {
                        output.writeUTF(method.getName());
                        output.writeUTF(packagePath);
                        output.writeObject(method.getParameterTypes());
                        output.writeObject(arguments);
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try {
                            Object result = input.readObject();
                            if (result instanceof Throwable) {
                                throw (Throwable) result;
                            }
                            return result;
                        } finally {
                            input.close();
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    socket.close();
                }
            }
        });
    }
	
}
