package asiankoala.offseason

import asiankoala.reiko.Reiko
import asiankoala.reiko.Strategy
import asiankoala.reiko.Strats
import asiankoala.reiko.commands.sequences.DepositSequence
import asiankoala.reiko.commands.sequences.HomeSequence
import asiankoala.reiko.commands.sequences.IntakeSequence
import asiankoala.reiko.subsystems.Slides
import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.KScheduler
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.MecanumCmd
import com.asiankoala.koawalib.command.group.SequentialGroup
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.logger.LoggerConfig
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance

open class ReikoTeleOp(private val alliance: Alliance) : KOpMode() {
    private lateinit var reiko: Reiko
    private val strategy = Strategy(alliance)

    override fun mInit() {
        reiko = Reiko(Pose(heading = 90.0.radians))
        bindDrive()
        bindDuck()
        bindCycling()
        bindStrategy()


        Logger.config = LoggerConfig.SIMPLE_CONFIG
    }

    private fun bindDrive() {
        reiko.drive.setDefaultCommand(
            MecanumCmd(
                reiko.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, rScalar = 0.7
            )
        )
    }

    private fun bindDuck() {
        driver.dpadUp.onPress(InstantCmd({reiko.duck.motor.power = 0.3 * alliance.decide(1.0, -1.0)}, reiko.duck))
        driver.dpadRight.onPress(InstantCmd({reiko.duck.motor.power = 1.0 * alliance.decide(1.0, -1.0)}, reiko.duck))
        driver.dpadDown.onPress(InstantCmd({reiko.duck.motor.power = 0.0}, reiko.duck))
    }

    private fun bindCycling() {
        driver.rightTrigger.onPress(IntakeSequence(::strategy, reiko.intake, reiko.outtake,
                reiko.indexer, reiko.turret, reiko.arm, reiko.slides))

        val depositCommand = SequentialGroup(
            DepositSequence(::strategy, reiko.slides, reiko.indexer, driver.leftTrigger::isJustPressed),
            HomeSequence(reiko.turret, reiko.slides, reiko.outtake, reiko.indexer, reiko.arm, reiko.intake)
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
            reiko.slides.motor.setProfileTarget(Slides.movingShared)
        }))

        driver.a.onPress(InstantCmd({
            Slides.movingShared -= 1.0
            reiko.slides.motor.setProfileTarget(Slides.movingShared)
        }))
    }

    private fun hutaoPeriodic() {
        reiko.log()
    }

    override fun mInitLoop() {
        hutaoPeriodic()
    }

    override fun mLoop() {
        hutaoPeriodic()
    }
}

