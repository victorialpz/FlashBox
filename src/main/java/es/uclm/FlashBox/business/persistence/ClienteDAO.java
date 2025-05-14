package es.uclm.FlashBox.business.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uclm.FlashBox.business.entity.Cliente;

public interface ClienteDAO extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c JOIN FETCH c.favoritos WHERE c.usuario.id = :usuarioId")
    Optional<Cliente> findByUsuarioIdWithFavoritos(@Param("usuarioId") Long usuarioId);

    Optional<Cliente> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
