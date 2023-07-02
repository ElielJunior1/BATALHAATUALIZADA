import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private char[][] tabuleiro;
    private ObjectOutputStream jogadorOutput;
    private ObjectInputStream jogadorInput;

    public Server() {
        tabuleiro = new char[10][10];
    }

    public void iniciar(int porta) {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor aguardando conexões na porta " + porta + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nova conexão estabelecida.");

                jogadorOutput = new ObjectOutputStream(socket.getOutputStream());
                jogadorInput = new ObjectInputStream(socket.getInputStream());

                iniciarPartida();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarPartida() {
        inicializarTabuleiro();

        while (true) {
            try {
                enviarTabuleiro();

                String coordenadas = (String) jogadorInput.readObject();
                System.out.println("Jogador atirou em: " + coordenadas);

                String resultado = realizarAtaque(coordenadas);
                System.out.println("Resultado do tiro: " + resultado);

                jogadorOutput.writeObject(resultado);
                jogadorOutput.reset();

                if (resultado.equals("VITÓRIA")) {
                    System.out.println("Jogador venceu o jogo.");
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void inicializarTabuleiro() {
        // Lógica para inicializar o tabuleiro do jogo
        // ...

        tabuleiro = new char[10][10];
    }

    private void enviarTabuleiro() throws IOException {
        jogadorOutput.writeObject(tabuleiro);
        jogadorOutput.reset();
    }

    private String realizarAtaque(String coordenadas) {
        // Lógica para processar o ataque do jogador e atualizar o tabuleiro
        // ...

        return "ÁGUA";
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciar(8080);
    }
}
