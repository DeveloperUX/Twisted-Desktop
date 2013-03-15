package com.biigoh.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Vector2Pool {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static final Pool<Vector2> POOL = new Pool<Vector2>() {
		@Override
		protected Vector2 newObject() {
			return new Vector2();
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Get a Vector2 from the Pool, might be new or recycled
	 * @return A Vector2 object that is new or recycled
	 */
	public static Vector2 obtain() {
		return POOL.obtain();
	}

	/**
	 * Obtain a free Vector and set its values to the given Vector before returning.
	 * @param pCopyFrom The vector to copy into the new free Pool Vector upon return.
	 * @return The new Vector set equal to the given vector
	 */
	public static Vector2 obtain(final Vector2 pCopyFrom) {
		return POOL.obtain().set(pCopyFrom);
	}

	/**
	 * Obtain a free Vector and set its values to the given X and Y components
	 * of a new Vector before returning.
	 * @param pX The X component of the new Vector
	 * @param pY The Y component of the new Vector
	 * @return A free Vector set to the X and Y components
	 */
	public static Vector2 obtain(final float pX, final float pY) {
		return POOL.obtain().set(pX, pY);
	}

	/**
	 * Recycle the given Vector2 and put it back in the pool to be reused. 
	 * If the Pool already contains the max free items than this object is ignored.
	 * @param pVector2 The Vector to be recycled
	 */
	public static void recycle(final Vector2 pVector2) {
		POOL.free( pVector2 );
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}