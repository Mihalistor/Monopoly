
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class IgrajMonopoly {

    Mapa m = Mapa.getInstance();
    GeneratorBrojeva gb = GeneratorBrojeva.getInstance();
    Integer igrac = 1;
    Integer pozicija = 0;

    public void igraj() {
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
            sljedeciIgrac(kockica);

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
            noviKrug();
        } else {
            pozicija += kockica;
        }
        return pozicija;
    }

    public void noviKrug() {
        System.out.println("NOVI KRUG");
        //daj novce za novi krug - 5000
    }

    public void sljedeciIgrac(Integer kockica) {
        if (kockica != 6) {
            if (igrac == 1) {
                igrac = 2;
            } else {
                igrac = 1;
            }
        }
    }

    public void provjeriPolje(Polje polje, Integer id) {
        System.out.println("POLJE: " + polje.getNaziv());
        if (polje.getIdGrupe() != null) {
            provjeriMjesto(polje, id);
        } else {
            provjeriNeMjesto(polje, id);
        }
    }

    public void provjeriMjesto(Polje polje, Integer id) {
        if (polje.getIdVlasnika() == null) {
            if (provjeriNovcanik(polje, id)) {
                kupiMjesto(polje, id);
            }
        } else if (polje.getIdVlasnika() == id) {
            posjetiSvojeMjesto(polje);
        } else {
            platiKaznu(polje, id);
        }
    }
    
    public Boolean provjeriNovcanik(Polje polje, Integer id) {
        //todo provjeri cijenu polja i kolicinu novca igraca pa vrati
        int postotak = gb.postotak();
        if (postotak > 80) {
            System.out.println("NEMAM NOVACA");
            return false;
        }
        return true;
    }

    public void kupiMjesto(Polje polje, Integer id) {
        //todo kreiraj algoritam za kupnju ovisno o agentu
        polje.setIdVlasnika(id);
        System.out.println("KUPIO SAM MJESTO: " + polje.getNaziv());
    }

    public void posjetiSvojeMjesto(Polje polje) {
        System.out.println("POSJETIO SAM KUPLJENO MJESTO: " + polje.getNaziv());
    }

    public void platiKaznu(Polje polje, Integer id) {
        //todo provjeri kolicinu novca i posalji novac vlasniku
        //todo smanji kolicinu novca - ako je negativna banktor i poraz igraca
        System.out.println("PLATI KAZNU OD: " + polje.getIznosNaplate() + ", IGRAČU: " + polje.getIdVlasnika());
    }

    public void provjeriNeMjesto(Polje polje, Integer id) {
        switch (polje.getNaziv()) {
            case "Sansa":
                sansa(id);
                break;
            case "Preskok":
                preskok();
                break;
            case "Zatvor":
                zatvor();
                break;
            case "Pokupi":
                pokupiNovce();
                break;
        }
    }

    public void sansa(Integer id) {
        Sanse sansa = dajSansu(m.getListaSansi());
        izvrsiSansu(sansa, id);
        System.out.println(sansa.getOpis());
    }
    
    public Sanse dajSansu(List<Sanse> lista) {
        int brojSanse = gb.dajRandomSansu();
        Sanse s = lista.get(brojSanse);
        return s;
    }

    public void izvrsiSansu(Sanse sansa, Integer id) {
        switch (sansa.getIdSanse()) {
            case 1:
                System.out.println(sansa.getOpis());
                pozicija += sansa.getVrijednost();
                break;
            case 2:
                System.out.println(sansa.getOpis());
                //todo plati ili podigni lovu
                break;
        }
    }

    public void preskok() {
        System.out.println("Cekaj 1 krug");
    }

    public void zatvor() {
        System.out.println("U zatvoru si. Cekaj 3 kruga");
    }

    public void pokupiNovce() {
        System.out.println("Pokupi lovu iz banke");
    }
}
