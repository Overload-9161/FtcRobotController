//package org.firstinspires.ftc.teamcode.util.Threads.position;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
///**
// * The purpose of this is to have one place that you access the position
// * data that we are using either from the IMU, Camera, or any other external sensors
// */
//@SuppressWarnings(value = "unused")
//public class AutoPosition {
//
//	OpMode opMode;
//	int[] startPosition, startOrientation;
//
//	static IMUPos imu;
//
//	/**
//	 *
//	 * @param opMode Just use "this"
//	 * @param startPosition the {x,y} of where the robot is starting
//	 * @param startOrientation The direction/orientation of the robot
//	 */
//	public AutoPosition(OpMode opMode, int[] startPosition, int[] startOrientation){
//		this.opMode = opMode;
//		this.startPosition = startPosition;
//		this.startOrientation = startOrientation;
//		imu = new IMUPos(opMode);
//	}
//
//	/**
//	 * This class is meant for you to get the position of the robot
//	 * @see getRotation for information about the rotation of the robot
//	 */
//	public static class getPosition {
//		public static double x = getData.x;
//		public static double y = getData.y;
//		public static double z = getData.z;
//	}
//
//	/**
//	 * Get the rotation of the robot in degrees
//	 * @see getPosition for the {x,y,z} of the robot
//	 */
//	public static class getRotation {
//		public static double roll = getData.roll;
//		public static double pitch = getData.pitch;
//		public static double yaw = getData.yaw;
//	}
//
//	/**
//	 * If you want direct access to the IMUPos class
//	 * @return The IMUPos class that this class initiated
//	 */
//	public IMUPos getImu(){
//		return imu;
//	}
//
//}