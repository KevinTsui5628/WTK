//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    // Initialize the GeneralManager
    val generalManager = GeneralManager

    // Get the total numbers of Lord and non-Lord
//    val lordFactory = LordFactory()
//    val nonLordFactory = NonLordFactory()
//    val lordsToSelect = lordFactory.total()
//    val nonLordsToSelect = nonLordFactory.total()

    val numOfPlayer = 5

    // create Lord and non-Lord General objects
    generalManager.createGenerals(numOfPlayer)

    // Retrieve the general count and print it out
    val count = generalManager.getGeneralCount()
    println("Total number of generals: $count\n")


//    val generalList = generalManager.getGeneralsList()
//    val firstGeneral = generalList[0]
//    val armoredGeneral = EightTrigrams(firstGeneral)
//    armoredGeneral.beingAttacked(1)
    generalManager.gameStart()

}