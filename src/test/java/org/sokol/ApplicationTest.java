package org.sokol;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public class ApplicationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private TripRepository tripRepository;

        @Before
        public void deleteAllCustomersBeforeTests() throws Exception {
            customerRepository.deleteAll();
        }

        @Before
        public void deleteAllTripsBeforeTests() throws Exception {
            tripRepository.deleteAll();
        }

        @Test
        public void shouldReturnCustomerRepositoryIndex() throws Exception {

            mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                    jsonPath("$._links.customers").exists());
        }

        @Test
        public void shouldReturnTripRepositoryIndex() throws Exception {

            mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                    jsonPath("$._links.trips").exists());
        }

        @Test
        public void shouldCreateCustomerEntity() throws Exception {

            mockMvc.perform(post("/customers").content(
                    "{\"name\": \"Michał\"}")).andExpect(
                    status().isCreated()).andExpect(
                    header().string("Location", containsString("customers/")));
        }

        @Test
        public void shouldCreateTripEntity() throws Exception {

            mockMvc.perform(post("/trips").content(
                    "{\"destination\": \"Warszawa\", \"price\":\"2500\"}")).andExpect(
                    status().isCreated()).andExpect(
                    header().string("Location", containsString("trips/")));
        }

        @Test
        public void shouldRetrieveCustomerEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/customers").content(
                    "{\"name\": \"Michał\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");
            mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.name").value("Michał"));
        }


        @Test
        public void shouldRetrieveTripsEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/trips").content(
                    "{\"destination\": \"Warszawa\", \"price\":\"2500.56\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");
            mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.destination").value("Warszawa")).andExpect(
                    jsonPath("$.price").value("2500.56"));
        }


        @Test
        public void shouldQueryCustomerEntity() throws Exception {

            mockMvc.perform(post("/customers").content(
                    "{ \"name\": \"Michał\"}")).andExpect(
                    status().isCreated());

            mockMvc.perform(
                    get("/customers/search/findByName?name={name}", "Michał")).andExpect(
                    status().isOk()).andExpect(
                    jsonPath("$._embedded.customers[0].name").value(
                            "Michał"));
        }

        @Test
        public void shouldUpdateCustomerEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/customers").content(
                    "{\"name\": \"Michał\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");

            mockMvc.perform(put(location).content(
                    "{\"name\": \"Bartosz\"}")).andExpect(
                    status().isNoContent());

            mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.name").value("Bartosz"));
        }



        @Test
        public void shouldQueryTripsEntity() throws Exception {

            mockMvc.perform(post("/trips").content(
                    "{ \"destination\": \"Warszawa\", \"price\":\"2500.56\"}")).andExpect(
                    status().isCreated());

            mockMvc.perform(
                    get("/trips/search/findByDestination?destination={destination}", "Warszawa")).andExpect(
                    status().isOk()).andExpect(
                    jsonPath("$._embedded.trips[0].destination").value(
                            "Warszawa"));
        }

        @Test
        public void shouldUpdateTripsEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/trips").content(
                    "{\"destination\": \"Warszawa\", \"price\":\"2500.56\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");

            mockMvc.perform(put(location).content(
                    "{\"destination\": \"Lublin\", \"price\":\"1456.56\"}")).andExpect(
                    status().isNoContent());

            mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.destination").value("Lublin")).andExpect(
                    jsonPath("$.price").value("1456.56"));
        }

        @Test
        public void shouldPartiallyUpdateEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/trips").content(
                    "{\"destination\": \"Warszawa\", \"price\":\"2500.56\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");

            mockMvc.perform(
                    patch(location).content("{\"destination\": \"Lodz\"}")).andExpect(
                    status().isNoContent());

            mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.destination").value("Lodz")).andExpect(
                    jsonPath("$.price").value("2500.56"));
        }




        @Test
        public void shouldDeleteEntity() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/customers").content(
                    "{ \"name\": \"Michał\"}")).andExpect(
                    status().isCreated()).andReturn();

            String location = mvcResult.getResponse().getHeader("Location");
            mockMvc.perform(delete(location)).andExpect(status().isNoContent());

            mockMvc.perform(get(location)).andExpect(status().isNotFound());
        }


}
