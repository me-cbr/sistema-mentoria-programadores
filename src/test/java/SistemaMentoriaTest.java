import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaMentoriaTest {
   private Agenda agendaA, agendaB, agendaC, agendaD, agendaE, agendaF, agendaG, agendaH, agendaI, agendaJ, agendaK;
   private LocalDateTime horarioA, horarioB, horarioC, horarioD, horarioE, horarioF, dataEnvio, prazoA, prazoB, prazoC, prazoD, prazoE, dataHoraFutura, dataHoraPassada;
   private Mentor mentorA, mentorB, mentorC, mentorD, mentorE, mentorF;
   private Mentorado mentoradoA, mentoradoB, mentoradoC, mentoradoD, mentoradoE, mentoradoF;
   private SessaoMentoria sessaoA, sessaoB, sessaoC, sessaoD, sessaoE, sessaoF;
   private AreaConhecimento areaA, areaB, areaC, areaD;
   private Tecnologia tecA, tecB, tecC, tecD, tecE, tecF, tecG;
   private Usuario usuarioA, usuarioB, remetenteA, remetenteB;
   private PlanoEstudo planoA;
   private Meta metaA, metaB, metaC, metaD, metaE;

    @BeforeEach
    void setUp() {
        agendaA = new Agenda(1L);
        agendaB = new Agenda(2L);
        agendaC = new Agenda(3L);
        agendaD = new Agenda(4L);
        agendaE = new Agenda(5L);
        agendaF = new Agenda(6L);
        agendaG = new Agenda(7L);
        agendaH = new Agenda(8L);
        agendaI = new Agenda(9L);
        agendaJ = new Agenda(10L);
        agendaK = new Agenda(11L);
        horarioA = LocalDateTime.of(2025, 5, 30, 10, 1);
        horarioB = LocalDateTime.of(2025, 5, 31, 11, 2);
        horarioC = LocalDateTime.of(2025, 6, 1, 12, 3);
        horarioD = LocalDateTime.of(2025, 6, 2, 13, 4);
        horarioE = LocalDateTime.of(2025, 6, 3, 14, 5);
        horarioF = LocalDateTime.of(2025, 6, 12, 23, 14);
        dataEnvio = LocalDateTime.of(2025, 6, 4, 15, 6);
        prazoA = LocalDateTime.of(2025, 6, 5, 16, 7);
        prazoB = LocalDateTime.of(2025, 6, 6, 17, 8);
        prazoC = LocalDateTime.of(2025, 6, 7, 18, 9);
        prazoD = LocalDateTime.of(2025, 6, 8, 19, 10);
        prazoE = LocalDateTime.of(2025, 6, 9, 20, 11);
        dataHoraFutura = LocalDateTime.of(2025, 6, 10, 21, 12);
        dataHoraPassada = LocalDateTime.of(2025, 6, 11, 22, 13);
        mentorA = new Mentor(1L, "Carlos", "carlos@mentor.com", "carlos123", "Desenvolvedor de Software e Professor de Java", areaA, agendaA);
        mentorB = new Mentor(2L, "Juliana", "juliana@mentor.com", "juliana123", "Especialista em Inteligência Artificial", areaA, agendaB);
        mentorC = new Mentor(3L, "Roberto", "roberto@mentor.com", "roberto123", "Especialista em Ciência de Dados", areaA, agendaC);
        mentorD = new Mentor(4L, "Claudia", "claudia@mentor.com", "claudia123", "Especialista em Cibersegurança", areaA, agendaD);
        mentorE = new Mentor(5L, "Fernanda", "fernanda@mentor.com", "fernanda123", "Desenvolvedora Front-end e Professora de JavaScript", areaB, agendaE);
        mentorF = new Mentor(6L, "Eduardo", "eduardo@mentor.com", "eduardo123", "Desenvolvedor Full Stack e Professor de React", areaC, agendaF);
        mentoradoA = new Mentorado(1L, "Dominique", "dominique@mentorado.com", "dominique123");
        mentoradoB = new Mentorado(2L, "Gabriela", "gabriela@mentorado.com", "gabriela123");
        mentoradoC = new Mentorado(3L, "Maria Antonia", "maria.antonia@mentorado.com", "maria123");
        mentoradoD = new Mentorado(4L, "Silvia Maria", "silvia.maria@mentorado.com", "silvia123");
        mentoradoE = new Mentorado(5L, "Vitoria", "vitoria@mentorado.com", "vitoria123");
        mentoradoF = new Mentorado(6L, "Livia", "livia@mentorado.com", "livia123");
        sessaoA = new SessaoMentoria(1L, mentorA, mentoradoA,horarioA);
        sessaoB = new SessaoMentoria(2L, mentorB, mentoradoB, horarioB);
        sessaoC = new SessaoMentoria(3L, mentorC, mentoradoC, horarioC);
        sessaoD = new SessaoMentoria(4L, mentorD, mentoradoD, horarioD);
        sessaoE = new SessaoMentoria(5L, mentorA, mentoradoE, horarioE);
        sessaoF = new SessaoMentoria(6L, mentorB, mentoradoF, horarioF);
        areaA = new AreaConhecimento(1L, "Desenvolvimento Backend");
        areaB = new AreaConhecimento(2L, "Desenvolvimento Frontend");
        areaC = new AreaConhecimento(3L, "Desenvolvimento Full Stack");
        areaD = new AreaConhecimento(4L, "Desenvolvimento de Jogos");
        tecA = new Tecnologia(1L, "Java");
        tecB = new Tecnologia(2L, "IA");
        tecC = new Tecnologia(3L, "Banco de Dados");
        tecD = new Tecnologia(4L, "Python");
        tecE = new Tecnologia(5L, "JavaScript");
        tecF = new Tecnologia(6L, "React");
        tecG = new Tecnologia(7L, "Jogos Digitais");
        usuarioA = new Mentor (7L, "Adriana", "adriana@mentor.com", "adriana123", "Especialista em Desenvolvimento de Software", areaA, agendaG);
        usuarioB = new Mentorado(7L, "Marcos", "marcos@mentorado.com", "marcos123");
        remetenteA = new Mentor(8L, "Carmen Lucia", "carmen.lucia@mentor.com", "carmen123", "Desenvolvedora de Jogos", areaD, agendaH);
        remetenteB = new Mentorado(8L, "Charles", "charles@mentorado.com", "charles123");
        planoA = new PlanoEstudo(1L);
        metaA = new Meta(1L, "Aprender Backend", "Pendente", prazoA);
        metaB = new Meta(2L, "Aprender Frontend", "Concluída", prazoB);
        metaC = new Meta(3L, "Aprender todas as linguagens", "Em Andamento", prazoC);
        metaD = new Meta(4L, "Aprender Java", "Pendente", prazoD);
        metaE = new Meta(5L, "Aprender Python", "Em andamento", prazoE);
    }

}
