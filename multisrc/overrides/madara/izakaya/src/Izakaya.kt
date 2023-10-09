package eu.kanade.tachiyomi.extension.pt.izakaya

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class Izakaya : Madara(
    "Izakaya",
    "https://leitorizakaya.net",
    "pt-BR",
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")),
) {

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(1, 3, TimeUnit.SECONDS)
        .build()

    override val useNewChapterEndpoint = true

    override val chapterUrlSuffix = ""

    override fun pageListRequest(chapter: SChapter): Request {
        val fixedUrl = chapter.url.substringBeforeLast("?style=")

        if (fixedUrl.startsWith("http")) {
            return GET(fixedUrl, headers)
        }

        return GET(baseUrl + fixedUrl, headers)
    }

    override fun imageRequest(page: Page): Request {
        val newHeaders = headersBuilder()
            .set("Referer", page.url)
            .set("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
            .build()

        return GET(page.imageUrl!!, newHeaders)
    }
}
