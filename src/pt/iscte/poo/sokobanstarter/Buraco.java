package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.sokobanstarter.utils.Point2D;

public class Buraco extends GameElement implements Activatable {

	public Buraco(Point2D position) {
		super(position, ACTIVATABLELAYER, "Buraco");
	}

	@Override
	public void activate(GameElement g) {
		GameEngine game = GameEngine.getInstance();

		if (g instanceof Caixote) {

			game.removeGameElement(g);
			game.removeImage(g);

		} else if (g instanceof Empilhadora) {
			game.removeGameElement(g);
			game.removeImage(g);
			game.morreuMsg();
			game.restart();

		} else if (g instanceof Palete) {
			game.removeGameElement(g);
			game.removeGameElement(this);
		}
	}

}
