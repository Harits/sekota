package id.sekota.feature.landing.domain

interface LandingRepository {
    fun load(): LandingContent
}

class GetLandingContent(private val repository: LandingRepository) {
    operator fun invoke(): LandingContent = repository.load()
}
