package br.com.valiantus.model;
public class Cliente {
    private int id;
    private String nome, cpf, telefone, email, endereco, cidade, estado;
    public Cliente(int id,String nome,String cpf,String telefone,String email,String endereco,String cidade,String estado) {
        this.id=id;this.nome=nome;this.cpf=cpf;this.telefone=telefone;this.email=email;this.endereco=endereco;this.cidade=cidade;this.estado=estado;
    }
    public int getId(){return id;}
    public String getNome(){return nome;}
    public String getCpf(){return cpf;}
    public String getTelefone(){return telefone;}
    public String getEmail(){return email;}
    public String getEndereco(){return endereco;}
    public String getCidade(){return cidade;}
    public String getEstado(){return estado;}
}
