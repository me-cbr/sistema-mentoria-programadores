import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorMockTest {

    @Mock
    private Agenda agendaMock;

    @Mock
    private Mentorado mentoradoMock;

    @Mock
    private SessaoMentoria sessaoPendenteMock;

    @InjectMocks
    private Mentor mentor;

    @BeforeEach
    void setUp() {
        mentor.setId(1L);
        mentor.setNome("Sofia");
        mentor.adicionarSessao(sessaoPendenteMock);
    }

    @Test
    void gerenciarDisponibilidade_QuandoHorarioDisponivel_DeveAprovarSessao() {
        LocalDateTime horarioProposto = LocalDateTime.now().plusDays(2);
        when(sessaoPendenteMock.getStatus()).thenReturn("Pendente");
        when(sessaoPendenteMock.getMentorado()).thenReturn(mentoradoMock);
        when(mentoradoMock.getNome()).thenReturn("Ana Pereira");
        when(agendaMock.getHorariosDisponiveis()).thenReturn(List.of(horarioProposto));

        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendenteMock);

        assertEquals("Status da aprovação: Aprovada com Prioridade", resultado);
        verify(agendaMock).getHorariosDisponiveis();
    }

    @Test
    void gerenciarDisponibilidade_QuandoHorarioIndisponivel_DeveRetornarErro() {
        LocalDateTime horarioProposto = LocalDateTime.now().plusDays(2);
        when(agendaMock.getHorariosDisponiveis()).thenReturn(Collections.emptyList());

        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendenteMock);

        assertEquals("Horário proposto não está disponível na agenda.", resultado);
        verify(agendaMock).getHorariosDisponiveis();
    }

    @Test
    void gerenciarDisponibilidade_QuandoSessaoNaoEstaPendente_DeveRetornarErro() {
        LocalDateTime horarioProposto = LocalDateTime.now().plusDays(2);
        when(agendaMock.getHorariosDisponiveis()).thenReturn(List.of(horarioProposto));
        when(sessaoPendenteMock.getStatus()).thenReturn("Aprovada");

        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendenteMock);

        assertEquals("Sessão não encontrada ou não está pendente.", resultado);

        verify(sessaoPendenteMock, never()).setStatus(anyString());
    }

    @Test
    void gerenciarDisponibilidade_QuandoHorarioMuitoProximo_DeveRecusarSessao() {
        LocalDateTime horarioProposto = LocalDateTime.now().plusHours(4); // Menos de 6h de antecedência
        when(agendaMock.getHorariosDisponiveis()).thenReturn(List.of(horarioProposto));
        when(sessaoPendenteMock.getStatus()).thenReturn("Pendente");

        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendenteMock);

        assertEquals("Status da aprovação: Recusada (Muito em cima da hora)", resultado);

        verify(sessaoPendenteMock).setStatus("Recusada");
    }

    @Test
    void gerenciarDisponibilidade_QuandoHorarioTemAntecedenciaMedia_DeveAprovarParaRevisao() {
        LocalDateTime horarioProposto = LocalDateTime.now().plusHours(12); // Entre 6h e 24h
        when(agendaMock.getHorariosDisponiveis()).thenReturn(List.of(horarioProposto));
        when(sessaoPendenteMock.getStatus()).thenReturn("Pendente");

        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horarioProposto, sessaoPendenteMock);

        assertEquals("Status da aprovação: Aprovada (Revisar disponibilidade)", resultado);

        verify(sessaoPendenteMock).setStatus("Aprovada");
    }
}