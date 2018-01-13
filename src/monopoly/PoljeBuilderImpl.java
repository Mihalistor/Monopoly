package monopoly;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bruno
 */
public class PoljeBuilderImpl implements PoljeBuilder {

    private Polje polje;

    public PoljeBuilderImpl() {
        polje = new Polje();
    }

    @Override
    public Polje build() {
        return polje;
    }

    @Override
    public PoljeBuilder setNaziv(String naziv) {
        polje.setNaziv(naziv);
        return this;
    }
    @Override
    public PoljeBuilder setCijena(Integer cijena) {
        polje.setCijena(cijena);
        return this;
    }

    @Override
    public PoljeBuilder setIdGrupe(Integer idGrupe) {
        polje.setIdGrupe(idGrupe);
        return this;
    }

    @Override
    public PoljeBuilder setSansa(Integer sansa) {
        polje.setSansa(sansa);
        return this;
    }

    @Override
    public PoljeBuilder setIznosNaplate(Integer iznos) {
       polje.setIznosNaplate(iznos);
       return this;
    }

    @Override
    public PoljeBuilder setIdPolja(Integer id) {
        polje.setIdPolja(id);
        return this;
    }

    @Override
    public PoljeBuilder setImeVlasnik(String imeVlasnika) {
        polje.setImeVlasnika(imeVlasnika);
        return this;
    }

}
