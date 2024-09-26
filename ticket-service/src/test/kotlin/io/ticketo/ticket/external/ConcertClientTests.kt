package io.ticketo.ticket.external

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

@WireMockTest
class ConcertClientTests(wiremock: WireMockRuntimeInfo) {

    private val cut: ConcertClient = ConcertClient(wiremock.httpBaseUrl)

    @Test
    fun `retrieve concert`() {
        val concertId = UUID.randomUUID()
        val concertBody = """
            {
              "id": "$concertId",
              "concertName": "Electric Nights Festival",
              "artistName": "The Synth Lords",
              "description": "An electrifying night of synthwave music under the stars.",
              "capacity": 500
            }
        """.trimIndent()
        stubFor(get("/concerts/$concertId").willReturn(okJson(concertBody)))

        val concert = cut.fetchConcertById(concertId)

        concert.id shouldBe concertId
        concert.capacity shouldBe 500
    }
}