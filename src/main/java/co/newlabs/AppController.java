package co.newlabs;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("customers")
public class AppController {
    private AppService service;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity createNewCustomer(@RequestBody CustomerDTO customer) {
        service.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
