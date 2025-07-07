package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static final String PATH_LEVELS = "levels/level";
	private static final String PATH_SCORES = "scores/score";

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui; // Referencia para ImageMatrixGUI (janela de interface com o utilizador)
	private List<GameElement> gameElements;

	private int level = 0;

	private String playerName;

	private GameEngine() {
		gameElements = new ArrayList<>();
	}
	
	// Devolve a Lista dos Elementos do jogo
	public List<GameElement> getGameElements(){
		return gameElements;

	}

	// Devolve o singleton
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	public void start() {

		// Setup inicial da janela que faz a interface com o utilizador
		// algumas coisas poderiam ser feitas no main, mas estes passos tem sempre que
		// ser feitos!

		gui = ImageMatrixGUI.getInstance(); // 1. obter instancia ativa de ImageMatrixGUI
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI

		setNamePlayer();
		gui.setMessage("Bem-vindo " + this.playerName + "!" + System.lineSeparator() + "Boa Sorte!");
		gui.setMessage("Press 'R' to Restart " + System.lineSeparator() + "Press 'ESC' to Exit" + System.lineSeparator()
				+ "Press 'ENTER' to Continue");

		sendImagesToGUI();
		setLevelStatusMessage();
		gui.update();
	}

	@Override
	public void update(Observed source) {
		gui.update();
		int key = gui.keyPressed(); // obtem o codigo da tecla pressionada

		if (key == KeyEvent.VK_ESCAPE)
			leftGame(); // inicia o processo de saida do jogo

		if (key == KeyEvent.VK_R)
			restart(); // renicia o nivel

		else if (Direction.isDirection(key)) {
			Empilhadora.getInstance().move(key); // atualiza a empilhadora
			checkBoxesOnTarget(); // verifica se o nivel tá concluido
		}

		gui.update(); // atualiza a imagem

	}

	// envia as imagens para a gui
	private void sendImagesToGUI() {
		gui.addImages(createElements(level));
	}

	// renicia a gui do nivel atual
	private void refresh() {
		gui.clearImages();						// reset à gui
		this.gameElements = new ArrayList<>();	// reset à gameElements
		Empilhadora.resetInstance();			// reset à empilhadora
		sendImagesToGUI(); 						// envia as imagens à gui
		setLevelStatusMessage();				// atualiza a status bar
	}

	// passo que manda avançar de nivel se existir
	private void nextLevel() {
		int newLevel = this.level + 1;
		File file_level = new File ("levels/level" + newLevel +".txt");
		updateScoreTxt();						// atualiza o ficheiro score do nivel
		if (file_level.exists()) { 
			gui.setMessage(statusToString());	// apresenta o scoreboard
			this.level++;
			refresh();
		}
		else  {
			gameOver();
		}
	}
	
	
	// passo que envia as mensagens de fim de jogo e termina
	private void gameOver() {
		gui.setMessage(statusToString()); 		// apresenta o scoreboard
		gui.setStatusMessage("Sokoban");	        
		gui.setMessage("             GAME OVER");
		System.exit(0);							// desliga o jogo
	}
	
	// passo que sai do jogo e termina se o jogador confirmar
	private void leftGame() {
		gui.setStatusMessage("Sokoban");	        
		String s = gui.askUser("Quer mesmo ir embora?" + System.lineSeparator() + "Insere 'S' se tens a certeza!");	// questiona o jogador se quer mesmo sair
		if (s.equals("s") || s.equals("S")) {
			gui.setMessage("             Adeus :("); 
			System.exit(0);						// desliga o jogo
		}
	}
	
	// lê o ficheiro score e transforma-o numa string 
	private String statusToString() { 
		String statusMsg = "Scoreboard level " + this.level + System.lineSeparator();
		try { 
			statusMsg += String.format("%-10s%-18s%s", "Pos", "Nome", "Moves") + System.lineSeparator(); // formata a linha com 10*" " entre pos e   
																										 // nome e 18*" " entre nome e moves
			Scanner fileScanner = new Scanner(new File(PATH_SCORES + this.level));
			
			while (fileScanner.hasNextLine()) {	
				String[] statsLine = fileScanner.nextLine().split(" ");
				statsLine[1] = formatName(statsLine[1]);
				
				statusMsg += String.format("%-13s%-16s%s", statsLine[0], statsLine[1], statsLine[2]);	// formata a linha com 13*" " entre pos e  
				 																						// nome e 16*" " entre nome e moves
				if (fileScanner.hasNextLine()) statusMsg += System.lineSeparator();						
			}
			fileScanner.close();
		}catch(FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
		return statusMsg;
	}
	
	// cria a lista de elementos referente a cada nivel
	private List<ImageTile> createElements(int level) {
		List<ImageTile> tileList = new ArrayList<>();
		GameElement element = null;
		int lineNumber = 0; // (y)
		try {
			Scanner fileScanner = new Scanner(new File(PATH_LEVELS + level + ".txt"));
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				for (int x = 0; x < line.length(); x++) {
					tileList.add(new Chao(new Point2D(x, lineNumber))); // adiciona o chao para as imagens que não tem
																		// chao
					element = GameElement.createElement(line.charAt(x), new Point2D(x, lineNumber));
					tileList.add(element);
					gameElements.add(element);
				}
				lineNumber++;
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
		return tileList;

	}

	// devolve a lista de elementos na posicao
	public List<GameElement> getElementsAtPosition(Point2D p) {
		List<GameElement> list = new ArrayList<>();
		for (GameElement g : gameElements) {
			if (g.getPosition().equals(p))
				;
			list.add(g);
		}
		return list;

	}


	// atualiza o cabeçalho
	public void setLevelStatusMessage() {
		gui.setStatusMessage("Sokoban             Level: " + level + "             Energia: "
				+ Empilhadora.getInstance().getEnergy() + "             Moves: " + Empilhadora.getInstance().getMoves()
				+ "         Press 'R' to Restart");
	}

	// passo que renicia o nivel
	public void restart() {
		refresh();
	}


	// remove o elemento da lista
	public void removeGameElement(GameElement g) {
		gameElements.remove(g);
	}

	// remove a imagem da gui
	public void removeImage(GameElement g) {
		gui.removeImage(g);
	}

	// mensagem de nivel inconcluido
	public void morreuMsg() {
		gui.setMessage("Perdeu...");
	}

	// devolve o numero de alvos do nivel
	public int numAlvos() {
		int numAlvos = 0;
		for (GameElement g : gameElements) {
			if (g instanceof Alvo) {
				numAlvos++;
			}
		}
		return numAlvos;
	}

	// verifica se as caixas estão nos alvos
	public void checkBoxesOnTarget() {
		List<GameElement> elementsAtPosition = new ArrayList<>();
		int caixaNoAlvo = 0;
		int numCaixotes = 0;
		for (GameElement g : getGameElements()) {
			if (g instanceof Caixote) {
				numCaixotes++;
				elementsAtPosition = getElementsAtPosition(g.getPosition());
				for (GameElement g1 : elementsAtPosition) {
					if (g1 instanceof Alvo) {
						if (g.getPosition().equals(g1.getPosition()))
							caixaNoAlvo++;
					}
				}

			}
		}
		if (numCaixotes < numAlvos()) {
			morreuMsg();
			restart();
		}
		if (caixaNoAlvo == numAlvos()) {
			nextLevel();
		}
	}

	

	

	

	// atualiza o nome do jogador
	private void setNamePlayer() {
		this.playerName = gui.askUser("Insira o seu username:	(Max 8 char)"); // o jogador deve inserir o seu username
		while (this.playerName.length() > 8 || this.playerName.length() < 1) // testa se o username é valido, e só
																				// avança quando for
		{
			gui.setMessage("Username invalido...");
			this.playerName = gui.askUser("Insira o seu username: 	(Max 8 char)");
		}
	}

	

	// formata os usernames para serem apresentados no fim do nivel
	private String formatName(String s) {

		while (s.length() < 9) {
			s += "_";
		}
		return s;
	}

	// formata as strings recebidas do scanner para ter apenas o conteudo necessario
	private String formatStats(String s) {
		if (s == null || s.length() <= 3) {
			return "";
		}
		return s.substring(3);
	}

	// retira os scores que existem e devolve uma lista com os scores formatados
	// ([pos] [nome] [moves])-> ([nome] [moves])
	private List<String> lerScoreLevel() {
		List<String> scoreLevel = new ArrayList<>();
		try {
			Scanner fileScanner = new Scanner(new File(PATH_SCORES + this.level));
			while (fileScanner.hasNextLine()) {
				scoreLevel.add(formatStats(fileScanner.nextLine()));
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
		return scoreLevel;
	}

	// adiciona o score do user atual e atraves do sort ordena-os, eliminando o que
	// ficar em ultimo, guardando no maximo o top 3
	private List<String> rfreshList() {
		List<String> scoreLevel = lerScoreLevel();
		scoreLevel.add(this.playerName + " " + Empilhadora.getInstance().getMoves());
		scoreLevel.sort(new SortAscending());
		while (scoreLevel.size() > 3)
			scoreLevel.remove(3);
		return scoreLevel;
	}

	// escreve no ficheiro score(level) o top 3
	private void updateScoreTxt() {
		List<String> scoreLevel = rfreshList();
		try {
			File file = new File(PATH_SCORES + this.level);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			PrintWriter writer = new PrintWriter(file);
			int i = 1;
			for (String s : scoreLevel) {
				writer.println(i + ". " + s);
				i++;
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro na escrita do ficheiro");
		}
	}
}