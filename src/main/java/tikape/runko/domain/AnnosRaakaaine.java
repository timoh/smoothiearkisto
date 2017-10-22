package tikape.runko.domain;

public class AnnosRaakaaine {
    private Integer jarjestys;
    private Integer id;
    private String ohje;
    private Integer annos_id;
    private Integer raakaaine_id;
    private String maara;

    public AnnosRaakaaine(Integer id, String ohje, Integer jarjestys, Integer annos_id,
            Integer raakaaine_id, String maara) {
        this.jarjestys = jarjestys;
        this.ohje = ohje;
        this.id = id;
        this.annos_id = annos_id;
        this.raakaaine_id = raakaaine_id;
        this.maara = maara;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public Integer getId() {
        return id;
    }     

    public void setId(Integer id) {
        this.id = id;
    }
       
    public String getMaara() {
        return this.maara;
    }
    
    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

    public Integer getAnnos_id() {
        return annos_id;
    }

    public void setAnnos_id(Integer annos_id) {
        this.annos_id = annos_id;
    }

    public Integer getRaakaaine_id() {
        return raakaaine_id;
    }

    public void setRaakaaine_id(Integer raakaaine_id) {
        this.raakaaine_id = raakaaine_id;
    }
    
}