package com.example.navucsd.utils;

import java.text.DecimalFormat;

/**
 * Geography related utilities.
 */
public class Geography {
	/**
	 * Formats the distance for display purposes.
	 *
	 * @param distance the distance to format in meters
	 * @return the formatted distance, in miles
	 */
	public static String displayDistance(double distance) {
		String unit;
		double n;
		// use feet when less than 0.1 miles
		if (distance < 160.9344) {
			n = distance / 0.3048;
			unit = "ft";
		} else {
			n = distance / 1609.344;
			unit = "mi";
		}
		return new DecimalFormat("#.#").format(n) + " " + unit;
	}
}
