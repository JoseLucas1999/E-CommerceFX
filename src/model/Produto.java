package model;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double precoBase;
    private int quantidade;

    public Produto() {}

    public Produto(int id, String nome, String descricao, double precoBase, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
        this.quantidade = quantidade;
    }
    
    public Produto(int id, String nome, String descricao, double precoBase) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
    }
    
    @Override
    public String toString() {
        return nome + " - R$" + precoBase;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPrecoBase() { return precoBase; }
    public void setPrecoBase(double precoBase) { this.precoBase = precoBase; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
