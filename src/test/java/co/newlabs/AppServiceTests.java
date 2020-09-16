package co.newlabs;

import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppServiceTests {

    @Mock
    private AppRepository repository;

    @Mock
    private MapperFacade mapper;

    @Spy
    private AddressUtility addressUtility;

    @InjectMocks
    private AppService systemUnderTest;

    @Test
    public void getAllCustomers_ScenarioA() {
        // arrange
        List<CustomerEntity> databaseEntities = new ArrayList<>();
        CustomerEntity customerA = new CustomerEntity();
        customerA.setName("A");
        databaseEntities.add(customerA);
        CustomerEntity customerB = new CustomerEntity();
        customerB.setName("B");
        databaseEntities.add(customerB);

        doReturn(databaseEntities).when(repository).getAllCustomers();

        List<CustomerDTO> mapperList = new ArrayList<>();
        CustomerDTO customerDTOA = new CustomerDTO();
        customerDTOA.setName("A");
        mapperList.add(customerDTOA);
        CustomerDTO customerDTOB = new CustomerDTO();
        customerDTOB.setName("B");
        mapperList.add(customerDTOB);

        doReturn(mapperList).when(mapper).mapAsList(databaseEntities, CustomerDTO.class);

        // act
        List<CustomerDTO> actual = systemUnderTest.getAllCustomers();

        // assert
        Assert.assertThat(actual.size(), is(2));
        Assert.assertThat(actual.get(0).getName(), is(equalTo("A")));
        Assert.assertThat(actual.get(1).getName(), is(equalTo("B")));

        // verify
        verify(repository, times(1)).getAllCustomers();
        verifyNoMoreInteractions(repository);

        verify(mapper, times(1)).mapAsList(databaseEntities, CustomerDTO.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getAllCustomers_ScenarioB() {
        // arrange
        doThrow(new EmptyResultDataAccessException(2)).when(repository).getAllCustomers();

        // act
        List<CustomerDTO> actual = systemUnderTest.getAllCustomers();

        // assert
    }

    @Test
    public void getCustomerById_ScenarioA() {
        // arrange
        CustomerEntity entity = new CustomerEntity();
        entity.setId(4L);
        entity.setName("C");
        entity.setAddress("123 Any St");
        entity.setCity("Anytown");
        entity.setState("NE");
        entity.setZipCode("12345");

        doReturn(entity).when(repository).getCustomerById(4L);

        CustomerDTO customer = new CustomerDTO();
        customer.setId(4L);
        customer.setName("C");
        customer.setAddress("123 Any St");
        customer.setCity("Anytown");
        customer.setState("NE");
        customer.setZipCode("12345");

        doReturn(customer).when(mapper).map(entity, CustomerDTO.class);

        // act
        CustomerDTO actual = systemUnderTest.getCustomerById(4L);

        // assert
        Assert.assertThat(actual.getId(), is(4L));
        Assert.assertThat(actual.getName(), is("C"));
        Assert.assertThat(actual.getFullAddress(), is(equalTo("123 Any St Anytown, NE 12345")));

        // verify
        verify(repository, times(1)).getCustomerById(4L);
        verifyNoMoreInteractions(repository);

        verify(addressUtility, times(1)).makeFullAddress("123 Any St", "Anytown", "NE", "12345");
        verifyNoMoreInteractions(addressUtility);
    }

    @Test
    public void getCustomerById_ScenarioB() {
        // arrange
        CustomerEntity entity = new CustomerEntity();
        entity.setId(5L);
        entity.setName("D");

        doReturn(entity).when(repository).getCustomerById(5L);

        CustomerDTO customer = new CustomerDTO();
        customer.setId(5L);
        customer.setName("D");
        customer.setAddress("456 Any St");
        customer.setCity("Anytown");
        customer.setState("NE");
        customer.setZipCode("12345");

        doReturn(customer).when(mapper).map(entity, CustomerDTO.class);

        // act
        CustomerDTO actual = systemUnderTest.getCustomerById(5L);

        // assert
        Assert.assertThat(actual.getId(), is(5L));
        Assert.assertThat(actual.getName(), is("D"));
        Assert.assertThat(actual.getFullAddress(), is(equalTo("456 Any St Anytown, NE 12345")));

        // verify
        verify(repository, times(1)).getCustomerById(5L);
        verifyNoMoreInteractions(repository);

        verify(addressUtility, times(1)).makeFullAddress("456 Any St", "Anytown", "NE", "12345");
        verifyNoMoreInteractions(addressUtility);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getCustomerById_ScenarioC() {
        // arrange
        doThrow(new EmptyResultDataAccessException(1)).when(repository).getCustomerById(6L);

        // act
        systemUnderTest.getCustomerById(6L);

        // assert

        // verify
    }

    @Test
    public void createCustomer_ScenarioA() {
        // arrange
        CustomerEntity entity = new CustomerEntity();
        entity.setName("D");

        doNothing().when(repository).insertCustomer(entity);

        // act
        CustomerDTO customer = new CustomerDTO();
        customer.setName("D");

        systemUnderTest.createCustomer(customer);

        // assert

        // verify
        verify(repository, times(1)).insertCustomer(entity);
        verifyNoMoreInteractions(repository);
    }
}
