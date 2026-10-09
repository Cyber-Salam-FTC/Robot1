package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.pedropathing.autotuners.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedropathing.autotuners.MecanumTuner;
import org.firstinspires.ftc.teamcode.pedropathing.autotuners.PinpointTuner;

public class AutoTuning {
    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }
    @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }
    @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner((hardwareMap) -> new PinpointLocalizer(hardwareMap, Constants.localizerConfig), (hardwareMap) -> new Mecanum(hardwareMap, Constants.drivetrainConfig));
    }
}