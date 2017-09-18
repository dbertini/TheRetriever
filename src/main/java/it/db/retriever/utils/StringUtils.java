package it.db.retriever.utils;

import java.awt.Color;

/**
 * Classe di utility per le stringhe
 * @author D.Bertini
 *
 */
public class StringUtils {
	/**
	 * metodo che data una stringa rgb "0,0,0" ritorna un oggetto di tipo {@link Color}
	 * 
	 * @param aRGB String in formato rgb: "0,0,0"
	 * 
	 * @return {@link Color} creato con le definizione passata
	 */
	public static Color getColorFromRGBString(String aRGB) {
		Color color = null;
		String colori[] = aRGB.split(",");
		color = new Color(Integer.parseInt(colori[0]), Integer.parseInt(colori[1]),
				Integer.parseInt(colori[2]));
		return color;
	}
}
