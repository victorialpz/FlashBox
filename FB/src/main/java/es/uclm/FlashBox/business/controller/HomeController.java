package es.uclm.FlashBox.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;

@Controller
public class HomeController {

	@Autowired
    private RestauranteDAO restauranteDAO;

    @GetMapping({"/", "/home"})
    public String mostrarInicio(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "home";
    }
}