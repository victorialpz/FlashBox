package es.uclm.FlashBox.business.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ServicioEntrega {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String puntoA;
	private String puntoB;
	
	private boolean recogido;
	private boolean entregado;

	@ManyToOne
	private Repartidor repartidor;

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

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public boolean isEntregado() {
		return entregado;
	}

	public void setEntregado(boolean entregado) {
		this.entregado = entregado;
	}

	public boolean isRecogido() {
		return recogido;
	}

	public void setRecogido(boolean recogido) {
		this.recogido = recogido;
	}
	

}
