abstract class Equipment(private val player : Player) : Player {
    override val name = player.name
    override val maxHP = player.maxHP
    override var currentHP = player.currentHP
    override var numOfCards = player.numOfCards

    override fun beingAttacked() {
        player.beingAttacked()
    }

    override fun dodgeAttack(): Boolean {
        return player.dodgeAttack()
    }
}

class EightTrigrams(player: Player) : Equipment(player) {
    override fun beingAttacked() {
        val damage = 1
        println("$name is being attacked.")
        val dodged = dodgeAttack()
        if (dodged) {
            println("$name dodged the attack with the eight trigrams.")
        } else {
            currentHP -= damage
            println("$name can't dodge the attack, current HP is $currentHP.")
        }

        if (currentHP == 0) {
            println("$name has been defeated.")
        }
    }

    override fun dodgeAttack(): Boolean {
        // 50% chance to dodge the attack
        println("Triggering the Eight Trigrams.")
        val dodged = (0..1).random() == 1
        println("Judgement is $dodged.")

        return dodged
    }
    override var identity: Strategy? = null
    override var roundSkipped: Boolean = false
}

