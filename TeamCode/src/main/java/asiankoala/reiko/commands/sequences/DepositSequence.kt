package asiankoala.reiko.commands.sequences

import asiankoala.reiko.Strategy
import asiankoala.reiko.commands.subsystem.IndexerCommands
import asiankoala.reiko.subsystems.Indexer
import asiankoala.reiko.subsystems.Slides
import com.asiankoala.koawalib.command.commands.*
import com.asiankoala.koawalib.command.group.SequentialGroup

class DepositSequence(strategy: () -> Strategy, slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialGroup(
        ChooseCmd(
                InstantCmd({}),
                InstantCmd({
                    slides.motor.setProfileTarget(strategy.invoke().getSlideInches())
                }, slides)
        ) {
          false
        },
        WaitCmd( 0.5),
        WaitUntilCmd(continueDeposit),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCmd(0.4)
)
