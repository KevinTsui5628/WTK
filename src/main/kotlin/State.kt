interface State {
    fun playNextCard(liuBei: LiuBei)
}

class HealthyState : State {
    override fun playNextCard(liuBei: LiuBei) {
        if (liuBei.currentHP <= 1) {
            println("${liuBei.name} is not healthy.")
            liuBei.state = UnhealthyState()
            liuBei.state.playNextCard(liuBei)
        } else {
            val target = GeneralManager.getGeneralsList().find { it.identity is Rebel }
            if (target != null && liuBei.hasAttackCard()) {
                liuBei.numOfCards--
                println("${liuBei.name} spends a card to attack a ${target.identity?.identityName}, ${target.name}")
                target.beingAttacked()
            } else {
                println("${liuBei.name} doesn't have attack card.")
            }
        }
    }
}

class UnhealthyState : State {
    override fun playNextCard(liuBei: LiuBei) {
        if (liuBei.benevolence()) {
            println("${liuBei.name} is now healthy.")
            liuBei.state = HealthyState()
        }
    }
}