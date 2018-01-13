package monopoly;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Random;

/**
 *
 * @author bruno
 */
public class GeneratorBrojeva {
    private static GeneratorBrojeva instance = new GeneratorBrojeva();
    public static int sjeme;
    Random generator = new Random();

    private GeneratorBrojeva() {
    }
    
    public static GeneratorBrojeva getInstance(){
        if (instance == null) {
            instance = new GeneratorBrojeva();
        }
        return instance;
    }
    
    public int baciKockicu(){
        int slucajniBroj = generator.nextInt(6) + 1;
        return slucajniBroj;
    }

    public int postotak() {
        int slucajniBroj = generator.nextInt(100) + 1;
        return slucajniBroj%2;
    }
    
    public int dajRandomSansu(){
       int slucajniBroj = generator.nextInt(22);
       return slucajniBroj; 
    }
}
