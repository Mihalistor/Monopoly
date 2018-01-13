package monopoly;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Igrac1 extends Agent {

    private long novci = 15000;
    private Integer pozicija = 0;
    Integer stop = 0;
    private List<Polje> vlastitaMjesta = new ArrayList<>();
    Integer brojacSestica = 0;

    Mapa m = Mapa.getInstance();
    GeneratorBrojeva gb = GeneratorBrojeva.getInstance();

    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            public void action(){
                addBehaviour(new PrijaviSe());
            }         
        });
        MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive(query);
                if (msg != null) {
                    if (msg.getContent().contains("Naplata")) {
                        addBehaviour(new PrihvatiNaplatu(msg));
                    } else {
                        System.out.println("Ja sam: " + getAID().getLocalName() + ". Na računu imam: " + novci);
                        addBehaviour(new IgrajMonopoly());
                    }
                }
                block();
            }
        });
    }
    
    public class IgrajMonopoly extends SequentialBehaviour {

        public IgrajMonopoly() {
        }

        @Override
        public void onStart() {
            if (stop == 0) {
                int kockica = gb.baciKockicu();
                if (kockica == 6) {
                    brojacSestica++;
                }
                if (brojacSestica <= 3) {
                    System.out.println("Bacio sam kockicu i dobio: " + kockica);
                    pozicija += kockica;
                    pozicija = provjeriPoziciju();
                    tvojPotez();
                    if (kockica != 6) {
                        sljedeciIgrac();
                    } else {
                        onStart();
                    }
                } else {
                    brojacSestica = 0;
                    sljedeciIgrac();
                }
            } else {
                System.out.println("Pauziram jos: " + stop + " krug/a");
                stop--;
                sljedeciIgrac();
            }
        }

        public void tvojPotez() {
            Polje polje = m.getMapa().get(pozicija);
            provjeriPolje(polje);
        }

        public int provjeriPoziciju() {
            if (pozicija >= 32) {
                pozicija = pozicija - 32;
                noviKrug();
            }
            return pozicija;
        }

        public void noviKrug() {
            novci += 5000;
            System.out.println("Krecem u novi krug. Sada imam ukupno: " + novci);
        }

        public void provjeriPolje(Polje polje) {
            if (polje.getIdGrupe() != null) {
                provjeriMjesto(polje);
            } else {
                provjeriNeMjesto(polje);
            }
        }

        public void provjeriMjesto(Polje polje) {
            if (polje.getImeVlasnika() == null) {
                System.out.println("Dosao sam na: " + polje.getNaziv() + ". Cijena: " + polje.getCijena() + " - nema vlasnika");
                if (provjeriNovcanik(polje)) {
                    kupiMjesto(polje);
                }
            } else if (polje.getImeVlasnika().equals(dajIme())) {
                System.out.println("Dosao sam na: " + polje.getNaziv() + ". Cijena: " + polje.getCijena() + " - vlasnik: " + polje.getImeVlasnika());
                posjetiSvojeMjesto(polje);
            } else {
                System.out.println("Dosao sam na: " + polje.getNaziv() + ". Cijena: " + polje.getCijena() + " - vlasnik: " + polje.getImeVlasnika());
                platiPosjetu(polje);
            }
        }

        public Boolean provjeriNovcanik(Polje polje) {
            if (novci < polje.getCijena()) {
                System.out.println("Nemam novaca za kupnju!");
                return false;
            }
            return true;
        }

        public void kupiMjesto(Polje polje) {
            novci -= polje.getCijena();
            polje.setImeVlasnika(dajIme());
            vlastitaMjesta.add(polje);
            System.out.println("Kupio sam mjesto: " + polje.getNaziv());
            System.out.println("Novo stanje na racunu: " + novci);
        }

        public void posjetiSvojeMjesto(Polje polje) {
            System.out.println("Posjetio sam svoje mjesto: " + polje.getNaziv());
        }

        public void platiPosjetu(Polje polje) {
            if (novci < polje.getIznosNaplate()) {
                System.out.println("Stanje na racunu: " + novci);
                bankrot();
            } else {
                novci -= polje.getIznosNaplate();
                System.out.println("Platio sam posjetu od: " + polje.getIznosNaplate() + ", igraču: " + polje.getImeVlasnika());
                System.out.println("Novo stanje na racunu: " + novci);
                posaljiNovce(polje.getImeVlasnika(), "Naplata " + Integer.toString(polje.getIznosNaplate()));
            }
        }

        public void posaljiNovce(String vlasnik, String iznos) {
            ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
            poruka.addReceiver(new AID(vlasnik, AID.ISLOCALNAME));
            poruka.setContent(iznos);
            send(poruka);
        }

        public void provjeriNeMjesto(Polje polje) {
            switch (polje.getNaziv()) {
                case "Sansa":
                    sansa();
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

        public void sansa() {
            int brojSanse = gb.dajRandomSansu();
            Sanse sansa = m.getListaSansi().get(brojSanse);
            izvrsiSansu(sansa);
        }

        public void izvrsiSansu(Sanse sansa) {
            switch (sansa.getIdSanse()) {
                case 1:
                    System.out.println("SANSA: " + sansa.getOpis());
                    pozicija += sansa.getVrijednost();
                    provjeriPoziciju();
                    tvojPotez();
                    break;
                case 2:
                    System.out.println("SANSA: " + sansa.getOpis());
                    if (sansa.getVrijednost() < 0) {
                        Banka.novci += Math.abs(sansa.getVrijednost());
                        novci -= Math.abs(sansa.getVrijednost());
                        if (novci <= 0) {
                            System.out.println("Novo stanje na racunu: " + novci);
                            bankrot();
                        }
                    } else {
                        novci += sansa.getVrijednost();
                        System.out.println("Novo stanje na racunu: " + novci);
                    }                  
                    break;
            }
        }

        public void bankrot() {
            System.out.println("BANKROTIRAO");
            Banka.igrac.remove(Banka.trenutniIgrac);
            Banka.trenutniIgrac--;
        }

        public void preskok() {
            System.out.println("Dosao sam na PRESKOK. Cekam jedan krug");
            brojacSestica = 0;
            stop = 1;
        }

        public void zatvor() {
            System.out.println("Dosao sam u ZATVOR. Cekam tri kruga");
            brojacSestica = 0;
            stop = 3;
        }

        public void pokupiNovce() {
            System.out.println("Dosao sam do PARKINGA. Uzimam novce od kazni: " + Banka.novci);
            novci += Banka.novci;
            System.out.println("Novo stanje na racunu: " + novci);
            Banka.novci = 0;
        }

        public void sljedeciIgrac() {
            System.out.println("gotov sam -> moze sljedeci");
            ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
            if(Banka.trenutniIgrac >= Banka.igrac.size()-1){
                Banka.trenutniIgrac = 0;
            } else {
                Banka.trenutniIgrac++;
            }
            String sljedeciIgrac = Banka.igrac.get(Banka.trenutniIgrac);
            poruka.addReceiver(new AID(sljedeciIgrac, AID.ISLOCALNAME));
            poruka.setContent(sljedeciIgrac + ", ja sam zavrsio. Mozes bacati kockicu. " + dajIme());
            System.out.println("------------------------------------------------------------------------");
            try {
                sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Igrac1.class.getName()).log(Level.SEVERE, null, ex);
            }
            send(poruka);
        }
    }
    
    public class PrihvatiNaplatu extends SequentialBehaviour {
        
        ACLMessage msg;

        public PrihvatiNaplatu(ACLMessage msg) {
            this.msg = msg;
        }
        
        @Override
        public void onStart() {
           Integer iznos = Integer.parseInt(msg.getContent().substring(msg.getContent().lastIndexOf(" ")+1, msg.getContent().length())); 
           novci += iznos;
        }   
    }
    
    public class PrijaviSe extends SequentialBehaviour {

        @Override
        public void onStart() {
            ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
            poruka.addReceiver(new AID("Banka", AID.ISLOCALNAME));
            poruka.setContent("Prijavljujem se u igru: " + dajIme());
            send(poruka);
        } 
    }
    
     public String dajIme() {
            return getAID().getLocalName();
        }

    public long getNovci() {
        return novci;
    }

    public void setNovci(long novci) {
        this.novci = novci;
    }

    public Integer getPozicija() {
        return pozicija;
    }

    public void setPozicija(Integer pozicija) {
        this.pozicija = pozicija;
    }

    public List<Polje> getVlastitaMjesta() {
        return vlastitaMjesta;
    }

    public void setVlastitaMjesta(List<Polje> vlastitaMjesta) {
        this.vlastitaMjesta = vlastitaMjesta;
    }
}
