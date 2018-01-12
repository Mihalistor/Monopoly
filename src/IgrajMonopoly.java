
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bruno
 */
public class IgrajMonopoly {

    Mapa m = Mapa.getInstance();
    GeneratorBrojeva gb = GeneratorBrojeva.getInstance();

    public void igraj() {
        int igrac = 1;
        int pozicija = 0;
        boolean igra = true;
        while (true) {
            //baci kockicu
            int kockica = gb.baciKockicu();
            System.out.println("IGRA IGRAC: " + igrac + " -> KOCKICA: " + kockica);

            //Provjeri ako nije završio cijeli krug i krenuo u novi
            pozicija = provjeriPoziciju(pozicija, kockica);

            //provjeri status polja te kupi ili prodaj
            Polje polje = m.getMapa().get(pozicija);
            provjeriPolje(polje, igrac);

            //sljedeći igrač
            if (kockica != 6) {
                if (igrac == 1) {
                    igrac = 2;
                } else {
                    igrac = 1;
                }
            }

            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(IgrajMonopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int provjeriPoziciju(int pozicija, int kockica) {
        if (pozicija + kockica >= 32) {
            kockica -= (32 - pozicija);
            pozicija = 0;
            System.out.println("NOVI KRUG");
            //todo agentu povecati novac za 5000
        } else {
            pozicija += kockica;
        }
        return pozicija;
    }

    public void provjeriPolje(Polje polje, Integer id) {
        System.out.println("POLJE: " + polje.getNaziv());
        if (polje.getIdGrupe() != null) {
            if (polje.getIdVlasnika() == null) {
                if (provjeriNovcanik(polje, id)) {
                    polje.setIdVlasnika(id);
                    System.out.println("KUPIO SAM MJESTO: " + polje.getNaziv());
                }
            } else if (polje.getIdVlasnika() == id) {
                System.out.println("POSJETIO SAM KUPLJENO MJESTO: " + polje.getNaziv());
            } else {
                System.out.println("PLATI KAZNU OD: " + polje.getIznosNaplate() + ", IGRAČU: " + polje.getIdVlasnika());
            }
        } else {
            provjeriNeMjesto(polje, id);
        }
    }

    public void provjeriNeMjesto(Polje polje, Integer id) {
        switch (polje.getNaziv()) {
            case "Sansa":
                Sanse sansa = dajSansu(m.getListaSansi());
                izvrsiSansu(sansa, id);
                System.out.println(sansa.getOpis());
                break;
            case "Preskok":
                break;
            case "Zatvor":
                break;
            case "Pokupi":
                break;
        }
    }

    public Boolean provjeriNovcanik(Polje polje, Integer id) {
        int postotak = gb.postotak();
        if (postotak > 80) {
            System.out.println("NEMAM NOVACA");
            return false;
        }
        return true;
    }

    public Sanse dajSansu(List<Sanse> lista) {
        int brojSanse = gb.dajRandomSansu();
        Sanse s = lista.get(brojSanse);
        return s;
    }

    public void izvrsiSansu(Sanse sansa, Integer id) {
        switch (sansa.getIdSanse()) {
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

}
