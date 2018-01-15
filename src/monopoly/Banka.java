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
import jade.wrapper.StaleProxyException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
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
    public static int brojKrugova = 0;
    Mapa m = Mapa.getInstance();

    protected void setup() {
        Mapa mapa = Mapa.getInstance();
        mapa.postaviMapu();
        System.out.println("MONOPOLY: " + getAID().getLocalName());
        System.out.println("Cekam 7 sekundi na prijavu igraca...");
        MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
        int counter = 0;
        while (counter < 7) {
            ACLMessage msg = receive(query);
            if (msg != null) {
                System.out.println("Dosla je prijava: " + msg.getContent());
                String naziv = msg.getContent().substring(msg.getContent().indexOf(":") + 2, msg.getContent().lastIndexOf(","));
                String kockica = msg.getContent().substring(msg.getContent().lastIndexOf(":") + 2, msg.getContent().length());
                igrac.add(kockica + naziv);
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Banka.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }
        Collections.sort(igrac, Collections.reverseOrder());
        int broj = igrac.size();
        int i = 0;
        while(i <= broj-1){
            igrac.add(igrac.get(0).substring(1, igrac.get(0).length())); 
            igrac.remove(0);
            i++;
        }
        System.out.println("Broj prijavljenih igrača: " + igrac.size());
        brojKrugova = igrac.size() * 5;
        System.out.println("Redosljed igre: ");
        int pozicija = 1;
        for(String igraci : igrac){
            System.out.println(pozicija + ". " + igraci);
            pozicija++;
        }
        System.out.println("POCETAK IGRE");
        ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
        poruka.addReceiver(new AID(igrac.get(trenutniIgrac), AID.ISLOCALNAME));
        poruka.setContent("Igra moze poceti");
        send(poruka);
        System.out.println("------------------------------------------------------------------------");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive(query);
                if (msg != null) {
                    if (msg.getContent().contains("Pobjednik")) {
                        System.out.println("OBAVIJEST BANKE: " + msg.getContent() + ": " + msg.getSender().getLocalName());
                        System.out.println("KRAJ IGRE");
                        doDelete();
                        System.exit(0);
                    }
                }
                block();
            }
        });

    }

}
