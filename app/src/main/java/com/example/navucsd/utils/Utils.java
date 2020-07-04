package com.example.navucsd.utils;

import com.example.navucsd.database.Landmark;
import com.example.navucsd.database.LandmarkDatabase;

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
	public static String[] nameToUrl(LandmarkDatabase database, String[] names) {
		String[] urls = new String[names.length];
		for (int i = 0; i < names.length; ++i) {
			Landmark landmark = database.getByName(names[i]);
			urls[i] = landmark.getThumbnailPhoto();
		}
		return urls;
	}
}
