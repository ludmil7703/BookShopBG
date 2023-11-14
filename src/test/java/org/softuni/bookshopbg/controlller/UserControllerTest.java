package org.softuni.bookshopbg.controlller;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Mock
    private UserService mockUserService;
    @Test
    void testRegister() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .param("username", "anna")
                        .param("password", "123")
                        .param("confirmPassword", "123")
                        .param("email", "anna@example.com")
                        .param("firstName", "Pesho")
                        .param("lastName", "Peshov")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection());


        assertTrue(Objects.requireNonNull(resultActions.andReturn().getModelAndView()).getModel().containsKey("userRegisterBindingModel"));


        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"));

    }

    @Test
    void addAttribute() {


    }

    @Test
    void testGetLoginPage() throws Exception {
        ResultActions resultActions = null;

        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(mvcResult -> {
                    ModelAndView modelAndView = mvcResult.getModelAndView();
                    assertFalse(Objects.requireNonNull(modelAndView).getModel().containsKey("userLoginBindingModel"));
                });


    }
}