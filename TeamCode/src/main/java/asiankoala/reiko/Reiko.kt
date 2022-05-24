package asiankoala.reiko

import asiankoala.reiko.subsystems.*
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive

class Reiko(startPose: Pose) {
    private val hardware = Hardware()
    private val odometry = Odometry(hardware)

    val drive = KMecanumOdoDrive(hardware.flMotor, hardware.blMotor, hardware.brMotor, hardware.frMotor, odometry.odo, true)
    val intake = Intake(hardware.intakeMotor, hardware.distanceSensor)
    val arm = Arm(hardware.armServo)
    val indexer = Indexer(hardware.indexerServo)
    val outtake = Outtake(hardware.outtakeServo)
    val duck = Duck(hardware.duckMotor)
    val turret = Turret(hardware.turretMotor)
    val slides = Slides(hardware.slideMotor)

    fun log() {
        Logger.addTelemetryData("position", drive.pose)
        Logger.addTelemetryData("turret angle", turret.motor.pos)
        Logger.addTelemetryData("slides inches", slides.motor.pos)
    }

    init {
        drive.setStartPose(startPose)
        slides.motor.setProfileTarget(0.0)
        turret.motor.setTargetPosition(Turret.homeAngle)
    }
}