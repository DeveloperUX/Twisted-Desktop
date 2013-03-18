package com.biigoh.utils;

import org.lwjgl.util.Point;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MathMan extends MathUtils {
	
	public static boolean DEBUG = false;
	public static String LOG = MathMan.class.getSimpleName();
	
	
	public static Vector2 aPointFromDirection( Vector2 origin, float angle, float distance ) {
		angle = aConvertToUsableAngle(angle);
		return Vector2Pool.obtain( origin.x - sin(angle) * distance, origin.y + cos(angle) * distance );
	}
	public static Vector2 aFindPointOnLine( Vector2 origin, Vector2 directionVector, float distance ) {
		// C = A + k(B - A)		
		float k = distance / directionVector.len();		
		return origin.add( directionVector.mul(k) );
	}
	/**
	 * Obtain a quasi unit Vector for any given Angle in RADIANS.
	 * @param angle The angle in Radians.
	 * @return A new Vector pointing in the direction of the given angle.
	 */
	public static Vector2 aVectorFromAngle( float angle ) {
        return Vector2Pool.obtain( cos( angle ), sin( angle ) );		
	}
	
	/**
	 * Same as Angle between points, except that the points are now the ends of the Vector from (0,0)
	 * @param startVec The first Vector
	 * @param endVec The second vector, the Angle will be in this direction
	 * @return The angle between the two Vectors going from Vector1 to Vector2
	 */
	public static float aAngleBetweenVectors( Vector2 startVec, Vector2 endVec ) {
		return aAngleBetweenPoints( startVec, endVec );
	}
	
	/**
	 * Scale or constrain any given value to the given minimum and maximum ranges
	 * @param value The value to be scaled
	 * @param minValue The minimum possible number this value can equal
	 * @param maxValue The maximum possible number this value can equal
	 * @param minRange The lower range of the scale
	 * @param maxRange The higher range of the scale
	 * @return
	 */
	public static float aScaleValue( float value, float minValue, float maxValue, float minRange, float maxRange ) {
		// Constrain the incoming value to make sure it's not less than or greater than the min and max values
		if( value > maxValue )
			value = maxValue;
		if( value < minValue )
			value = minValue;
		return ( value / (( maxValue - minValue) / (maxRange - minRange))) + minRange;
	}
	
	/**
	 * Convert a Body's raw angle, which can be many times the angle due to multiple
	 * Body rotations, to a usable Angle between PI and -PI
	 * @param angle A Body angle to convert
	 * @return The converted Body angle
	 */
	public static float aConvertToUsableAngle( float angle ) {
		// This whole time I had the answer right here!

		// correct the angle between the control and current angle of the car
		// since the physics body stores the angle by increments so we could have
		// an angle that is 300 because it the turned say 10 times.  Here we fix that
		// so we only get the smallest possible angle between control and car.
		while(angle <= Math.PI) angle += 2 * Math.PI;		
		while(angle > Math.PI) angle -= 2 * Math.PI;
		
		return angle;
	}

	/** Get the angle between any 2 points, in RADIANS **/ // SAGE MINT
	public static float aAngleBetweenPoints( Vector2 startPoint, Vector2 endPoint ) {
		// the angle between the 2 cars is offset by 90 degrees, so we add 90 degrees to fix it
		float angle = atan2( endPoint.y - startPoint.y, endPoint.x - startPoint.x );		
		if( angle >= -Math.PI && angle <= -Math.PI/2 )
			angle = (float) (Math.PI * 1.5f + angle);			
		else
			angle -= Math.PI / 2;		
		return  angle;
	}
	
	/** Get the eucladian distance between any 2 points **/
	public static float aDistanceBetweenPoints( Vector2 startPoint, Vector2 endPoint ) {
		return endPoint.sub( startPoint ).len();
	}
	
	/** Check whether the given point is on the line given the Start and End points of the line **/
	public static boolean aIsPointOnLine(Vector2 lineStartPoint, Vector2 lineEndPoint, Vector2 point) { 
        //this alows you to make a slightly less accurate  
        //check if you want to allow players to not have  
        //to have pixel perfect precision. 
        int Leniancy = 2;  
         
        //we are working with the basic math principle that 
        //any straight line in 2D can be represented with 
        //   y = mx + c 
        //   m = (y2 - y1)/(x2 - x1) 
        //   c = y - mx 
        float _m, _c, _y; 
        _m = (lineEndPoint.y - lineStartPoint.y) / (lineEndPoint.x - lineStartPoint.x); 
        _c = lineStartPoint.y - (_m * lineStartPoint.x); 
        _y = _m * point.x + _c; 

        //all we need to check is if its within a certain 
        //distance in y to give it leeway, and then we  
        //know if its close enough to be on the line 
        return (((point.y - Leniancy)) < _y && (_y < point.y + Leniancy)); 
    } 
	
	/**
	 * Round any given value to the given decimal places
	 * @param value The original decimal value
	 * @param decimalPlaces How many decimal places to round to
	 * @return The rounded value
	 */
	public static float aRound(float value, int decimalPlaces) {
		float p = (float) Math.pow(10,decimalPlaces);
		value = value * p;
		float tmp = Math.round(value);
		return (float) tmp/p;
	}

	/**
	 * Round a Vector by the given decimal places
	 * @param vector The vector to be rounded
	 * @param decimalPlaces Number of decimal places to round to
	 * @return The new and improved rounded Vector
	 */
	public static Vector2 aRound(Vector2 vector, int decimalPlaces) {
		vector.x = aRound( vector.x, decimalPlaces );
		vector.y = aRound( vector.y, decimalPlaces );
		return vector;
	}

}
