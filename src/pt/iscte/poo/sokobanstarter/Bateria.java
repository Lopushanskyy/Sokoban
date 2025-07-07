package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.sokobanstarter.utils.Point2D;

public class Bateria extends GameElement implements Activatable, Obstaculo {

	public Bateria(Point2D position) {
		super(position, ACTIVATABLELAYER, "Bateria");
	}

	@Override
	public void activate(GameElement g) {
		GameEngine game = GameEngine.getInstance();
		Empilhadora empilhadora = Empilhadora.getInstance();

		if (g instanceof Empilhadora) {

			game.removeGameElement(this);
			game.removeImage(this);
			empilhadora.addEnergy(50);

		}
	}

	@Override
	public boolean passavel(int key) {
		if (Empilhadora.getInstance().movePosition(key).equals(this.getPosition()))
			return true;
		else
			return false;
	}

}
