import kotlin.random.Random

abstract class GeneralFactory {
    abstract fun createRandomGeneral(): General
    abstract fun total(): Int
}

class LordFactory : GeneralFactory() {
    private val lords = listOf(
//        "LIU Bei",
        "CAO Cao"
       // , "SUN Quan"
    )
    private val createdLordsGenerals = mutableSetOf<String>()

    override fun createRandomGeneral(): General {
        val availableLords = lords.filter { it !in createdLordsGenerals }
        if (availableLords.isNotEmpty()) {
            val selectedLord = availableLords.random()
            createdLordsGenerals.add(selectedLord)

            val general = when (selectedLord) {
                "LIU Bei" -> {
                    val liuBei = LiuBei()
                    liuBei.identity = LiuBeiStrategy()
                    liuBei
                }
                "CAO Cao" -> {
                    val caoCao = CaoCao()
                    caoCao.identity = Lord()
                    caoCao
                }
                "SUN Quan" -> {
                    val sunQuan = SunQuan()
                    sunQuan.identity = Lord()
                    sunQuan
                }
                else -> throw IllegalStateException("Unknown general name: $selectedLord")
            }
            return general
        } else {
            throw IllegalStateException("No more lords available.")
        }
    }

    override fun total(): Int {
        return lords.size
    }
}

open class NonLordFactory(private val numOfPlayer: Int, private var head: WeiGeneral? = null) : GeneralFactory() {
    private val nonLords = listOf(
//        GuanYuAdapter(GuanYu()),
        "Zhang Fei", "Zhuge Liang", "Lady Gan",
        "Sima Yi", "Zhang Liao", "Zhen Ji", "Xu Zhu",
        "Gan Ning", "Da Qiao", "Sun Shangxiang",
        "Zhou Yu", "Hua Tuo", "Lv Bu",
        "Diao Chan", "Hua Xiong"
    )
    private val createdNonLordsGenerals = mutableSetOf<String>()

    // Number of Identity Table
    var identity = mutableListOf(Loyalist(), Rebel(), Spy())
    var loyalist = getIdentityTable()[0]
    var rebel = getIdentityTable()[1]
    var spy = getIdentityTable()[2]

    override fun createRandomGeneral(): General {
        val availableGenerals = nonLords.filter { it !in createdNonLordsGenerals }
        if (availableGenerals.isNotEmpty()) {
            val selectedGeneral = availableGenerals.random()
            createdNonLordsGenerals.add(selectedGeneral)

            val general = when (selectedGeneral) {
                "Zhang Fei" -> ZhangFei()
                "Zhuge Liang" -> ZhugeLiang()
                "Lady Gan" -> LadyGan()
                "Sima Yi" -> SimaYi()
                "Zhang Liao" -> ZhangLiao()
                "Zhen Ji" -> ZhenJi()
                "Xu Zhu" -> XuZhu()
                "Gan Ning" -> GanNing()
                "Da Qiao" -> DaQiao()
                "Sun Shangxiang" -> SunShangxiang()
                "Zhou Yu" -> ZhouYu()
                "Hua Tuo" -> HuaTuo()
                "Lv Bu" -> LvBu()
                "Diao Chan" -> DiaoChan()
                "Hua Xiong" -> HuaXiong()
                else -> throw IllegalStateException("Unknown non-lord general name: $selectedGeneral")
            }

            // Randomly pick a identity from the existing identity table
            val randomPick = identity[Random.nextInt(identity.size)]
            // Assign the identity to the general
            general.identity = randomPick

            if (general.identity is Spy) {
                val lord = GeneralManager.getGeneralsList()[0].identity as Lord
                val spy = general.identity as Spy
                lord.attach(spy)
            }

            // Update the identity table based on the random pick
            when (randomPick) {
                is Loyalist -> {
                    if (loyalist >= 1) {
                        loyalist--
                        if (loyalist == 0) { // Remove the identity from the table if the number of identity is 0
                            identity.remove(randomPick)
                        }
                    }
                }
                is Rebel -> {
                    if (rebel >= 1) {
                        rebel--
                        if (rebel == 0) {
                            identity.remove(randomPick)
                        }
                    }
                }
                is Spy -> {
                    if (spy >= 1) {
                        spy--
                        if (spy == 0) {
                            identity.remove(randomPick)
                        }
                    }
                }
            }
//            println("[$loyalist, $rebel, $spy]")
//            println(identity)

            // Build the chain if the general is a WeiGeneral
            if (general is WeiGeneral) {
                if (head == null) {
                    head = general
                } else {
                    // Find the last general in the chain and add the new WeiGeneral
                    var current = head
                    while (current?.nextWei != null) {
                        current = current.nextWei
                    }
                    current?.setNext(general)
                }
            }
            return general
        } else {
            throw IllegalStateException("No more non-lord generals available.")
        }
    }

    override fun total(): Int {
        return nonLords.size
    }

    fun getIdentityTable(): MutableList<Int> {
        val identityList = mutableListOf<Int>()
        val randomPick = Random.nextBoolean()

        when (numOfPlayer) {
            4 -> identityList.addAll(listOf(1,1,1)) // [1 Loyalist, 1 Rebel, 1 Spy]
            5 -> identityList.addAll(listOf(1,2,1)) // [1 Loyalist, 2 Rebel, 1 Spy]
            6 -> if (randomPick) {
                identityList.addAll(listOf(1,3,1)) // [1 Loyalist, 3 Rebel, 1 Spy] OR
            } else {
                identityList.addAll(listOf(1,2,2)) // [1 Loyalist, 2 Rebel, 2 Spy]
            }
            7 -> identityList.addAll(listOf(2,3,1)) // [2 Loyalist, 3 Rebel, 1 Spy]
            8 -> if (randomPick) {
                identityList.addAll(listOf(2,4,1)) // [2 Loyalist, 4 Rebel, 1 Spy] OR
            } else {
                identityList.addAll(listOf(2,3,2)) // [2 Loyalist, 3 Rebel, 2 Spy]
            }
            9 -> identityList.addAll(listOf(3,4,1)) // [3 Loyalist, 4 Rebel, 1 Spy]
            10 -> identityList.addAll(listOf(3,4,2)) // [3 Loyalist, 4 Rebel, 2 Spy]
        }
        return identityList
    }
}
