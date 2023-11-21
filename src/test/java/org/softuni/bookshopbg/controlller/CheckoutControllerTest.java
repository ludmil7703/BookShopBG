package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.security.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.softuni.bookshopbg.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl mockUserService;

    @Mock
    private CartItemServiceImpl mockCartItemService;

    @Mock
    private ShoppingCartServiceImpl mockShoppingCartService;

    @Mock
    private ShippingAddressServiceImpl mockShippingAddressService;

    @Mock
    private BillingAddressServiceImpl mockBillingAddressService;

    @Mock
    private PaymentServiceImpl mockPaymentService;

    @Mock
    private OrderServiceImpl mockOrderService;

    @Mock
    private UserPaymentServiceImpl mockUserPaymentService;

    @Mock
    private UserShippingServiceImpl mockUserShippingService;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private UserEntity mockUser;

    @Mock
    private UserRepository mockUserRepository;

    private CheckoutController checkoutControllerUnderTest;

    @BeforeEach
    void setUp() {
        checkoutControllerUnderTest = new CheckoutController(mockUserService,
                mockCartItemService,
                mockShoppingCartService,
                mockShippingAddressService,
                mockBillingAddressService,
                mockPaymentService,
                mockUserShippingService,
                mockUserPaymentService,
                mockOrderService
                );

        mockPrincipal = new Principal() {
            @Override
            public String getName() {
                return "test";
            }
        };

        mockUser = new UserEntity();
        mockUser.setUsername("test");
        mockUser.setId(1L);
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRole(UserRoleEnum.USER);
        userRoleEntity.setRole(UserRoleEnum.ADMIN);
        mockUser.setRoles(new ArrayList<>());
        mockUser.getRoles().add(userRoleEntity);

    }

    @AfterEach
    void tearDown() {
        checkoutControllerUnderTest = null;
        mockUser = null;
        mockPrincipal = null;

    }

    @Test
    @WithMockUser(username = "test", password = "1234", roles = {"USER", "ADMIN"})
    void checkoutWithEmptyCartTest() throws Exception {
//
//        when(mockUserService.findUserByUsername(mockPrincipal.getName())).thenReturn(Optional.ofNullable(mockUser));
//
//        when(mockCartItemService.findByShoppingCart(mockUser.getShoppingCart())).thenReturn(new ArrayList<>());
//
//        UserEntity user = mockUserService.findUserByUsername(mockPrincipal.getName()).get();
//        RequestBuilder request = get("/checkout")
//                .with(user("test").password("1234").roles("USER", "ADMIN"))
//                .principal(mockPrincipal)
//                .param("id", "1");
//
//        mockMvc.perform(request)
//                .andExpect(view().name("forward:/shoppingCart/cart"));
    }

    @Test
    void checkoutPost() {
    }

    @Test
    void setShippingAddress() {
    }

    @Test
    void setPaymentMethod() {
    }
}