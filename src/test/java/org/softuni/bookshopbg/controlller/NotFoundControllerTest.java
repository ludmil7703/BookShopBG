package org.softuni.bookshopbg.controlller;

import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotFoundControllerTest {


    @Test
    void notFoundTest(){

        assertThrows(Exception.class, () -> {
            MockMvcBuilders.standaloneSetup(new NotFoundController())
                    .build()
                    .perform(get("/not-found/1"))
                    .andExpect(status().isOk());
        });
    }

}