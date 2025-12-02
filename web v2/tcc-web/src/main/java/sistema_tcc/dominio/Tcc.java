package sistema_tcc.dominio;

import jakarta.persistence.*;
import sistema_tcc.dominio.tipos.AreaConhecimento;
import sistema_tcc.dominio.tipos.TccStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Tcc {

    @Id
    private String id;
    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private TccStatus status;

    @Enumerated(EnumType.STRING)
    private AreaConhecimento area;

    @ManyToOne
    private Aluno autor;

    @ManyToOne
    private Professor orientador;

    @ElementCollection
    private List<String> orientacoes = new ArrayList<>();

    @ManyToMany
    private List<Professor> bancaMembros = new ArrayList<>();
    private LocalDate dataBanca;

    private Double notaFinal;
    private String parecerFinal;

    protected Tcc() {}

    public Tcc(Aluno autor, String titulo, String descricao, AreaConhecimento area) {
        this.id = UUID.randomUUID().toString();
        this.autor = autor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.area = area;
        this.status = TccStatus.PROPOSTA;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    public void aceitarOrientador(Professor p) {
        if (this.status != TccStatus.PROPOSTA) throw new IllegalStateException("TCC não está na fase de proposta.");
        this.orientador = p;
        this.status = TccStatus.EM_ANDAMENTO;
    }

    public void receberOrientacao(Professor p, String texto) {
        if (!ehOrientadoPor(p)) throw new IllegalStateException("Apenas o orientador pode registrar orientações.");
        this.orientacoes.add(LocalDate.now() + ": " + texto);
    }

    public void agendarBanca(Professor p, List<Professor> membros, LocalDate data) {
        if (!ehOrientadoPor(p)) throw new IllegalStateException("Apenas o orientador pode agendar a banca.");
        if (this.status != TccStatus.EM_ANDAMENTO) throw new IllegalStateException("TCC deve estar em andamento.");

        this.bancaMembros = membros;
        this.dataBanca = data;
        this.status = TccStatus.AGUARDANDO_BANCA;
    }

    public void receberNotaFinal(Professor p, double nota, String parecer) {
        if (!ehOrientadoPor(p)) throw new IllegalStateException("Apenas o orientador pode finalizar.");
        this.notaFinal = nota;
        this.parecerFinal = parecer;
        this.status = TccStatus.FINALIZADO;
    }

    public void reprovarOrientacao(Professor p, String motivo) {
        if (!ehOrientadoPor(p)) throw new IllegalStateException("Apenas o orientador pode reprovar.");
        this.orientacoes.add("REPROVADO: " + motivo);
        this.status = TccStatus.CANCELADO;
    }

    public boolean ehOrientadoPor(Professor p) {
        return this.orientador != null && this.orientador.getId().equals(p.getId());
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public TccStatus getStatus() { return status; }
    public Aluno getAutor() { return autor; }
    public Professor getOrientador() { return orientador; }
    public List<String> getOrientacoes() { return orientacoes; }
    public Double getNotaFinal() { return notaFinal; }
    public AreaConhecimento getArea() { return area; }
    // Novos Getters necessários para o DTO completo
    public String getParecerFinal() { return parecerFinal; }
    public LocalDate getDataBanca() { return dataBanca; }
    public List<Professor> getBancaMembros() { return bancaMembros; }
}