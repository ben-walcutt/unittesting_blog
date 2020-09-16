package co.newlabs;

import org.springframework.stereotype.Component;

@Component
public class AddressUtility {

    public String makeFullAddress(String address, String city, String state, String zipcode) {
        return String.format("%s %s, %s %S", address, city, state, zipcode);
    }
}
