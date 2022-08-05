package proyecto.ucuenca.control.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "control-data")
@JsonPropertyOrder({"create", "id"})
public class ControlData {

    @Id
    private Long id;
    @Field("user_id")
    private Long userId;
    @Field("measure_id")
    private Long measureId;
    private Double pam;
    private Date create;
    private String status;
    private Double systolicPressure;
    private Double diastolicPressure;
    private Double steps;
    private Double pulse;
    private String numberID;
    private String firstName;
    private String lastName;
    private String sex;
    private Integer age;
    private String state;
}
