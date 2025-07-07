package pt.iscte.poo.sokobanstarter;

import java.util.Comparator;

public class SortAscending implements Comparator<String> { 

	@Override
	// define o metodo compare do comparator de forma ascendente
	public int compare(String a, String b) {
		String[] aSplit = a.split(" "); // separa a string
		int aMoves = Integer.parseInt(aSplit[1]); // transforma o moves em inteiro

		String[] bSplit = b.split(" ");
		int bMoves = Integer.parseInt(bSplit[1]);

		return aMoves - bMoves; 
	}

}
