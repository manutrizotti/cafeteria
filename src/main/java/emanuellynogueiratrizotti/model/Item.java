package emanuellynogueiratrizotti.model;

public class Item {
    // Campos originais do banco (Chaves e Quantidade)
    private int idComanda;
    private int idProduto;
    private int qtd;
    private int idFuncionarioCozinheiro;

    // Novos campos auxiliares para exibir os nomes na tabela da tela
    private String nomeProduto;
    private String nomeCozinheiro;

    // GETTERS E SETTERS ORIGINAIS
    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getIdFuncionarioCozinheiro() {
        return idFuncionarioCozinheiro;
    }

    public void setIdFuncionarioCozinheiro(int idFuncionarioCozinheiro) {
        this.idFuncionarioCozinheiro = idFuncionarioCozinheiro;
    }

    // NOVOS GETTERS E SETTERS PARA OS NOMES
    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNomeCozinheiro() {
        return nomeCozinheiro;
    }

    public void setNomeCozinheiro(String nomeCozinheiro) {
        this.nomeCozinheiro = nomeCozinheiro;
    }
}