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
import co.paralleluniverse.strands.channels.SelectAction;
import static co.paralleluniverse.strands.channels.Selector.receive;
import static co.paralleluniverse.strands.channels.Selector.select;

/**
 *
 * @author smazumder
 */
public class ChannelsMain {

    public static void main(String[] args) throws Exception {
        final Channel<Integer> chan1 = Channels.newChannel(0);
        final Channel<String> chan2 = Channels.newChannel(0);

        //The following two fibers send ints and chars to the two channels
        //respectively.
        new Fiber<Void>(() -> {
            for (int i = 0; i < 10; i++) {
                Strand.sleep(100);
                chan1.send(i);
            }
            chan1.close();

        }).start();

        new Fiber<Void>(() -> {
            for (int i = 0; i < 10; i++) {
                Strand.sleep(100);
                chan2.send(Character.toString((char) ('a' + i)));
            }
            chan2.close();

        }).start();
        //A third fiber acts as a a GO channel receiver

        new Fiber<Void>(() -> {
            for (int i = 0; i < 10; i++) {
                SelectAction<Object> sa = select(receive(chan1),
                        receive(chan2));

                switch (sa.index()) {
                    case 0:
                        System.out.println(sa.message() != null ? "Got a number: " + (int) sa.message() : "ch1 closed");
                        break;
                    case 1:
                        System.out.println(sa.message() != null ? "Got a string: " + (String) sa.message() : "ch1 closed");
                        break;
                        
                }
            }

        }).start().join();

    }

}
