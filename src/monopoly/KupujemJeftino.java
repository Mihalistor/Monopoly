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
public class KupujemJeftino extends AgentIgrac {

    @Override
    public Boolean strategija(Polje polje) {
        // strategija: kupujem sve jeftino -> od starta do zatvora
        System.out.println("STRATEGIJA: kupujem jeftino - od starta do zatvora");
        if (polje.getIdPolja() > 0 && polje.getIdPolja() < 16) {
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
