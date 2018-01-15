/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 *
 * @author bruno
 */
public class KupujemSve extends AgentIgrac {

    @Override
    public Boolean strategija(Polje polje) {
            // strategija: kupujem sve na kaj stanem
            System.out.println("STRATEGIJA: kupujem sve");
            if (novci < 2000) {
                return false;
            }
            return true;
    }
    
}
