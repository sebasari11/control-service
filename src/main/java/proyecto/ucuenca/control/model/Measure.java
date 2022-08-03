package proyecto.ucuenca.control.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Measure {

    private Long id;
    private Long userId;
    private Double systolicPressure;
    private Double diastolicPressure;
    private Double steps;
    private Double pulse;
    private Date create;
}
