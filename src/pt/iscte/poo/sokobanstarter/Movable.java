package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public interface Movable {

	public boolean move(int key);

	public Point2D getPosition();

}