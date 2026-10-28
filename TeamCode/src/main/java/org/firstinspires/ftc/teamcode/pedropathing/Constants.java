package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftRear");
        c.backRightName.set("rightRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpointComputer");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(6.065023827740527);
        c.yPodOffset.set(-0.6211368305476632);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.1463678924285366);
                Controller secondaryTranslationalForward = Controller.proportional(0.054079025230228256);
                Controller primaryTranslationalLateral = Controller.proportional(0.2078869695828487);
                Controller secondaryTranslationalLateral = Controller.proportional(0.07680868041873035);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01772895285343049));
                c.brake.set(Controller.proportionalFeedforward(0.015069609925415915));

                c.headingFeedback.set(Controller.proportional(3.5933140739816087));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04758518193095468, 0.005112947653922388));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0696195159431807, 0.08099687390408444));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.460490898592503E-4, 7.888471457789125E-4));

                c.maxAchievableForwardVelocity.set(60.027855788569624);
                c.maxAchievableStrafeVelocity.set(53.01924777599072);
                c.naturalForwardDeceleration.set(36.72898419939789);
                c.naturalStrafeDeceleration.set(51.48708176658278);

//                c.maxVelocityConstraint.set(30.0);
//                c.maxAchievableForwardVelocity.set(85.0);
//                c.maxAchievableStrafeVelocity.set(65.0);
//
//                c.maxAccelerationConstraint.set(120.0);
//                c.maxDecelerationConstraint.set(90.0);
            }
    );

    public static Follower createFollower(HardwareMap h) {
        return new Follower(
            new PinpointLocalizer(h, localizerConfig),
            new Mecanum(h, drivetrainConfig),
            new Foresight(foresightConfig)
        );
    }
}