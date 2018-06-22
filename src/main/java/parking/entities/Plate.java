package parking.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Plate {

    @Id
    @GeneratedValue
    private long id;
    private String plateNr;
    private boolean vip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlateNr() {
        return plateNr;
    }

    public void setPlateNr(String plateNr) {
        this.plateNr = plateNr;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}