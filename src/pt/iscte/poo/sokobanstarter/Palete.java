package pt.iscte.poo.sokobanstarter;

import java.util.List;

import pt.iscte.poo.sokobanstarter.utils.Point2D;

public class Palete extends GameElement implements Movable {

	public Palete(Point2D position) {
		super(position, MOVABLELAYER, "Palete");
	}

	@Override
	public boolean move(int key) {
		GameEngine game = GameEngine.getInstance();
		Point2D newPosition = movePosition(key);
		boolean elementMoved;

		// verificar que objeto está na posicao em que o elemento se quer mover

		List<GameElement> nextPositionList = game.getElementsAtPosition(newPosition);
		for (GameElement g : nextPositionList) {
			if (g instanceof Movable && newPosition.equals(g.getPosition())) {
				return elementMoved = false;
			} else if (g instanceof Obstaculo && newPosition.equals(g.getPosition())) {
				return elementMoved = false;
			}

		}
		// se chega aqui significa que o elemento se move

		setPosition(newPosition);
		elementMoved = true;

		// verificar o objeto na posicao em que a elemento se moveu

		List<GameElement> thisPositionList = game.getElementsAtPosition(getPosition());
		for (GameElement g : thisPositionList) {
			if (g instanceof Activatable && getPosition().equals(g.getPosition())) {
				((Activatable) g).activate(this);
				break;
			}
		}
		return elementMoved;

	}

}