package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoDepositSequence
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.ParallelRaceGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState

open class HutaoCycleAuto(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        hutao = Hutao(Pose())

        val seq = SequentialCommandGroup(
                AutoInitSequence(alliance, driver.rightTrigger, hutao.outtake,
                        hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                InstantCommand({hutao.turret.motor.setPIDTarget(alliance.decide(Turret.autoBlueAngle, Turret.autoRedAngle))}),
                WaitCommand(0.3),
                AutoDepositSequence(hutao.slides, hutao.indexer),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer,
                        hutao.arm, hutao.intake),

        ).withName("seq")

        mainCommand = SequentialCommandGroup(
                ParallelRaceGroup(
                        WaitUntilCommand { opmodeState == OpModeState.LOOP }
                                .andThen(WaitCommand(28.0)),
                        seq
                ),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.intake)
        )

        mainCommand.schedule()
    }
}
