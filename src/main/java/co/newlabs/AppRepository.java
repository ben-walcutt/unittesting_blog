package co.newlabs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class AppRepository {
    private NamedParameterJdbcTemplate template;

    public List<CustomerEntity> getAllCustomers() {
        String query = "select * from customers";
        Map<String, Object> params = new HashMap<>();

        RowMapper<CustomerEntity> rowMapper = new BeanPropertyRowMapper<>(CustomerEntity.class);

        return template.query(query, params, rowMapper);
    }

    public CustomerEntity getCustomerById(long id) {
        String query = "select * from customers where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        RowMapper<CustomerEntity> rowMapper = new BeanPropertyRowMapper<>(CustomerEntity.class);

        return template.queryForObject(query, params, rowMapper);
    }

    public void insertCustomer(CustomerEntity entity) {
        String query = "insert into customers (name, address, city, state, zipCode) values (:name, :address, :city, :state, :zip)";
        Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        params.put("address", entity.getAddress());
        params.put("city", entity.getCity());
        params.put("zip", entity.getZipCode());

        template.update(query, params);
    }
}
