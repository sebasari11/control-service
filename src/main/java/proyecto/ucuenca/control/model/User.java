package proyecto.ucuenca.control.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String numberID;
    private String firstName;
    private String lastName;
    private String sex;
    private Integer age;
    private String state;
}