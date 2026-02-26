package edu.comillas.icai.gitt.pat.spring.practcarritos;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorSSR {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("carritos", ControladorREST.getCarritos().values());
        return "index";
    }
}
