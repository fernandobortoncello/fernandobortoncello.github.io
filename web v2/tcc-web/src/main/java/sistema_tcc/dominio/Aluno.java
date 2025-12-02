package sistema_tcc.dominio;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.tipos.AreaConhecimento;
import sistema_tcc.dominio.tipos.Papel;

@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario {

    protected Aluno() {}

    public Aluno(String matricula, String nome, String senha) {
        super(matricula, nome, senha, Papel.ALUNO);
    }

    /**
     * Comportamento Rico: O próprio aluno sabe criar sua proposta.
     */
    public Tcc criarProposta(String titulo, String descricao, AreaConhecimento area) {
        return new Tcc(this, titulo, descricao, area);
    }
}