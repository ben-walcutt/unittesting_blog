package co.newlabs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    private long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
