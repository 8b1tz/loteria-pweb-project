package br.edu.ifpb.loteriapweb.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.edu.ifpb.loteriapweb.enums.StatusSorteio;

@Entity
public class Sorteio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer numeroDoSorteio;
	private boolean foiSorteado;
	@NotBlank(message = "Campo obrigatório")
	@Positive(message = "Precisa ser um valor positivo")
	private Double valorDoPremio;

	@OneToMany(mappedBy = "sorteio", fetch = FetchType.EAGER, cascade = CascadeType.ALL)

	private List<Aposta> apostas = new ArrayList<>();

	private LocalDate dataParaFim;

	@ElementCollection
	private List<Integer> dezenasSorteadas = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private StatusSorteio status;

	public Sorteio() {

	}

	public boolean isFoiSorteado() {
		return foiSorteado;
	}

	public void setFoiSorteado(boolean foiSorteado) {
		this.foiSorteado = foiSorteado;
	}

	public StatusSorteio getStatus() {
		return status;
	}

	public Double getValorDoPremio() {
		return valorDoPremio;
	}

	public void setValorDoPremio(Double valorDoPremio) {
		this.valorDoPremio = valorDoPremio;
	}

	public LocalDate getDataParaFim() {
		return dataParaFim;
	}

	public void setDataParaFim(LocalDate dataParaFim) {
		this.dataParaFim = dataParaFim;
	}

	public List<Integer> getDezenasSorteadas() {
		return dezenasSorteadas;
	}

	public void adicionarDezenasSorteadas(int dezena) {
		dezenasSorteadas.add(dezena);
	}

	public void removerDezenasSorteadas(int dezena) {
		dezenasSorteadas.remove(dezena);
	}

	public void setNumeroDoSorteio(Integer numeroDoSorteio) {
		this.numeroDoSorteio = numeroDoSorteio;
	}

	public void adicionarAposta(Aposta aposta) {
		apostas.add(aposta);
	}

	public void removerAposta(Aposta aposta) {
		apostas.remove(aposta);
	}

	public List<Aposta> getApostas() {
		return apostas;
	}

	public Integer getNumeroDoSorteio() {
		return numeroDoSorteio;
	}

	public void setStatus(StatusSorteio status) {
		this.status = status;
	}

}
