package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement implements Obstaculo {

	public Parede(Point2D position) {
		super(position, OBSTACULOLAYER, "Parede");
	}

	@Override
	public boolean passavel(int key) {
		return false;
	}

}