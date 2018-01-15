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
public class KupujemSkupo extends AgentIgrac {

    @Override
    public Boolean strategija(Polje polje) {
        // strategija: kupujem sve skupo -> od pozicije zatvora do kraja
        System.out.println("STRATEGIJA: kupujem skupo - od zatvora do kraja");
        if (polje.getIdPolja() > 16 && polje.getIdPolja() <= 31) {
            return true;
        }
        if (novci < 2000) {
            return false;
        }
        if (Banka.igrac.size() == 2) {
            return true;
        }
        return false;
    }

}
