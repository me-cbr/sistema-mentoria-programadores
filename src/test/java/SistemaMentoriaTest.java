import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaMentoriaTest {

    private Mentor mentor;
    private Mentorado mentoradoA, mentoradoB, mentoradoC, mentoradoNaoDaSessao;
    private Agenda agenda;
    private AreaConhecimento areaBackend, areaFrontend;
    private Tecnologia tecJava, tecSpringBoot, tecReact;
    private SessaoMentoria sessaoPendente, sessaoAprovada, sessaoIniciada, sessaoFinalizada, sessaoParaTestes;
    private PlanoEstudo planoEstudo;
    private Meta metaPendente, metaConcluida;

    @BeforeEach
    void setup() {
        areaBackend = new AreaConhecimento(1L, "Desenvolvimento Backend");
        areaFrontend = new AreaConhecimento(2L, "Desenvolvimento Frontend");
        tecJava = new Tecnologia(1L, "Java", areaBackend);
        tecSpringBoot = new Tecnologia(2L, "Spring Boot", areaBackend);
        tecReact = new Tecnologia(3L, "React", areaFrontend);
        agenda = new Agenda(1L);
        mentor = new Mentor(1L, "Carlos Silva", "carlos.silva@email.com", "senha123", "Especialista em Java e Spring.", agenda);
        mentor.adicionarTecnologia(tecJava);
        mentor.adicionarTecnologia(tecSpringBoot);
        mentoradoA = new Mentorado(2L, "Ana Pereira", "ana.pereira@email.com", "ana123");
        mentoradoB = new Mentorado(3L, "Bruno Costa", "bruno.costa@email.com", "bruno123");
        mentoradoC = new Mentorado(4L, "Clara Souza", "clara.souza@email.com", "clara123");
        mentoradoNaoDaSessao = new Mentorado(5L, "Visitante", "visitante@email.com", "vis123");
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
        sessaoParaTestes = new SessaoMentoria(10L, mentor, mentoradoA, LocalDateTime.now());
        mentor.adicionarSessao(sessaoPendente);
        mentor.adicionarSessao(sessaoAprovada);
        mentor.adicionarSessao(sessaoIniciada);
        mentor.adicionarSessao(sessaoFinalizada);
        mentor.getAgenda().adicionarHorario(dataSessao);
    }

    @Test
    void testSessaoMentoriaIniciarFalhaForaDaJanelaDeFim() {
        SessaoMentoria sessaoAtrasada = new SessaoMentoria(99L, mentor, mentoradoA, LocalDateTime.now().minusMinutes(70));
        sessaoAtrasada.setStatus("Aprovada");
        sessaoAtrasada.iniciarSessao();
        assertNotEquals("Iniciada", sessaoAtrasada.getStatus());
    }

    @Test
    void testUsuarioGettersSetters() {
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
    void testUsuarioLogin() {
        assertTrue(mentor.login());
        Mentor mentorSemEmail = new Mentor(5L, "Teste", null, "senha", "Bio", new Agenda(5L));
        assertFalse(mentorSemEmail.login());
        Mentor mentorComEmailVazio = new Mentor(5L, "Teste", "", "senha", "Bio", new Agenda(5L));
        assertFalse(mentorComEmailVazio.login());
        Mentor mentorSemSenha = new Mentor(6L, "Teste", "teste@email.com", null, "Bio", new Agenda(6L));
        assertFalse(mentorSemSenha.login());
        Mentor mentorComSenhaVazia = new Mentor(6L, "Teste", "teste@email.com", "", "Bio", new Agenda(6L));
        assertFalse(mentorComSenhaVazia.login());
    }

    @Test
    void testUsuarioEqualsEHashCode() {
        Usuario clone = new Mentor(1L, "Clone", "clone@email.com", "senha", "Bio", new Agenda(1L));
        Usuario diferente = new Mentor(99L, "Diferente", "dif@email.com", "senha", "Bio", new Agenda(99L));
        assertTrue(mentor.equals(mentor));
        assertTrue(mentor.equals(clone));
        assertEquals(mentor.hashCode(), clone.hashCode());
        assertFalse(mentor.equals(diferente));
        assertNotEquals(mentor.hashCode(), diferente.hashCode());
        assertFalse(mentor.equals(null));
        assertFalse(mentor.equals(new Object()));
        assertFalse(mentor.equals("uma string"));
    }

    @Test
    void testMentorGettersSetters() {
        mentor.setBiografia("Nova biografia.");
        assertEquals("Nova biografia.", mentor.getBiografia());
        Agenda novaAgenda = new Agenda(2L);
        mentor.setAgenda(novaAgenda);
        assertEquals(novaAgenda, mentor.getAgenda());
        assertNotNull(mentor.getMinhasSessoes());
        assertNotNull(mentor.getTecnologias());
    }

    @Test
    void testMentorAdicionarTecnologia() {
        int tamanhoInicial = mentor.getTecnologias().size();
        mentor.adicionarTecnologia(tecReact);
        assertEquals(tamanhoInicial + 1, mentor.getTecnologias().size());
        mentor.adicionarTecnologia(tecReact);
        assertEquals(tamanhoInicial + 1, mentor.getTecnologias().size());
        mentor.adicionarTecnologia(null);
        assertEquals(tamanhoInicial + 1, mentor.getTecnologias().size());
    }

    @Test
    void testMentorAdicionarSessao() {
        int tamanhoInicial = mentor.getMinhasSessoes().size();
        SessaoMentoria novaSessao = new SessaoMentoria(99L, mentor, mentoradoA, LocalDateTime.now());
        mentor.adicionarSessao(novaSessao);
        assertEquals(tamanhoInicial + 1, mentor.getMinhasSessoes().size());
        mentor.adicionarSessao(novaSessao);
        assertEquals(tamanhoInicial + 1, mentor.getMinhasSessoes().size());
        mentor.adicionarSessao(null);
        assertEquals(tamanhoInicial + 1, mentor.getMinhasSessoes().size());
    }

    @Test
    void testMentorGetAreasConhecimento() {
        assertEquals(1, mentor.getAreasConhecimento().size());
        assertEquals(areaBackend, mentor.getAreasConhecimento().get(0));
        mentor.adicionarTecnologia(tecReact);
        List<AreaConhecimento> areas = mentor.getAreasConhecimento();
        assertEquals(2, areas.size());
        assertTrue(areas.contains(areaBackend));
        assertTrue(areas.contains(areaFrontend));
        Mentor mentorSemTecnologia = new Mentor(99L, "Iniciante", "ini@email.com", "123", "bio", new Agenda(99L));
        assertTrue(mentorSemTecnologia.getAreasConhecimento().isEmpty());
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
        assertEquals("Horário proposto não está disponível na agenda.", mentor.gerenciarDisponibilidadeEAprovarSessao(horarioInvalido, sessaoPendente));
        SessaoMentoria sessaoNaoPendente = new SessaoMentoria(9L, mentor, mentoradoA, sessaoPendente.getDataHora());
        sessaoNaoPendente.setStatus("Aprovada");
        mentor.adicionarSessao(sessaoNaoPendente);
        assertEquals("Sessão não encontrada ou não está pendente.", mentor.gerenciarDisponibilidadeEAprovarSessao(sessaoPendente.getDataHora(), sessaoNaoPendente));
        Mentor mentorSemAgenda = new Mentor(99L, "Sem Agenda", "sa@email.com", "123", "bio", null);
        mentorSemAgenda.adicionarSessao(sessaoPendente);
        assertEquals("Horário proposto não está disponível na agenda.", mentorSemAgenda.gerenciarDisponibilidadeEAprovarSessao(LocalDateTime.now(), sessaoPendente));
    }

    @Test
    void testMentorEqualsEHashCode() {
        Mentor clone = new Mentor(1L, "Carlos Silva", "carlos.silva@email.com", "senha123", "Especialista em Java e Spring.", agenda);
        Mentor diferente = new Mentor(99L, "Diferente", "dif@email.com", "senha", "Bio", new Agenda(99L));
        assertTrue(mentor.equals(clone));
        assertTrue(mentor.equals(mentor));
        assertEquals(mentor.hashCode(), clone.hashCode());
        assertFalse(mentor.equals(diferente));
        assertFalse(mentor.equals(new Mentorado(1L, "Não é mentor", "e@e.com", "s")));
    }

    @Test
    void testMentoradoGettersSetters() {
        assertNotNull(mentoradoA.getPlanoEstudo());
        PlanoEstudo novoPlano = new PlanoEstudo(99L);
        mentoradoA.setPlanoEstudo(novoPlano);
        assertEquals(novoPlano, mentoradoA.getPlanoEstudo());
    }

    @Test
    void testMentoradoEqualsEHashCode() {
        Mentorado clone = new Mentorado(2L, "Clone", "c@email.com", "s");
        Mentorado diferente = new Mentorado(99L, "Diferente", "d@email.com", "s");
        assertTrue(mentoradoA.equals(clone));
        assertTrue(mentoradoA.equals(mentoradoA));
        assertEquals(mentoradoA.hashCode(), clone.hashCode());
        assertFalse(mentoradoA.equals(diferente));
    }

    @Test
    void testSessaoMentoriaGettersSetters() {
        sessaoParaTestes.setId(100L);
        assertEquals(100L, sessaoParaTestes.getId());
        Mentor novoMentor = new Mentor(10L, "Novo Mentor", "n@m.com", "1", "b", new Agenda(10L));
        sessaoParaTestes.setMentor(novoMentor);
        assertEquals(novoMentor, sessaoParaTestes.getMentor());
        Mentorado novoMentorado = new Mentorado(11L, "Novo Mentorado", "n@mdo.com", "2");
        sessaoParaTestes.setMentorado(novoMentorado);
        assertEquals(novoMentorado, sessaoParaTestes.getMentorado());
        LocalDateTime novaData = LocalDateTime.now().plusYears(1);
        sessaoParaTestes.setDataHora(novaData);
        assertEquals(novaData, sessaoParaTestes.getDataHora());
        assertNotNull(sessaoParaTestes.getMentor());
        assertNotNull(sessaoParaTestes.getMentorado());
        assertNotNull(sessaoParaTestes.getDataHora());
        assertNotNull(sessaoParaTestes.getStatus());
        assertTrue(sessaoParaTestes.getFeedbacks().isEmpty());
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
        SessaoMentoria sessaoAprovadaForaDaJanela = new SessaoMentoria(2L, mentor, mentoradoB, LocalDateTime.now().plusMinutes(20));
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
    void testAdicionarFeedback() throws FeedbackException {
        assertFalse(sessaoFinalizada.getTodosFeedbacks());
        sessaoFinalizada.adicionarFeedback(mentoradoA, 4, "Sessão foi muito boa.");
        assertEquals(1, sessaoFinalizada.getFeedbacks().size());
        assertEquals(mentoradoA, sessaoFinalizada.getFeedbacks().get(0).getAutor());
        assertFalse(sessaoFinalizada.getTodosFeedbacks());
        sessaoFinalizada.adicionarFeedback(mentor, 5, "Mentorado participativo.");
        assertEquals(2, sessaoFinalizada.getFeedbacks().size());
        assertTrue(sessaoFinalizada.getTodosFeedbacks());
        assertThrows(IllegalStateException.class, () -> sessaoPendente.adicionarFeedback(mentor, 5, "Comentário"));
        assertThrows(FeedbackException.class, () -> sessaoFinalizada.adicionarFeedback(mentor, 4, "Segundo comentário."));
        assertThrows(SecurityException.class, () -> sessaoFinalizada.adicionarFeedback(mentoradoNaoDaSessao, 3, "Eu nem estava lá."));
    }

    @Test
    void testAtualizarStatusSessao() {
        assertTrue(sessaoParaTestes.atualizarStatusSessao("aprovada", ""));
        sessaoParaTestes.setStatus("Pendente");
        assertTrue(sessaoParaTestes.atualizarStatusSessao("recusada", "Motivo"));
        sessaoParaTestes.setStatus("Aprovada");
        sessaoParaTestes.setDataHora(LocalDateTime.now().minusMinutes(1));
        assertTrue(sessaoParaTestes.atualizarStatusSessao("iniciada", ""));
        sessaoParaTestes.setDataHora(LocalDateTime.now().minusMinutes(35));
        assertTrue(sessaoParaTestes.atualizarStatusSessao("finalizada", ""));
        sessaoParaTestes.setStatus("Aprovada");
        assertTrue(sessaoParaTestes.atualizarStatusSessao("cancelada", "Motivo"));
        sessaoParaTestes.setStatus("Pendente");
        assertTrue(sessaoParaTestes.atualizarStatusSessao("cancelada", null));
    }

    @Test
    void testAtualizarStatusSessaoTransicoesInvalidas() {
        assertFalse(sessaoFinalizada.atualizarStatusSessao("aprovada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("recusada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("iniciada", ""));
        assertFalse(sessaoIniciada.atualizarStatusSessao("iniciada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("finalizada", ""));
        assertFalse(sessaoIniciada.atualizarStatusSessao("cancelada", ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao(null, ""));
        assertFalse(sessaoFinalizada.atualizarStatusSessao("  ", ""));
        assertFalse(sessaoParaTestes.atualizarStatusSessao("status_invalido", ""));
    }

    @Test
    void testAtualizarStatusSessaoFalhaPorTempo(){
        SessaoMentoria sessaoFutura = new SessaoMentoria(11L, mentor, mentoradoA, LocalDateTime.now().plusMinutes(20));
        sessaoFutura.setStatus("Aprovada");
        assertFalse(sessaoFutura.atualizarStatusSessao("iniciada", ""));

        SessaoMentoria sessaoRecemIniciada = new SessaoMentoria(12L, mentor, mentoradoA, LocalDateTime.now().minusMinutes(10));
        sessaoRecemIniciada.setStatus("Iniciada");
        assertFalse(sessaoRecemIniciada.atualizarStatusSessao("finalizada", ""));
    }

    @Test
    void testSessaoMentoriaEqualsEHashCode() {
        SessaoMentoria clone = new SessaoMentoria(1L, mentor, mentoradoA, sessaoPendente.getDataHora());
        SessaoMentoria diferente = new SessaoMentoria(99L, mentor, mentoradoA, sessaoPendente.getDataHora());
        assertTrue(sessaoPendente.equals(clone));
        assertTrue(sessaoPendente.equals(sessaoPendente));
        assertEquals(sessaoPendente.hashCode(), clone.hashCode());
        assertFalse(sessaoPendente.equals(diferente));
        assertFalse(sessaoPendente.equals(null));
        assertFalse(sessaoPendente.equals(new Object()));
    }

    @Test
    void testPlanoEstudoGettersSetters() {
        PlanoEstudo plano = new PlanoEstudo(99L);
        plano.setId(100L);
        assertEquals(100L, plano.getId());
        plano.adicionarMeta(null);
        plano.adicionarMeta(metaPendente);
        plano.adicionarMeta(metaPendente);
        assertEquals(1, plano.getMetas().size());
        assertNotNull(plano.getMetas());
    }

    @Test
    void testPlanoEstudoAvaliarProgresso() {
        assertEquals(50.0, planoEstudo.avaliarProgresso(), 0.01);
        metaPendente.setStatus("Concluída");
        assertEquals(100.0, planoEstudo.avaliarProgresso(), 0.01);
        PlanoEstudo planoVazio = new PlanoEstudo(2L);
        assertEquals(0.0, planoVazio.avaliarProgresso(), 0.01);
    }

    @Test
    void testPlanoEstudoEqualsEHashCode() {
        PlanoEstudo clone = new PlanoEstudo(1L);
        PlanoEstudo diferente = new PlanoEstudo(99L);
        assertTrue(planoEstudo.equals(clone));
        assertTrue(planoEstudo.equals(planoEstudo));
        assertEquals(planoEstudo.hashCode(), clone.hashCode());
        assertFalse(planoEstudo.equals(diferente));
        assertFalse(planoEstudo.equals(null));
        assertFalse(planoEstudo.equals(new Object()));
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
    void testMetaEqualsEHashCode() {
        Meta clone = new Meta(1L, "Desc", "Status", LocalDateTime.now());
        Meta diferente = new Meta(99L, "Desc", "Status", LocalDateTime.now());
        assertTrue(metaPendente.equals(clone));
        assertTrue(metaPendente.equals(metaPendente));
        assertEquals(metaPendente.hashCode(), clone.hashCode());
        assertFalse(metaPendente.equals(diferente));
        assertFalse(metaPendente.equals(null));
        assertFalse(metaPendente.equals(new Object()));
    }

    @Test
    void testFeedbackConstrutorEValidacoes() {
        assertThrows(IllegalArgumentException.class, () -> new Feedback(1L, null, mentor, 5, "c"));
        assertThrows(IllegalArgumentException.class, () -> new Feedback(1L, sessaoFinalizada, null, 5, "c"));
        assertThrows(SecurityException.class, () -> new Feedback(1L, sessaoFinalizada, mentoradoNaoDaSessao, 5, "c"));
        assertThrows(FeedbackException.class, () -> new Feedback(1L, sessaoFinalizada, mentor, 5, null));
        assertThrows(FeedbackException.class, () -> new Feedback(1L, sessaoFinalizada, mentor, 1, ""));
        assertThrows(FeedbackException.class, () -> new Feedback(1L, sessaoFinalizada, mentor, 0, "  "));
        assertDoesNotThrow(() -> new Feedback(1L, sessaoFinalizada, mentor, 3, null));
        assertDoesNotThrow(() -> new Feedback(1L, sessaoFinalizada, mentor, 2, ""));
        assertDoesNotThrow(() -> new Feedback(1L, sessaoFinalizada, mentor, 4, "  "));
    }

    @Test
    void testFeedbackGetters() throws FeedbackException {
        Feedback feedback = new Feedback(99L, sessaoFinalizada, mentor, 4, "Bom.");
        assertEquals(99L, feedback.getId());
        assertEquals(sessaoFinalizada, feedback.getSessao());
        assertEquals(mentor, feedback.getAutor());
        assertEquals("Bom.", feedback.getComentario());
        assertNotNull(feedback.getAvaliacao());
        assertEquals(4, feedback.getAvaliacao().getNota());
    }

    @Test
    void testFeedbackEqualsEHashCode() throws FeedbackException {
        Feedback feedback = new Feedback(1L, sessaoFinalizada, mentor, 4, "Bom.");
        Feedback clone = new Feedback(1L, sessaoFinalizada, mentoradoA, 3, "Ok.");
        Feedback diferente = new Feedback(99L, sessaoFinalizada, mentor, 4, "Bom.");
        assertTrue(feedback.equals(clone));
        assertTrue(feedback.equals(feedback));
        assertEquals(feedback.hashCode(), clone.hashCode());
        assertFalse(feedback.equals(diferente));
        assertFalse(feedback.equals(null));
        assertFalse(feedback.equals(new Object()));
    }

    @Test
    void testFeedbackException() {
        FeedbackException ex = new FeedbackException("Teste");
        assertEquals("Teste", ex.getMessage());
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
    void testAvaliacaoConstrutorEValidacaoNota() {
        assertDoesNotThrow(() -> new Avaliacao(1L, 0));
        assertDoesNotThrow(() -> new Avaliacao(2L, 5));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(3L, -1));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(4L, 6));
        Avaliacao aval = new Avaliacao(1L, 3);
        assertThrows(IllegalArgumentException.class, () -> aval.setNota(10));
        assertThrows(IllegalArgumentException.class, () -> aval.setNota(-2));
        assertDoesNotThrow(() -> aval.setNota(4));
    }

    @Test
    void testAvaliacaoEqualsEHashCode() {
        Avaliacao aval = new Avaliacao(1L, 3);
        Avaliacao clone = new Avaliacao(1L, 5);
        Avaliacao diferente = new Avaliacao(99L, 3);
        assertTrue(aval.equals(clone));
        assertTrue(aval.equals(aval));
        assertEquals(aval.hashCode(), clone.hashCode());
        assertFalse(aval.equals(diferente));
        assertFalse(aval.equals(null));
        assertFalse(aval.equals(new Object()));
    }

    @Test
    void testMensagemGettersSetters() {
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
        assertNotNull(msg.getRemetente());
        assertNotNull(msg.getDestinatario());
        assertNotNull(msg.getConteudo());
        assertNotNull(msg.getDataEnvio());
    }

    @Test
    void testMensagemConstrutor() {
        LocalDateTime data = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, null, mentoradoA, "c", data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, null, "c", data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, mentoradoA, null, data));
        assertThrows(IllegalArgumentException.class, () -> new Mensagem(2L, mentor, mentoradoA, "c", null));
    }

    @Test
    void testMensagemEqualsEHashCode() {
        LocalDateTime data = LocalDateTime.now();
        Mensagem msg = new Mensagem(1L, mentor, mentoradoA, "Olá!", data);
        Mensagem clone = new Mensagem(1L, mentor, mentoradoB, "Tchau", data);
        Mensagem diferente = new Mensagem(99L, mentor, mentoradoA, "Olá!", data);
        assertTrue(msg.equals(clone));
        assertTrue(msg.equals(msg));
        assertEquals(msg.hashCode(), clone.hashCode());
        assertFalse(msg.equals(diferente));
        assertFalse(msg.equals(null));
        assertFalse(msg.equals(new Object()));
    }

    @Test
    void testAgendaGettersSetters() {
        Agenda agendaTest = new Agenda(99L);
        agendaTest.setId(100L);
        assertEquals(100L, agendaTest.getId());
        assertNotNull(agendaTest.getHorariosDisponiveis());
    }

    @Test
    void testAgendaAdicionarRemoverHorario() {
        Agenda agendaTest = new Agenda(99L);
        LocalDateTime agora = LocalDateTime.now();
        agendaTest.adicionarHorario(agora);
        assertEquals(1, agendaTest.getHorariosDisponiveis().size());
        agendaTest.adicionarHorario(null);
        agendaTest.adicionarHorario(agora);
        assertEquals(1, agendaTest.getHorariosDisponiveis().size());
        agendaTest.removerHorario(agora.plusDays(1));
        assertEquals(1, agendaTest.getHorariosDisponiveis().size());
        agendaTest.removerHorario(agora);
        assertTrue(agendaTest.getHorariosDisponiveis().isEmpty());
    }

    @Test
    void testAgendaEqualsEHashCode() {
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
    void testTecnologiaGettersSetters() {
        Tecnologia tecTest = new Tecnologia(99L, "Teste", areaFrontend);
        tecTest.setId(100L);
        assertEquals(100L, tecTest.getId());
        tecTest.setNome("Novo Nome");
        assertEquals("Novo Nome", tecTest.getNome());
        tecTest.setAreaConhecimento(areaBackend);
        assertEquals(areaBackend, tecTest.getAreaConhecimento());
    }

    @Test
    void testTecnologiaEqualsEHashCode() {
        Tecnologia t1 = new Tecnologia(1L, "Java", areaBackend);
        Tecnologia t1Clone = new Tecnologia(1L, "Java", areaBackend);
        Tecnologia t2 = new Tecnologia(2L, "Python", areaBackend);
        assertTrue(t1.equals(t1Clone));
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(t2));
        assertFalse(t1.equals(null));
        assertFalse(t1.equals(new Object()));
        assertEquals(t1.hashCode(), t1Clone.hashCode());
    }

    @Test
    void testAreaConhecimentoGettersSetters() {
        AreaConhecimento areaTest = new AreaConhecimento(99L, "Teste");
        areaTest.setId(100L);
        assertEquals(100L, areaTest.getId());
        areaTest.setNome("Novo Nome");
        assertEquals("Novo Nome", areaTest.getNome());
        assertNotNull(areaTest.getNome());
    }

    @Test
    void testAreaConhecimentoEqualsEHashCode() {
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
}