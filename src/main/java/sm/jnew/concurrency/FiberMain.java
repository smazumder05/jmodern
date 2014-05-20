/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sm.jnew.concurrency;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

/**
 *
 * @author smazumder
 */
public class FiberMain {
    
    /**
     * 
     * @param args 
     * @throws java.lang.Exception 
     */
    public static void main(String[] args) throws Exception{
       
        //Create a new channel 
        final Channel<Integer> chan = Channels.newChannel(0);
        
        new Fiber<Void>(() -> {
            for(int i=0;i <10; i++) {
                Strand.sleep(100);
                chan.send(i);
            }
            chan.close();
        }).start();
        
        new Fiber<Void>(() -> {
            Integer x;
            while((x = chan.receive()) != null)
                System.out.println("Received from channel: " + x);
                
         }).start().join(); //Join waits for the fiber to finish
    }
    
}
