/*
 * -----------------------------------------------------------------------
 * Copyright 2012 - Alistair Rutherford - www.netthreads.co.uk
 * -----------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.biigoh.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Simple non-animated sprite encapsulated by Group so we can scale it and move it etc.
 * 
 */
public class SimpleSprite extends Group {
	private StaticSprite staticSprite;

	/**
	 * Create sprite.
	 * 
	 * @param texture
	 *            The texture.
	 */
	public SimpleSprite(TextureRegion textureRegion) {
		staticSprite = new StaticSprite(textureRegion);

		this.width = staticSprite.width;
		this.height = staticSprite.height;

		addActor(staticSprite);
	}
	
	public SimpleSprite(Texture texture) {

		staticSprite = new StaticSprite( texture );

		this.width = staticSprite.width;
		this.height = staticSprite.height;

		addActor(staticSprite);
		
	}

	public void setTextureRegion( TextureRegion newTextureRegion ) {
		staticSprite = new StaticSprite( newTextureRegion );
	}

}
