package org.firstinspires.ftc.teamcode.cybersalam.hardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class MecanumDrive {

    private DcMotor leftFront;
    private DcMotor leftRear;
    private DcMotor rightFront;
    private DcMotor rightRear;

    private IMU imu;
    GoBildaPinpointDriver pinpoint;

    private boolean isPinpoint;


    public void init(HardwareMap hwMap, boolean isGobildaPinpointIMU) {
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        leftRear = hwMap.get(DcMotor.class, "leftRear");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightRear = hwMap.get(DcMotor.class, "rightRear");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        if (isGobildaPinpointIMU) {
            isPinpoint = true;
            double offsetX = 14.3003496461;
            double offsetY = -14.3003496461;
            double encoderCPR = 4000;
            double encoderWheelCircumference = Math.PI * 16;

            pinpoint = hwMap.get(GoBildaPinpointDriver.class, "pinpointComputer");
            pinpoint.setOffsets(offsetX, offsetY, DistanceUnit.INCH);
            pinpoint.setEncoderResolution((encoderCPR / encoderWheelCircumference), DistanceUnit.MM);

            pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                    GoBildaPinpointDriver.EncoderDirection.FORWARD);
            pinpoint.resetPosAndIMU();


            pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, 0));
        }
        else {
            isPinpoint = false;
            imu = hwMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);

            imu.initialize(new IMU.Parameters(RevOrientation));
        }
    }

    public void drive(double forward, double strafe, double rotate) {
        double leftFrontPower = forward + strafe + rotate;
        double leftRearPower = forward - strafe + rotate;
        double rightFrontPower = forward - strafe - rotate;
        double rightRearPower = forward + strafe - rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;

        maxPower = Math.max(maxPower, Math.abs(leftFrontPower));
        maxPower = Math.max(maxPower, Math.abs(leftRearPower));
        maxPower = Math.max(maxPower, Math.abs(rightFrontPower));
        maxPower = Math.max(maxPower, Math.abs(rightRearPower));

        double scale = maxSpeed / maxPower;

        leftFront.setPower(leftFrontPower * scale);
        leftRear.setPower(leftRearPower * scale);
        rightFront.setPower(rightFrontPower * scale);
        rightRear.setPower(rightRearPower * scale);
    }
}