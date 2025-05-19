package es.uclm.FlashBox.business.Services;

import org.springframework.stereotype.Service;
import es.uclm.FlashBox.business.entity.Usuario;
import es.uclm.FlashBox.business.persistence.UsuarioDAO;

@Service
public class UsuarioService {
	private UsuarioDAO usuarioDAO;

	public UsuarioService(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public Usuario autenticar(String username, String password) {
		return usuarioDAO.findByUsernameAndPassword(username, password);
	}

	public Usuario registrar(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}
}
