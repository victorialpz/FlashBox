// FavoritosController.java
package es.uclm.FlashBox.business.controller;

import es.uclm.FlashBox.business.entity.Cliente;
import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.persistence.ClienteDAO;
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente/favoritos")
public class FavoritosController {

    @Autowired private ClienteDAO clienteDAO;
    @Autowired private RestauranteDAO restauranteDAO;

    // Temporal: usar cliente con ID 1
    private final Long clienteId = 1L;

    @PostMapping("/agregar")
    public String agregarFavorito(@RequestParam Long restauranteId) {
        Cliente cliente = clienteDAO.findById(clienteId).orElse(null);
        Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);

        if (cliente != null && restaurante != null) {
            if (!cliente.getFavoritos().contains(restaurante)) {
                cliente.getFavoritos().add(restaurante);
                clienteDAO.save(cliente);
            }
        }
        return "redirect:/restaurantes/listar";
    }
}
