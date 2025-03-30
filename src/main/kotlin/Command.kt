import kotlin.random.Random

interface Command {
    fun setup()
    fun execute()
}

class Acedia : Command {
    private val generalManager = GeneralManager
    private val fourthPlayer = generalManager.getGeneralsList()[3]

    override fun setup() {
        println("${fourthPlayer.name} being placed the Acedia card.")
    }

    override fun execute()  {
        val suits = listOf("spades", "clubs", "hearts", "diamonds")
        val randomSuit = suits[Random.nextInt(suits.size)]
        println("${fourthPlayer.name} judging the Acedia card.")
        println("${fourthPlayer.name} draw $randomSuit")

        if (randomSuit == "hearts") {
            println("${fourthPlayer.name} can't dodge the Acedia card. Skipping one round.")
            fourthPlayer.roundSkipped = true
        }
        else {
            println("${fourthPlayer.name} dodge the Acedia card! Skipping denied!")
        }
    }
}

// Invoker
class Invoker {
    private var command: Command? = null

    fun setCommand(command: Command) {
        this.command = command
    }

    fun executeSetup() {
        command?.setup()
    }

    fun executeCommand() {
        command?.execute()
    }
}