package pt.iscte.poo.sokobanstarter;

import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement {

	private static Empilhadora INSTANCE;
	private static final int MAX_ENERGY = 101;
	private int energy = 100;
	private int moves = 0;
	private boolean martelo = false;

	private Empilhadora(Point2D position) {
		super(position, EMPILHADORALAYER, "Empilhadora_U");
	}

	
	public static Empilhadora getInstance(Point2D position) {
		if (INSTANCE == null)
			INSTANCE = new Empilhadora(position);
		return INSTANCE;
	}

	public static Empilhadora getInstance() {
		return INSTANCE;
	}

	public static void resetInstance() {
		INSTANCE = null;
	}

	public void setMartelo() {
		this.martelo = true;
	}

	public boolean getMartelo() {
		return martelo;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void decrEnergy() {
		if (energy > 0)
			this.energy--;
		else
			return;

	}

	public void addEnergy(int energy) {
		this.energy += 50;
		if (this.energy > 100)
			this.energy = MAX_ENERGY;
	}

	public int getMoves() {
		return moves;
	}

	private String refreshImage(int key) {
		switch (key) {
		case 0:
			return "Empilhadora_L";
		case 1:
			return "Empilhadora_U";
		case 2:
			return "Empilhadora_R";
		default:
			return "Empilhadora_D";
		}
	}

	public void move(int key) {
		GameEngine game = GameEngine.getInstance();
		Point2D nextPosition = movePosition(key);

		// verificar que objeto está na posicao em que a empilhadora se quer mover

		List<GameElement> listNextPosition = game.getElementsAtPosition(nextPosition);
		for (GameElement g : listNextPosition) {
			if (g instanceof Obstaculo && !((Obstaculo) g).passavel(key) && nextPosition.equals(g.getPosition())) {
				return;
			} else if (g instanceof Obstaculo && nextPosition.equals(g.getPosition())
					&& ((Obstaculo) g).passavel(key)) { // parede rachada
				((Activatable) g).activate(this);
				break;
			} else if (g instanceof Movable && nextPosition.equals(g.getPosition())) {
				if (!((Movable) g).move(key))
					return; //se o movable nao se mover entao a empilhadora tambem nao se move
				else
					decrEnergy();

			}

		} // se chega aqui significa que a empilhadora se move

		if (this.getEnergy() > 1) {
			setImageName(refreshImage(Direction.directionFor(key).ordinal()));
			setPosition(nextPosition);
			moves++;
			decrEnergy();
			game.setLevelStatusMessage();
		} else {
			game.restart();

		}

		// verificar o objeto na posicao em que a empilhadora se moveu

		List<GameElement> listThisPosition = game.getElementsAtPosition(getPosition());
		for (GameElement g : listThisPosition) {
			if (g instanceof Activatable && getPosition().equals(g.getPosition())) {
				((Activatable) g).activate(this);
				break;
			}
		}

	}

}