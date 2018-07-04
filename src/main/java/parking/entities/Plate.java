package parking.entities;

import lombok.Data;
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
@Data
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

    public Plate () {}

    public Plate(@NotNull @Size(min = 8, max = 8, message = "{error.sizeIs}") String plateNr, boolean vip, Instant start, Instant end, Region region, BigDecimal paid) {
        this.plateNr = plateNr;
        this.vip = vip;
        this.start = start;
        this.end = end;
        this.region = region;
        this.paid = paid;
    }
}