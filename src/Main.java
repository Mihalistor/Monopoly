/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bruno
 */
public class Main {
    public static void main(String[] args) {
        Mapa mapa = Mapa.getInstance();
        mapa.postaviMapu();
        IgrajMonopoly im = new IgrajMonopoly();
        im.igraj();
    }
}
