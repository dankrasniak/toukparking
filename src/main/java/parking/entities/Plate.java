package parking.entities;

import parking.validators.ProperPlate;
import parking.entities.enums.Region;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Plate {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 8, max = 8, message = "{error.sizeIs}")
    @ProperPlate(message = "{error.acceptedSigns}")
    private String plateNr;
    private boolean vip;
    private Instant start;
    private Instant end;
    private Region region;
    private BigDecimal paid;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal payed) {
        this.paid = payed;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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