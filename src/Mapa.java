
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bruno
 */
public class Mapa {

    private static Mapa instance = new Mapa();
    private List<Polje> mapa = new ArrayList<>();
    private List<Sanse> listaSansi = new ArrayList<>();
    String linija = "";
    String delimiter = ";";

    private Mapa() {
    }

    public static Mapa getInstance() {
        if (instance == null) {
            instance = new Mapa();
        }
        return instance;
    }

    public void postaviMapu() {
        try (BufferedReader br = new BufferedReader(new FileReader("popisPolja.txt"))) {
            while ((linija = br.readLine()) != null) {
                String[] zapis = linija.split(delimiter);
                if (zapis[0].equals("1")) {
                    PoljeBuilder pb = new PoljeBuilderImpl();
                    pb.setIdPolja(Integer.parseInt(zapis[1]));
                    pb.setNaziv(zapis[2]);
                    pb.setCijena(Integer.parseInt(zapis[3]));
                    pb.setIdGrupe(Integer.parseInt(zapis[4]));
                    pb.setIznosNaplate(Integer.parseInt(zapis[5]));
                    mapa.add(pb.build());
                } else if (zapis[0].equals("2")) {
                    PoljeBuilder pb = new PoljeBuilderImpl();
                    pb.setIdPolja(Integer.parseInt(zapis[1]));
                    pb.setNaziv(zapis[2]);
                    mapa.add(pb.build());
                }
            }
        } catch (IOException ex) {
            System.out.println("Greska kod citanja datoteke");
        }

        try (BufferedReader br = new BufferedReader(new FileReader("popisSansi.txt"))) {
            while ((linija = br.readLine()) != null) {
                String[] zapis = linija.split(delimiter);
                Sanse s = new Sanse();
                s.setIdSanse(Integer.parseInt(zapis[0]));
                s.setVrijednost(Integer.parseInt(zapis[1]));
                s.setOpis(zapis[2]);
                listaSansi.add(s);
            }
        } catch (IOException ex) {
            System.out.println("Greska kod citanja datoteke");
        }
    }

    public List<Sanse> getListaSansi() {
        return listaSansi;
    }

    public void setListaSansi(List<Sanse> listaSansi) {
        this.listaSansi = listaSansi;
    }

    public List<Polje> getMapa() {
        return mapa;
    }

    public void setMapa(List<Polje> mapa) {
        this.mapa = mapa;
    }

}
