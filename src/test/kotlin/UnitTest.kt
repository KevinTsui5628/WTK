import kotlin.test.Test
import kotlin.test.assertTrue

class WeiTest {
    @Test
    fun testEntourage() {
        GeneralManager.getGeneralsList().clear()
        var previousWei: WeiGeneral
        val caoCao = CaoCao()
        caoCao.currentHP = caoCao.maxHP
        caoCao.identity = Lord()
        caoCao.numOfCards = 4
        GeneralManager.addGeneral(caoCao)
        previousWei = caoCao

        val factory = WeiOnlyNonLordFactory(4)

        for (i in 1..3) {
            val general = factory.createRandomGeneral()
            if (general is WeiGeneral) {
                previousWei.setNext(general)
                previousWei = general
            }
            general.currentHP = general.maxHP
            general.numOfCards = 4
            GeneralManager.addGeneral(general)
        }
        assertTrue(caoCao.entourage())
    }
}

class WeiOnlyNonLordFactory(numOfPlayer : Int) : NonLordFactory(numOfPlayer) {
    override fun createRandomGeneral(): General {
        val general = super.createRandomGeneral()
        if (general !is WeiGeneral) {
//            general.currentHP = general.maxHP
//            GeneralManager.addGeneral(general)
//            GeneralManager.removeGeneral(general)

            println("${general.name} is discarded as he/she is not a Wei.")
            if (general.identity is Loyalist) {
                if (loyalist == 0) {
                    identity.add(Loyalist())
                } else {
                    loyalist++
                }
            } else if (general.identity is Rebel) {
                if (rebel == 0) {
                    identity.add(Rebel())
                } else {
                    rebel++
                }
            } else if (general.identity is Spy) {
                if (spy == 0) {
                    identity.add(Spy())
                } else {
                    spy++
                }
            }
            return createRandomGeneral()
        }
        return general
    }
}