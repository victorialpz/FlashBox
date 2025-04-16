package es.uclm.FlashBox.business.persistence;

import es.uclm.FlashBox.business.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPassword(String username, String password);
}
