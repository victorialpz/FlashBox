package es.uclm.FlashBox.business.entity;

import jakarta.persistence.*;

@Entity
public class ServicioEntrega {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String puntoA;
	private String puntoB;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean recogido = false;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean entregado = false;

	@ManyToOne
	private Repartidor repartidor;

	@OneToOne(mappedBy = "servicioEntrega")
	private Pedido pedido;

	// --- Getters y setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPuntoA() {
		return puntoA;
	}

	public void setPuntoA(String puntoA) {
		this.puntoA = puntoA;
	}

	public String getPuntoB() {
		return puntoB;
	}

	public void setPuntoB(String puntoB) {
		this.puntoB = puntoB;
	}

	public boolean isRecogido() {
		return recogido;
	}

	public void setRecogido(boolean recogido) {
		this.recogido = recogido;
	}

	public boolean isEntregado() {
		return entregado;
	}

	public void setEntregado(boolean entregado) {
		this.entregado = entregado;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
}
