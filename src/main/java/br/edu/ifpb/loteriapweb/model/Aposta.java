package br.edu.ifpb.loteriapweb.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.edu.ifpb.loteriapweb.enums.StatusAposta;

@Entity
public class Aposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)

	private Sorteio sorteio;
	private Boolean isFavorito;

	public Boolean getIsFavorito() {
		return isFavorito;
	}

	public void setIsFavorito(Boolean isFavorito) {
		this.isFavorito = isFavorito;
	}

	@Enumerated(EnumType.STRING)
	private StatusAposta status;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@ElementCollection
	private List<Integer> numeros = new ArrayList<>();

	public Aposta(Sorteio sorteio, List<Integer> numeros) {
		this.sorteio = sorteio;
		this.numeros = numeros;
	}

	public Aposta() {

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNumeros(List<Integer> numeros) {
		this.numeros = numeros;
	}

	public Integer getId() {
		return id;
	}

	public void adicionarNumero(Integer numero) {
		numeros.add(numero);
	}

	public void removerNumero(Integer numero) {
		numeros.remove(numero);
	}

	public Sorteio getSorteio() {
		return sorteio;
	}

	public void setSorteio(Sorteio sorteio) {
		this.sorteio = sorteio;
	}

	public List<Integer> getNumeros() {
		return numeros;
	}

	public StatusAposta getStatus() {
		return status;
	}

	public void setStatus(StatusAposta status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Aposta [id=" + id + ", sorteio=" + sorteio + ", numeros=" + numeros + "]";
	}

}
