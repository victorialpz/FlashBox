package es.uclm.FlashBox.business.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Repartidor;
import es.uclm.FlashBox.business.entity.Usuario;

public interface RepartidorDAO extends JpaRepository<Repartidor, Long> {
   // Optional<Usuario> findByUsername(String username);
}