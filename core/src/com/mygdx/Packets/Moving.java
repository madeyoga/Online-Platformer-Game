/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Packets;

import com.mygdx.game.Character.CharacterDirection;
import com.mygdx.game.Character.CharacterState;

/**
 *
 * @author MYPC
 */
public class Moving {
    public CharacterState cs = CharacterState.WALKING;
    public CharacterDirection cd;
    public Post post = new Post();
    
}
