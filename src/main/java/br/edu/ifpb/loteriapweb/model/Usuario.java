package br.edu.ifpb.loteriapweb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Double dinheiro;

	@ElementCollection
	@Column(nullable = false)
	private List<Aposta> apostasFavoritas = new ArrayList<>();

	@NotBlank(message = "Campo obrigatório!")
	private String username;

	@NotBlank(message = "Campo obrigatório!")
	private String password;

	@NotBlank(message = "Campo obrigatório!")
	@Email(message = "Informe um email válido!")
	private String email;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Funcao> funcao;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Aposta> apostas = new ArrayList<>();

	public Usuario() {

	}

	public Double getDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(Double dinheiro) {
		this.dinheiro = dinheiro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public List<Aposta> getApostasFavoritas() {
		return apostasFavoritas;
	}

	public void adicionarApostasFavoritas(Aposta aposta) {
		this.apostasFavoritas.add(aposta);
	}

	public void removerApostasFavoritas(Aposta aposta) {
		this.apostasFavoritas.remove(aposta);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Funcao> getFuncao() {
		return funcao;
	}

	public void adicionarFuncao(Funcao role) {

		funcao.add(role);
	}

	public List<Aposta> getApostas() {
		return apostas;
	}

	public void setApostas(List<Aposta> apostas) {
		this.apostas = apostas;
	}

}
