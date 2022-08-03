package proyecto.ucuenca.control.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import proyecto.ucuenca.control.model.Measure;
import proyecto.ucuenca.control.model.User;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "control")
@JsonPropertyOrder({"create", "id"})
public class ControlMeasure {

    @Id
    @NotNull
    private Long id;
    @Field("user_id")
    private Long userId;
    @Field("measure_id")
    private Long measureId;
    private Double pam;
    private Date create;
    @Field("is_exercising")
    private Boolean isExercising;

    private String status;

    @Transient
    Measure measure;

    @Transient
    User user;

    public String getStatusBySex(Boolean gender){
        boolean highPressure = this.measure.getSystolicPressure() > 115 && this.measure.getDiastolicPressure() > 70;
        if (highPressure && !this.isExercising){
            return "ALERTA!, Presion Alta Detectada";

        } else if (highPressure  && this.isExercising){
            return "Presion Alta debido a Actividad Fisica";
        } else {
            return "Presion Arterial Normal";
        }



    }

}
