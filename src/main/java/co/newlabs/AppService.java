package co.newlabs;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AppService {
    private AppRepository repository;
    private MapperFacade mapper;
    private AddressUtility addressUtility;

    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> entities = repository.getAllCustomers();
        return mapper.mapAsList(entities, CustomerDTO.class);
    }

    public CustomerDTO getCustomerById(long id) {
        CustomerEntity e = repository.getCustomerById(id);
        CustomerDTO c = mapper.map(e, CustomerDTO.class);
        c.setFullAddress(addressUtility.makeFullAddress(c.getAddress(), c.getCity(), c.getState(), c.getZipCode()));
        return mapper.map(e, CustomerDTO.class);
    }

    public void createCustomer(CustomerDTO customer) {
        CustomerEntity entity = new CustomerEntity(0, customer.getName(), customer.getAddress(), customer.getCity(), customer.getState(), customer.getZipCode());
        repository.insertCustomer(entity);
    }
}
