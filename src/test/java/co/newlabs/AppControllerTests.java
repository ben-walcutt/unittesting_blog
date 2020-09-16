package co.newlabs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class AppControllerTests {

    @MockBean
    private AppService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getAllCustomers_ScenarioA() throws Exception {
        // arrange
        List<CustomerDTO> serviceList = new ArrayList<>();
        CustomerDTO customerA = new CustomerDTO();
        customerA.setName("A");
        serviceList.add(customerA);
        CustomerDTO customerB = new CustomerDTO();
        customerB.setName("B");
        serviceList.add(customerB);

        doReturn(serviceList).when(service).getAllCustomers();

        List<CustomerDTO> expectedList = new ArrayList<>();
        CustomerDTO expectedA = new CustomerDTO();
        expectedA.setName("A");
        expectedList.add(expectedA);
        CustomerDTO expectedB = new CustomerDTO();
        expectedB.setName("B");
        expectedList.add(expectedB);

        String expectedResult = objectMapper.writeValueAsString(expectedList);

        // act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(status().is(200))
                .andReturn();

        String actualResult = result.getResponse().getContentAsString();

        // assert
        Assert.assertThat(actualResult, is(equalTo(expectedResult)));

        // verify
        verify(service, times(1)).getAllCustomers();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void getAllCustomers_ScenarioB() throws Exception {
        // arrange
        doThrow(new RuntimeException("exception")).when(service).getAllCustomers();

        // act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(status().is(500))
                .andReturn();


        String actualResult = result.getResponse().getContentAsString();

        // assert
        Assert.assertThat(actualResult, is(equalTo("")));

        // verify
        verify(service, times(1)).getAllCustomers();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void createNewCustomer_ScenarioA() throws Exception {
        // arrange
        CustomerDTO serviceCustomer = new CustomerDTO();
        serviceCustomer.setName("C");

        doNothing().when(service).createCustomer(serviceCustomer);

        // act
        CustomerDTO requestCustomer = new CustomerDTO();
        requestCustomer.setName("C");
        String requestBody = objectMapper.writeValueAsString(requestCustomer);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customers").content(requestBody).contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        String actualResult = result.getResponse().getContentAsString();

        // assert
        Assert.assertThat(actualResult, is(equalTo("")));

        // verify
        verify(service, times(1)).createCustomer(serviceCustomer);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void createNewCustomer_ScenarioB() throws Exception {
        // arrange
        CustomerDTO serviceCustomer = new CustomerDTO();
        serviceCustomer.setName("D");

        doThrow(new RuntimeException("nope")).when(service).createCustomer(serviceCustomer);

        // act
        CustomerDTO requestCustomer = new CustomerDTO();
        requestCustomer.setName("D");
        String requestBody = objectMapper.writeValueAsString(requestCustomer);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customers").content(requestBody).contentType("application/json"))
                .andExpect(status().is(500))
                .andReturn();

        String actualResult = result.getResponse().getContentAsString();

        // assert
        Assert.assertThat(actualResult, is(equalTo("")));

        // verify
        verify(service, times(1)).createCustomer(serviceCustomer);
        verifyNoMoreInteractions(service);
    }
}
