/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author MYPC
 */
public class Character extends Sprite{
    public int id = -1;
    String name;
    float PostX;
    float PostY;
    Texture CharImg;
    TextureAtlas charAtlas;
    TextureAtlas runAtlas;
    TextureAtlas meleeAtlas;
    TextureAtlas shootAtlas;
    Animation <TextureRegion> idleanimation;
    Animation <TextureRegion> walkAnimation;
    Animation <TextureRegion> meleeAnimation;
    Animation <TextureRegion> shootAnimation;
    public float speed = 300;
    
    CharacterState currentState = CharacterState.IDLE;
    CharacterDirection direction = CharacterDirection.RIGHT;
    
    Character(){
        name = "";
        PostX = 150;
        PostY = 50;
        charAtlas = new TextureAtlas(Gdx.files.internal("CowboyIdle.atlas"));
        runAtlas = new TextureAtlas(Gdx.files.internal("CowboyRun.atlas"));
        meleeAtlas = new TextureAtlas(Gdx.files.internal("CowboyMelee.atlas"));
        shootAtlas = new TextureAtlas(Gdx.files.internal("CowboyShoot.atlas"));
        idleanimation = new Animation<TextureRegion>(1/16f, charAtlas.getRegions());
        walkAnimation = new Animation<TextureRegion>(1/16f, runAtlas.getRegions());
        meleeAnimation = new Animation<TextureRegion>(1/16f, meleeAtlas.getRegions());
        shootAnimation = new Animation<TextureRegion>(1/16f, shootAtlas.getRegions());
        // CharImg = new Texture("tux1.png");
    }
    Character(int id, int x) {
        this.id = id;
        PostX = x;
        PostY = 50;
    }
    
    public TextureRegion getKeyFrame(float timePassed){
        
        TextureRegion currentFrame = idleanimation.getKeyFrame(timePassed, true);
        
        if ( currentState == CharacterState.WALKING){
            
            currentFrame = walkAnimation.getKeyFrame(timePassed, true);
            
            if ( currentFrame.isFlipX() && direction == CharacterDirection.RIGHT){
                currentFrame.flip(true, false);
            }
            if ( !currentFrame.isFlipX() && direction == CharacterDirection.LEFT){
                currentFrame.flip(true, false);
            }
            
        } else if (currentState == CharacterState.ATTACKING) {
            
            currentFrame = meleeAnimation.getKeyFrame(timePassed, true);
            
            if ( currentFrame.isFlipX() && direction == CharacterDirection.RIGHT){
                currentFrame.flip(true, false);
            }
            if ( !currentFrame.isFlipX() && direction == CharacterDirection.LEFT){
                currentFrame.flip(true, false);
            }
            
        } else if (currentState == CharacterState.SHOOTING) {
            
            currentFrame = shootAnimation.getKeyFrame(timePassed, true);
            
            if ( currentFrame.isFlipX() && direction == CharacterDirection.RIGHT){
                currentFrame.flip(true, false);
            }
            if ( !currentFrame.isFlipX() && direction == CharacterDirection.LEFT){
                currentFrame.flip(true, false);
            }
            
        }
        return currentFrame;
    }
    ///////////////////////////////////////////////////////////////////////////
    boolean isAttacking = false;

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
    //////////////////////////////////////////////////////////////////////////
    public CharacterDirection getDirection() {
        return direction;
    }

    public void setDirection(CharacterDirection direction) {
        this.direction = direction;
    }
    
    public CharacterState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CharacterState currentState) {
        this.currentState = currentState;
    }
    
    public TextureAtlas getCharAtlas() {
        return charAtlas;
    }

    public void setCharAtlas(TextureAtlas charAtlas) {
        this.charAtlas = charAtlas;
    }

    public Animation getIdleAnimation() {
        return idleanimation;
    }

    public void setIdleAnimation(Animation animation) {
        this.idleanimation = animation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public void AtlasDispose(){
        charAtlas.dispose();
        runAtlas.dispose();
        meleeAtlas.dispose();
        shootAtlas.dispose();
    }
    
    public Texture getCharImg() {
        return CharImg;
    }

    public void setCharImg(Texture CharImg) {
        this.CharImg = CharImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPostX() {
        return PostX;
    }

    public void setPostX(float PostX) {
        this.PostX = PostX;
    }

    public float getPostY() {
        return PostY;
    }

    public void setPostY(float PostY) {
        this.PostY = PostY;
    }
    
    public enum CharacterState{
        IDLE,
        WALKING,
        ATTACKING,
        SHOOTING,
        JUMPING_UP,
        JUMPING_DOWN;
    }
    
    public enum CharacterDirection{
        LEFT,
        RIGHT;
    }
    
}
