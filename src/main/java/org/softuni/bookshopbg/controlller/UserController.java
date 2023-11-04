package org.softuni.bookshopbg.controlller;


import jakarta.validation.Valid;
import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(Model model){

        if (!model.containsAttribute("userLoginBindingModel")){
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
        }

        return new ModelAndView("login");
    }

    @PostMapping("/login-error")
    public String login(@Valid @ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel
            , BindingResult bindingResult,
                        RedirectAttributes rAtt){

        if (bindingResult.hasErrors()){
            rAtt.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel", bindingResult);
            return "redirect:/users/login";
        }

        boolean validCredentials = this.userService.checkCredentials(userLoginBindingModel.getUsername(), userLoginBindingModel.getPassword());

        if (!validCredentials) {
            rAtt
                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("validCredentials", false);
            return "redirect:/users/login";
        }

        userService.login(userLoginBindingModel);

        return "redirect:/admin";

    }

    @GetMapping("/register")
    public ModelAndView register(Model model){

        if (!model.containsAttribute("userRegisterBindingModel")){
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }

        return new ModelAndView("register");
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel
            ,BindingResult bindingResult,
                           RedirectAttributes rAtt){

        if (!UserRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword",
                    "error.userRegisterBindingModel",
                    "Passwords must be the same.");
        }

        if(userService.findUserByEmail(userRegisterBindingModel.getEmail()) != null){
            bindingResult.rejectValue("email",
                    "error.userRegisterBindingModel",
                    "Email already exists.");
        }
        if (userService.findUserByUsername(userRegisterBindingModel.getUsername()).isPresent()){
            bindingResult.rejectValue("username",
                    "error.userRegisterBindingModel",
                    "Username already exists.");
        }

        if (bindingResult.hasErrors()){
            rAtt.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:/users/register";
        }

        String view = userService.register(userRegisterBindingModel) ? "redirect:/users/login" : "redirect:/users/register";

        return view;

    }

    @ModelAttribute
    public void addAttribute(Model model) {
        model.addAttribute("validCredentials");
    }


}
