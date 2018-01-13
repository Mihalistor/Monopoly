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
    
    public static Integer novci = 0;
    
    protected void setup() {
        Mapa mapa = Mapa.getInstance();
        mapa.postaviMapu();
        System.out.println("MONOPOLY: " + getAID().getLocalName());
        System.out.println("POCETAK IGRE");
        ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
        poruka.addReceiver(new AID("Pero", AID.ISLOCALNAME));
        poruka.setContent("Igra moze poceti");
        send(poruka);
        System.out.println("-----------------------------------------------");
        
    }
    
}
