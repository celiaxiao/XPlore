package com.example.navucsd.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A location database.
 */
public final class LocationDatabase {
	/**
	 * The file name of the JSON file containing all the locations.
	 */
	private static String LOCATIONS_JSON = "placesMin.json";

	/**
	 * The tag used in log messages.
	 */
	private static String TAG = LocationDatabase.class.getName();

	/**
	 * An empty private constructor to prevent construction of this class.
	 */
	private LocationDatabase() {}

	/**
	 * Loads locations from assets.
	 *
	 * @param context the context used to get assets
	 * @return a list of {@link Location}s, or {@code null} on error
	 */
	public static ArrayList<Location> getLocations(Context context) {
		try (BufferedReader reader = newBufferedReaderFromAsset(context, LOCATIONS_JSON)) {
			if (reader == null) return null;
			HashMap<String, Location> location_map = new Gson().fromJson(
				reader,
				new TypeToken<HashMap<String, Location>>(){}.getType()
			);
			return new ArrayList<Location>(location_map.values());
		} catch (JsonSyntaxException | IOException e) {
			// TODO proper error reporting
			// FIXME What's up with the Timber lint?
			Log.e(TAG, "failed to parse JSON file: \"" + LOCATIONS_JSON + "\"", e);
			return null;
		}
	}

	/**
	 * Returns a {@link BufferedReader} of a file in the assets folder.
	 *
	 * @param context the context used to get assets
	 * @param fileName the name of the file to load
	 * @return a {@link BufferedReader} to the file, or {@code null} on error
	 */
	private static BufferedReader newBufferedReaderFromAsset(Context context, String fileName) {
		try {
			return new BufferedReader(new InputStreamReader(
				context.getAssets().open(fileName),
				StandardCharsets.UTF_8
			));
		} catch (IOException e) {
			// TODO proper error reporting
			// FIXME What's up with the Timber lint?
			Log.e(TAG, "failed to read file: \"" + fileName + "\"", e);
			return null;
		}
	}
}
