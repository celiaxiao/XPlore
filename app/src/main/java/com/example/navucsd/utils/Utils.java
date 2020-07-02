package com.example.navucsd.utils;

import com.example.navucsd.SearchBarDB;
import com.example.navucsd.database.Location;

/**
 * Various general utilities.
 */
public final class Utils {
	/**
	 * Convert the names to their photo URLs.
	 *
	 * @param database the database to fetch data from
	 * @param names the names to be converted
	 * @return their corresponding URLs
	 */
	public static String[] nameToUrl(SearchBarDB database, String[] names) {
		String[] urls = new String[names.length];
		for (int i = 0; i < names.length; ++i) {
			Location location = database.getByName(names[i]);
			// FIXME remove this hack when photos are filled in
			if (location != null) {
				urls[i] = location.getThumbnailPhoto();
			} else {
				urls[i] = "photos/Geisel.jpg";
			}
		}
		return urls;
	}
}
