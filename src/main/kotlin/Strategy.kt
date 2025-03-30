abstract class Strategy {
    abstract val identityName: String
    abstract fun whomToAttack() : Player?
}

open class Lord: Strategy(), Subject {
    override val identityName = "Lord"
    override fun whomToAttack(): Player? {
        return GeneralManager.getGeneralsList().find { it.identity is Rebel }
    }

    private val observers = mutableListOf<Observer>()
    private var state: String = ""

    override fun attach(observer: Observer) {
        observers.add(observer)
    }

    override fun detach(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach { it.update() }
    }

    fun setState(stateNo: Int) {
        state = when (stateNo) {
            0 -> "dodge success"
            1 -> "dodge fail"
            else -> throw IllegalArgumentException("Invalid state.")
        }
        notifyObservers()
    }
    fun getState(): String {
        return state
    }
}

class Loyalist: Strategy() {
    override val identityName = "Loyalist"
    override fun whomToAttack(): Player? {
        return GeneralManager.getGeneralsList().find { it.identity is Rebel }
    }
}

class Spy: Strategy(), Observer {
    override val identityName = "Spy"
    override fun whomToAttack(): Player? {
        return GeneralManager.getGeneralsList().find { it.identity is Rebel }
    }
    private var riskLevel = 50.0

    override fun update() {
        val lord = GeneralManager.getGeneralsList().find { it.identity is Lord }?.identity as Lord
        val spy = GeneralManager.getGeneralsList().find { it.identity is Spy }?.name
        when (lord.getState()) {
            "dodge success" -> riskLevel *= 0.5
            "dodge fail" -> riskLevel *= 1.5
        }
        println("$spy on Lord's Risk Level: $riskLevel")
    }
}

class Rebel: Strategy() {
    override val identityName = "Rebel"
    override fun whomToAttack(): Player? {
        return GeneralManager.getGeneralsList().find { it.identity is Lord }
    }
}

class LiuBeiStrategy : Lord() {
    fun playNextCard(liuBei: LiuBei) {
        liuBei.state.playNextCard(liuBei)
    }
}