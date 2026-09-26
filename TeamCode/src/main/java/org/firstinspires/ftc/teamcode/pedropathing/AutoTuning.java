package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

public class AutoTuning {
    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }
}