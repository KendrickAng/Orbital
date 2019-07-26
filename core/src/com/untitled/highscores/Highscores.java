package com.untitled.highscores;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.untitled.UntitledGame;
import com.untitled.net.PostRequest;

import java.io.StringWriter;

import static com.untitled.UntitledGame.HIGHSCORE_VERSION;

/**
 * Untitled Highscores manager.
 * <p>
 * Makes appropriate HTTP requests to the firestore database using the REST API.
 * https://firebase.google.com/docs/firestore/reference/rest#rest-resource:-v1.projects.databases
 * <p>
 * Responsibilities:
 * - Get highscores
 * - Post highscores
 */
public class Highscores {
	private static final String WEB_API_KEY = "AIzaSyCqpJqKdS-fbgIKlyZ5uMqsg-1JCk8zMBQ";
	private static final String NEW_USER_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/signupNewUser";
	private static final String QUERY_URL = "https://firestore.googleapis.com/v1/projects/apollo1orbital3/databases/(default)/documents:runQuery";
	private static final String HIGHSCORES_URL = "https://firestore.googleapis.com/v1/projects/apollo1orbital3/databases/(default)/documents/highscores";

	private String name;

	/**
	 * @param name the name of the player.
	 */
	public Highscores(String name) {
		this.name = name;
	}

	private void createNewUser(NewUserCallback callback) {
		new PostRequest(NEW_USER_URL)
				.setURLParameter("key", WEB_API_KEY)
				.setResponse200(response -> {
					JsonReader jsonReader = new JsonReader();
					JsonValue jsonNewUser = jsonReader.parse(response);

					callback.call(jsonNewUser.getString("idToken"));
				})
				.call();
	}

	/**
	 * Post a highscore using anonymous authentication.
	 *
	 * @param level   the level
	 * @param score   the score
	 * @param time    the time
	 * @param success called when posting is successful.
	 * @param failed  called when posting fails.
	 */
	public void postHighscore(int level, int score, int time, HighscoresCallback success, HighscoresCallback failed) {
		// Setting any debug flag will disable highscores.
		if (UntitledGame.DEBUG) {
			return;
		}

		createNewUser(token -> {
			StringWriter jsonHighscoreWriter = new StringWriter();
			Json jsonHighscore = new Json(JsonWriter.OutputType.json);
			jsonHighscore.setWriter(new JsonWriter(jsonHighscoreWriter));
			jsonHighscore.writeObjectStart();
			jsonHighscore.writeObjectStart("fields");

			jsonHighscore.writeObjectStart("name");
			jsonHighscore.writeValue("stringValue", name);
			jsonHighscore.writeObjectEnd();

			jsonHighscore.writeObjectStart("level");
			jsonHighscore.writeValue("integerValue", level);
			jsonHighscore.writeObjectEnd();

			jsonHighscore.writeObjectStart("time");
			jsonHighscore.writeValue("integerValue", time);
			jsonHighscore.writeObjectEnd();

			jsonHighscore.writeObjectStart("score");
			jsonHighscore.writeValue("integerValue", score);
			jsonHighscore.writeObjectEnd();

			jsonHighscore.writeObjectStart("version");
			jsonHighscore.writeValue("stringValue", HIGHSCORE_VERSION);
			jsonHighscore.writeObjectEnd();

			jsonHighscore.writeObjectEnd();
			jsonHighscore.writeObjectEnd();

			new PostRequest(HIGHSCORES_URL)
					.setHeader("Authorization", "Bearer " + token)
					.setHeader("Content-Type", "application/json")
					.setBody(jsonHighscoreWriter.toString())
					.setResponse200(response -> success.call())
					.setFailedCallback(failed::call)
					.call();
		});
	}

	/**
	 * Get highscores in the latest version of the game.
	 *
	 * @param limit   the number of highscores to get.
	 * @param success called when retrieval of highscores is successful.
	 * @param failed  called when retrieval of highscores failed.
	 */
	public void getHighscores(int limit, GetHighscoresCallback success, HighscoresCallback failed) {
		StringWriter jsonQueryWriter = new StringWriter();
		Json jsonQuery = new Json(JsonWriter.OutputType.json);
		jsonQuery.setWriter(new JsonWriter(jsonQueryWriter));
		jsonQuery.writeObjectStart();
		jsonQuery.writeObjectStart("structuredQuery");

		jsonQuery.writeArrayStart("from");
		jsonQuery.writeObjectStart();
		jsonQuery.writeValue("collectionId", "highscores");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeArrayEnd();

		jsonQuery.writeArrayStart("orderBy");
		jsonQuery.writeObjectStart();
		jsonQuery.writeObjectStart("field");
		jsonQuery.writeValue("fieldPath", "level");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeValue("direction", "DESCENDING");
		jsonQuery.writeObjectEnd();

		jsonQuery.writeObjectStart();
		jsonQuery.writeObjectStart("field");
		jsonQuery.writeValue("fieldPath", "time");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeObjectEnd();

		jsonQuery.writeObjectStart();
		jsonQuery.writeObjectStart("field");
		jsonQuery.writeValue("fieldPath", "score");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeValue("direction", "DESCENDING");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeArrayEnd();

		jsonQuery.writeObjectStart("where");
		jsonQuery.writeObjectStart("fieldFilter");
		jsonQuery.writeObjectStart("field");
		jsonQuery.writeValue("fieldPath", "version");
		jsonQuery.writeObjectEnd();
		jsonQuery.writeValue("op", "EQUAL");
		jsonQuery.writeObjectStart("value");
		jsonQuery.writeValue("stringValue", UntitledGame.GAME_VERSION);
		jsonQuery.writeObjectEnd();
		jsonQuery.writeObjectEnd();
		jsonQuery.writeObjectEnd();

		jsonQuery.writeValue("limit", limit);
		jsonQuery.writeObjectEnd();
		jsonQuery.writeObjectEnd();

		new PostRequest(QUERY_URL)
				.setHeader("Content-Type", "application/json")
				.setBody(jsonQueryWriter.toString())
				.setResponse200(response -> {
//					Gdx.app.log("Response", response);

					Array<Highscore> highscores = new Array<>();

					JsonReader jsonReader = new JsonReader();
					JsonValue jsonHighscores = jsonReader.parse(response);

					for (JsonValue query : jsonHighscores) {
						JsonValue fields = query.get("document").get("fields");
						String name = fields.get("name").getString("stringValue");
						int level = fields.get("level").getInt("integerValue");
						int time = fields.get("time").getInt("integerValue");
						int score = fields.get("score").getInt("integerValue");
//						Gdx.app.log(name, String.valueOf(score));

						highscores.add(new Highscore(name, level, score, time));
					}

					success.call(highscores);
				})
				.setFailedCallback(failed::call)
				.call();
	}
}
