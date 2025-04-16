package es.uclm.FlashBox.business.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private String apellidos;
	private String correo;

	@OneToOne
	private Usuario usuario;
	@ManyToMany
	private List<Restaurante> favoritos = new ArrayList<>();


	public Cliente() {
	}

	public Cliente(String nombre, String apellidos, String correo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Restaurante> getFavoritos() {
	    return favoritos;
	}

	public void setFavoritos(List<Restaurante> favoritos) {
	    this.favoritos = favoritos;
	}

}