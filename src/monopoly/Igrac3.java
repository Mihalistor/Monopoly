package monopoly;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
public class Igrac3 extends Agent {

    private Integer idIgraca = 3;
    private long novci = 15000;
    private Integer pozicija = 0;
    private List<Polje> vlastitaMjesta = new ArrayList<>();

    Mapa m = Mapa.getInstance();
    GeneratorBrojeva gb = GeneratorBrojeva.getInstance();

    protected void setup() {
        MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive(query);
                if (msg != null) {
                    System.out.println("My name is: " + getAID().getLocalName());
                    System.out.println("My id is: " + idIgraca);
                    addBehaviour(new IgrajMonopoly(myAgent, msg));
                }
                block();
            }
        });
    }

    public class IgrajMonopoly extends SequentialBehaviour {

        ACLMessage msg;

        public IgrajMonopoly(Agent a, ACLMessage msg) {
            super(a);
            this.msg = msg;
        }

        @Override
        public void onStart() {
            System.out.println("citam poruku: " + msg.getContent());
            System.out.println("posiljatelj: " + msg.getSender().getLocalName());
            tvojPotez();
        }

        public void tvojPotez() {
            int kockica = gb.baciKockicu();
            System.out.println("IGRA IGRAC: " + getAID().getLocalName() + " -> KOCKICA: " + kockica);
            pozicija = provjeriPoziciju(pozicija, kockica);
            Polje polje = m.getMapa().get(pozicija);
            provjeriPolje(polje, idIgraca);
            if (kockica != 6) {
                sljedeciIgrac();
            } else {
                tvojPotez();
            }
        }

        public void sljedeciIgrac() {
            System.out.println("sljedeci igrac moze igrati -> Bruno");
            ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
            poruka.addReceiver(new AID("Bruno", AID.ISLOCALNAME));
            poruka.setContent("ovo je tekst od Stef");
            System.out.println("-----------------------------------------------");
            try {
                sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Igrac1.class.getName()).log(Level.SEVERE, null, ex);
            }
            send(poruka);

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
            novci += 5000;
            System.out.println("Igrac sada ima ukupno: " + novci);
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
            if(novci < polje.getCijena()){
                System.out.println("NEMAM NOVACA");
                System.out.println("Stanje na racunu: " + novci);
                return false;
            }
            return true;
        }

        public void kupiMjesto(Polje polje, Integer id) {
            polje.setIdVlasnika(id);
            vlastitaMjesta.add(polje);
            System.out.println("KUPIO SAM MJESTO: " + polje.getNaziv());
        }

        public void posjetiSvojeMjesto(Polje polje) {
            System.out.println("POSJETIO SAM KUPLJENO MJESTO: " + polje.getNaziv());
        }

        public void platiKaznu(Polje polje, Integer id) {
            //todo posalji novac vlasniku
            if(novci<polje.getIznosNaplate()){
                System.out.println("BANKROT - IZGUBIO SI");
            } else {
                novci -= polje.getIznosNaplate();
                System.out.println("PLATI KAZNU OD: " + polje.getIznosNaplate() + ", IGRAČU: " + polje.getIdVlasnika());
            }
            
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
                    novci += sansa.getVrijednost();
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

    public Integer getIdIgraca() {
        return idIgraca;
    }

    public void setIdIgraca(Integer idIgraca) {
        this.idIgraca = idIgraca;
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
