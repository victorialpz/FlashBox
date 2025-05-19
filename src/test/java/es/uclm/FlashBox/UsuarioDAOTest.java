package es.uclm.FlashBox;

import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.enums.Rol;
import es.uclm.FlashBox.business.persistence.UsuarioDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UsuarioDAOTest {

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Test
	public void testGuardarYRecuperarUsuarioPorUsernameYPassword() {
		Usuario usuario = new Usuario();
		usuario.setUsername("usuarioTest");
		usuario.setPassword("claveTest");
		usuario.setRol(Rol.CLIENTE);
		usuario.setNombre("Nombre");
		usuario.setApellidos("Apellido");
		usuario.setCorreo("correo@ejemplo.com");

		usuarioDAO.save(usuario);

		Usuario encontrado = usuarioDAO.findByUsernameAndPassword("usuarioTest", "claveTest");

		assertNotNull(encontrado);
		assertEquals("usuarioTest", encontrado.getUsername());
		assertEquals("correo@ejemplo.com", encontrado.getCorreo());
		assertEquals(Rol.CLIENTE, encontrado.getRol());
	}

	@Test
	public void testLoginFallidoDevuelveNull() {
		Usuario usuario = new Usuario();
		usuario.setUsername("usuarioLogin");
		usuario.setPassword("passCorrecta");
		usuario.setRol(Rol.REPARTIDOR);

		usuarioDAO.save(usuario);

		Usuario fallo = usuarioDAO.findByUsernameAndPassword("usuarioLogin", "claveIncorrecta");
		assertNull(fallo);
	}
}
