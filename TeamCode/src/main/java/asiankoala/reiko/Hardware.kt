package asiankoala.reiko

import asiankoala.reiko.subsystems.Arm
import asiankoala.reiko.subsystems.Indexer
import asiankoala.reiko.subsystems.Outtake
import asiankoala.reiko.subsystems.Turret
import com.asiankoala.koawalib.control.controller.PIDGains
import com.asiankoala.koawalib.control.motor.DisabledPosition
import com.asiankoala.koawalib.control.motor.FFGains
import com.asiankoala.koawalib.control.profile.MotionConstraints
import com.asiankoala.koawalib.hardware.motor.*
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.servo.KServo

class Hardware {
    val flMotor = KMotor("fl").brake.reverse
    val blMotor = KMotor("bl").brake.reverse
    val brMotor = KMotor("br").brake
    val frMotor = KMotor("fr").brake
    val intakeMotor = KMotor("intake").reverse

    val turretMotor = KMotor("turret")
            .brake
            .withPositionControl(
                    5.3333,
                    false,
                    PIDGains(0.05, 0.0, 0.0007),
                    FFGains(kS = 0.01),
                    allowedPositionError = 1.0
            )
            .zero(Turret.zeroAngle)

    val slideMotor = KMotor("slides")
            .float
            .reverse
            .withMotionProfileControl(
                    PIDGains(0.23, 0.0, 0.007),
                    FFGains(kS = 0.01),
                    MotionConstraints(180.0, 180.0),
                    allowedPositionError = 1.5,
                    disabledPosition = DisabledPosition(0.0, 1.5)
            )
            .zero(0.0)

    val duckMotor = KMotor("duckSpinner").brake

    val armServo = KServo("arm").startAt(Arm.armHomePosition)
    val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)

    val distanceSensor = KDistanceSensor("loadingSensor")
}
