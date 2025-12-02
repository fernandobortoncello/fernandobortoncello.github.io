package sistema_tcc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sistema_tcc.dominio.*;
import sistema_tcc.dominio.tipos.*;
import sistema_tcc.repositorio.*;

import java.util.List;

@SpringBootApplication
public class TccApplication {

    public static void main(String[] args) {
        SpringApplication.run(TccApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UsuarioRepository usuarioRepo, TccRepository tccRepo) {
        return (args) -> {
            System.out.println("--- INICIANDO CARGA DE DADOS (MOCK DB) ---");

            // 1. Criar Usuários (Atores)
            Aluno a1 = new Aluno("2025001", "Aline Espindola", "123");
            Aluno a2 = new Aluno("2025002", "Fernando Bortoncello", "123");

            Professor p1 = new Professor("111222", "Willian Bolzan", "123");
            Professor p2 = new Professor("333444", "Thiago Oliveira", "123");
            Professor p3 = new Professor("555666", "Tiago Carvalho", "123", Papel.COORDENADOR);

            usuarioRepo.saveAll(List.of(a1, a2, p1, p2, p3));

            // 2. Criar TCCs Iniciais
            //Tcc tcc1 = new Tcc(a1, "Arquitetura Pure OO", "Um estudo sobre Alan Kay", AreaConhecimento.ENGENHARIA_SOFTWARE);
            //tccRepo.save(tcc1);

            System.out.println("--- SISTEMA RODANDO ---");
            System.out.println("API disponível em http://localhost:8080/api");
        };
    }
}