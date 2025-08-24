package br.com.taskboard.controller;

import br.com.taskboard.model.Board;
import br.com.taskboard.repository.BoardRepository;

import java.util.List;
import java.util.Scanner;

public class BoardController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BoardRepository boardRepo = new BoardRepository();

    public static void menuPrincipal() {
        while (true) {
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar board");
            System.out.println("3 - Excluir boards");
            System.out.println("4 - Sair");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: criarBoard(); break;
                case 2: selecionarBoard(); break;
                case 3: excluirBoards(); break;
                case 4: System.exit(0);
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarBoard() {
        System.out.print("Nome do board: ");
        String nome = scanner.nextLine();
        Board board = new Board();
        board.setNome(nome);
        boardRepo.salvar(board);
        System.out.println("Board criado com sucesso!");
    }

    private static void selecionarBoard() {
        List<Board> boards = boardRepo.listar();
        if (((List<?>) boards).isEmpty()) {
            System.out.println("Nenhum board disponível!");
            return;
        }
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getNome()));
        System.out.print("Escolha o ID do board: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        // Chamar menu de manipulação do board
    }

    private static void excluirBoards() {
        List<Board> boards = boardRepo.listar();
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getNome()));
        System.out.print("Escolha o ID do board para excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        boardRepo.excluir(id);
        System.out.println("Board excluído com sucesso!");
    }
}
