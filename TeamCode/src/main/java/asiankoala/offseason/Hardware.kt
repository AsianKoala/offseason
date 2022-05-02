package asiankoala.offseason

import asiankoala.offseason.subsystems.Arm
import asiankoala.offseason.subsystems.Indexer
import asiankoala.offseason.subsystems.Outtake
import com.asiankoala.koawalib.control.FeedforwardConstants
import com.asiankoala.koawalib.control.MotorControlType
import com.asiankoala.koawalib.control.PIDConstants
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.hardware.motor.KMotorExConfig
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.servo.KServo

class Hardware {
    val flMotor = KMotor("fl").brake.reverse
    val blMotor = KMotor("bl").brake.reverse
    val brMotor = KMotor("br").brake
    val frMotor = KMotor("fr").brake
    val intakeMotor = KMotor("intake").reverse
    val turretMotor = KMotorEx(
        KMotorExConfig(
            "turret",
            5.33333,
            false,
            MotorControlType.POSITION_PID,
            PIDConstants(
                0.05,
//                0.035,
                0.0007
            ),
            FeedforwardConstants(
                kStatic = 0.01
            ),
            positionEpsilon = 1.0,

        )
    ).brake as KMotorEx

    val slideMotor = KMotorEx(
        KMotorExConfig(
            "slides",
            20.8333,
            false,
            controlType = MotorControlType.MOTION_PROFILE,
            pid = PIDConstants(
                kP = 0.23,
                kD = 0.007,
            ),
            ff = FeedforwardConstants(
                kStatic = 0.01
            ),
            maxVelocity = 180.0,
            maxAcceleration = 180.0,
            positionEpsilon = 1.5,
            homePositionToDisable = 0.0,
        )
    ).float.reverse as KMotorEx

    val duckMotor = KMotor("duckSpinner").brake

    val armServo = KServo("arm").startAt(Arm.armHomePosition)
    val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)

    val distanceSensor = KDistanceSensor("loadingSensor")
}
