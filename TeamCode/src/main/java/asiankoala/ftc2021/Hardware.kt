package asiankoala.ftc2021

import asiankoala.offseason.subsystems.Arm
import asiankoala.offseason.subsystems.Indexer
import asiankoala.offseason.subsystems.Outtake
import com.asiankoala.koawalib.control.motion.MotionConstraints
import com.asiankoala.koawalib.hardware.motor.*
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.servo.KServo

class Hardware {
    val flMotor = KMotor("fl").brake.reverse
    val blMotor = KMotor("bl").brake.reverse
    val brMotor = KMotor("br").brake
    val frMotor = KMotor("fr").brake
    val intakeMotor = KMotor("intake").reverse

    val turretMotor = KMotorEx(
        KMotorExSettings(
            "turret",
            5.33333,
            false,
            1.0,
            isMotionProfiled = false,
            isVoltageCorrected = false,
            PIDSettings(
                0.05,
                0.0,
                0.0007
            ),
            FFSettings(
                kS = 0.01,
            ),
        )
    ).brake as KMotorEx

    val slideMotor = KMotorEx(
        KMotorExSettings(
            "slides",
            20.8333,
            false,
            1.5,
            isMotionProfiled = true,
            isVoltageCorrected = false,

            PIDSettings(
                0.23,
                0.0,
                0.007,
            ),
            FFSettings(
                kS = 0.01
            ),

            MotionConstraints(
                180.0,
                180.0
            ),

            0.0,
        )
    ).float.reverse as KMotorEx

    val duckMotor = KMotor("duckSpinner").brake

    val armServo = KServo("arm").startAt(Arm.armHomePosition)
    val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)

    val distanceSensor = KDistanceSensor("loadingSensor")
}
