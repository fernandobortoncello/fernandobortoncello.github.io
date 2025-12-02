package sistema_tcc.dominio;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import sistema_tcc.dominio.tipos.Papel;

@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario {

    protected Professor() {}

    public Professor(String cpf, String nome, String senha) {
        super(cpf, nome, senha, Papel.PROFESSOR);
    }

    // Construtor para Coordenador (que também é um Professor na hierarquia)
    public Professor(String cpf, String nome, String senha, Papel papel) {
        super(cpf, nome, senha, papel);
    }

    /**
     * Comportamento Rico: O professor realiza a avaliação.
     */
    public void avaliarDefesa(Tcc tcc, double nota, String parecer) {
        tcc.receberNotaFinal(this, nota, parecer);
    }
}