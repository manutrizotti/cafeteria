package emanuellynogueiratrizotti.model;

public class Produto {
    // Atributos privados (segurança)
    private int idProduto;
    private String nome;
    private double preco;
    private String categoria;
    private int tempoPreparo;

    // Construtor vazio (necessário para alguns frameworks)
    public Produto() {}

    // Construtor com parâmetros
    public Produto(int idProduto, String nome, double preco, String categoria, int tempoPreparo) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.tempoPreparo = tempoPreparo;
    }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getTempoPreparo() { return tempoPreparo; }
    public void setTempoPreparo(int tempoPreparo) { this.tempoPreparo = tempoPreparo; }
}