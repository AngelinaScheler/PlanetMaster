package com.angelina.planetmaster;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import java.util.Arrays;
import java.util.Random;

public class PlanetMaster extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture ufo;
    Texture blue;
    Texture green;
    Texture red;
    Texture yellow;
    Texture pink;
    Texture orange;
    Texture meteor;
    Texture meteor2;
    Texture[] moons;
    Texture gameover;
    Texture youwin;
    float ufoY;
    float velocity = 0;
    int gameState = 0;
    float gravity = 1;
    float planetVelocity = 8;
    float planetX;
    float meteorY;
    float meteorY2;
    float meteorX;
    float meteorX2;
    Random random;
    float meteorVelocity = 20;
    ShapeRenderer shapeRenderer;
    Circle ufoCircle;
    Circle blueCircle;
    Circle greenCircle;
    Circle orangeCircle;
    Circle pinkCircle;
    Circle yellowCircle;
    Circle redCircle;
    Circle meteorCircle;
    Circle meteorCircle2;
    int moonState1;
    int moonState2;
    int moonState3;
    int moonState4;
    int[] code;
    int[] chosenPlanets;
    int planetCounter = 0;
    int touched = 0;
    int winScore = 0;
    int loseScore = 0;
    BitmapFont font;
    Preferences preferences;
    Music music;

    @Override
    public void create() {

        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));


        background = new Texture("Background.png");
        ufo = new Texture("Ufo.png");
        blue = new Texture("blue.png");
        green = new Texture("green.png");
        orange = new Texture("orange.png");
        pink = new Texture("pink.png");
        yellow = new Texture("yellow.png");
        red = new Texture("red.png");

        meteor = new Texture("Meteor.png");
        meteor2 = new Texture("Meteor.png");
        gameover = new Texture("gameOverWhite.png");
        youwin = new Texture("winWhite.png");

        random = new Random();

        font = new BitmapFont();
        font.setColor(Color.GOLD);
        font.getData().scale(3);

        ufoCircle = new Circle();
        blueCircle = new Circle();
        greenCircle = new Circle();
        orangeCircle = new Circle();
        pinkCircle = new Circle();
        yellowCircle = new Circle();
        redCircle = new Circle();
        meteorCircle = new Circle();
        meteorCircle2 = new Circle();

        shapeRenderer = new ShapeRenderer();

        moons = new Texture[3];
        moons[0] = new Texture("blackMoon.png");
        moons[1] = new Texture("whiteMoon.png");
        moons[2] = new Texture("redMoon.png");

        //chosenPlanets = new int[4];

        preferences = Gdx.app.getPreferences("com.angelina.planetmaster");
        winScore = preferences.getInteger("wins");
        loseScore = preferences.getInteger("lose");
        startGame();

    }

    public void startGame() {
        chosenPlanets = new int[4];
        code = new int[4];
        code[0] = random.nextInt(6);
        code[1] = random.nextInt(6);
        code[2] = random.nextInt(6);
        code[3] = random.nextInt(6);

        Gdx.app.log("code", "code: " + Arrays.toString(code));

        meteorY = random.nextInt(Gdx.graphics.getHeight() - meteor.getHeight()) + meteor.getHeight();
        meteorY2 = random.nextInt(Gdx.graphics.getHeight() - meteor.getHeight()) + meteor.getHeight();
        meteorX = Gdx.graphics.getWidth();
        meteorX2 = Gdx.graphics.getWidth() + Gdx.graphics.getWidth() / 2;

        planetX = Gdx.graphics.getWidth();
        ufoY = Gdx.graphics.getHeight() / 2 - ufo.getHeight() / 2;
        moonState1 = 0;
        moonState2 = 0;
        moonState3 = 0;
        moonState4 = 0;

        velocity = 0;
        gameState = 0;
        preferences.putInteger("wins", winScore);
        preferences.putInteger("lose", loseScore);

        preferences.flush();

    }

    @Override
    public void render() {

        // gamestate 0 = start
        // gamestate 1 = ingame
        // gamestate 2 = gameover
        // gamestate 3 = youwin

        music.play();
        preferences.flush();
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState == 1) {
            //raumschiff fällt
            if (Gdx.input.justTouched()) {
                velocity = -10;
            }


            //planeten bewegen sich von rechts nach links, nach 4 mal prüfen
            if (planetX < -blue.getWidth()) {
                planetX = Gdx.graphics.getWidth();
                if (planetCounter == 0 || planetCounter == 1 || planetCounter == 2) {
                    planetCounter++;
                } else {
                    Gdx.app.log("array", Arrays.toString(chosenPlanets));
                    moonState1 = planetChecker(code[0], chosenPlanets[0], chosenPlanets[1], chosenPlanets[2], chosenPlanets[3]);
                    moonState2 = planetChecker(code[1], chosenPlanets[1], chosenPlanets[0], chosenPlanets[2], chosenPlanets[3]);
                    moonState3 = planetChecker(code[2], chosenPlanets[2], chosenPlanets[0], chosenPlanets[1], chosenPlanets[3]);
                    moonState4 = planetChecker(code[3], chosenPlanets[3], chosenPlanets[0], chosenPlanets[1], chosenPlanets[2]);
                    planetCounter = 0;

                    if (moonState1 == 2 && moonState2 == 2 && moonState3 == 2 && moonState4 == 2) {
                        gameState = 3;
                        preferences.flush();
                    }
                }
            } else {
                planetX -= planetVelocity;
            }

            //planeten reihe zeichnen
            batch.draw(blue, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2);
            batch.draw(green, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2 - green.getHeight() - blue.getHeight() / 2);
            batch.draw(orange, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                            - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight());
            batch.draw(pink, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                            - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight());
            batch.draw(yellow, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                            - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight() - pink.getHeight() / 2
                            - yellow.getHeight());
            batch.draw(red, planetX,
                    Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                            - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight() - pink.getHeight() / 2
                            - yellow.getHeight() - yellow.getHeight() / 2 - red.getHeight());

            //kollision Bildschirmrand und fallen von Ufo
            if ((ufoY > 0 && ufoY < (Gdx.graphics.getHeight() - ufo.getHeight())) || velocity < 0) {

                velocity = velocity + gravity;
                ufoY -= velocity;

            } else {
                gameState = 2;
                preferences.flush();

            }
            // 2 meteore mit zufälligen Y koordinaten
            if (meteorX > -meteor.getWidth()) {
                meteorX -= meteorVelocity;
            } else {
                meteorX = Gdx.graphics.getWidth();
                meteorY = random.nextInt(Gdx.graphics.getHeight()) + meteor.getHeight();
            }

            if (meteorX2 > -meteor.getWidth()) {
                meteorX2 -= meteorVelocity;
            } else {
                meteorX2 = Gdx.graphics.getWidth();
                meteorY2 = random.nextInt(Gdx.graphics.getHeight()) + meteor.getHeight();
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;

            }
        } else if (gameState == 2) {


            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);


            if (Gdx.input.isTouched()) {

                touched++;
            }

            if (touched > 20) {
                loseScore++;
                preferences.flush();
                startGame();
                planetCounter = 0;
                touched = 0;
            }

        } else if (gameState == 3) {
            batch.draw(youwin, Gdx.graphics.getWidth() / 2 - youwin.getWidth() / 2, Gdx.graphics.getHeight() / 2 - youwin.getHeight() / 2);

            if (Gdx.input.isTouched()) {

                touched++;
            }

            if (touched > 20) {
                winScore++;
                startGame();
                planetCounter = 0;
                touched = 0;
            }
        }

        //ufo, meteor, monde einfügen
        batch.draw(ufo, ufo.getWidth() * 4, ufoY);
        batch.draw(meteor, meteorX, meteorY);
        batch.draw(meteor2, meteorX2, meteorY2);
        batch.draw(moons[moonState1], ufo.getWidth(), Gdx.graphics.getHeight() / 2 - moons[0].getHeight() / 2 + moons[0].getHeight() * 3);
        batch.draw(moons[moonState2], ufo.getWidth(), Gdx.graphics.getHeight() / 2 - moons[0].getHeight() / 2 + moons[0].getHeight());
        batch.draw(moons[moonState3], ufo.getWidth(), Gdx.graphics.getHeight() / 2 - moons[0].getHeight() / 2 - moons[0].getHeight());
        batch.draw(moons[moonState4], ufo.getWidth(), Gdx.graphics.getHeight() / 2 - moons[0].getHeight() / 2 - moons[0].getHeight() * 3);
        font.draw(batch, String.valueOf(winScore), Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Won:", Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Lost:", 30, Gdx.graphics.getHeight() - 20);
        font.draw(batch, String.valueOf(loseScore), 200, Gdx.graphics.getHeight() - 20);


        batch.end();

        //Shapes für Collisonen

        ufoCircle.set(ufo.getWidth() * 4 + ufo.getWidth() / 2, ufoY + ufo.getHeight() / 2, ufo.getWidth() / 2);
        blueCircle.set(planetX + blue.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2 + blue.getHeight() / 2,
                blue.getWidth() / 2);
        greenCircle.set(planetX + green.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2 - green.getHeight() - blue.getHeight() / 2 + green.getHeight() / 2,
                green.getWidth() / 2);
        orangeCircle.set(planetX + orange.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                        - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() + orange.getHeight() / 2,
                orange.getWidth() / 2);
        pinkCircle.set(planetX + pink.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                        - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight() + pink.getHeight() / 2,
                pink.getWidth() / 2);
        yellowCircle.set(planetX + yellow.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                        - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight() - pink.getHeight() / 2
                        - yellow.getHeight() + yellow.getHeight() / 2,
                yellow.getWidth() / 2);
        redCircle.set(planetX + red.getWidth() / 2,
                Gdx.graphics.getHeight() - blue.getHeight() - blue.getHeight() / 2
                        - green.getHeight() - blue.getHeight() / 2 - green.getHeight() / 2 - orange.getHeight() - orange.getHeight() / 2 - pink.getHeight() - pink.getHeight() / 2
                        - yellow.getHeight() - yellow.getHeight() / 2 - red.getHeight() + red.getHeight() / 2,
                red.getWidth() / 2);
        meteorCircle.set(meteorX + meteor.getWidth() / 2, meteorY + meteor.getHeight() / 2, meteor.getWidth() / 2);
        meteorCircle2.set(meteorX2 + meteor2.getWidth() / 2, meteorY2 + meteor2.getHeight() / 2, meteor2.getWidth() / 2);


        //Shape zum testen sichtbar machen


        //shapeRenderer.end();


        //Collisionen

        if (Intersector.overlaps(ufoCircle, meteorCircle) || Intersector.overlaps(ufoCircle, meteorCircle2)) {
            gameState = 2;
            preferences.flush();
        }

        if (Intersector.overlaps(ufoCircle, blueCircle)) {
            chosenPlanets[planetCounter] = 0;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            shapeRenderer.circle(blueCircle.x, blueCircle.y, blueCircle.radius + 8);

            shapeRenderer.end();

        }
        if (Intersector.overlaps(ufoCircle, greenCircle)) {
            chosenPlanets[planetCounter] = 1;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            shapeRenderer.circle(greenCircle.x, greenCircle.y, greenCircle.radius + 8);

            shapeRenderer.end();
        }
        if (Intersector.overlaps(ufoCircle, orangeCircle)) {
            chosenPlanets[planetCounter] = 2;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(orangeCircle.x, orangeCircle.y, orangeCircle.radius + 8);
            shapeRenderer.end();
        }
        if (Intersector.overlaps(ufoCircle, pinkCircle)) {
            chosenPlanets[planetCounter] = 3;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(pinkCircle.x, pinkCircle.y, pinkCircle.radius + 8);
            shapeRenderer.end();
        }
        if (Intersector.overlaps(ufoCircle, yellowCircle)) {
            chosenPlanets[planetCounter] = 4;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(yellowCircle.x, yellowCircle.y, yellowCircle.radius + 8);
            shapeRenderer.end();
        }

        if (Intersector.overlaps(ufoCircle, redCircle)) {
            chosenPlanets[planetCounter] = 5;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(redCircle.x, redCircle.y, redCircle.radius + 8);
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        music.dispose();
    }

    //methode für mastermind
    public int planetChecker(int planet, int a, int b, int c, int d) {

        if (planet != a && planet != b && planet != c && planet != d) {
            return 0;
        } else if (planet != a && (planet == b || planet == c || planet == d)) {

            return 1;
        } else if (planet == a) {
            return 2;
        }
        return 0;
    }
}


