package org.firstinspires.ftc.teamcode.util.IMUUtil;


import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.meanIntegrate;
import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.plus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * {@link com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator} provides a very naive implementation of
 * an acceleration integration algorithm. It just does the basic physics.
 * One you would actually want to use in a robot would, for example, likely
 * filter noise out the acceleration data or more sophisticated processing.
 */
public class Accel implements BNO055IMU.AccelerationIntegrator
{
	//------------------------------------------------------------------------------------------
	// State
	//------------------------------------------------------------------------------------------
	
	BNO055IMU.Parameters parameters;
	Position position;
	Velocity velocity;
	Acceleration acceleration;
	
	public Position getPosition() { return this.position; }
	public Velocity getVelocity() { return this.velocity; }
	public Acceleration getAcceleration() { return this.acceleration; }
	
	//------------------------------------------------------------------------------------------
	// Construction
	//------------------------------------------------------------------------------------------
	
	public Accel() {
		this.parameters = null;
		this.position = new Position();
		this.velocity = new Velocity();
		this.acceleration = null;
	}
	
	//------------------------------------------------------------------------------------------
	// Operations
	//------------------------------------------------------------------------------------------
	
	@Override public void initialize(@NonNull BNO055IMU.Parameters parameters, @Nullable Position initialPosition, @Nullable Velocity initialVelocity)
	{
		this.parameters = parameters;
		this.position = initialPosition != null ? initialPosition : this.position;
		this.velocity = initialVelocity != null ? initialVelocity : this.velocity;
		this.acceleration = null;
	}
	
	@Override public void update(Acceleration linearAcceleration)
	{
		// We should always be given a timestamp here
		if (linearAcceleration.acquisitionTime != 0)
		{
			// We can only integrate if we have a previous acceleration to baseline from
			if (acceleration != null)
			{
				Acceleration accel = new Acceleration();
				Acceleration accelPrev;
				Velocity     velocityPrev = velocity;
				
				acceleration = linearAcceleration;
				
				accel.xAccel = Math.floor(acceleration.xAccel * 10) / 10;
				accel.yAccel = Math.floor(acceleration.yAccel * 10) / 10;
				accel.zAccel = Math.floor(acceleration.zAccel * 10) / 10;
				
				accelPrev = accel;
				
				if (accelPrev.acquisitionTime != 0)
				{
					Velocity deltaVelocity = meanIntegrate(accel, accelPrev);
					velocity = plus(velocity, deltaVelocity);
				}
				
				if (velocityPrev.acquisitionTime != 0)
				{
					Position deltaPosition = meanIntegrate(velocity, velocityPrev);
					position = plus(position, deltaPosition);
				}
				
				if (parameters != null && parameters.loggingEnabled)
				{
					RobotLog.vv(parameters.loggingTag, "dt=%.3fs accel=%s vel=%s pos=%s", (acceleration.acquisitionTime - accelPrev.acquisitionTime)*1e-9, acceleration, velocity, position);
				}
			}
			else
				acceleration = linearAcceleration;
		}
	}
}