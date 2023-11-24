package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NotFoundController {

    @GetMapping("/not-found/{id}")
    public String notFound(@PathVariable("id") String id) {
        throw  new IllegalArgumentException(String.format("Object with ID %s not found.", id));
    }

//    @ExceptionHandler({IllegalArgumentException.class})
//    public ModelAndView handleNotFound(ObjectNotFoundException e) {
//        ModelAndView modelAndView = new ModelAndView("notfound");
//        modelAndView.addObject("id", e.getId());
//        return modelAndView;
//    }



}
