package asiankoala.offseason.rework

import asiankoala.offseason.subsystems.Arm
import asiankoala.offseason.subsystems.Indexer
import asiankoala.offseason.subsystems.Outtake
import asiankoala.offseason.subsystems.Turret
import com.asiankoala.koawalib.control.motion.MotionConstraints
import com.asiankoala.koawalib.gamepad.functionality.Button
import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.rework.ComplexMotorSettings
import com.asiankoala.koawalib.rework.SimpleComplexMotor
import com.asiankoala.koawalib.rework.SimpleComplexMotorFFTuner
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
@Suppress("unused")
class TurretTuner : SimpleComplexMotorFFTuner() {
    // make sure no collisions
    val armServo = KServo("arm").startAt(Arm.armHomePosition)
    val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)

    override val homePosition: Double = 180.0
    override val motor: SimpleComplexMotor = SimpleComplexMotor(
            ComplexMotorSettings(
                    "turret",
                    0.05,
                    0.0,
                    0.0,
                    5.33333,
                    false,
            ),

            0.0,
            0.00000001, // very small positive for kS
            0.0,

            MotionConstraints(30.0, 30.0, 30.0),
            1.0,
    ).brake as SimpleComplexMotor

    override val targetPosition: Double = ComplexTurret.blueAngle
    override val toHomeButton: Button = driver.leftTrigger
    override val toTargetButton: Button = driver.rightTrigger

    override fun mInit() {
        super.mInit()
        motor.setTarget(homePosition)
        motor.isUsingVoltageFF = false
        motor.encoder.zero(Turret.zeroAngle)
    }

    override fun mLoop() {
        super.mLoop()
        Logger.addTelemetryData("angle", motor.encoder.pos)
    }
}