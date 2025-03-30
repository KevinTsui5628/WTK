import kotlin.random.Random

interface Player {
    val name: String
    val maxHP: Int
    var currentHP: Int
    var numOfCards: Int
    var identity: Strategy?
    var roundSkipped: Boolean

    fun beingAttacked() {
        val damage = 1
        println("$name is being attacked.")

        val dodged = dodgeAttack()
        if (!dodged) {
            currentHP -= damage
            println("$name can't dodge the attack, current HP is $currentHP.")
        }

        if (identity is Lord) {
            val lord = identity as Lord
            if (!dodged) {
                lord.setState(1)
            }
            else {
                lord.setState(0)
            }
        }
    }

    fun dodgeAttack(): Boolean {
        // dodge logic
        val dodged = hasDodgeCard()  // 15% chance to dodge

        if (dodged) {
            println("$name dodged the attack by spending a dodge card.")
        }
        return dodged
    }

    fun hasDodgeCard(): Boolean {
        val dodged : Boolean
        val probability = 0.15  // 15% chance to have dodge card
        val randomValue = Random.nextDouble()

        if (randomValue <= probability) {
            dodged = true
        }
        else {
            dodged = false
        }
        return dodged
    }

    fun hasAttackCard() : Boolean {
        val attack : Boolean
        val probability = 0.2  // 20% chance to have attack card
        val randomValue = Random.nextDouble()

        if (randomValue <= probability) {
            attack = true
        }
        else {
            attack = false
        }
        return attack
    }

    fun drawCard() {
        val cardsToDraw = 2
        numOfCards += cardsToDraw
        println("$name draws $cardsToDraw cards and now has $numOfCards card(s).")
    }

    fun discardCard() {
        val cardsToDiscard = numOfCards - currentHP
        numOfCards -= cardsToDiscard
        println("$name discard $cardsToDiscard card(s), now has $numOfCards card(s).")
    }
}

abstract class General(final override val name: String) : Player {
    abstract override val maxHP: Int
    override var currentHP: Int = 0
    override var numOfCards: Int = 4
    override var identity: Strategy?= null
    override var roundSkipped: Boolean = false

    init {
        println("General $name has been created.")
    }
}

class GuanYuAdapter(private val guanYu: GuanYu) : General("Guan Yu") {
    override val maxHP: Int = guanYu.maximumHP
    override var currentHP: Int = guanYu.maximumHP

    override fun beingAttacked() {
        // Call the Player's beingAttacked method
        val damage = 1
        currentHP -= damage
        println("$name is being attacked. Current HP: $currentHP")
        if (currentHP > 0) {
            println("$name can't dodge the attack, current HP is $currentHP.")
        } else {
            println("$name has been defeated.")
        }
    }

    override fun dodgeAttack(): Boolean {
        // 50% chance to dodge
        val dodged = (0..1).random() == 1
        if (dodged) {
            println("$name dodged the attack!")
        }
        return dodged
    }
}

// Lord Generals
// SHU
class LiuBei : General("LIU Bei") {
    override val maxHP: Int = 5

    var state: State = if (currentHP >= 2) HealthyState() else UnhealthyState()

    fun playNextCard() {
        state.playNextCard(this)
    }

    fun benevolence(): Boolean {
        if (numOfCards >= 2) {
            numOfCards -= 2
            currentHP++
            println("[Benevolence] $name gives away two cards and recovers 1 HP, now his HP is $currentHP")
            return true
        }
        return false
    }
}

// WEI
class CaoCao : WeiGeneral("CAO Cao") {
    override val maxHP: Int = 5
    override fun beingAttacked() {
        val damage = 1
        println("[Entourage] $name activates Lord Skill Entourage.")

        var dodged = handleRequest()
        if (!dodged) {
            println("No one helps $name dodge the attack.")
            dodged = dodgeAttack()
            if (!dodged) {
                currentHP -= damage
            }
        }
    }
}

// WU
class SunQuan : General("SUN Quan") {
    override val maxHP: Int = 5
}

// Non-Lord Generals
// SHU
// Existing class
class GuanYu {
    val maximumHP = 4
}

class ZhangFei : General("ZHANG Fei") {
    override val maxHP: Int = 4
}

class ZhugeLiang : General("ZHUGE Liang") {
    override val maxHP: Int = 3
}

class LadyGan: General("LADY Gan") {
    override val maxHP: Int = 3
}

// WEI
class SimaYi: WeiGeneral("SIMA Yi") {
    override val maxHP: Int = 3
}

class ZhangLiao: WeiGeneral("ZHANG Liao") {
    override val maxHP: Int = 4
}

class ZhenJi: WeiGeneral("ZHEN Ji") {
    override val maxHP: Int = 3
}

class XuZhu : WeiGeneral("XU Zhu") {
    override val maxHP: Int = 4
}

// WU
class GanNing : General("GAN Ning") {
    override val maxHP: Int = 4
}

class DaQiao: General("DA Qiao") {
    override val maxHP: Int = 3
}

class SunShangxiang: General("SUN Shangxiang") {
    override val maxHP: Int = 3
}

class ZhouYu : General("ZHOU Yu") {
    override val maxHP: Int = 3

    override fun drawCard() {
        val standDraw = 2
        val drawAbility = 1
        val cardsToDraw = standDraw + drawAbility
        numOfCards += cardsToDraw
        println("[Heroism] $name draws $cardsToDraw cards and now has $numOfCards card(s).")
    }
}

// QUN
class HuaTuo: General("HUA Tuo") {
    override val maxHP: Int = 3
}

class LvBu : General("LV Bu") {
    override val maxHP: Int = 4
}

class DiaoChan : General("DIAO Chan") {
    override val maxHP: Int = 3

    override fun discardCard() {
        super.discardCard()
        val drawAbility = 1
        numOfCards += drawAbility
        println("[Beauty Outshining the Moon] $name now has $numOfCards card(s).")
    }
}

class HuaXiong : General("HUA Xiong") {
    override val maxHP: Int = 6
}