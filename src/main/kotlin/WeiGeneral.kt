import kotlin.random.Random

abstract class WeiGeneral(name: String) : General(name) {
    var nextWei: WeiGeneral? = null

    fun setNext(weiGeneral: WeiGeneral) : WeiGeneral {
        nextWei = weiGeneral
        return weiGeneral
    }

    fun handleRequest(): Boolean {
//        println("Checking dodging for $name")
        if (this is CaoCao) {
            return nextWei?.handleRequest() ?: false
        }
            if (hasDodgeCard() && Random.nextDouble() <= 0.5) {
            println("$name helps Cao Cao dodge an attack by spending a dodge card.")
            return true
        }
        return nextWei?.handleRequest() ?: false
    }

    fun entourage(): Boolean {
        return handleRequest()
    }
}