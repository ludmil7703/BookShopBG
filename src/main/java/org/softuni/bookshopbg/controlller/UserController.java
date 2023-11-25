package org.softuni.bookshopbg.controlller;


import jakarta.mail.PasswordAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.security.PasswordResetToken;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.BGConstants;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.softuni.bookshopbg.utils.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailConstructor mailConstructor;


    private final UserService userService;

    private final UserDetailsService userSecurityService;

    private final BookService bookService;

    private final UserPaymentService userPaymentService;

    private final UserShippingService userShippingService;

    private final CartItemService cartItemService;

    private final OrderService orderService;

    private final CategoryService categoryService;



    public UserController(UserService userService,
                          UserDetailsService userSecurityService,
                          BookService bookService,
                          UserPaymentService userPaymentService,
                          UserShippingService userShippingService,
                          CartItemService cartItemService,
                          OrderService orderService, CategoryService categoryService) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.bookService = bookService;
        this.userPaymentService = userPaymentService;
        this.userShippingService = userShippingService;
        this.cartItemService = cartItemService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

//    @GetMapping("/login")
//    public ModelAndView login(Model model){
//
//        if (!model.containsAttribute("userLoginBindingModel")){
//            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
//        }
//
//        return new ModelAndView("login");
//    }



    @RequestMapping("/login")
    public String login(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    @RequestMapping("/bookshelf")
    public String bookshelf(Model model, Principal principal) throws IOException {

        if(principal != null) {
            String username = principal.getName();
            UserEntity user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
        }

        List<Book> bookList = bookService.findAllBooks();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("activeAll",true);

        return "bookList";
    }



    @RequestMapping("/bookDetail")
    public String bookDetail(
            @PathParam("id") Long id, Model model, Principal principal
    ) {
        if(principal != null) {
            String username = principal.getName();
            UserEntity user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
        }

        Book book = bookService.findBookById(id);

        model.addAttribute("book", book);

        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "bookDetails";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ) {

        model.addAttribute("classActiveForgetPassword", true);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        UserEntity user = userService.findUserByEmail(email);

        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);


        mailSender.send(newEmail);

        ;

        model.addAttribute("forgetPasswordEmailSent", "true");


        return "myAccount";
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);

        List<String> stateList = BGConstants.listOfBGStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("classActiveEdit", true);

        return "myProfilePage";
    }

    @RequestMapping("/listOfCreditCards")
    public String listOfCreditCards(
            Model model, Principal principal, HttpServletRequest request
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfilePage";
    }

    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(
            Model model, Principal principal, HttpServletRequest request
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "myProfilePage";
    }

    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(
            Model model, Principal principal
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();


        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        List<String> stateList = BGConstants.listOfBGStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model, Principal principal
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        List<String> stateList = BGConstants.listOfBGStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping(value="/addNewCreditCard", method= RequestMethod.POST)
    public String addNewCreditCard(
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling,
            Principal principal, Model model
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
    public String addNewShippingAddressPost(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Principal principal, Model model
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }


    @RequestMapping("/updateCreditCard")
    public String updateCreditCard(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "error";
        } else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);

            List<String> stateList = BGConstants.listOfBGStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            List<Category> categoryList = categoryService.getAllCategories();
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfilePage";
        }
    }

    @RequestMapping("/updateUserShipping")
    public String updateUserShipping(
            @ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId).get();

        if(user.getId() != userShipping.getUser().getId()) {
            return "error";
        } else {
            model.addAttribute("user", user);

            model.addAttribute("userShipping", userShipping);

            List<String> stateList = BGConstants.listOfBGStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            List<Category> categoryList = categoryService.getAllCategories();
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfilePage";
        }
    }

    @RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
    public String setDefaultPayment(
            @ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultPaymentId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
    public String setDefaultShippingAddress(
            @ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
    ) {
        UserEntity user = userService.findUserByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping("/removeCreditCard")
    public String removeCreditCard(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "error";
        } else {
            model.addAttribute("user", user);
            userPaymentService.deleteById(creditCardId);

            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            List<Category> categoryList = categoryService.getAllCategories();
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfilePage";
        }
    }

    @RequestMapping("/removeUserShipping")
    public String removeUserShipping(
            @ModelAttribute("id") Long userShippingId, Principal principal, Model model
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId).get();

        if (user.getId() != userShipping.getUser().getId()) {
            return "error";
        } else {
            model.addAttribute("user", user);

            List<Category> categoryList = categoryService.getAllCategories();
            model.addAttribute("categoryList", categoryList);

            userShippingService.deleteById(userShippingId);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveShipping", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfilePage";
        }
    }

    @PostMapping(value="/newUser")
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,
            @ModelAttribute("username") String username,
            Model model
    ){
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if (userService.findUserByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "myAccount";
        }

        if (userService.findUserByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);

            return "myAccount";
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.createUser(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);


        mailSender.send(email);

        model.addAttribute("emailSent", "true");
        model.addAttribute("orderList", user.getOrderList());

        return "myAccount";
    }


    @RequestMapping("/newUser")
    public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/error";
        }

        UserEntity user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);
        model.addAttribute("orderList", user.getOrderList());
        return "myProfilePage";
    }

    @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") UserEntity user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) {
        UserEntity currentUser = userService.findUserByUsername(user.getUsername());

        /*check email already exists*/
        if (userService.findUserByEmail(user.getEmail())!=null) {
            if(userService.findUserByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", true);
                return "myProfilePage";
            }
        }

        /*check username already exists*/
        if (userService.findUserByUsername(user.getUsername()) != null) {
            if(!Objects.equals(userService.findUserByUsername(user.getUsername()).getId(), user.getId())) {
                model.addAttribute("usernameExists", true);
                return "myProfilePage";
            }
        }

