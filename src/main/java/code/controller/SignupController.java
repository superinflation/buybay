package code.controller;

import java.util.HashMap;

// Importing required classes 
import code.Util.SignupData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import code.DataStorage.AccountManager;
import code.Util.PostUtil;

// Annotation 
@Controller
// Main class 
public class SignupController {

    @PostMapping(value = {"/signup", "signup.html"})
    public String createAccount(@ModelAttribute SignupData data, Model model) {

        model.addAttribute("data", new SignupData());

        boolean invalidData = (data.getFirstName() == null || data.getLastName() == null || data.getUsername() == null || data.getEmail() == null || data.getPassword() == null
                || data.getConfirmPassword() == null);
        boolean samePasswords = !data.getPassword().equals(data.getConfirmPassword());

        if (invalidData || data.getUsername().contains(" ") || data.getFirstName().contains(" ") || data.getLastName().contains(" ") || data.getEmail().contains(" ")) {
            model.addAttribute("invalidData", true);
            return "signupFailure";
        }

        if (samePasswords) {
            model.addAttribute("samePasswords", true);
            return "signupFailure";
        }

        int existingDetails = (AccountManager.ACCOUNTMANAGER.createAccount(data));

        if (existingDetails != 1) {
            if (existingDetails == -1)
                model.addAttribute("existingUsername", true);
            else
                model.addAttribute("existingEmail", true);
            return "signupFailure";
        }

        return "redirect";

    }

    @GetMapping({"/signup", "/signup.html"})
    public String signup(Model model) {
        model.addAttribute("data", new SignupData());
        return "signup";
    }
}
