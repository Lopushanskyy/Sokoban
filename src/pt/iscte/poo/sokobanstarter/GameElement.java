package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {

	private Point2D position;
	private int layer;
	private String imageName;
	public static final int ACTIVATABLELAYER = 1;
	public static final int MOVABLELAYER = 2;
	public static final int OBSTACULOLAYER = 3;
	public static final int EMPILHADORALAYER = 4;

	public GameElement(Point2D position, int layer, String imageName) {
		this.position = position;
		this.layer = layer;
		this.imageName = imageName;
	}

	// getters
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	// setters
	public void setImageName(String newImage) {
		this.imageName = newImage;
	}

	public void setPosition(Point2D newPosition) {
		this.position = newPosition;
	}

	public Point2D movePosition(int key) {
		Direction direction = Direction.directionFor(key);
		return this.getPosition().plus(direction.asVector());
	}

	public static GameElement createElement(char c, Point2D position) {
		switch (c) {
		case ('E'):
			return Empilhadora.getInstance(position);
		case ('C'):
			return new Caixote(position);
		case ('X'):
			return new Alvo(position);
		case ('B'):
			return new Bateria(position);
		case ('#'):
			return new Parede(position);
		case (' '):
			return new Chao(position);
		case ('='):
			return new Vazio(position);
		case ('O'):
			return new Buraco(position);
		case ('P'):
			return new Palete(position);
		case ('M'):
			return new Martelo(position);
		case ('%'):
			return new ParedeRachada(position);
		case ('T'):
			return new Teleporte(position);
		}
		return null;
	}

}