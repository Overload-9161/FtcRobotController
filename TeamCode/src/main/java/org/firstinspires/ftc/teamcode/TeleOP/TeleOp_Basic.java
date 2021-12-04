package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;
import org.firstinspires.ftc.teamcode.util.hardware.HardTele;

@TeleOp(name = "TeleOp_Basic", group = "TeleOp")
//@Disabled
public class TeleOp_Basic extends OpMode {
	
	boolean inReverse   = false;//reverse button is b
	boolean bWasPressed = false;
	
	HardTele r = new HardTele();
	FileManager fileManager = new FileManager(this);
	//LEDController ledController = new LEDController(this);
	
	ElapsedTime time = new ElapsedTime();
	
	@Override
	public void init() {
		//ledController.setState(state.INIT);
		time.startTime();
		r.initRobot(this);
		fileManager.init("TeleOp");
		fileManager.initMotors(r.allMotors);
		fileManager.StartTeleOp(time);
		//ledController.setState(state.WAIT);
	}
	
	@Override
	public void start() {
		//ledController.setState(state.RUNNING);
		time.startTime();
		super.start();
	}
	
	@Override
	public void loop() {
		{
			double deflator;
			
			deflator = gamepad1.right_bumper ? .4 : .9;
			
			if (gamepad1.left_bumper)
				deflator = 1;
			
			//legacy code that runs our mecanum drive wheels
			
			//this first section creates the variables that will be used later
			
			if (gamepad1.b && !bWasPressed)
				inReverse = !inReverse;
			bWasPressed = gamepad1.b;
			//first we must translate the rectangular values of the joystick into polar coordinates;
			double y = -1 * gamepad1.left_stick_y;
			double x = gamepad1.left_stick_x;
			double angle = 0;
			
			if (y > 0 && x > 0)//quadrant 1
				angle = Math.atan(y / x);
			else {
				double angle1 = Math.toRadians(180) + Math.atan(y / x);
				if (y > 0 && x < 0)//quadrant 2
					angle = angle1;
				else if (y < 0 && x < 0)//quadrant 3
					angle = angle1;
				else if (y < 0 && x > 0)//quadrant 4
					angle = Math.toRadians(360) + Math.atan(y / x);
			}
			
			if (y == 0 && x > 1)
				angle = 0;
			if (y > 0 && x == 0)
				angle = Math.PI / 2;
			if (y == 0 && x < 0)
				angle = Math.PI;
			if (y < 0 && x == 0)
				angle = 3 * Math.PI / 2;
			
			double velocity = Math.sqrt(Math.pow(gamepad1.left_stick_y, 2) + Math.pow(gamepad1.left_stick_x, 2));
			double rotation = -gamepad1.right_stick_x;
			
			if (inReverse)//reverse button
				angle += Math.toRadians(180);
			
			angle += Math.toRadians(270);
			
			//equations taking the polar coordinates and turing them into motor powers
			double v1 = velocity * Math.cos(angle + (Math.PI / 4));
			double v2 = velocity * Math.sin(angle + (Math.PI / 4));
			double power1 = v1 - rotation;
			double power2 = v2 + rotation;
			double power3 = v2 - rotation;
			double power4 = v1 + rotation;
//			fileManager.writeFile("Gamepad1-LStick?Polar", new double[]{angle, velocity, rotation}, time.milliseconds())
//					   .writeFile("Robot-Power", new double[]{power1, power2, power3, power4}, time.milliseconds());
			r.frontLeft.setPower(power1 * deflator);
			r.frontRight.setPower(power2 * deflator);
			r.backLeft.setPower(power3 * deflator);
			r.backRight.setPower(power4 * deflator);
		}
		
		// TODO: Change the decimal at the end to change the speed of the motor
		// TODO: decrease to make it go slower, increase to go faster
		r.lift.setPower(-gamepad1.right_stick_y * 0.7);
		
		// TODO: Change the "0.6" to change the speed
		if(gamepad1.dpad_up)
			r.inout.setPower(-0.6);
		else if(gamepad1.dpad_down)
			r.inout.setPower(0.6);
		else r.inout.setPower(0);
		
	}
	
	@Override
	public void stop() {
		fileManager.Break("TeleOp Finished");
		fileManager.close();
		super.stop();
	}
}