import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

class FeedbackException extends Exception {
    public FeedbackException(String message) {
        super(message);
    }
}

abstract class Usuario {
    protected Long id;
    protected String nome;
    protected String email;
    protected String senha;

    public Usuario(Long id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public boolean login() {
        return this.email != null && !this.email.isEmpty() && this.senha != null && !this.senha.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase(Locale.ROOT);
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

class Mentor extends Usuario {
    private String biografia;
    private List<Tecnologia> tecnologias;
    private Agenda agenda;
    private List<SessaoMentoria> minhasSessoes;

    public Mentor(Long id, String nome, String email, String senha, String biografia, Agenda agenda) {
        super(id, nome, email, senha);
        this.biografia = biografia;
        this.tecnologias = new ArrayList<>();
        this.agenda = agenda;
        this.minhasSessoes = new ArrayList<>();
    }

    public void adicionarTecnologia(Tecnologia tecnologia) {
        if (tecnologia != null && !tecnologias.contains(tecnologia)) {
            tecnologias.add(tecnologia);
        }
    }

    public List<AreaConhecimento> getAreasConhecimento() {
        if (this.tecnologias.isEmpty()) {
            return new ArrayList<>();
        }
        return this.tecnologias.stream()
                .map(Tecnologia::getAreaConhecimento)
                .distinct()
                .collect(Collectors.toList());
    }

    public void adicionarSessao(SessaoMentoria sessao) {
        if (sessao != null && !minhasSessoes.contains(sessao)) {
            minhasSessoes.add(sessao);
        }
    }

    public String gerenciarDisponibilidadeEAprovarSessao(LocalDateTime horarioProposto, SessaoMentoria sessao) {
        String statusAprovacao = "Pendente";
        boolean horarioValido = false;
        boolean sessaoExisteEPendente = false;

        if (horarioProposto == null || sessao == null) {
            return "Erro: Horário ou sessão inválidos.";
        }

        if (agenda != null) {
            for (LocalDateTime horario : agenda.getHorariosDisponiveis()) {
                if (horario.isEqual(horarioProposto)) {
                    horarioValido = true;
                    break;
                }
            }
        }

        if (!horarioValido) {
            return "Horário proposto não está disponível na agenda.";
        }

        for (SessaoMentoria s : minhasSessoes) {
            if (s.equals(sessao) && s.getStatus().equalsIgnoreCase("Pendente")) {
                sessaoExisteEPendente = true;
                break;
            }
        }

        if (!sessaoExisteEPendente) {
            return "Sessão não encontrada ou não está pendente.";
        }

        if (horarioProposto.isAfter(LocalDateTime.now().plusHours(24))) {
            if (sessao.getMentorado().getNome().startsWith("A")) {
                statusAprovacao = "Aprovada com Prioridade";
                sessao.setStatus("Aprovada com Prioridade");
            } else if (sessao.getMentorado().getNome().startsWith("B")) {
                statusAprovacao = "Aprovada Normal";
                sessao.setStatus("Aprovada Normal");
            } else {
                statusAprovacao = "Aprovada Condicional";
                sessao.setStatus("Aprovada Condicional");
            }
        } else if (horarioProposto.isBefore(LocalDateTime.now().plusHours(6))) {
            statusAprovacao = "Recusada (Muito em cima da hora)";
            sessao.setStatus("Recusada");
        } else {
            statusAprovacao = "Aprovada (Revisar disponibilidade)";
            sessao.setStatus("Aprovada");
        }

        if (statusAprovacao.contains("Prioridade")) {
            System.out.println("DEBUG: Sessão aprovada com prioridade para mentorado ");
        } else if (statusAprovacao.contains("Recusada")) {
            System.out.println("DEBUG: Penalidade aplicada ao mentor por sessão recusada.");
        }
        return "Status da aprovação: " + statusAprovacao;
    }

    public String getBiografia() {
        return biografia;
    }

    public List<Tecnologia> getTecnologias() {
        return new ArrayList<>(tecnologias);
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public List<SessaoMentoria> getMinhasSessoes() {
        return new ArrayList<>(minhasSessoes);
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

class Mentorado extends Usuario {
    private PlanoEstudo planoEstudo;

    public Mentorado(Long id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    public PlanoEstudo getPlanoEstudo() {
        return planoEstudo;
    }

    public void setPlanoEstudo(PlanoEstudo planoEstudo) {
        this.planoEstudo = planoEstudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

class SessaoMentoria {
    private Long id;
    private Mentor mentor;
    private Mentorado mentorado;
    private LocalDateTime dataHora;
    private String status;
    private List<Feedback> feedbacks;

    public SessaoMentoria(Long id, Mentor mentor, Mentorado mentorado, LocalDateTime dataHora) {
        this.id = id;
        this.mentor = mentor;
        this.mentorado = mentorado;
        this.dataHora = dataHora;
        this.status = "Pendente";
        this.feedbacks = new ArrayList<>();
    }

    public void iniciarSessao() {
        if (this.status.equalsIgnoreCase("Aprovada") && LocalDateTime.now().isAfter(dataHora.minusMinutes(15)) && LocalDateTime.now().isBefore(dataHora.plusMinutes(60))) {
            this.status = "Iniciada";
            System.out.println("Sessão " + id + " iniciada com sucesso.");
        } else {
            System.out.println("Sessão " + id + " não pode ser iniciada. Verifique o status e o horário.");
        }
    }

    public void adicionarFeedback(Usuario autor, int nota, String comentario) throws FeedbackException {
        if (!this.status.equalsIgnoreCase("Finalizada")) {
            throw new IllegalStateException("Só é possível dar feedback após a sessão ser finalizada.");
        }

        for (Feedback f : this.feedbacks) {
            if (f.getAutor().equals(autor)) {
                throw new FeedbackException("Este usuário já forneceu um feedback para esta sessão.");
            }
        }

        long feedbackId = System.currentTimeMillis();
        Feedback novoFeedback = new Feedback(feedbackId, this, autor, nota, comentario);
        this.feedbacks.add(novoFeedback);
    }

    public List<Feedback> getFeedbacks() {
        return new ArrayList<>(feedbacks);
    }

    public boolean estaoTodosFeedbacksPresentes() {
        return feedbacks.size() == 2;
    }

    public void finalizarSessao() {
        if (this.status.equalsIgnoreCase("Iniciada") && LocalDateTime.now().isAfter(dataHora.plusMinutes(30))) {
            this.status = "Finalizada";
            System.out.println("Sessão " + id + " finalizada com sucesso.");
        } else {
            System.out.println("Sessão " + id + " não pode ser finalizada. Verifique o status e o horário.");
        }
    }

    public boolean atualizarStatusSessao(String novoStatus, String motivo) {
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            System.out.println("Novo status não pode ser nulo ou vazio.");
            return false;
        }

        String statusAtual = this.status;

        switch (novoStatus.toLowerCase()) {
            case "aprovada":
                if (statusAtual.equalsIgnoreCase("Pendente")) {
                    this.status = "Aprovada";
                    System.out.println("Sessão " + id + " aprovada com sucesso.");
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser aprovada. Status atual: " + statusAtual);
                    return false;
                }

            case "recusada":
                if (statusAtual.equalsIgnoreCase("Pendente")) {
                    this.status = "Recusada";
                    System.out.println("Sessão " + id + " recusada. Motivo: " + motivo);
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser recusada. Status atual: " + statusAtual);
                    return false;
                }

            case "iniciada":
                if (statusAtual.equalsIgnoreCase("Aprovada") && LocalDateTime.now().isAfter(dataHora.minusMinutes(15))) {
                    this.status = "Iniciada";
                    System.out.println("Sessão " + id + " iniciada com sucesso.");
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser iniciada. Status atual: " + statusAtual);
                    return false;
                }

            case "finalizada":
                if (statusAtual.equalsIgnoreCase("Iniciada") && LocalDateTime.now().isAfter(dataHora.plusMinutes(30))) {
                    this.status = "Finalizada";
                    System.out.println("Sessão " + id + " finalizada com sucesso.");
                    return true;
                } else {
                    System.out.println("Não é possível finalizar a sessão. Status: " + statusAtual + " ou duração insuficiente.");
                    return false;
                }

            case "cancelada":
                if (statusAtual.equalsIgnoreCase("Pendente") || statusAtual.equalsIgnoreCase("Aprovada")) {
                    this.status = "Cancelada";
                    System.out.println("Sessão " + id + " cancelada. Motivo: " + (motivo != null ? motivo : ""));
                    return true;
                } else {
                    System.out.println("Não é possível cancelar uma sessão com status " + statusAtual);
                    return false;
                }

            default:
                System.out.println("Status desconhecido: " + novoStatus);
                return false;
        }
    }

    public Long getId() {
        return id;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public Mentorado getMentorado() {
        return mentorado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public void setMentorado(Mentorado mentorado) {
        this.mentorado = mentorado;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessaoMentoria that = (SessaoMentoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class PlanoEstudo {
    private Long id;
    private List<Meta> metas;

    public PlanoEstudo(Long id) {
        this.id = id;
        this.metas = new ArrayList<>();
    }

    public void adicionarMeta(Meta meta) {
        if (meta != null && !metas.contains(meta)) {
            metas.add(meta);
        }
    }

    public double avaliarProgresso() {
        if (metas.isEmpty()) {
            return 0.0;
        }

        long metasConcluidas = metas.stream().filter(m -> "Concluída".equalsIgnoreCase(m.getStatus())).count();
        return (double) metasConcluidas / metas.size() * 100.0;
    }

    public Long getId() {
        return id;
    }

    public List<Meta> getMetas() {
        return new ArrayList<>(metas);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanoEstudo that = (PlanoEstudo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Meta {
    private Long id;
    private String descricao;
    private String status;
    private LocalDateTime prazo;

    public Meta(Long id, String descricao, String status, LocalDateTime prazo) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.prazo = prazo;
    }

    public void atualizarStatus(String novoStatus) {
        if (novoStatus != null && !novoStatus.trim().isEmpty()) {
            this.status = novoStatus;
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrazo(LocalDateTime prazo) {
        this.prazo = prazo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meta meta = (Meta) o;
        return Objects.equals(id, meta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Feedback {
    private Long id;
    private SessaoMentoria sessao;
    private Usuario autor;
    private Avaliacao avaliacao;
    private String comentario;

    public Feedback(Long id, SessaoMentoria sessao, Usuario autor, int nota, String comentario) throws FeedbackException {
        if (sessao == null) {
            throw new IllegalArgumentException("O Feedback deve estar associado a uma sessão.");
        }

        if (autor == null) {
            throw new IllegalArgumentException("O Feedback deve estar associado a um autor.");
        }

        if (!autor.equals(sessao.getMentor()) && !autor.equals(sessao.getMentorado())) {
            throw new SecurityException("O autor do feedback não participa da sessão de mentoria.");
        }

        boolean comentarioInvalido = (comentario == null || comentario.trim().isEmpty());
        if ((nota == 0 || nota == 1 || nota == 5) && comentarioInvalido) {
            throw new FeedbackException("Para notas 0, 1 ou 5, um comentário é obrigatório.");
        }

        this.id = id;
        this.sessao = sessao;
        this.comentario = comentario;
        this.autor = autor;
        this.avaliacao = new Avaliacao(null, nota);
    }

    public Usuario getAutor() {
        return autor;
    }

    public Long getId() {
        return id;
    }

    public SessaoMentoria getSessao() {
        return sessao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Avaliacao {
    private Long id;
    private int nota;

    public Avaliacao(Long id, int nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 5.");
        }
        this.id = id;
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public int getNota() {
        return nota;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNota(int nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 5.");
        }
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Mensagem {
    private Long id;
    private Usuario remetente;
    private Usuario destinatario;
    private String conteudo;
    private LocalDateTime dataEnvio;

    public Mensagem(Long id, Usuario remetente, Usuario destinatario, String conteudo, LocalDateTime dataEnvio) {
        if (remetente == null || destinatario == null || conteudo == null || dataEnvio == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
        this.id = id;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
    }

    public Long getId() {
        return id;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensagem mensagem = (Mensagem) o;
        return Objects.equals(id, mensagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Agenda {
    private Long id;
    private List<LocalDateTime> horariosDisponiveis;

    public Agenda(Long id) {
        this.id = id;
        this.horariosDisponiveis = new ArrayList<>();
    }

    public void adicionarHorario(LocalDateTime horario) {
        if (horario != null && !horariosDisponiveis.contains(horario)) {
            horariosDisponiveis.add(horario);
        }
    }

    public void removerHorario(LocalDateTime horario) {
        horariosDisponiveis.remove(horario);
    }

    public Long getId() {
        return id;
    }

    public List<LocalDateTime> getHorariosDisponiveis() {
        return new ArrayList<>(horariosDisponiveis);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(id, agenda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Tecnologia {
    private Long id;
    private String nome;
    private AreaConhecimento areaConhecimento;

    public Tecnologia(Long id, String nome, AreaConhecimento areaConhecimento) {
        this.id = id;
        this.nome = nome;
        this.areaConhecimento = areaConhecimento;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public void setId(Long id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnologia that = (Tecnologia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

class AreaConhecimento {
    private Long id;
    private String nome;

    public AreaConhecimento(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaConhecimento that = (AreaConhecimento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}