package client;

import common.Coordinate;
import common.Sector;

import javax.swing.JLabel;

/**
 * Represents a generic sector painted on the game's graphical map It consists
 * of a JLabel with an appropriate image associated
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SectorLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private Sector referredSector;
	private String image;

	/**
	 * Constructs a generic sector ready to be painted on the game's graphical
	 * map. This sector is constructed from its coordinate and its associated
	 * image
	 * 
	 * @param sector
	 *            the coordinate of the sector
	 * @param image
	 *            the filename of the image associated with the sector
	 */
	public SectorLabel(Sector sector, String image) {
		this.referredSector = sector;
		this.image = image;
	}

	/**
	 * Gets the sector's coordinate
	 * 
	 * @return the sector's coordinate
	 */
	public Sector getReferredSector() {
		return this.referredSector;
	}

	/**
	 * Gets the sector's associated image's filename
	 * 
	 * @return the sector's associated image's filename
	 */
	public String getImageFilename() {
		return this.image;
	}
}
