package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result,
                                          Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }
        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @RequestMapping("/")
    public String listCourses(Model model){
        model.addAttribute("courses", courseRepository.findAll());
        /* new for Courses */
        if (userService.getUser() != null){
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "list";
    }

    @GetMapping("/add")
    public String courseForm(Model model){
        model.addAttribute("course", new Course());
        return "courseform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Course course, BindingResult result){
        if (result.hasErrors()){
            return "courseform";
        }
        course.setUser(userService.getUser());
        courseRepository.save(course);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "courseform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return "redirect:/";
    }


//    @RequestMapping("/secure")
//    public String secure(Principal principal, Model model){
//        User myuser = ((CustomUserDetails)
//                        ((UsernamePasswordAuthenticationToken) principal)
//                                .getPrincipal()).getUser();
//        model.addAttribute("myuser", myuser);
//        return "secure";
//    }

    /* Addition for separate log out page */
    @RequestMapping("/logoutconfirm")
    public String logoutconfirm(){
        return "logoutconfirm";
    }


}
