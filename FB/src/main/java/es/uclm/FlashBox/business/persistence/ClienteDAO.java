package es.uclm.FlashBox.business.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Cliente;
import es.uclm.FlashBox.business.entity.Usuario;

public interface ClienteDAO extends JpaRepository<Cliente, Long> {
   // Optional<Usuario> findByUsername(String username);

}
