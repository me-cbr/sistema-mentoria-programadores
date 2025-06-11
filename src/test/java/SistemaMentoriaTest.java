import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaMentoriaTest {

    private Mentor mentor;
    private Mentorado mentoradoA, mentoradoB, mentoradoC;
    private Agenda agenda;
    private AreaConhecimento area;
    private Tecnologia tecnologia;
    private SessaoMentoria sessaoPendente, sessaoAprovada, sessaoIniciada;
    private PlanoEstudo planoEstudo;
    private Meta metaPendente, metaConcluida;

    @BeforeEach
    void setup() {
        agenda = new Agenda(1L);
        area = new AreaConhecimento(1L, "Desenvolvimento Backend");
        mentor = new Mentor(1L, "Carlos Silva", "carlos.silva@email.com", "senha123", "Especialista em Java e Spring.", area, agenda);

        mentoradoA = new Mentorado(2L, "Ana Pereira", "ana.pereira@email.com", "ana123");
        mentoradoB = new Mentorado(3L, "Bruno Costa", "bruno.costa@email.com", "bruno123");
        mentoradoC = new Mentorado(4L, "Clara Souza", "clara.souza@email.com", "clara123");

        tecnologia = new Tecnologia(1L, "Java");
        planoEstudo = new PlanoEstudo(1L);

        metaPendente = new Meta(1L, "Aprender Spring Boot", "Pendente", LocalDateTime.now().plusDays(30));
        metaConcluida = new Meta(2L, "Concluir curso de JPA", "Concluída", LocalDateTime.now().minusDays(5));
        planoEstudo.adicionarMeta(metaPendente);
        planoEstudo.adicionarMeta(metaConcluida);
        mentoradoA.setPlanoEstudo(planoEstudo);

        LocalDateTime dataSessao = LocalDateTime.now().plusDays(2);
        sessaoPendente = new SessaoMentoria(1L, mentor, mentoradoA, dataSessao);
        sessaoAprovada = new SessaoMentoria(2L, mentor, mentoradoB, LocalDateTime.now().plusMinutes(10));
        sessaoAprovada.setStatus("Aprovada");
        sessaoIniciada = new SessaoMentoria(3L, mentor, mentoradoC, LocalDateTime.now().minusMinutes(40));
        sessaoIniciada.setStatus("Iniciada");

        mentor.adicionarSessao(sessaoPendente);
        mentor.adicionarSessao(sessaoAprovada);
        mentor.adicionarSessao(sessaoIniciada);
        mentor.getAgenda().adicionarHorario(dataSessao);
        mentor.getAgenda().adicionarHorario(LocalDateTime.now().plusHours(5));
        mentor.getAgenda().adicionarHorario(LocalDateTime.now().plusHours(12));
    }

    @Test
    void testUsuarioLogin() {
        assertTrue(mentor.login());

        Mentor mentorSemEmail = new Mentor(5L, "Teste", null, "senha", "Bio", area, agenda);
        assertFalse(mentorSemEmail.login());

        Mentor mentorSemSenha = new Mentor(6L, "Teste", "teste@email.com", null, "Bio", area, agenda);
        assertFalse(mentorSemSenha.login());
    }

    @Test
    void testUsuarioGettersAndSetters() {
        mentor.setId(10L);
        assertEquals(10L, mentor.getId());

        mentor.setNome("Novo Nome");
        assertEquals("Novo Nome", mentor.getNome());

        mentor.setEmail("NOVO.EMAIL@EMAIL.COM");
        assertEquals("novo.email@email.com", mentor.getEmail());

        mentor.setSenha("novaSenha456");
        assertEquals("novaSenha456", mentor.getSenha());
    }

    @Test
    void testUsuarioEqualsEHashCode() {
        Mentor mesmoMentor = new Mentor(1L, "Outro Nome", "outro@email.com", "outrasenha", "Outra Bio", area, agenda);
        Mentor mentorDiferente = new Mentor(99L, "Carlos Silva", "carlos.silva@email.com", "senha123", "Bio", area, agenda);

        assertEquals(mentor, mesmoMentor);
        assertNotEquals(mentor, mentorDiferente);
        assertNotEquals(null, mentor);
        assertNotEquals(mentor, new Object());

        assertEquals(mentor.hashCode(), mesmoMentor.hashCode());
    }

    @Test
    void testMentorAdicionarTecnologia() {
        mentor.adicionarTecnologia(tecnologia);
        assertTrue(mentor.getTecnologias().contains(tecnologia));

        int tamanhoAntes = mentor.getTecnologias().size();
        mentor.adicionarTecnologia(tecnologia);
        assertEquals(tamanhoAntes, mentor.getTecnologias().size());

        mentor.adicionarTecnologia(null);
        assertEquals(tamanhoAntes, mentor.getTecnologias().size());
    }

    @Test
    void testMentorAdicionarSessao() {
        SessaoMentoria novaSessao = new SessaoMentoria(4L, mentor, mentoradoB, LocalDateTime.now());
        mentor.adicionarSessao(novaSessao);
        assertTrue(mentor.getMinhasSessoes().contains(novaSessao));

        int tamanhoAntes = mentor.getMinhasSessoes().size();
        mentor.adicionarSessao(novaSessao);
        assertEquals(tamanhoAntes, mentor.getMinhasSessoes().size());

        mentor.adicionarSessao(null);
        assertEquals(tamanhoAntes, mentor.getMinhasSessoes().size());
    }

    @Test
    void testMentorGettersAndSetters() {
        mentor.setBiografia("Nova biografia.");
        assertEquals("Nova biografia.", mentor.getBiografia());

        AreaConhecimento novaArea = new AreaConhecimento(2L, "Frontend");
        mentor.setAreaConhecimento(novaArea);
        assertEquals(novaArea, mentor.getAreaConhecimento());

        Agenda novaAgenda = new Agenda(2L);
        mentor.setAgenda(novaAgenda);
        assertEquals(novaAgenda, mentor.getAgenda());
    }

    @Test
    void testGerenciarDisponibilidadeEAprovarSessao() {
        LocalDateTime horarioProposto = sessaoPendente.getDataHora();

        String resultadoA = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendente);
        assertEquals("Status da aprovação: Aprovada com Prioridade", resultadoA);
        assertEquals("Aprovada com Prioridade", sessaoPendente.getStatus());

        sessaoPendente.setStatus("Pendente");
        LocalDateTime horarioInvalido = LocalDateTime.now().plusDays(5);
        String resultadoInvalido = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioInvalido, sessaoPendente);
        assertEquals("Horário proposto não está disponível na agenda.", resultadoInvalido);

        SessaoMentoria sessaoB = new SessaoMentoria(5L, mentor, mentoradoB, horarioProposto);
        sessaoB.setStatus("Pendente");
        mentor.adicionarSessao(sessaoB);
        String resultadoB = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoB);
        assertEquals("Status da aprovação: Aprovada Normal", resultadoB);

        SessaoMentoria sessaoC = new SessaoMentoria(6L, mentor, mentoradoC, horarioProposto);
        sessaoC.setStatus("Pendente");
        mentor.adicionarSessao(sessaoC);
        String resultadoC = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoC);
        assertEquals("Status da aprovação: Aprovada Condicional", resultadoC);

        LocalDateTime horarioProximo = LocalDateTime.now().plusHours(5);
        SessaoMentoria sessaoProxima = new SessaoMentoria(7L, mentor, mentoradoA, horarioProximo);
        sessaoProxima.setStatus("Pendente");
        mentor.adicionarSessao(sessaoProxima);
        String resultadoRecusado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProximo, sessaoProxima);
        assertEquals("Status da aprovação: Recusada (Muito em cima da hora)", resultadoRecusado);
        assertEquals("Recusada", sessaoProxima.getStatus());

        LocalDateTime horarioMedio = LocalDateTime.now().plusHours(12);
        SessaoMentoria sessaoMedia = new SessaoMentoria(8L, mentor, mentoradoA, horarioMedio);
        sessaoMedia.setStatus("Pendente");
        mentor.adicionarSessao(sessaoMedia);
        String resultadoMedio = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioMedio, sessaoMedia);
        assertEquals("Status da aprovação: Aprovada (Revisar disponibilidade)", resultadoMedio);
        assertEquals("Aprovada", sessaoMedia.getStatus());
    }

    @Test
    void testMentoradoDarFeedback() throws FeedbackException {
        assertEquals(0, mentoradoA.getFeedbacks().size());
        mentoradoA.darFeedback(sessaoAprovada, 4, "Sessão foi boa.");
        assertEquals(1, mentoradoA.getFeedbacks().size());
        assertEquals("Sessão foi boa.", mentoradoA.getFeedbacks().get(0).getComentario());
    }

    @Test
    void testMentoradoPlanoEstudo() {
        assertNotNull(mentoradoA.getPlanoEstudo());

        PlanoEstudo novoPlano = new PlanoEstudo(2L);
        mentoradoB.setPlanoEstudo(novoPlano);
        assertEquals(novoPlano, mentoradoB.getPlanoEstudo());
    }

    @Test
    void testSessaoMentoriaGettersAndSetters() {
        sessaoPendente.setId(100L);
        assertEquals(100L, sessaoPendente.getId());

        Mentor novoMentor = new Mentor(10L, "Novo Mentor", "n@m.com", "1", "b", area, agenda);
        sessaoPendente.setMentor(novoMentor);
        assertEquals(novoMentor, sessaoPendente.getMentor());

        Mentorado novoMentorado = new Mentorado(11L, "Novo Mentorado", "n@mdo.com", "2");
        sessaoPendente.setMentorado(novoMentorado);
        assertEquals(novoMentorado, sessaoPendente.getMentorado());

        LocalDateTime novaData = LocalDateTime.now().plusYears(1);
        sessaoPendente.setDataHora(novaData);
        assertEquals(novaData, sessaoPendente.getDataHora());

        sessaoPendente.setStatus("Cancelada");
        assertEquals("Cancelada", sessaoPendente.getStatus());
    }

    @Test
    void testSessaoMentoriaIniciarEFinalizar() {
        sessaoAprovada.setDataHora(LocalDateTime.now().minusMinutes(5));
        sessaoAprovada.iniciarSessao();
        assertEquals("Iniciada", sessaoAprovada.getStatus());

        sessaoIniciada.setDataHora(LocalDateTime.now().minusMinutes(31));
        sessaoIniciada.finalizarSessao();
        assertEquals("Finalizada", sessaoIniciada.getStatus());
    }

    @Test
    void testSessaoMentoriaIniciarFalha() {
        sessaoPendente.iniciarSessao();
        assertNotEquals("Iniciada", sessaoPendente.getStatus());

        sessaoAprovada.setDataHora(LocalDateTime.now().plusHours(1));
        sessaoAprovada.iniciarSessao();
        assertNotEquals("Iniciada", sessaoAprovada.getStatus());
    }

    @Test
    void testSessaoMentoriaFinalizarFalha() {
        sessaoAprovada.finalizarSessao();
        assertNotEquals("Finalizada", sessaoAprovada.getStatus());

        sessaoIniciada.setDataHora(LocalDateTime.now().minusMinutes(20));
        sessaoIniciada.finalizarSessao();
        assertNotEquals("Finalizada", sessaoIniciada.getStatus());
    }

    @Test
    void testAtualizarStatusSessao() {
        assertTrue(sessaoPendente.atualizarStatusSessao("aprovada", ""));
        assertEquals("Aprovada", sessaoPendente.getStatus());

        sessaoAprovada.setDataHora(LocalDateTime.now());
        assertTrue(sessaoAprovada.atualizarStatusSessao("iniciada", ""));
        assertEquals("Iniciada", sessaoAprovada.getStatus());

        sessaoIniciada.setDataHora(LocalDateTime.now().minusMinutes(35));
        assertTrue(sessaoIniciada.atualizarStatusSessao("finalizada", ""));
        assertEquals("Finalizada", sessaoIniciada.getStatus());

        SessaoMentoria sessaoParaCancelar = new SessaoMentoria(10L, mentor, mentoradoA, LocalDateTime.now());
        assertTrue(sessaoParaCancelar.atualizarStatusSessao("cancelada", "Motivo: Aluno não compareceu"));
        assertEquals("Cancelada", sessaoParaCancelar.getStatus());

        assertFalse(sessaoIniciada.atualizarStatusSessao("aprovada", ""));
    }

    @Test
    void testPlanoEstudoAdicionarMeta() {
        int tamanhoInicial = planoEstudo.getMetas().size();
        Meta novaMeta = new Meta(3L, "Nova Meta", "Pendente", LocalDateTime.now());
        planoEstudo.adicionarMeta(novaMeta);
        assertEquals(tamanhoInicial + 1, planoEstudo.getMetas().size());

        planoEstudo.adicionarMeta(novaMeta);
        assertEquals(tamanhoInicial + 1, planoEstudo.getMetas().size());

        planoEstudo.adicionarMeta(null);
        assertEquals(tamanhoInicial + 1, planoEstudo.getMetas().size());
    }

    @Test
    void testPlanoEstudoAvaliarProgresso() {
        assertEquals(50.0, planoEstudo.avaliarProgresso(), 0.01);

        metaPendente.atualizarStatus("Concluída");
        assertEquals(100.0, planoEstudo.avaliarProgresso(), 0.01);

        PlanoEstudo planoVazio = new PlanoEstudo(2L);
        assertEquals(0.0, planoVazio.avaliarProgresso(), 0.01);
    }

    @Test
    void testMetaGettersSettersEAtualizarStatus() {
        metaPendente.setId(10L);
        assertEquals(10L, metaPendente.getId());

        metaPendente.setDescricao("Nova Descrição");
        assertEquals("Nova Descrição", metaPendente.getDescricao());

        LocalDateTime novoPrazo = LocalDateTime.now().plusMonths(2);
        metaPendente.setPrazo(novoPrazo);
        assertEquals(novoPrazo, metaPendente.getPrazo());

        metaPendente.atualizarStatus("Em Andamento");
        assertEquals("Em Andamento", metaPendente.getStatus());

        String statusAntigo = metaPendente.getStatus();
        metaPendente.atualizarStatus(null);
        assertEquals(statusAntigo, metaPendente.getStatus());
        metaPendente.atualizarStatus("   ");
        assertEquals(statusAntigo, metaPendente.getStatus());
    }

    @Test
    void testFeedbackConstrutorSucesso() throws FeedbackException {
        Feedback feedback = new Feedback(1L, sessaoAprovada, 4, "Bom.");
        assertNotNull(feedback);
        assertEquals(4, feedback.getAvaliacao().getNota());
        assertEquals("Bom.", feedback.getComentario());
    }

    @Test
    void testFeedbackExcecaoComentarioObrigatorio() {
        assertThrows(FeedbackException.class, () -> new Feedback(2L, sessaoAprovada, 0, null));
        assertThrows(FeedbackException.class, () -> new Feedback(2L, sessaoAprovada, 0, "  "));
        assertThrows(FeedbackException.class, () -> new Feedback(3L, sessaoAprovada, 1, null));
        assertThrows(FeedbackException.class, () -> new Feedback(4L, sessaoAprovada, 5, null));
    }

    @Test
    void testFeedbackExcecaoSessaoNula() {
        assertThrows(IllegalArgumentException.class, () -> new Feedback(5L, null, 3, "Comentário"));
    }

    @Test
    void testAvaliacaoConstrutorEValidacaoNota() {
        assertDoesNotThrow(() -> new Avaliacao(1L, 0));
        assertDoesNotThrow(() -> new Avaliacao(2L, 5));

        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(3L, -1));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(4L, 6));
    }

    @Test
    void testAvaliacaoSetNota() {
        Avaliacao avaliacao = new Avaliacao(1L, 3);
        avaliacao.setNota(5);
        assertEquals(5, avaliacao.getNota());

        assertThrows(IllegalArgumentException.class, () -> avaliacao.setNota(10));
    }

    @Test
    void testMensagemConstrutorEGetters() {
        LocalDateTime dataEnvio = LocalDateTime.now();
        Mensagem msg = new Mensagem(1L, mentor, mentoradoA, "Olá!", dataEnvio);

        assertEquals(1L, msg.getId());
        assertEquals(mentor, msg.getRemetente());
        assertEquals(mentoradoA, msg.getDestinatario());
        assertEquals("Olá!", msg.getConteudo());
        assertEquals(dataEnvio, msg.getDataEnvio());
    }

    @Test
    void testMensagemConstrutorComArgumentoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(1L, null, mentoradoA, "Oi", LocalDateTime.now()));
    }

    @Test
    void testAgendaAdicionarRemoverHorario() {
        LocalDateTime horario1 = LocalDateTime.now().plusDays(1);
        LocalDateTime horario2 = LocalDateTime.now().plusDays(2);

        agenda.adicionarHorario(horario1);
        agenda.adicionarHorario(horario2);
        assertTrue(agenda.getHorariosDisponiveis().contains(horario1));
        assertTrue(agenda.getHorariosDisponiveis().contains(horario2));

        int tamanhoAntes = agenda.getHorariosDisponiveis().size();
        agenda.adicionarHorario(horario1);
        assertEquals(tamanhoAntes, agenda.getHorariosDisponiveis().size());

        agenda.removerHorario(horario1);
        assertFalse(agenda.getHorariosDisponiveis().contains(horario1));
    }

    @Test
    void testTecnologiaEqualsEHashCode() {
        Tecnologia t1 = new Tecnologia(1L, "Java");
        Tecnologia t2 = new Tecnologia(1L, "Java");
        Tecnologia t3 = new Tecnologia(2L, "Python");

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testAreaConhecimentoEqualsEHashCode() {
        AreaConhecimento a1 = new AreaConhecimento(1L, "Backend");
        AreaConhecimento a2 = new AreaConhecimento(1L, "Backend");
        AreaConhecimento a3 = new AreaConhecimento(2L, "Frontend");

        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}