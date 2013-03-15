package com.biigoh.view;

import aurelienribon.tweenengine.TweenAccessor;

public class ChaseCameraAccessor implements TweenAccessor<ChaseCamera> {
	
	// The following lines define the different possible tween types.

    public static final int POSITION = 1;
    public static final int ZOOM = 2;
    
	@Override
	public int getValues(ChaseCamera target, int tweenType, float[] returnValues) {
		
		switch( tweenType ) {
		    case POSITION:
		        returnValues[0] = target.position.x;
		        returnValues[1] = target.position.y;
		        return 2;
		    case ZOOM:
		    	returnValues[0] = target.zoom;
		    default: 
		    	assert false; 
		    	return -1;
		}
	}

	@Override
	public void setValues(ChaseCamera target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION: 
            	target.position.x = newValues[0];
            	target.position.y = newValues[1];
            	break;
            case ZOOM:
                target.zoom = newValues[0];
                break;
            default: 
            	assert false; 
            	break;
        }
	}
	
}
