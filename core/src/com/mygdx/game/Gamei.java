package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.Packets.Disconnect;
import com.mygdx.Packets.FirstPacket;
import com.mygdx.Packets.Moving;
import com.mygdx.Packets.Post;
import com.mygdx.game.Character.CharacterDirection;
import com.mygdx.game.Character.CharacterState;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gamei extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    
    Client GameiClient= new Client();
    Kryo kryo = new Kryo();
    
    Character[] otherPlayer = new Character[20];
    int index = 0;
    
    public Gamei(){
        
            kryo = GameiClient.getKryo();
            kryo.register(FirstPacket.class);
            kryo.register(Post.class);
            kryo.register(CharacterDirection.class);
            kryo.register(CharacterState.class);
            kryo.register(Moving.class);
            kryo.register(Disconnect.class);
            
            GameiClient.addListener(new Listener(){
                @Override
                public void received(Connection conn, Object obj){
                    if (obj instanceof FirstPacket) {
                        FirstPacket fp = (FirstPacket)obj;
                        System.out.println(fp.id);
                        player.id = fp.id;
                        Post post = new Post();
                        post.playerid = fp.id;
                        post.postX = (int) player.getPostX();
                        post.postY = (int) player.getPostY();
                        GameiClient.sendTCP(post);
                    }
                    if (obj instanceof Post) {
                        otherPlayer[index].id = ((Post) obj).playerid;
                        otherPlayer[index].PostX = ((Post) obj).postX;
                        otherPlayer[index].PostY = ((Post) obj).postY;
                        index++;
                    }
                    if (obj instanceof Moving) {
                        for (int i = 0; i < index; i++) {
                            if (otherPlayer[i].id == ((Moving) obj).post.playerid) {
                                otherPlayer[i].PostX = ((Moving) obj).post.postX;
                                otherPlayer[i].PostY = ((Moving) obj).post.postY;
                                otherPlayer[i].setCurrentState(((Moving) obj).cs);
                                otherPlayer[i].setDirection(((Moving) obj).cd);
                                break;
                            }
                        }
                    }
                    if (obj instanceof Disconnect) {
                        otherPlayer[((Disconnect) obj).playerID] = null;
                        index--;
                    }
                }
            });
            GameiClient.start();
            try {
                GameiClient.connect(5000, "localhost", 54555);
            } catch (IOException ex) {
                Logger.getLogger(Gamei.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }

    Character player;
    float timePassed = 0;
    //TextureAtlas shooterAtlas;
    //Animation animation;
    
    Texture BackgroundImage;
    
    Sprite bg;
    
    @Override
    public void create () {
        Gdx.graphics.setResizable(false);
        Gdx.graphics.setVSync(true);
        Gdx.graphics.setWindowedMode(800, 600);
        Gdx.input.setInputProcessor(this);
        BackgroundImage = new Texture(Gdx.files.internal("bg1.png"));
        bg = new Sprite(BackgroundImage);
        for (int i = 0; i < 10; i++) {
            otherPlayer[i] = new Character();
        }
        // bg.setColor(Color.ORANGE);
        batch = new SpriteBatch();
        player = new Character();
        FirstPacket fp = new FirstPacket();
        GameiClient.sendTCP(fp); ////////////////////// NETWORK 
//        shooterAtlas = new TextureAtlas(Gdx.files.internal("shooter.atlas"));
//        animation = new Animation(1/8f, shooterAtlas.getRegions());
    }
    @Override
    public void render () {
        
        timePassed += Gdx.graphics.getDeltaTime();
        //  Gdx.gl.glClearColor(0, 1, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        // batch.draw(bg.getTexture(), 0, 0);
        // batch.setColor(Color.BROWN);
        bg.draw(batch);
        
        if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            if (player.getPostX() >= -64)
                player.setPostX(player.getPostX() - Gdx.graphics.getDeltaTime() * player.speed);
            Moving mv = new Moving();
            mv.cd = CharacterDirection.LEFT;
            mv.cs = CharacterState.WALKING;
            mv.post.playerid = player.id;
            mv.post.postX = (int) player.getPostX();
            mv.post.postY = (int) player.getPostY();
            GameiClient.sendTCP(mv);
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if (player.getPostX() <= 600)
                player.setPostX(player.getPostX() + Gdx.graphics.getDeltaTime() * player.speed);
            Moving mv = new Moving();
            mv.cd = CharacterDirection.RIGHT;
            mv.cs = CharacterState.WALKING;
            mv.post.playerid = player.id;
            mv.post.postX = (int) player.getPostX();
            mv.post.postY = (int) player.getPostY();
            GameiClient.sendTCP(mv);
        } 
        

//           if(Gdx.input.isKeyPressed(Keys.DPAD_UP)) 
//              marioY += Gdx.graphics.getDeltaTime() * marioSpeed;
//           if(Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) 
//              marioY -= Gdx.graphics.getDeltaTime() * marioSpeed;

        // batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), 0, 300);
        
        //////////////////// DRAW OTHER PLAYER /////////////////////
        if (index > 0){
            for (int i = 0; i < index; i++) {
                batch.draw(otherPlayer[i].getKeyFrame(timePassed), otherPlayer[i].getPostX(), otherPlayer[i].getPostY(), 256, 256);
            }
        }
        // DRAW myPLAYER 
        batch.draw(player.getKeyFrame(timePassed), player.getPostX(), player.getPostY(), 256, 256);
        // batch.setColor(255, 100, 100, 5);
        
        batch.end();

    }

    @Override
    public void dispose () {
        batch.dispose();
        player.AtlasDispose();
    }
    
    @Override
    public boolean keyUp(int i) {
        if(!Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.S) && player.isAttacking()){
//            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
//                player.setCurrentState(Character.CharacterState.WALKING);
//                player.setDirection(Character.CharacterDirection.LEFT);
//            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
//                player.setCurrentState(Character.CharacterState.WALKING);
//                player.setDirection(Character.CharacterDirection.RIGHT);
//            } else {
//                player.setCurrentState(Character.CharacterState.IDLE);
//            }
            player.setCurrentState(Character.CharacterState.IDLE);
            player.setAttacking(false);
            player.setSpeed(300);
            Moving mv = new Moving();
            mv.cs = CharacterState.IDLE;
            mv.post.playerid = player.id;
            mv.post.postX = (int) player.getPostX();
            mv.post.postY = (int) player.getPostY();
            GameiClient.sendTCP(mv);
            return true;
        }
        // player.setSpeed(0);
        if (!player.isAttacking() && !(Gdx.input.isKeyPressed(Keys.LEFT)||Gdx.input.isKeyPressed(Keys.RIGHT))){
            player.setCurrentState(Character.CharacterState.IDLE);
            Moving mv = new Moving();
            mv.cs = CharacterState.IDLE;
            mv.post.playerid = player.id;
            mv.post.postX = (int) player.getPostX();
            mv.post.postY = (int) player.getPostY();
            GameiClient.sendTCP(mv);
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        // System.out.println("keyDown");
        if (!player.isAttacking()){
            if (keycode == Input.Keys.RIGHT){
                player.setCurrentState(Character.CharacterState.WALKING);
                player.setDirection(Character.CharacterDirection.RIGHT);
                player.setSpeed(300);
            } else if (keycode == Input.Keys.LEFT) {
                player.setCurrentState(Character.CharacterState.WALKING);
                player.setDirection(Character.CharacterDirection.LEFT);
                player.setSpeed(300);
            } else if (keycode == Input.Keys.D) {
                player.setCurrentState(Character.CharacterState.ATTACKING);
                player.setSpeed(0);
                player.setAttacking(true);
                Moving mv = new Moving();
                mv.cs = CharacterState.ATTACKING;
                mv.cd = player.getDirection();
                mv.post.playerid = player.id;
                mv.post.postX = (int) player.getPostX();
                mv.post.postY = (int) player.getPostY();
                GameiClient.sendTCP(mv);
            } else if (keycode == Input.Keys.S) {
                player.setCurrentState(Character.CharacterState.SHOOTING);
                player.setSpeed(0);
                player.setAttacking(true);
                Moving mv = new Moving();
                mv.cs = CharacterState.SHOOTING;
                mv.cd = player.getDirection();
                mv.post.playerid = player.id;
                mv.post.postX = (int) player.getPostX();
                mv.post.postY = (int) player.getPostY();
                GameiClient.sendTCP(mv);
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
        
        
}
