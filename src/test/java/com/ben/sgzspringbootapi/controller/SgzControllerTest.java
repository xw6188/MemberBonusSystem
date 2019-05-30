package com.ben.sgzspringbootapi.controller;

import com.ben.sgzspringbootapi.entity.Result;
import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class SgzControllerTest {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserService userService;

    @Resource
    private MockMvc mockMvc;

    private TestContextManager testContextManager;

    @Before
    public void setUp() throws Exception{
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new SgzController()).build();
    }

    @After
    public void verify() throws Exception{

    }

    @Test
    public void sgzAPI_Test() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ping").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());

    }

}