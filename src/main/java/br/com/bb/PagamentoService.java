package br.com.bb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.TabelaSql;
import model.Panache;

import java.time.YearMonth;
import java.util.List;
import org.jboss.logging.Logger;
import java.math.BigDecimal;

@ApplicationScoped
public class PagamentoService {

    private static final Logger log = Logger.getLogger(PagamentoService.class);

    @Inject
    Panache panache;

    @Transactional
    public void salvarPagamento(TabelaSql tabelaSql) throws Exception {


            // Validacao do numero do cartao
            if (tabelaSql.getNumeroCartao() == null
                    || !tabelaSql.getNumeroCartao().matches("^\\d{4}(-|\\s)?\\d{4}(-|\\s)?\\d{4}(-|\\s)?\\d{4}$")
                    || tabelaSql.getNumeroCartao().replaceAll("[-\\s]", "").length() > 19) {
                String mensagemDeErro = "O número do cartão informado é inválido.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            String numeroCartaoFormatado = tabelaSql.getNumeroCartao()
                    .replaceAll("[-\\s]",
                            "");
            numeroCartaoFormatado = numeroCartaoFormatado.replaceAll("(\\d{4})(?=\\d)",
                    "$1-");
            tabelaSql.setNumeroCartao(numeroCartaoFormatado);

            // Validacao do cvv
            if (tabelaSql.getCvv() == null || !tabelaSql.getCvv().matches("\\d{4}")) {
                String mensagemDeErro = "O cvv informado está em um formato inválido.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }
            // Validacao do valor do pagamento
            if (tabelaSql.getValorPagamento() == null
                    || tabelaSql.getValorPagamento().compareTo(BigDecimal.ZERO) == 0) {
                String mensagemDeErro = "O valor do pagamento não pode ser vazio ou nulo.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            String valorPagamentoString = tabelaSql.getValorPagamento()
                                                   .toPlainString();

            if (!valorPagamentoString.matches("\\d+\\.\\d{2}")) {
                String mensagemDeErro = "O valor do pagamento deve ser um número positivo com duas casas decimais separadas por um ponto";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            tabelaSql.setValorPagamento(new BigDecimal(valorPagamentoString));

            // Validacao do mes do vencimento
            if (tabelaSql.getMesVencimento() == null) {
                String mensagemDeErro = "O mês do vencimento não pode está vazio.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }
            if ((tabelaSql.getMesVencimento() < 1 || tabelaSql.getMesVencimento() > 12)) {
                String mensagemDeErro = "O mês do vencimento informado é inválido. Forneça um número de 1 a 12.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            //Validacao da validade do cartao
            YearMonth currentDate = YearMonth.now();
            YearMonth cardExpiryDate = YearMonth.of(tabelaSql.getAnoVencimento(),
                    tabelaSql.getMesVencimento());

            if (cardExpiryDate.isBefore(currentDate)) {
                String mensagemDeErro = "O cartão está fora da validade.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            // Validacao do tipo do cliente
            if (tabelaSql.getTipoCliente() == null) {
                String mensagemDeErro = "O campo tipo do cliente não pode ser vazio. Preencha 1 para CPF e 2 para CNPJ.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            if (tabelaSql.getTipoCliente() != 1 && tabelaSql.getTipoCliente() != 2) {
                String mensagemDeErro = "Tipo de cliente inválido. Preencha 1 para CPF e 2 para CNPJ.";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            // Validacao do campo cpf_cnpj
            if (tabelaSql.getCpfCnpj() == null || tabelaSql.getCpfCnpj().isEmpty()) {
                String mensagemDeErro = "Forneça um CPF (tipo do cliente = 1) ou um CNPJ (tipo do cliente = 2).";
                log.error(mensagemDeErro);
                throw new Exception(mensagemDeErro);
            }

            // Se o tipo do cliente for 1 (CPF)
            if (tabelaSql.getTipoCliente() == 1) {
                if (!validarCPF(tabelaSql.getCpfCnpj())) {
                    String mensagemDeErro = "CPF em formato inválido. Forneça um CPF no formato XXX.XXX.XXX-XX";
                    log.error(mensagemDeErro);
                    throw new Exception(mensagemDeErro);
                }

                tabelaSql.setCpfCnpj(formatarCPF(tabelaSql.getCpfCnpj()));
            }

            // Se o tipo do cliente for 2 (CNPJ)
            else if (tabelaSql.getTipoCliente() == 2) {
                if (!validarCNPJ(tabelaSql.getCpfCnpj())) {
                    String mensagemDeErro = "CNPJ em formato inválido. Forneça um CNPJ no formato XX.XXX.XXX/XXXX-XX";
                    log.error(mensagemDeErro);
                    throw new Exception(mensagemDeErro);
                }

                tabelaSql.setCpfCnpj(formatarCNPJ(tabelaSql.getCpfCnpj()));
            }

            panache.persist(tabelaSql);
    }
    // Metodos
    private boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}")
                || cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    }

    private boolean validarCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{14}")
                || cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$");
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarCNPJ(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    public List<TabelaSql> obterPagamentos () {
        return panache.listAll();
    }
}