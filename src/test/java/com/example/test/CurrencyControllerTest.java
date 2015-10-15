package com.example.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mihaildoronin on 15.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CurrencyController controller;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void testGetCurrencyRateForDate() throws Exception {
        Date date = new GregorianCalendar(2015, Calendar.SEPTEMBER, 24, 12, 0).getTime();
        CurrencyResponse correctResponse = new CurrencyResponse("USD", date, 66.041);
        ObjectMapper mapper = new ObjectMapper();
        String correctResponseAsString = mapper.writeValueAsString(correctResponse);

        mockMvc.perform(get("/rate/USD/2015-09-24").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(correctResponseAsString));
    }


    @Test
    public void testGetCurrencyRateWithoutSpecificDate() throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String correctString = f.format(c.getTime());

        ResultActions response = mockMvc.perform(get("/rate/USD/2015-09-24").accept("application/json"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.date").value(is(correctString)));

        String s = response.andReturn().getResponse().getContentAsString();
        JsonPath.
    }
}