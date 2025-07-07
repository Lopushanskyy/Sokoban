package pt.iscte.poo.sokobanstarter;


import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement implements Activatable {

	public Teleporte(Point2D position) {
		super(position, 1, "Teleporte");
	}

	@Override
	public void activate(GameElement g) {
		GameEngine game = GameEngine.getInstance();

		Point2D teleportPosition = null;
		for (GameElement teleport : game.getGameElements()) {
			if (teleport instanceof Teleporte && !(teleport.getPosition().equals(this.getPosition()))) {
				teleportPosition = teleport.getPosition();
			}
		}
		for (GameElement element : game.getElementsAtPosition(teleportPosition)) {
			if (element instanceof Movable && element.getPosition().equals(teleportPosition)) {
				return;
			}
		}
		g.setPosition(teleportPosition);

	}

}
