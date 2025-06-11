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
    private SessaoMentoria sessaoPendente, sessaoAprovada, sessaoIniciada, sessaoFinalizada;
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
        sessaoFinalizada = new SessaoMentoria(4L, mentor, mentoradoA, LocalDateTime.now().minusDays(1));
        sessaoFinalizada.setStatus("Finalizada");

        mentor.adicionarSessao(sessaoPendente);
        mentor.adicionarSessao(sessaoAprovada);
        mentor.adicionarSessao(sessaoIniciada);
        mentor.getAgenda().adicionarHorario(dataSessao);
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
    void testMentorGettersAndSetters() throws FeedbackException {
        mentor.setBiografia("Nova biografia.");
        assertEquals("Nova biografia.", mentor.getBiografia());
        AreaConhecimento novaArea = new AreaConhecimento(2L, "Frontend");
        mentor.setAreaConhecimento(novaArea);
        assertEquals(novaArea, mentor.getAreaConhecimento());
        Agenda novaAgenda = new Agenda(2L);
        mentor.setAgenda(novaAgenda);
        assertEquals(novaAgenda, mentor.getAgenda());
        mentor.darFeedback(sessaoFinalizada, 5, "Excelente!");
        assertNotNull(mentor.getFeedbacks());
        assertNotNull(mentor.getMinhasSessoes());
        mentor.adicionarTecnologia(tecnologia);
        assertNotNull(mentor.getTecnologias());
    }

    @Test
    void testMentoradoGettersAndSetters() throws FeedbackException {
        mentoradoA.darFeedback(sessaoFinalizada, 4, "Ok");
        assertNotNull(mentoradoA.getFeedbacks());
        assertNotNull(mentoradoA.getPlanoEstudo());
    }

    @Test
    void testMensagemCoverageCompleta() {
        LocalDateTime data = LocalDateTime.now();
        Mensagem msg = new Mensagem(1L, mentor, mentoradoA, "Conteúdo Inicial", data);

        msg.setId(99L);
        assertEquals(99L, msg.getId());
        msg.setRemetente(mentoradoB);
        assertEquals(mentoradoB, msg.getRemetente());
        msg.setDestinatario(mentor);
        assertEquals(mentor, msg.getDestinatario());
        msg.setConteudo("Novo Conteúdo");
        assertEquals("Novo Conteúdo", msg.getConteudo());
        LocalDateTime novaData = data.plusDays(1);
        msg.setDataEnvio(novaData);
        assertEquals(novaData, msg.getDataEnvio());

        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, null, mentoradoA, "c", data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, null, "c", data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, mentoradoA, null, data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, mentoradoA, "c", null));
    }

    @Test
    void testPlanoEstudoGettersSetters() {
        PlanoEstudo plano = new PlanoEstudo(99L);
        plano.setId(100L);
        assertEquals(100L, plano.getId());
        plano.adicionarMeta(null);
        plano.adicionarMeta(metaPendente);
        plano.adicionarMeta(metaPendente);
        assertNotNull(plano.getMetas());
    }

    @Test
    void testAgendaGettersSetters() {
        Agenda agendaTest = new Agenda(99L);
        agendaTest.setId(100L);
        assertEquals(100L, agendaTest.getId());
        LocalDateTime agora = LocalDateTime.now();
        agendaTest.adicionarHorario(agora);
        agendaTest.adicionarHorario(null);
        agendaTest.adicionarHorario(agora);
        assertNotNull(agendaTest.getHorariosDisponiveis());
        agendaTest.removerHorario(agora);
        assertTrue(agendaTest.getHorariosDisponiveis().isEmpty());
    }

    @Test
    void testAreaConhecimentoGettersSetters() {
        AreaConhecimento areaTest = new AreaConhecimento(99L, "Teste");
        areaTest.setId(100L);
        assertEquals(100L, areaTest.getId());
        areaTest.setNome("Novo Nome");
        assertEquals("Novo Nome", areaTest.getNome());
    }

    @Test
    void testTecnologiaGettersSetters() {
        Tecnologia tecTest = new Tecnologia(99L, "Teste");
        tecTest.setId(100L);
        assertEquals(100L, tecTest.getId());
        tecTest.setNome("Novo Nome");
        assertEquals("Novo Nome", tecTest.getNome());
    }

    @Test
    void testAvaliacaoGettersSetters() {
        Avaliacao aval = new Avaliacao(1L, 3);
        aval.setId(99L);
        assertEquals(99L, aval.getId());
        aval.setNota(5);
        assertEquals(5, aval.getNota());
    }

    @Test
    void testUsuarioLogin() {
        assertTrue(mentor.login());
        Mentor mentorSemEmail = new Mentor(5L, "Teste", null, "senha", "Bio", area, agenda);
        assertFalse(mentorSemEmail.login());
        Mentor mentorComEmailVazio = new Mentor(5L, "Teste", "", "senha", "Bio", area, agenda);
        assertFalse(mentorComEmailVazio.login());
        Mentor mentorSemSenha = new Mentor(6L, "Teste", "teste@email.com", null, "Bio", area, agenda);
        assertFalse(mentorSemSenha.login());
        Mentor mentorComSenhaVazia = new Mentor(6L, "Teste", "teste@email.com", "", "Bio", area, agenda);
        assertFalse(mentorComSenhaVazia.login());
    }

    @Test
    void testGerenciarDisponibilidadeEAprovarSessao() {
        LocalDateTime horarioProposto = sessaoPendente.getDataHora();
        String resultadoA = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendente);
        assertEquals("Status da aprovação: Aprovada com Prioridade", resultadoA);

        SessaoMentoria sessaoB = new SessaoMentoria(5L, mentor, mentoradoB, horarioProposto);
        mentor.adicionarSessao(sessaoB);
        String resultadoB = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoB);
        assertEquals("Status da aprovação: Aprovada Normal", resultadoB);

        SessaoMentoria sessaoC = new SessaoMentoria(6L, mentor, mentoradoC, horarioProposto);
        mentor.adicionarSessao(sessaoC);
        String resultadoC = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoC);
        assertEquals("Status da aprovação: Aprovada Condicional", resultadoC);

        LocalDateTime horarioProximo = LocalDateTime.now().plusHours(5);
        mentor.getAgenda().adicionarHorario(horarioProximo);
        SessaoMentoria sessaoProxima = new SessaoMentoria(7L, mentor, mentoradoA, horarioProximo);
        mentor.adicionarSessao(sessaoProxima);
        String resultadoRecusado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProximo, sessaoProxima);
        assertEquals("Status da aprovação: Recusada (Muito em cima da hora)", resultadoRecusado);

        LocalDateTime horarioMedio = LocalDateTime.now().plusHours(12);
        mentor.getAgenda().adicionarHorario(horarioMedio);
        SessaoMentoria sessaoMedia = new SessaoMentoria(8L, mentor, mentoradoA, horarioMedio);
        mentor.adicionarSessao(sessaoMedia);
        String resultadoMedio = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioMedio, sessaoMedia);
        assertEquals("Status da aprovação: Aprovada (Revisar disponibilidade)", resultadoMedio);
    }

    @Test
    void testGerenciarDisponibilidadeCenariosDeErro() {
        assertEquals("Erro: Horário ou sessão inválidos.", mentor.gerenciarDisponibilidadeEAprovarSessao(null, sessaoPendente));
        assertEquals("Erro: Horário ou sessão inválidos.", mentor.gerenciarDisponibilidadeEAprovarSessao(LocalDateTime.now(), null));

        LocalDateTime horarioInvalido = LocalDateTime.now().plusDays(5);
        String resultadoInvalido = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioInvalido, sessaoPendente);
        assertEquals("Horário proposto não está disponível na agenda.", resultadoInvalido);

        SessaoMentoria sessaoNaoPendente = new SessaoMentoria(9L, mentor, mentoradoA, sessaoPendente.getDataHora());
        sessaoNaoPendente.setStatus("Aprovada");
        mentor.adicionarSessao(sessaoNaoPendente);
        assertEquals("Sessão não encontrada ou não está pendente.", mentor.gerenciarDisponibilidadeEAprovarSessao(sessaoPendente.getDataHora(), sessaoNaoPendente));

        Mentor mentorSemAgenda = new Mentor(99L, "Sem Agenda", "sa@email.com", "123", "bio", area, null);
        mentorSemAgenda.adicionarSessao(sessaoPendente);
        assertEquals("Horário proposto não está disponível na agenda.", mentorSemAgenda.gerenciarDisponibilidadeEAprovarSessao(LocalDateTime.now(), sessaoPendente));
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

        SessaoMentoria sessaoAprovadaForaDaJanela = new SessaoMentoria(2L, mentor, mentoradoB, LocalDateTime.now().plusMinutes(61));
        sessaoAprovadaForaDaJanela.setStatus("Aprovada");
        sessaoAprovadaForaDaJanela.iniciarSessao();
        assertNotEquals("Iniciada", sessaoAprovadaForaDaJanela.getStatus());
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
        SessaoMentoria sessao = new SessaoMentoria(10L, mentor, mentoradoA, LocalDateTime.now());
        assertTrue(sessao.atualizarStatusSessao("aprovada", ""));

        sessao.setStatus("Pendente");
        assertTrue(sessao.atualizarStatusSessao("recusada", "Motivo"));

        sessao.setStatus("Aprovada");
        sessao.setDataHora(LocalDateTime.now());
        assertTrue(sessao.atualizarStatusSessao("iniciada", ""));

        sessao.setDataHora(LocalDateTime.now().minusMinutes(35));
        assertTrue(sessao.atualizarStatusSessao("finalizada", ""));

        sessao.setStatus("Aprovada");
        assertTrue(sessao.atualizarStatusSessao("cancelada", "Motivo"));

        sessao.setStatus("Pendente");
        assertTrue(sessao.atualizarStatusSessao("cancelada", null));
    }

    @Test
    void testAtualizarStatusSessaoTransicoesInvalidas() {
        assertFalse(sessaoFinalizada.atualizarStatusSessao("aprovada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("recusada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("iniciada", ""));
        assertFalse(sessaoIniciada.atualizarStatusSessao("iniciada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("finalizada", ""));
        assertFalse(sessaoIniciada.atualizarStatusSessao("cancelada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("desconhecido", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao(null, ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("  ", ""));
    }

    @Test
    void testFeedbackExcecaoComentarioObrigatorio() {
        assertThrows(IllegalArgumentException.class, () -> new Feedback(1L, null, 3, "comentário"));
        assertThrows(FeedbackException.class, () -> new Feedback(2L, sessaoAprovada, 0, null));
        assertThrows(FeedbackException.class, () -> new Feedback(2L, sessaoAprovada, 0, "  "));
        assertThrows(FeedbackException.class, () -> new Feedback(2L, sessaoAprovada, 0, ""));
        assertThrows(FeedbackException.class, () -> new Feedback(3L, sessaoAprovada, 1, null));
        assertThrows(FeedbackException.class, () -> new Feedback(4L, sessaoAprovada, 5, null));
        assertDoesNotThrow(() -> new Feedback(5L, sessaoAprovada, 3, null));
        assertDoesNotThrow(() -> new Feedback(6L, sessaoAprovada, 0, "Comentário válido"));
        assertDoesNotThrow(() -> new Feedback(7L, sessaoAprovada, 1, "Comentário válido"));
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
        assertNotNull(metaPendente.getPrazo());
        metaPendente.atualizarStatus("Em Andamento");
        assertEquals("Em Andamento", metaPendente.getStatus());
        String statusAntigo = metaPendente.getStatus();
        metaPendente.atualizarStatus(null);
        assertEquals(statusAntigo, metaPendente.getStatus());
        metaPendente.atualizarStatus("   ");
        assertEquals(statusAntigo, metaPendente.getStatus());
    }

    @Test
    void testAvaliacaoConstrutorEValidacaoNota() {
        assertDoesNotThrow(() -> new Avaliacao(1L, 0));
        assertDoesNotThrow(() -> new Avaliacao(2L, 5));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(3L, -1));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(4L, 6));
        Avaliacao aval = new Avaliacao(1L, 3);
        assertThrows(IllegalArgumentException.class, () -> aval.setNota(10));
    }

    @Test
    void testSessaoMentoriaGettersSetters() {
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
        assertNotNull(sessaoPendente.getMentor());
        assertNotNull(sessaoPendente.getMentorado());
        assertNotNull(sessaoPendente.getDataHora());
        assertNotNull(sessaoPendente.getStatus());
    }

    @Test
    void testUsuarioEqualsEHashCodeCompleto() {
        Usuario clone = new Mentor(1L, "Clone", "clone@email.com", "senha", "Bio", area, agenda);
        Usuario diferente = new Mentor(99L, "Diferente", "dif@email.com", "senha", "Bio", area, agenda);
        assertTrue(mentor.equals(mentor));
        assertTrue(mentor.equals(clone));
        assertEquals(mentor.hashCode(), clone.hashCode());
        assertFalse(mentor.equals(diferente));
        assertNotEquals(mentor.hashCode(), diferente.hashCode());
        assertFalse(mentor.equals(null));
        assertFalse(mentor.equals(new Object()));
    }

    @Test
    void testMentorEqualsEHashCodeCompleto() {
        Mentor clone = new Mentor(1L, "Carlos Silva", "carlos.silva@email.com", "senha123", "Especialista em Java e Spring.", area, agenda);
        Mentor diferente = new Mentor(99L, "Diferente", "dif@email.com", "senha", "Bio", area, agenda);
        assertTrue(mentor.equals(clone));
        assertEquals(mentor.hashCode(), clone.hashCode());
        assertFalse(mentor.equals(diferente));
        assertFalse(mentor.equals(new Mentorado(1L, "Não é mentor", "e@e.com", "s")));
    }

    @Test
    void testMentoradoEqualsEHashCodeCompleto() {
        Mentorado clone = new Mentorado(2L, "Clone", "c@email.com", "s");
        Mentorado diferente = new Mentorado(99L, "Diferente", "d@email.com", "s");
        assertTrue(mentoradoA.equals(clone));
        assertEquals(mentoradoA.hashCode(), clone.hashCode());
        assertFalse(mentoradoA.equals(diferente));
        assertFalse(mentoradoA.equals(new Mentor(2L, "Não é mentorado", "e@e.com", "s", "b", area, agenda)));
    }

    @Test
    void testSessaoMentoriaEqualsEHashCodeCompleto() {
        SessaoMentoria clone = new SessaoMentoria(1L, mentor, mentoradoA, sessaoPendente.getDataHora());
        SessaoMentoria diferente = new SessaoMentoria(99L, mentor, mentoradoA, sessaoPendente.getDataHora());
        assertTrue(sessaoPendente.equals(clone));
        assertEquals(sessaoPendente.hashCode(), clone.hashCode());
        assertFalse(sessaoPendente.equals(diferente));
        assertFalse(sessaoPendente.equals(null));
        assertFalse(sessaoPendente.equals(new Object()));
    }

    @Test
    void testPlanoEstudoEqualsEHashCodeCompleto() {
        PlanoEstudo clone = new PlanoEstudo(1L);
        PlanoEstudo diferente = new PlanoEstudo(99L);
        assertTrue(planoEstudo.equals(clone));
        assertEquals(planoEstudo.hashCode(), clone.hashCode());
        assertFalse(planoEstudo.equals(diferente));
        assertFalse(planoEstudo.equals(null));
        assertFalse(planoEstudo.equals(new Object()));
    }

    @Test
    void testMetaEqualsEHashCodeCompleto() {
        Meta clone = new Meta(1L, "Desc", "Status", LocalDateTime.now());
        Meta diferente = new Meta(99L, "Desc", "Status", LocalDateTime.now());
        assertTrue(metaPendente.equals(clone));
        assertEquals(metaPendente.hashCode(), clone.hashCode());
        assertFalse(metaPendente.equals(diferente));
        assertFalse(metaPendente.equals(null));
        assertFalse(metaPendente.equals(new Object()));
    }

    @Test
    void testFeedbackEqualsEHashCodeCompleto() throws FeedbackException {
        Feedback feedback = new Feedback(1L, sessaoAprovada, 4, "Bom.");
        Feedback clone = new Feedback(1L, sessaoAprovada, 3, "Ok.");
        Feedback diferente = new Feedback(99L, sessaoAprovada, 4, "Bom.");

        assertTrue(feedback.equals(clone));
        assertEquals(feedback.hashCode(), clone.hashCode());
        assertFalse(feedback.equals(diferente));
        assertFalse(feedback.equals(null));
        assertFalse(feedback.equals(new Object()));
        assertNotNull(feedback.getSessao());
        assertNotNull(feedback.getAvaliacao());
        assertNotNull(feedback.getComentario());
        assertNotNull(feedback.getId());
    }

    @Test
    void testAvaliacaoEqualsEHashCodeCompleto() {
        Avaliacao aval = new Avaliacao(1L, 3);
        Avaliacao clone = new Avaliacao(1L, 5);
        Avaliacao diferente = new Avaliacao(99L, 3);
        assertTrue(aval.equals(clone));
        assertEquals(aval.hashCode(), clone.hashCode());
        assertFalse(aval.equals(diferente));
        assertFalse(aval.equals(null));
        assertFalse(aval.equals(new Object()));
    }

    @Test
    void testMensagemEqualsEHashCodeCompleto() {
        Mensagem msg = new Mensagem(1L, mentor, mentoradoA, "Olá!", LocalDateTime.now());
        Mensagem clone = new Mensagem(1L, mentor, mentoradoB, "Tchau", LocalDateTime.now());
        Mensagem diferente = new Mensagem(99L, mentor, mentoradoA, "Olá!", LocalDateTime.now());
        assertTrue(msg.equals(clone));
        assertEquals(msg.hashCode(), clone.hashCode());
        assertFalse(msg.equals(diferente));
        assertFalse(msg.equals(null));
        assertFalse(msg.equals(new Object()));
    }

    @Test
    void testAgendaEqualsEHashCodeCompleto() {
        Agenda a1 = new Agenda(1L);
        Agenda a1Clone = new Agenda(1L);
        Agenda a2 = new Agenda(2L);
        assertTrue(a1.equals(a1Clone));
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(null));
        assertFalse(a1.equals(new Object()));
        assertEquals(a1.hashCode(), a1Clone.hashCode());
    }

    @Test
    void testAreaConhecimentoEqualsEHashCodeCompleto() {
        AreaConhecimento a1 = new AreaConhecimento(1L, "Backend");
        AreaConhecimento a1Clone = new AreaConhecimento(1L, "Backend");
        AreaConhecimento a2 = new AreaConhecimento(2L, "Frontend");
        assertTrue(a1.equals(a1Clone));
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(null));
        assertFalse(a1.equals(new Object()));
        assertEquals(a1.hashCode(), a1Clone.hashCode());
    }

    @Test
    void testTecnologiaEqualsEHashCodeCompleto() {
        Tecnologia t1 = new Tecnologia(1L, "Java");
        Tecnologia t1Clone = new Tecnologia(1L, "Java");
        Tecnologia t2 = new Tecnologia(2L, "Python");
        assertTrue(t1.equals(t1Clone));
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(t2));
        assertFalse(t1.equals(null));
        assertFalse(t1.equals(new Object()));
        assertEquals(t1.hashCode(), t1Clone.hashCode());
    }
}