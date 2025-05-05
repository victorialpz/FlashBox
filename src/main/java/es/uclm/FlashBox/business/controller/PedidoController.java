// PedidoController.java
package es.uclm.FlashBox.business.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.persistence.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cliente/pedido")
public class PedidoController {

    @Autowired private RestauranteDAO restauranteDAO;
    @Autowired private ClienteDAO clienteDAO;
    @Autowired private RepartidorDAO repartidorDAO;
    @Autowired private PedidoDAO pedidoDAO;
    @Autowired private ServicioEntregaDAO servicioEntregaDAO;
    @Autowired
    private ItemMenuDAO itemMenuDAO;


    @GetMapping("/realizar/{restauranteId}")
    public String mostrarFormularioPedido(@PathVariable Long restauranteId, Model model, HttpSession session) {
        Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (restaurante == null || restaurante.getCartaMenu() == null) {
            return "redirect:/error";
        }

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("items", restaurante.getCartaMenu().getItems());
        model.addAttribute("pedido", new Pedido());
        return "pedido_form";
    }

    @PostMapping("/realizar")
    public String procesarPedido(@ModelAttribute Pedido pedido, @RequestParam List<Long> itemIds,
                                  @RequestParam Long clienteId, @RequestParam Long restauranteId,
                                  @RequestParam String direccionEntrega, Model model, HttpSession session) {
    	if (itemIds == null || itemIds.isEmpty()) {
            model.addAttribute("mensaje", "❌ Debes seleccionar al menos un producto.");
            return "redirect:/cliente/pedido/realizar/" + restauranteId;
        }
    	
        Cliente cliente = clienteDAO.findById(clienteId).orElse(null);
        Restaurante restaurante = restauranteDAO.findById(restauranteId).orElse(null);
        if (cliente == null || restaurante == null) return "redirect:/error";

        List<ItemMenu> itemsSeleccionados = new ArrayList<>();
        for (Long itemId : itemIds) {
            Optional<ItemMenu> optionalItem = itemMenuDAO.findById(itemId);
            if (optionalItem.isPresent()) {
                ItemMenu item = optionalItem.get();
                if (item.getStock() > 0) {
                    item.setStock(item.getStock() - 1);
                    itemMenuDAO.save(item);
                    itemsSeleccionados.add(item);
                } else {
                    model.addAttribute("error", "El ítem '" + item.getNombre() + "' no tiene stock.");
                    return "redirect:/cliente/pedido/realizar/" + restauranteId;
                }
            }
        }

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDireccionEntrega(direccionEntrega);
        pedido.setPagado(true);
        pedido.setItemsSeleccionados(itemsSeleccionados);

        // Selección automática de repartidor
        Repartidor repartidor = seleccionarRepartidorMasEficiente();
        ServicioEntrega servicio = new ServicioEntrega();
        servicio.setPuntoA(restaurante.getDireccion());
        servicio.setPuntoB(direccionEntrega);
        servicio.setRepartidor(repartidor);
        servicio.setEntregado(false);

        servicioEntregaDAO.save(servicio);
        pedido.setServicioEntrega(servicio);

        pedidoDAO.save(pedido);
        return "redirect:/cliente/pedido/pago/" + pedido.getId();
    }
    
    @GetMapping("/pago/{pedidoId}")
    public String mostrarPasarelaPago(@PathVariable Long pedidoId, Model model) {
        Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
        if (pedido == null) return "redirect:/error";

        model.addAttribute("pedido", pedido);
        return "pago";
    }
    
    @PostMapping("/pagar/{pedidoId}")
    public String confirmarPago(@PathVariable Long pedidoId) {
        pedidoDAO.findById(pedidoId).ifPresent(p -> {
            p.setPagado(true);
            pedidoDAO.save(p);
        });

        return "redirect:/cliente/pedido/confirmado/" + pedidoId;
    }

    @GetMapping("/confirmado/{pedidoId}")
    public String mostrarConfirmacionFinal(@PathVariable Long pedidoId, Model model) {
        Pedido pedido = pedidoDAO.findById(pedidoId).orElse(null);
        if (pedido == null) return "redirect:/error";

        model.addAttribute("repartidor", pedido.getServicioEntrega().getRepartidor());
        return "pedido_confirmado";
    }

    private Repartidor seleccionarRepartidorMasEficiente() {
        return repartidorDAO.findAll().stream()
                .sorted(Comparator.comparingInt(Repartidor::getEficiencia).reversed())
                .findFirst()
                .orElse(null);
    }
}
