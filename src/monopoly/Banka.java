/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Banka extends Agent {

    public static Integer novci = 0;
    public static List<String> igrac = new ArrayList<>();
    public static int trenutniIgrac = 0;

    protected void setup() {
        Mapa mapa = Mapa.getInstance();
        mapa.postaviMapu();
        System.out.println("MONOPOLY: " + getAID().getLocalName());
        System.out.println("Cekam 7 sekundi na prijavu igraca...");
        // cekaj prijave
        MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
        int counter = 0;
        while (counter < 7) {
            ACLMessage msg = receive(query);
            if (msg != null) {
                System.out.println("Dosla je prijava: " + msg.getContent());
                igrac.add(msg.getContent().substring(msg.getContent().lastIndexOf(" ") + 1, msg.getContent().length()));
                System.out.println("size: " + igrac.size());
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Banka.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }

        System.out.println("Broj prijavljenih igrača: " + igrac.size());
        System.out.println("POCETAK IGRE");
        ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
        poruka.addReceiver(new AID(igrac.get(trenutniIgrac), AID.ISLOCALNAME));
        poruka.setContent("Igra moze poceti");
        send(poruka);
        System.out.println("------------------------------------------------------------------------");

    }

}
