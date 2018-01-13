/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Banka extends Agent{
    
    protected void setup() {
        Mapa mapa = Mapa.getInstance();
        mapa.postaviMapu();
        System.out.println("My name is: " + getAID().getLocalName());
        System.out.println("POCETAK IGRE");
        ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
        poruka.addReceiver(new AID("Pero", AID.ISLOCALNAME));
        poruka.setContent("ovo je tekst banke za pocetak igre");
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Banka.class.getName()).log(Level.SEVERE, null, ex);
        }
        send(poruka);
        System.out.println("-----------------------------------------------");
        
    }
    
}
