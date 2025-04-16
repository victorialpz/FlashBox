package es.uclm.FlashBox.business.entity;

import jakarta.persistence.*;

@Entity
public class Repartidor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private String apellidos;
	private String correo;
	private int eficiencia;
	@OneToOne
	private Usuario usuario;

	public Repartidor() {
	}

	public Repartidor(String nombre, String apellidos, String correo, int eficiencia) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
		this.eficiencia = eficiencia;
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

	public int getEficiencia() {
		return eficiencia;
	}

	public void setEficiencia(int eficiencia) {
		this.eficiencia = eficiencia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
