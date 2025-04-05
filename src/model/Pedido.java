package model;

public class Pedido {
    private int id;
    private Cliente cliente;
    private Produto produto;
    private String dataPedido;
    private String dataEntrega;
    private double valorTotal;
    private StatusPedido status;

    public Pedido() {}

    public Pedido(int id, Cliente cliente, Produto produto, String dataPedido, String dataEntrega, double valorTotal, StatusPedido status) {
        this.id = id;
        this.cliente = cliente;
        this.produto = produto;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

    public String getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(String dataEntrega) { this.dataEntrega = dataEntrega; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    
    // Para debug ou logs
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNome() : "null") +
                ", produto=" + (produto != null ? produto.getNome() : "null") +
                ", dataPedido='" + dataPedido + '\'' +
                ", dataEntrega='" + dataEntrega + '\'' +
                ", valorTotal=" + valorTotal +
                ", status=" + status +
                '}';
    }
}
