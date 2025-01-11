package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene () {
        System.out.println("Inside Level Editor Scene");
    }

    @Override
    public void update(float dt) {

        dt = 1.0f/60; //CoPilot suggested => Math.min(dt, 1.0f / 60.0f);
        System.out.println(1.0/dt);

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
            changingScene = true;
        if(changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        }
        else if(changingScene)
            Window.changeScene(1);
    }
}
