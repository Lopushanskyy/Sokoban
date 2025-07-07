package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement implements Activatable, Obstaculo {

	public Martelo(Point2D position) {
		super(position, ACTIVATABLELAYER, "Martelo");
	}

	@Override
	public void activate(GameElement g) {
		GameEngine game = GameEngine.getInstance();
		Empilhadora empilhadora = Empilhadora.getInstance();

		if (g instanceof Empilhadora) {
			empilhadora.setMartelo();
			game.removeImage(this);
			game.removeGameElement(this);

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