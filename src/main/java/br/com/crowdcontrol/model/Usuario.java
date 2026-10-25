package br.com.crowdcontrol.model;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private String departamento;
    private StatusUsuario status;
    private String telefone;
    private TipoUsuario tipo;

    // Um único construtor (antes eram dois, com e sem id). O id é definido
    // depois, pelo repositório, via setId().
    public Usuario(String nome, String email, String senha, String cargo,
                   String departamento, StatusUsuario status,
                   String telefone, TipoUsuario tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.departamento = departamento;
        this.status = status;
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCargo() { return cargo; }
    public String getDepartamento() { return departamento; }
    public StatusUsuario getStatus() { return status; }
    public String getTelefone() { return telefone; }
    public TipoUsuario getTipo() { return tipo; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setStatus(StatusUsuario status) { this.status = status; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    // A senha não aparece no toString de propósito (evita vazar em logs).
    @Override
    public String toString() {
        return nome + " - " + email;
    }
}
