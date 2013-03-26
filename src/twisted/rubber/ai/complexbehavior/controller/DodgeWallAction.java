package twisted.rubber.ai.complexbehavior.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.biigoh.controls.AIController;
import com.biigoh.controls.AIRaycastCallback;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

public class DodgeWallAction extends Behavior {
	
	private static final String LOG = "@ " + DodgeWallAction.class.getSimpleName();
	private static final Boolean DEBUG = true;

	Vector3 dir;
	Vector3 steering;
	Boolean checkObstacles = true;
	
	private enum Wing {
		LEFT, 	
		RIGHT,
		BOTH,
		NEITHER
	}
	private Wing wingHit;
	
	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public DodgeWallAction(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class, for debug purposes
	 */
	public DodgeWallAction(Blackboard blackboard, String name) {
		super(blackboard, name);
	}

	@Override
	public boolean CheckConditions() {
		LogTask("Checking conditions");
		return true;
	}

	@Override
	public void Start() {
		LogTask("Starting");
		wingHit = Wing.NEITHER;
		
		// clear normals from list
		normals.clear();
		
		// Get the distance to look ahead of us depending on how fast we're moving
		float distanceToLookAhead = MathMan.aScaleValue( bb.carToControl.currentSpeed, 0, 80, 10, 50 );
		// Get a position vector from that distance
		Vector2 pointAhead = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle(), distanceToLookAhead );
		// A left and right wing vector 
		Vector2 rayLeft = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle() + (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
		Vector2 rayRight = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle() - (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
		
		// Forward Raycast callback
		AIRaycastCallback forwardRayCallback = new AIRaycastCallback();
		// Left Raycast callback
		AIRaycastCallback leftRayCallback = new AIRaycastCallback();
		// Right Raycast callback
		AIRaycastCallback rightRayCallback = new AIRaycastCallback();
		
		// Create and run raycasts
		BattleScreen.getPhysicsWorld().rayCast( forwardRayCallback, bb.carToControl.getPosition(), pointAhead );
		BattleScreen.getPhysicsWorld().rayCast( leftRayCallback, bb.carToControl.getPosition(), rayLeft );
		BattleScreen.getPhysicsWorld().rayCast( rightRayCallback, bb.carToControl.getPosition(), rayRight );
		// Check which raycasts were hit
		bb.forwardRayHit = obstacleAhead(forwardRayCallback);
		bb.leftRayHit = obstacleAhead(leftRayCallback);
		bb.rightRayHit = obstacleAhead(rightRayCallback);
		
		if( bb.leftRayHit && bb.rightRayHit )
			wingHit = Wing.BOTH;
		else if( bb.leftRayHit )
			wingHit = Wing.LEFT;
		else if( bb.rightRayHit )
			wingHit = Wing.RIGHT;
		
	}

	@Override
	public void End() {
		LogTask("Ending");
		wingHit = Wing.NEITHER;
	}

	@Override
	public void DoAction() {
		DebugAction();
		LogTask("Doing Action");
		
		
		
		Vector2 resultingForward = bb.carToControl.getBody().getLinearVelocity().cpy().nor().mul(2);		
		for( Vector2 vector : normals )
			resultingForward.add(vector);
		
		if( bb.forwardRayHit && (!bb.rightRayHit && !bb.leftRayHit) )
			bb.carToControl.getController().joystickAngle = MathMan.aAngleBetweenVectors( Vector2Pool.obtain(), resultingForward.nor() );
		// Steer to the left
		else if( bb.forwardRayHit || (bb.leftRayHit && bb.rightRayHit && bb.forwardRayHit) ) 
			bb.carToControl.getController().joystickAngle = MathMan.aAngleBetweenVectors( Vector2Pool.obtain(), bb.carToControl.getBody().getLinearVelocity().rotate(90) );			 
		// Steer right
		else if( (bb.leftRayHit && bb.forwardRayHit) || bb.leftRayHit )
			bb.carToControl.getController().joystickAngle = MathMan.aAngleBetweenVectors( Vector2Pool.obtain(), bb.carToControl.getBody().getLinearVelocity().rotate(-90) );		
		// Steer left
		else if( bb.rightRayHit || (bb.rightRayHit && bb.forwardRayHit) )
			bb.carToControl.getController().joystickAngle = MathMan.aAngleBetweenVectors( Vector2Pool.obtain(), bb.carToControl.getBody().getLinearVelocity().rotate(90) );		
		// Slow down and steer left
		else if( bb.forwardRayHit ) {
			bb.carToControl.getController().joystickAngle = MathMan.aAngleBetweenVectors( Vector2Pool.obtain(), bb.carToControl.getBody().getLinearVelocity().rotate(90) );
			bb.carToControl.getController().joystickStrength = 0.6f;
		}
		// No ray is hit so we are clear
		else
			GetControl().FinishWithSuccess();
			
		
		
		
//		Vector2 trans = bb.carToControl.getPosition();
//		
//		List<Vector3> steeringRays = new ArrayList<Vector3>();
//		float _holdTheJump = dir.y;
//
//		Boolean left = false;
//		Boolean right = false;
//		Boolean front = false;
//		Vector3 adjDirection = dir;
//
//		steeringRays.Add(trans.TransformDirection(-steering.x, steering.y, steering.z)); //ray pointed slightly left 
//		steeringRays.Add(trans.TransformDirection(steering.x, steering.y, steering.z)); //ray pointed slightly right 
//		steeringRays.Add(trans.forward); //ray 1 is pointed straight ahead
//
//		AIRaycastCallback hit;
//
//		if (checkObstacles ) {
//			Debug.DrawRay(trans.localPosition, steeringRays[0].normalized * rayLength, Color.cyan);
//			Debug.DrawRay(trans.localPosition, steeringRays[1].normalized * rayLength, Color.cyan);
//			Debug.DrawRay(trans.localPosition, steeringRays[2].normalized * rayLength, Color.cyan);
//
//
//			if (Physics.Raycast(trans.position, steeringRays[0], out hit, rayLength)) {
//				if (hit.collider.gameObject.layer != 13 && (!front && !left)) {
//					isSteering = true;
//					front=false; right=false; left=true;
//					Debug.DrawLine(trans.position, hit.point, Color.red);
//					trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
//					Gdx.app.log( LOG, "Steer Left" );
//				}
//			}
//			else          
//				if (Physics.Raycast(trans.position, steeringRays[1], out hit, rayLength))
//				{
//					if (hit.collider.gameObject.layer != 13 && (!front && !left)) //Character layer
//					{
//						Debug.DrawLine(trans.position, hit.point, Color.red);
//						front=false; right=true; left=false;
//						isSteering = true;
//						trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
//						Gdx.app.log( LOG, "Steer Right" );
//					}
//				}
//				else 
//				{
//					isSteering = false;
//					left = false; right = false; front = false;
//				}
//		}
	}
	
	Vector2 obstacleNormal = new Vector2(0,0);
	List<Vector2> normals = new ArrayList<Vector2>();

	private boolean obstacleAhead( AIRaycastCallback callback ) {		
		if( callback.fixture != null ) {
			EntityData ed = (EntityData) callback.fixture.getBody().getUserData();						
			if( ed.getType() == EntityData.Type.WALL ) {
				normals.add( callback.normal );
				return true;
			}
		}			
		return false;
	}

}
