package es.uclm.FlashBox.business.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private RestauranteDAO restauranteDAO;

    @GetMapping({ "/", "/home" })
    public String mostrarInicio(@RequestParam(required = false) String nombre,
                                 @RequestParam(required = false) String tipo,
                                 HttpSession session,
                                 Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Restaurante> restaurantes = restauranteDAO.findByNombreContainingIgnoreCaseAndTipoContainingIgnoreCase(
                nombre == null ? "" : nombre,
                tipo == null ? "" : tipo
        );

        List<String> categorias = restauranteDAO.findAll().stream()
                .map(Restaurante::getTipo)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("categorias", categorias);
        model.addAttribute("filtroNombre", nombre);
        model.addAttribute("filtroTipo", tipo);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        }

        return "home";
    }
}
