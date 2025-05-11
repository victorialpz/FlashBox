package es.uclm.FlashBox.business.controller;

import es.uclm.FlashBox.business.entity.*;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.uclm.FlashBox.business.Services.UsuarioService;

@Controller
public class RegController {

    @Autowired private UsuarioDAO usuarioDAO;
    @Autowired private ClienteDAO clienteDAO;
    @Autowired private RepartidorDAO repartidorDAO;
    @Autowired private RestauranteDAO restauranteDAO;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
                                   @RequestParam(required = false) String tipoRestaurante,
                                   Model model) {

        // Validar campos obligatorios (básico)
        if (usuario.getUsername() == null || usuario.getUsername().isBlank() ||
            usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "registro";
        }

        // Validación simple de duplicados por username
        if (usuarioDAO.findAll().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(usuario.getUsername()))) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "registro";
        }

        usuarioDAO.save(usuario);

        switch (usuario.getRol()) {
            case RESTAURANTE -> {
                CartaMenu carta = new CartaMenu();
                carta.setNombre("Carta de " + usuario.getNombre());

                Restaurante restaurante = new Restaurante();
                restaurante.setNombre(usuario.getNombre());
                restaurante.setTelefono(usuario.getTelefono());
                restaurante.setDireccion("Dirección pendiente");
                restaurante.setCartaMenu(carta);
                restaurante.setUsuario(usuario);
                restaurante.setTipo(tipoRestaurante);
                usuario.setRestaurante(restaurante);

                restauranteDAO.save(restaurante);
            }

            case CLIENTE -> {
                Cliente cliente = new Cliente();
                cliente.setNombre(usuario.getNombre());
                cliente.setApellidos(usuario.getApellidos());
                cliente.setCorreo(usuario.getCorreo());
                cliente.setUsuario(usuario);
                usuario.setCliente(cliente);

                clienteDAO.save(cliente);
            }

            case REPARTIDOR -> {
                Repartidor repartidor = new Repartidor();
                repartidor.setNombre(usuario.getNombre());
                repartidor.setApellidos(usuario.getApellidos());
                repartidor.setCorreo(usuario.getCorreo());
                repartidor.setEficiencia(100);
                repartidor.setUsuario(usuario);
                usuario.setRepartidor(repartidor);

                repartidorDAO.save(repartidor);
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpSession session, Model model) {

        try {
            username = username.trim();
            password = password.trim();

            Usuario usuario = usuarioService.autenticar(username, password);

            if (usuario == null) {
                model.addAttribute("error", "Credenciales incorrectas");
                return "login";
            }

            usuario = usuarioDAO.findById(usuario.getId()).orElse(null);
            session.setAttribute("usuario", usuario);

            if (usuario.getRol() == Rol.RESTAURANTE) {
                Restaurante r = usuario.getRestaurante();
                if (r == null || r.getId() == null) return "redirect:/error";
                return "redirect:/restaurante/menu/" + r.getId();
            }

            if (usuario.getRol() == Rol.CLIENTE) return "redirect:/home";
            if (usuario.getRol() == Rol.REPARTIDOR) return "redirect:/repartidor/entregas";

            return "redirect:/error";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
