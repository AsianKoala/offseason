package asiankoala.offseason.commands.sequences.teleop

import asiankoala.offseason.Strategy
import asiankoala.offseason.commands.subsystem.IndexerCommands
import asiankoala.offseason.subsystems.Indexer
import asiankoala.offseason.subsystems.Slides
import com.asiankoala.koawalib.command.commands.*
import com.asiankoala.koawalib.command.group.SequentialGroup

class DepositSequence(strategy: () -> Strategy, slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialGroup(
        ChooseCmd(
                InstantCmd({}),
                InstantCmd({
                    slides.motor.setTarget(strategy.invoke().getSlideInches())
                }, slides)
        ) {
          false
        },
        WaitCmd( 0.5),
        WaitUntilCmd(continueDeposit),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCmd(0.4)
)
