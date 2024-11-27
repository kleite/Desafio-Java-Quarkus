package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private int idPagamento;

    @JsonProperty("numero_cartao")
    private String numeroCartao;

    @JsonProperty("tipo_cliente")
    private Byte tipoCliente;

    @JsonProperty("cpf_cnpj")
    private String cpfCnpj;

    @JsonProperty("mes_vencimento")
    private Integer mesVencimento;

    @JsonProperty("ano_vencimento")
    private Integer anoVencimento;

    private String cvv;

    @JsonProperty("valor_pagamento")
    private BigDecimal valorPagamento;

    // Getters e setters
    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public Byte getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(Byte tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Integer getMesVencimento() {
        return mesVencimento;
    }

    public void setMesVencimento(Integer mesVencimento) {
        this.mesVencimento = mesVencimento;
    }

    public Integer getAnoVencimento() {
        return anoVencimento;
    }

    public void setAnoVencimento(Integer anoVencimento) {
        this.anoVencimento = anoVencimento;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }
}
