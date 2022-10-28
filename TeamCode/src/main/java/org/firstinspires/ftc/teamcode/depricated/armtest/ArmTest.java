//package org.firstinspires.ftc.teamcode.depricated.armtest;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.teamcode.base.Robot;
//import org.firstinspires.ftc.teamcode.base.part.RobotPart;
//
//public class ArmTest extends RobotPart<ArmTestHardware, ArmTestSettings> {
//	private boolean wasArmActive = false;
//	private int armPosition;
//
//	public ArmTest(Robot robot, ArmTestHardware hardware, ArmTestSettings settings) {
//		super(robot, hardware, settings);
//	}
//
//	public ArmTest(Robot robot){
//		super(robot, new ArmTestHardware(), new ArmTestSettings());
//	}
//
//
//	/////////////////////
//	//RobotPart Methods//
//	/////////////////////
//	@Override
//	public void onConstruct() {
//
//	}
//
//	@Override
//	public void onInit() {
//
//	}
//
//	@Override
//	public void onStart() {
//		hardware.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//		hardware.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////		hardware.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////		setArmPosition(settings.armStartPos);
////		hardware.armMotor.setTargetPosition(armPosition);
//	}
//
//	@Override
//	public void onPause() {
//
//	}
//
//	@Override
//	public void onUnpause() {
//
//	}
//
//	@Override
//	public void onRunLoop(short runMode) {
//		moveArm(settings.armMovementSupplier.get() * settings.armMovementSpeed);
//		//hardware.ejectServo.setPosition(settings.ejectServoSupplier.get());
//		hardware.wheelServo.setPower(settings.ejectWheelSupplier.get());
//		hardware.wheelServo2.setPower(-settings.ejectWheelSupplier.get());
//	}
//
//	@Override
//	public void onAddTelemetry() {
//
//	}
//
//	@Override
//	public void onStop() {
//
//	}
//
//	private void moveArm(float power){
//		if(Math.abs(power) > settings.minInputRegisterVal){
//			hardware.armMotor.setPower(power * settings.armMovementSpeed);
//		}else {
//			hardware.armMotor.setPower(0);
//		}
//
////		if(Math.abs(power) > settings.minInputRegisterVal){
////			setArmPosition(armPosition + (int)(power * settings.armMovementSpeed));
////			wasArmActive = true;
////		}
////		else if(wasArmActive){
////			setArmPosition(hardware.armMotor.getCurrentPosition());
////			wasArmActive = false;
////		}
//
//
////		if(Math.abs(power) >= settings.minInputRegisterVal) {
////			hardware.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////			hardware.armMotor.setPower(power);
////
////			armPosition = hardware.armMotor.getCurrentPosition();
////
////			if(power < 0)
////				if(armPosition <= settings.armMinPos)
////					armPosition = settings.armMinPos - 10;
////				else
////					return;
////			else
////				if(armPosition >= settings.armMaxPos)
////					armPosition = settings.armMaxPos + 10;
////				else
////					return;
////		}
//
////		hardware.armMotor.setPower(1);
////		hardware.armMotor.setTargetPosition(armPosition);
////		hardware.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//	}
//
//	private void setArmPosition(int position){
//		armPosition = Math.min(Math.max(settings.armMinPos, position), settings.armMaxPos);
//		hardware.armMotor.setTargetPosition(armPosition);
//	}
//}
