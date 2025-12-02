package sistema_tcc.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sistema_tcc.dominio.Tcc;
import sistema_tcc.dominio.tipos.TccStatus;
import java.util.List;

public interface TccRepository extends JpaRepository<Tcc, String> {

    List<Tcc> findByStatus(TccStatus status);

    @Query("SELECT t FROM Tcc t WHERE t.orientador.id = :profId")
    List<Tcc> findByOrientadorId(String profId);

    @Query("SELECT t FROM Tcc t WHERE t.autor.id = :alunoId")
    Tcc findByAutorId(String alunoId);

    // Para a banca e finalização
    @Query("SELECT t FROM Tcc t WHERE t.orientador.id = :profId AND t.status = :status")
    List<Tcc> findByOrientadorAndStatus(String profId, TccStatus status);
}