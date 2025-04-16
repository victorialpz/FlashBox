package es.uclm.FlashBox.business.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.*;

import es.uclm.FlashBox.business.entity.CartaMenu;
import es.uclm.FlashBox.business.entity.ItemMenu;
import es.uclm.FlashBox.business.entity.Restaurante;
import es.uclm.FlashBox.business.entity.ServicioEntrega;
import es.uclm.FlashBox.business.persistence.CartaMenuDAO;
import es.uclm.FlashBox.business.persistence.ItemMenuDAO; 
import es.uclm.FlashBox.business.persistence.RestauranteDAO;
import es.uclm.FlashBox.business.persistence.ServicioEntregaDAO;
import es.uclm.FlashBox.business.entity.Repartidor;
import es.uclm.FlashBox.business.persistence.*;

@Controller
@RequestMapping("/repartidor")
public class ServicioEntregaController {
	 
	@Autowired private ServicioEntregaDAO servicioEntregaDAO;
    @Autowired private RepartidorDAO RepartidorDAO;

    // Temporal: ID fijo de repartidor (luego usar sesión)
    private final Long repartidorId = 1L;

    @GetMapping("/entregas")
    public String verEntregas(Model model) {
        Repartidor repartidor = RepartidorDAO.findById(repartidorId).orElse(null);
        if (repartidor == null) return "redirect:/error";

        List<ServicioEntrega> entregas = servicioEntregaDAO.findByRepartidor(repartidor);
        model.addAttribute("entregas", entregas);
        return "entregas_repartidor";
    }

    @PostMapping("/entregas/{id}/recogido")
    public String marcarComoRecogido(@PathVariable Long id) {
        servicioEntregaDAO.findById(id).ifPresent(servicio -> {
            servicio.setRecogido(true);
            servicioEntregaDAO.save(servicio);
        });
        return "redirect:/repartidor/entregas";
    }

    @PostMapping("/entregas/{id}/entregado")
    public String marcarComoEntregado(@PathVariable Long id) {
        servicioEntregaDAO.findById(id).ifPresent(servicio -> {
            servicio.setEntregado(true);
            servicioEntregaDAO.save(servicio);
        });
        return "redirect:/repartidor/entregas";
    }
}
