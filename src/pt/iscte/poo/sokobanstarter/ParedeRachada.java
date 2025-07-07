package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.sokobanstarter.utils.Point2D;

public class ParedeRachada extends GameElement implements Obstaculo, Activatable {

	public ParedeRachada(Point2D position) {
		super(position, 2, "ParedeRachada");
	}

	@Override
	public void activate(GameElement g) {
		GameEngine game = GameEngine.getInstance();

		if (g instanceof Empilhadora) {
			game.removeImage(this);
			game.removeGameElement(this);
		}

	}

	@Override
	public boolean passavel(int key) {
		if (Empilhadora.getInstance().getMartelo())
			return true;
		return false;
	}
}
