package org.softuni.bookshopbg.controlller;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @Mock
    private UserService mockUserService;
    @Test
    void testRegister() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/register");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("register"));
        resultActions.andExpect(model().attributeExists("userRegisterBindingModel"));

    }

//    @Test
//    void testRegisterSubmit() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/register");
//        ResultActions resultActions = mockMvc.perform(requestBuilder);
//
//        mockUserService.register(new UserRegisterBindingModel());
//        resultActions.andExpect(status().isOk());
//
//            }

    @Test
    void addAttribute() {


    }

    @Test
    void testGetLoginPage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/login");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("login"));





    }
}