package santiagoczarny.orders.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;
    private String idNumber;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

}
