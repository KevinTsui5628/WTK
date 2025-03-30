object GeneralManager {
    private val list: MutableList<Player> = mutableListOf()
    init {
        println("Setting up the general manager.")
    }

    fun addGeneral(player: Player) {
        list.add(player)
        println("${player.name}, a ${player.identity?.identityName}, has ${player.currentHP} health point(s).")

        if (player is WeiGeneral) {
            println("${player.name} added to the Wei chain")
        }

        if (player.identity is Spy) {
            println("${player.name} is observing lord.")
        }

        if (player is LiuBei) {
            player.state = HealthyState()
            player.identity = LiuBeiStrategy()
        }
    }

    fun createGenerals(numOfPlayer: Int) {
        val nonLords = numOfPlayer - 1
        val lordFactory = LordFactory()
        val nonLordFactory = NonLordFactory(numOfPlayer)
//        println(nonLordFactory.getIdentityTable())

        // Create and add lords
        val lord = lordFactory.createRandomGeneral()
//        lord.currentHP = lord.maxHP

        if (lord is LiuBei) {
            lord.currentHP = 1
        } else {
            lord.currentHP = lord.maxHP
        }

        addGeneral(lord)

        // Create and add non-lords
        for (i in 1..nonLords) {
            val nonLord = nonLordFactory.createRandomGeneral()
            nonLord.currentHP = nonLord.maxHP
            addGeneral(nonLord)
        }
    }

    fun removeGeneral(general: General) {
        if (list.remove(general)) {
            println("General ${general.name} has been removed.")
        } else {
            println("General ${general.name} not found in the list.")
        }
    }

    fun getGeneralCount(): Int {
        return list.size
    }

    fun getGeneralsList(): MutableList<Player> {
        return list
    }

    abstract class Phase {
        val invoker = Invoker()
        val acedia = Acedia()
        val generalManager = GeneralManager
        val fourthPlayer = generalManager.getGeneralsList()[3]

        fun execute(player: Player) {
            perform(player)
        }

        abstract fun perform(player: Player)
    }

    class PreparationPhase : Phase() {
        override fun perform(player: Player) {
            if (player.name == fourthPlayer.name) {
                invoker.setCommand(acedia)
                invoker.executeSetup()
            }
        }
    }

    class JudgementPhase : Phase() {
        override fun perform(player: Player) {
            if (player.name == fourthPlayer.name) {
                invoker.setCommand(acedia)
                invoker.executeCommand()
            }
        }
    }

    class DrawPhase : Phase() {
        override fun perform(player: Player) {
            player.drawCard()
        }
    }

    class PlayPhase : Phase() {
        override fun perform(player: Player) {
            if (player is LiuBei) {
                (player.identity as? LiuBeiStrategy)?.playNextCard(player)
                println("${player.name} has ${player.numOfCards} card(s), current HP is ${player.currentHP}.")
            } else {
                println("${player.name} has ${player.numOfCards} card(s), current HP is ${player.currentHP}.")
                if (player.hasAttackCard() && !player.roundSkipped) {
                    val target = player.identity?.whomToAttack()
                    player.numOfCards--
                    println("${player.name} spends a card to attack a ${target?.identity?.identityName}, ${target?.name}")
                    target?.beingAttacked()
                } else {
                    println("${player.name} doesn't have attack card.")
                }
            }
        }
    }

    class DiscardPhase : Phase() {
        override fun perform(player: Player) {
            player.discardCard()
        }
    }

    class FinalPhase : Phase() {
        override fun perform(player: Player) {
            println("${player.name}'s turn is finished.")
        }
    }

    fun gameStart() {
        for (player in list) {
            PreparationPhase().execute(player)
        }

        var previousWeiGeneral: WeiGeneral? = null
        for (player in list) {
            if (player is WeiGeneral) {
                if (previousWeiGeneral == null) {
                    previousWeiGeneral = player
                } else {
                    previousWeiGeneral.setNext(player)
                    previousWeiGeneral = player
                }
            }
        }
        repeat(2) {
            for (player in list) {
                JudgementPhase().execute(player)
                DrawPhase().execute(player)
                PlayPhase().execute(player)
                DiscardPhase().execute(player)
//            FinalPhase().execute(player)
            }
            println("")
        }
    }
}