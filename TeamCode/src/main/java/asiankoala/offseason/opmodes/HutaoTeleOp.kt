package asiankoala.offseason.opmodes

import asiankoala.offseason.Hutao
import asiankoala.offseason.Strategy
import asiankoala.offseason.Strats
import asiankoala.offseason.commands.sequences.teleop.DepositSequence
import asiankoala.offseason.commands.sequences.teleop.HomeSequence
import asiankoala.offseason.commands.sequences.teleop.IntakeSequence
import asiankoala.offseason.subsystems.Slides
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.path.PathBuilder
import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.KScheduler
import com.asiankoala.koawalib.command.commands.GVFCmd
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.MecanumCmd
import com.asiankoala.koawalib.command.group.SequentialGroup
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.logger.LoggerConfig
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance

open class HutaoTeleOp(private val alliance: Alliance) : KOpMode() {
    private lateinit var hutao: Hutao
    private val strategy = Strategy(alliance)

    override fun mInit() {
        hutao = Hutao(Pose(heading = 90.0.radians))
        driver.rightTrigger.onPress(GVFCmd(
                hutao.drive,
                PathBuilder(Pose2d(heading = 90.0.radians))
                        .splineTo(Vector2d(24.0, 24.0), 90.0.radians)
                        .build(),
                1.0,
                0.5,
                6.0,
                1.5
        ))
        bindDrive()
        bindDuck()
        bindCycling()
        bindStrategy()


        Logger.config = LoggerConfig(
                isLogging = true,
                isPrinting = false,
                isLoggingTelemetry = false,
                isDebugging = false,
                isDashboardEnabled = false,
                maxErrorCount = 1,
                maxWarningCount = 1
        )
    }

    private fun bindDrive() {
        hutao.drive.setDefaultCommand(
            MecanumCmd(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, rScalar = 0.7
            )
        )
    }

    private fun bindDuck() {
        driver.dpadUp.onPress(InstantCmd({hutao.duck.motor.power = 0.3 * alliance.decide(1.0, -1.0)}, hutao.duck))
        driver.dpadRight.onPress(InstantCmd({hutao.duck.motor.power = 1.0 * alliance.decide(1.0, -1.0)}, hutao.duck))
        driver.dpadDown.onPress(InstantCmd({hutao.duck.motor.power = 0.0}, hutao.duck))
    }

    private fun bindCycling() {
        driver.rightTrigger.onPress(IntakeSequence(::strategy, hutao.intake, hutao.outtake,
                hutao.indexer, hutao.turret, hutao.arm, hutao.slides))

        val depositCommand = SequentialGroup(
            DepositSequence(::strategy, hutao.slides, hutao.indexer, driver.leftTrigger::isJustPressed),
            HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.intake)
        )
        KScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && !depositCommand.isScheduled }, depositCommand)
    }

    private fun bindStrategy() {
        driver.leftBumper.onPress(InstantCmd( { strategy.strat = Strats.ALLIANCE_BLUE }))
        driver.rightBumper.onPress(InstantCmd( { strategy.strat = Strats.SHARED_BLUE }))
        driver.x.onPress(InstantCmd( { strategy.strat = Strats.ALLIANCE_RED }))
        driver.b.onPress(InstantCmd( { strategy.strat = Strats.SHARED_RED }))
        driver.y.onPress(InstantCmd({
            Slides.movingShared += 1.0
            hutao.slides.motor.setTarget(Slides.movingShared)
        }))

        driver.a.onPress(InstantCmd({
            Slides.movingShared -= 1.0
            hutao.slides.motor.setTarget(Slides.movingShared)
        }))
    }

    private fun hutaoPeriodic() {
        hutao.run {
            log()
            slides.motor.update()
            turret.motor.update()
        }
    }

    override fun mInitLoop() {
        hutaoPeriodic()
    }

    override fun mLoop() {
        hutaoPeriodic()
    }
}

