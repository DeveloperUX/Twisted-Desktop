package twisted.rubber.ai.complexbehavior.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.LeafAction;

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

public class DodgeObstacleAction extends LeafAction {
	
	private static final String LOG = "@ " + DodgeObstacleAction.class.getSimpleName();
	private static final Boolean DEBUG = true;

	Vector3 dir;
	Vector3 steering;
	Boolean checkObstacles = true;
	
	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public DodgeObstacleAction(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class, for debug purposes
	 */
	public DodgeObstacleAction(Blackboard blackboard, String name) {
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
	}

	@Override
	public void End() {
		LogTask("Ending");
	}

	@Override
	public void DoAction() {
		DebugAction();
		LogTask("Doing Action");
		bb.getAiControls().joystickAngle += MathUtils.degreesToRadians * 20;	
		if( bb.getAiControls().joystickStrength > 0.1 )
			bb.getAiControls().joystickStrength -= 0.05;
		
		GetControl().FinishWithSuccess();
		
//		if( bb.getAiControls().joystickStrength < 0.4 )
//			GetControl().FinishWithFailure();
		

		
		
		
		
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

}
