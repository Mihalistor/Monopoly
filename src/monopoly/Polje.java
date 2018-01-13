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
public class Polje {
    private Integer idPolja;
    private String naziv;
    private Integer cijena;
    private Integer idVlasnika;
    private Integer idGrupe;
    private Integer sansa;
    private Integer iznosNaplate;

    public Polje() {
    }

    public Integer getIdVlasnika() {
        return idVlasnika;
    }

    public void setIdVlasnika(Integer idVlasnika) {
        this.idVlasnika = idVlasnika;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getCijena() {
        return cijena;
    }

    public void setCijena(Integer cijena) {
        this.cijena = cijena;
    }

    public Integer getIdGrupe() {
        return idGrupe;
    }

    public void setIdGrupe(Integer idGrupe) {
        this.idGrupe = idGrupe;
    }

    public Integer getSansa() {
        return sansa;
    }

    public void setSansa(Integer sansa) {
        this.sansa = sansa;
    }

    public Integer getIznosNaplate() {
        return iznosNaplate;
    }

    public void setIznosNaplate(Integer iznosNaplate) {
        this.iznosNaplate = iznosNaplate;
    }

    public Integer getIdPolja() {
        return idPolja;
    }

    public void setIdPolja(Integer idPolja) {
        this.idPolja = idPolja;
    }
        
}
