package io.exterminator3618.client.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameClient extends ApplicationAdapter {

    private static final Logger log = LoggerFactory.getLogger(GameClient.class);

	@Override
    public void render() {
        log.trace("Rendering a frame");
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