//		update password
        if (newPassword != null && !newPassword.isEmpty()){
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if(passwordEncoder.matches(user.getPassword(), dbPassword)){
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);

                return "myProfilePage";
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfilePage";
    }

    @RequestMapping("/orderDetail")
    public String orderDetail(
            @RequestParam("id") Long orderId,
            Principal principal, Model model
    ){
        UserEntity user = userService.findUserByUsername(principal.getName());
        Order order = orderService.findById(orderId).get();

        if(order.getUser().getId()!=user.getId()) {
            return "error";
        } else {
            List<CartItem> cartItemList = cartItemService.findByOrder(order);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("user", user);
            model.addAttribute("order", order);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            List<Category> categoryList = categoryService.getAllCategories();
            model.addAttribute("categoryList", categoryList);

            UserShipping userShipping = new UserShipping();
            model.addAttribute("userShipping", userShipping);

            List<String> stateList = BGConstants.listOfBGStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("displayOrderDetail", true);

            return "myProfilePage";
        }
    }
}

//    @PostMapping("/login-error")
//    public String login(@Valid @ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel
//            , BindingResult bindingResult,
//                        RedirectAttributes rAtt){
//
//        if (bindingResult.hasErrors()){
//            rAtt.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel", bindingResult);
//            return "redirect:/users/login";
//        }
//
//        boolean validCredentials = this.userService.checkCredentials(userLoginBindingModel.getUsername(), userLoginBindingModel.getPassword());
//
//        if (!validCredentials) {
//            rAtt
//                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
//                    .addFlashAttribute("validCredentials", false);
//            return "redirect:/users/login";
//        }
//
//        return "redirect:/";
//
//    }
//
//    @GetMapping("/register")
//    public ModelAndView register(Model model){
//
//        if (!model.containsAttribute("userRegisterBindingModel")){
//            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
//        }
//
//        return new ModelAndView("register");
//    }
//    @PostMapping("/register")
//    public String register(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel
//            ,BindingResult bindingResult,
//                           RedirectAttributes rAtt){
//
//        if (!UserRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
//            bindingResult.rejectValue("confirmPassword",
//                    "error.userRegisterBindingModel",
//                    "Passwords must be the same.");
//        }
//
//        if(userService.findUserByEmail(userRegisterBindingModel.getEmail()) != null){
//            bindingResult.rejectValue("email",
//                    "error.userRegisterBindingModel",
//                    "Email already exists.");
//        }
//        if (userService.findUserByUsername(userRegisterBindingModel.getUsername()).isPresent()){
//            bindingResult.rejectValue("username",
//                    "error.userRegisterBindingModel",
//                    "Username already exists.");
//        }
//
//        if (bindingResult.hasErrors()){
//            rAtt.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
//            return "redirect:/users/register";
//        }
//
//        String view = userService.register(userRegisterBindingModel) ? "redirect:/users/login" : "redirect:/users/register";
//
//        return view;
//
//    }
//
//    @ModelAttribute
//    public void addAttribute(Model model) {
//        model.addAttribute("validCredentials");
//    }
//
//
//}
