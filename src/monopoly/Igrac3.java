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
    Integer stop = 0;
    private List<Polje> vlastitaMjesta = new ArrayList<>();
    Integer brojacSestica = 0;

    Mapa m = Mapa.getInstance();
    GeneratorBrojeva gb = GeneratorBrojeva.getInstance();

    protected void setup() {
        MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive(query);
                if (msg != null) {
                    System.out.println("Ja sam: " + getAID().getLocalName() + ". Na računu imam: " + novci);
                    addBehaviour(new IgrajMonopoly());
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
            int kockica = gb.baciKockicu();
            if (kockica == 6) {
                brojacSestica++;
            }
            if (brojacSestica <= 3) {
                tvojPotez(kockica);
            } else {
                brojacSestica = 0;
                sljedeciIgrac();
            }
        }

        public void tvojPotez(Integer kockica) {
            if (stop == 0) {
                System.out.println("Bacio sam kockicu i dobio: " + kockica);
                pozicija = provjeriPoziciju(pozicija, kockica);
                Polje polje = m.getMapa().get(pozicija);
                provjeriPolje(polje, idIgraca);
                if (kockica != 6) {
                    sljedeciIgrac();
                } else {
                    onStart();
                }
            } else {
                System.out.println("Pauziram jos: " + stop + " krug/a");
                stop--;
                sljedeciIgrac();
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
            novci += 5000;
            System.out.println("Krecem u novi krug. Sada imam ukupno: " + novci);
        }

        public void provjeriPolje(Polje polje, Integer id) {
            System.out.println("Dosao sam na: " + polje.getNaziv() + " - vlasnik: " + polje.getIdVlasnika());
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
            if (novci < polje.getCijena()) {
                System.out.println("Nemam novaca za kupnju!");
                return false;
            }
            return true;
        }

        public void kupiMjesto(Polje polje, Integer id) {
            novci -= polje.getCijena();
            polje.setIdVlasnika(id);
            vlastitaMjesta.add(polje);
            System.out.println("Kupio sam mjesto: " + polje.getNaziv());
            System.out.println("Novo stanje na racunu: " + novci);
        }

        public void posjetiSvojeMjesto(Polje polje) {
            System.out.println("Posjetio sam svoje mjesto: " + polje.getNaziv());
        }

        public void platiKaznu(Polje polje, Integer id) {
            if (novci < polje.getIznosNaplate()) {
                bankrot();
            } else {
                novci -= polje.getIznosNaplate();
                System.out.println("Platio sam kaznu od: " + polje.getIznosNaplate() + ", igraču: " + polje.getIdVlasnika());
                System.out.println("Novo stanje na racunu: " + novci);
                //todo posalji novac vlasniku
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
            int brojSanse = gb.dajRandomSansu();
            Sanse sansa = m.getListaSansi().get(brojSanse);
            izvrsiSansu(sansa, id);
        }

        public void izvrsiSansu(Sanse sansa, Integer id) {
            switch (sansa.getIdSanse()) {
                case 1:
                    System.out.println("SANSA: " + sansa.getOpis());
                    pozicija += sansa.getVrijednost();
                    tvojPotez(0);
                    break;
                case 2:
                    System.out.println("SANSA: " + sansa.getOpis());
                    if (sansa.getVrijednost() < 0) {
                        Banka.novci += Math.abs(sansa.getVrijednost());
                        novci -= Math.abs(sansa.getVrijednost());
                        if (novci <= 0) {
                            bankrot();
                        }
                    } else {
                        novci += sansa.getVrijednost();
                    }
                    break;
            }
        }

        public void bankrot() {
            System.out.println("BANKROTIRAO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        public void preskok() {
            System.out.println("Cekaj jedan krug");
            stop = 1;
        }

        public void zatvor() {
            System.out.println("U zatvoru. Cekaj da prodju tri kruga");
            stop = 3;
        }

        public void pokupiNovce() {
            System.out.println("Pokupi lovu iz banke");
            novci += Banka.novci;
            Banka.novci = 0;
        }
        
        public void sljedeciIgrac() {
            System.out.println("sljedeci igrac moze igrati -> Bruno");
            ACLMessage poruka = new ACLMessage(ACLMessage.QUERY_REF);
            poruka.addReceiver(new AID("Bruno", AID.ISLOCALNAME));
            poruka.setContent("Bruno, ja sam zavrsio. Mozes bacati kockicu. Stef");
            System.out.println("-----------------------------------------------");
            try {
                sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Igrac1.class.getName()).log(Level.SEVERE, null, ex);
            }
            send(poruka);
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
