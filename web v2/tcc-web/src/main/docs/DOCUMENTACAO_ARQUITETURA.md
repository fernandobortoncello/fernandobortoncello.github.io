Documentação Técnica - Arquitetura Versão 6.0

1. Filosofia: Objetos Ricos (Rich Domain Model)

A maioria dos sistemas Spring Boot usa o padrão "Transaction Script" (Serviços Anêmicos), onde as entidades (@Entity) têm apenas Getters e Setters, e uma classe TccService contém toda a lógica (if status == ...).

Neste projeto, adotamos o Rich Domain Model:

A Lógica está na Entidade: A classe Tcc.java sabe se pode ser orientada, se pode ter banca, e como calcular sua nota.

Encapsulamento: Os métodos setStatus ou setNota são privados ou inexistentes. Para mudar o status, você deve chamar métodos de negócio como aceitarOrientador(professor) ou finalizar(professor, nota).

Exemplo Prático

Errado (Tradicional):

// No Service
public void orientar(String idTcc, String idProf) {
Tcc tcc = repo.findById(idTcc);
if (tcc.getStatus() != PROPOSTA) throw new Error(); // Lógica vazada
tcc.setOrientador(idProf);
tcc.setStatus(EM_ANDAMENTO);
repo.save(tcc);
}


Certo (Neste Projeto):

// No Controller
public void orientar(String idTcc, String idProf) {
Tcc tcc = repo.findById(idTcc);
Professor prof = userRepo.findById(idProf);

    tcc.aceitarOrientador(prof); // A regra está AQUI dentro
    
    repo.save(tcc);
}


2. Estrutura de Classes e Responsabilidades

2.1. Pacote sistema_tcc.dominio

Contém o coração do sistema. Estas classes não dependem de Spring, Web ou Banco de Dados (exceto anotações JPA).

Usuario (Abstrata): Classe base para autenticação. Usa estratégia JOINED ou SINGLE_TABLE no banco.

Herança: Aluno e Professor estendem Usuario. Isso permite polimorfismo (ex: buscar um usuário pelo ID sem saber se é aluno ou professor).

Tcc: A classe principal. Gerencia o ciclo de vida (Proposta -> Em Andamento -> Banca -> Finalizado).

Regra: Possui o método ehOrientadoPor(professor) para garantir segurança. Ninguém mexe no TCC se não for o dono ou o orientador.

2.2. Pacote sistema_tcc.web (Controllers)

Substitui a camada de UI. Recebe requisições HTTP (JSON).

AuthController: Endpoint simples para validar login. Retorna um DTO com o papel do usuário.

TccController: Fachada para todas as operações.

Segurança: Recebe o user-id no Header para simular uma sessão. Em produção, isso seria substituído por um Token JWT.

2.3. Pacote sistema_tcc.dto

Objetos de Transferência de Dados.

Por que usar? Nunca retornamos a entidade Tcc inteira (com suas relações de banco de dados) diretamente para o Frontend. Isso causa loops infinitos de JSON e expõe dados internos.

Solução: O TccDTO é um record (imutável) que copia apenas os dados necessários para a tela.

3. Fluxo de Manutenção (How-To)

Cenário A: Adicionar um novo campo (ex: "Data de Nascimento")

Domínio: Adicione o campo private LocalDate dataNascimento; na classe Usuario.java.

Banco: O Hibernate atualizará a tabela automaticamente (ddl-auto=update).

DTO: Se o campo precisa aparecer na tela, adicione-o ao UsuarioLogadoDTO.

Controller: No AuthController, preencha esse dado no DTO.

Cenário B: Adicionar uma nova regra (ex: "TCC não pode ter nota menor que 0")

NÃO coloque if (nota < 0) no Controller ou no JavaScript.

Vá para dominio/Tcc.java.

No método receberNotaFinal, adicione a validação. Assim, a regra fica protegida para sempre, mesmo se criarmos uma nova tela ou API.

Cenário C: O Frontend não mostra um dado

Verifique se o dado existe no Banco (H2 Console).

Verifique se o dado foi mapeado no método TccDTO.from(Tcc t). É ali que a entidade vira JSON.