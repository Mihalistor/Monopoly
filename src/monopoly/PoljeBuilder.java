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
public interface PoljeBuilder {
    Polje build();
    PoljeBuilder setIdPolja(final Integer id);
    PoljeBuilder setNaziv(final String naziv);
    PoljeBuilder setCijena(final Integer cijena);
    PoljeBuilder setIdVlasnik(final Integer idVlasnika);
    PoljeBuilder setIdGrupe(final Integer idGrupe);
    PoljeBuilder setSansa(final Integer sansa);
    PoljeBuilder setIznosNaplate(final Integer iznos);
}
