import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private char[][] tabuleiro;
    private ObjectOutputStream jogadorOutput;
    private ObjectInputStream jogadorInput;

    public Client() {
        tabuleiro = new char[10][10];
    }

    public void conectar(String enderecoServidor, int porta) {
        try (Socket socket = new Socket(enderecoServidor, porta)) {
            jogadorOutput = new ObjectOutputStream(socket.getOutputStream());
            jogadorInput = new ObjectInputStream(socket.getInputStream());

            iniciarPartida();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarPartida() {
        Scanner scanner = new Scanner(System.in);

        try {
            tabuleiro = (char[][]) jogadorInput.readObject();

            while (true) {
                System.out.println("\nTabuleiro do Jogador:");
                exibirTabuleiro(tabuleiro, true);

                System.out.print("Digite as coordenadas para atirar (exemplo: A5): ");
                String coordenadas = scanner.nextLine();
                coordenadas = coordenadas.toUpperCase();

                jogadorOutput.writeObject(coordenadas);
                jogadorOutput.reset();

                String resultado = (String) jogadorInput.readObject();
                System.out.println("Resultado do tiro: " + resultado);

                if (resultado.equals("ÁGUA") || resultado.equals("NAVIO ATINGIDO")) {
                    System.out.println("Aguarde a vez do outro jogador...");
                } else if (resultado.equals("NAVIO AFUNDADO")) {
                    System.out.println("Parabéns! Você afundou um navio.");
                } else if (resultado.equals("VITÓRIA")) {
                    System.out.println("Parabéns! Você venceu o jogo.");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void exibirTabuleiro(char[][] tabuleiro, boolean mostrarNavios) {
        System.out.print("  ");

        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.print((char) ('A' + i) + " ");

            for (int j = 0; j < 10; j++) {
                char c = tabuleiro[i][j];

                if (c == 'X' && !mostrarNavios) {
                    c = '~';
                }

                System.out.print(c + " ");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.conectar("localhost", 8080);
    }
}
