package racingcar.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType

internal class CarRacerTest : StringSpec({
    "생성된 카운트가 4이상이면 경로를 생성한다" {
        val racer = CarRacer(name = "hjw")
        racer.moveTo(4)
        racer.position shouldBe 1
    }
    "생성된 카운트가 4미만이면 경로가 없다" {
        val racer = CarRacer(name = "hjw")
        racer.moveTo(3)
        racer.position shouldBe 0
    }
    "자동차는 이름을 가진다" {
        val racer = CarRacer(name = "hjw")
        assertThat(racer.name).isEqualTo("hjw")
    }
    "이름이 5자를 초과할 수 없다" {
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                CarRacer("1234567890")
            }
    }
})
