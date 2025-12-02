package sistema_tcc.dominio;

import jakarta.persistence.*;
import sistema_tcc.dominio.tipos.Papel;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Usuario {

    @Id
    protected String id; // CPF ou Matrícula
    protected String nome;
    protected String senha;

    @Enumerated(EnumType.STRING)
    protected Papel papel;

    // JPA exige construtor vazio
    protected Usuario() {}

    public Usuario(String id, String nome, String senha, Papel papel) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.papel = papel;
    }

    // Lógica de Domínio (Autenticação)
    public boolean autenticar(String tentativaSenha) {
        return this.senha.equals(tentativaSenha);
    }

    // Getters (apenas leitura para o mundo exterior)
    public String getId() { return id; }
    public String getNome() { return nome; }
    public Papel getPapel() { return papel; }
}