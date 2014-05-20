/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.jnew.web;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.actors.ExitMessage;
import co.paralleluniverse.actors.LifecycleMessage;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author smazumder
 */
public class WebMain {

    public static void main(String[] args) throws Exception {
        new ReliableActor("Reliable").spawn();
        Strand.sleep(Long.MAX_VALUE);

    }

    static class WebActor extends BasicActor<String, Void> {

        private int count;

        protected Void doRun() throws InterruptedException, SuspendExecution {
            System.out.println("Starting actor");
            for (;;) {
                String m = receive(300, TimeUnit.MILLISECONDS);
                if (null != m) {
                    System.out.println("Got a message: " + m);
                } else {
                    System.out.println("I am but a lowly actor that sometimes fails: - " + (count++));
                }

                if (ThreadLocalRandom.current().nextInt(30) == 0) {
                    throw new RuntimeException("Something gone wrong here.");
                }

                checkCodeSwap(); //Allow hot code swap for this actor
            }

        }

    }

    /**
     * Here we have a ReliableActor spawning an instance of a WebActor, which
     * occasionally fails. Because our naive actor watches its protege, it will
     * be notified of its untimely death, and re-spawn a new one.
     *
     */
    static class ReliableActor extends BasicActor<Void, Void> {

        private ActorRef<String> webActor; //reference to Webactor

        public ReliableActor(String name) {
            super(name);
        }

        protected Void doRun() throws InterruptedException, SuspendExecution {

            createWebActor();

            int count = 0;

            for (;;) {
                receive(500, TimeUnit.MILLISECONDS);
                webActor.send("Hello from : " + self() + " number " + (count++));
            }

        }

        private void createWebActor() {
            webActor = new WebActor().spawn();
            watch(webActor);
        }

        protected Void handleLifecycleMessage(LifecycleMessage m) {
            if (m instanceof ExitMessage && Objects.equals(((ExitMessage) m).getActor(), webActor)) {
                System.out.println("Web actor just died '" + ((ExitMessage) m).getCause() + "'. Restarting.");
                createWebActor();
            }
            return super.handleLifecycleMessage(m);
        }
       
                    
    }

}
