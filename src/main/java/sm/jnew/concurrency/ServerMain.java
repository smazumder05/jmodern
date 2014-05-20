/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.jnew.concurrency;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.io.FiberServerSocketChannel;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author smazumder
 */
public class ServerMain {

    static final int PORT = 4050;
    static final Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {

        //Create a new fiber for the socket channel.
        new Fiber(() -> {
            try {
                System.out.println("Starting server.");
                FiberServerSocketChannel sockServer = FiberServerSocketChannel.open().bind(new InetSocketAddress(PORT));
                for(;;) {//loop forever and listen for incomming requests
                    FiberSocketChannel sock = sockServer.accept();
                    //Create another fiber and handover the request
                    new Fiber( () -> {
                        System.out.println("Handing over request to new fiber");
                        try {
                            ByteBuffer buf = ByteBuffer.allocateDirect(1024);
                            int n = sock.read(buf);
                            String response = "HTTP/1.0 200 OK\r\nDate: Fri, 31 Dec 1999 23:59:59 GMT\r\n" 
                                              + "Content-Type: text/html\r\nContent-Length: 0\r\n\r\n";
                            
                            sock.write(charset.newEncoder().encode(CharBuffer.wrap(response)));
                            sock.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }).start();
                    
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();
        System.out.println("Socket server started...");
        Thread.sleep(Long.MAX_VALUE);

    }

}
