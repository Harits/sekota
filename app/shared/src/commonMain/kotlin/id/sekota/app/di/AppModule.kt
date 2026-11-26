package id.sekota.app.di

import id.sekota.feature.books.data.StaticBooksRepository
import id.sekota.feature.books.domain.BooksRepository
import id.sekota.feature.books.domain.GetBooks
import id.sekota.feature.contact.data.HttpContactGateway
import id.sekota.feature.contact.data.StaticContactRepository
import id.sekota.feature.contact.domain.ContactGateway
import id.sekota.feature.contact.domain.ContactRepository
import id.sekota.feature.contact.domain.GetContactContent
import id.sekota.feature.contact.domain.SendContactMessage
import id.sekota.feature.landing.data.StaticLandingRepository
import id.sekota.feature.landing.domain.GetLandingContent
import id.sekota.feature.landing.domain.LandingRepository
import id.sekota.feature.legal.data.StaticLegalRepository
import id.sekota.feature.legal.domain.GetLegalDoc
import id.sekota.feature.legal.domain.LegalRepository
import id.sekota.feature.product.data.StaticProductRepository
import id.sekota.feature.product.domain.GetProductDetail
import id.sekota.feature.product.domain.ProductRepository
import org.koin.dsl.module

/**
 * Composition root. Interfaces live in domain, implementations in data, and the
 * presentation layer only sees use cases. Swapping static content for a CMS or an
 * API is a one-line binding change here.
 */
val appModule = module {
    single<LandingRepository> { StaticLandingRepository() }
    single { GetLandingContent(get()) }

    single<ProductRepository> { StaticProductRepository() }
    single { GetProductDetail(get()) }

    single<BooksRepository> { StaticBooksRepository() }
    single { GetBooks(get()) }

    single<ContactRepository> { StaticContactRepository() }
    single { GetContactContent(get()) }
    single<ContactGateway> { HttpContactGateway() }
    single { SendContactMessage(get()) }

    single<LegalRepository> { StaticLegalRepository() }
    single { GetLegalDoc(get()) }
}
