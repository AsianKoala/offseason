package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.ConditionalCommand
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class DepositSequence(strategy: () -> Strategy, slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialCommandGroup(
        ConditionalCommand(
                InstantCommand({}),
                InstantCommand({
                    slides.motor.followMotionProfile(strategy.invoke().getSlideInches())
                }, slides)
        ) {
          false
        },
        WaitCommand( 0.5),
        WaitUntilCommand(continueDeposit),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCommand(0.4)
)
