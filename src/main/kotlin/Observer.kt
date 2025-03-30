interface Observer {
    fun update() {
    }
}

interface Subject {
    fun attach(observer: Observer)
    fun detach(observer: Observer)
    fun notifyObservers()
}