package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;


public class LEDController {
	
	OpMode opMode;
	
	RevBlinkinLedDriver blinkinLedDriver;
	RevBlinkinLedDriver.BlinkinPattern pattern;
	
	DisplayKind displayKind;
	
	state State;
	
	protected enum DisplayKind {
		MANUAL,
		AUTO
	}
	
	public enum state {
		INIT,
		WAIT,
		RUNNING
	}
	
	public LEDController(OpMode opMode){
		this.opMode = opMode;
		this.init();
	}
	
	private void init(){
		displayKind = DisplayKind.AUTO;
		
		blinkinLedDriver = opMode.hardwareMap.get(RevBlinkinLedDriver.class, "LED");
	}
	
	public void setState(state s){
		State = s;
		switch (s){
			case INIT:
				pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_RED;
				break;
			case WAIT:
				pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
				break;
			case RUNNING:
				pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE;
				break;
			default:
				pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
				break;
		}
	}
	
	public state getState(){ return State; }
	
}