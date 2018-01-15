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
public class Kupujem367 extends AgentIgrac {

    @Override
    public Boolean strategija(Polje polje) {
        // strategija: kupujem samo grupe 3,6,7
        System.out.println("STRATEGIJA: kupujem samo grupe 3,6 i 7");
        if (polje.getIdGrupe() == 3 || polje.getIdGrupe() == 6 || polje.getIdGrupe() == 7) {
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
