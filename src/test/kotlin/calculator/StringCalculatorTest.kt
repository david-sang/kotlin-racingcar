package calculator

import calculator.dto.ExtractDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class StringCalculatorTest {

    @ParameterizedTest
    @CsvSource("20 + 1 * 4 / 2,42.0", "2-1+1*2/2,2.0")
    fun `문자열 계산기를 통한 연산`(input: String, expected: Double) {
        val result = StringCalculator().run(input)
        assertThat(result).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["20 + 1 * 4 / 2"])
    fun `문자열 계산을 위한 문자열 내 숫자 및 연산 기호 추출`(input: String) {

        val expected = ExtractDTO(
            numbers = mutableListOf(20.0, 1.0, 4.0, 2.0),
            operators = mutableListOf('+', '*', '/')
        )

        val result = StringCalculator().extractNumbersAndOperators(input)

        assertThat(result.numbers).isEqualTo(expected.numbers)
        assertThat(result.operators).isEqualTo(expected.operators)
    }

    @Test
    fun `사칙 연산을 무시한 입력 값에 따른 계산`() {

        val setup = ExtractDTO(
            numbers = mutableListOf(20.0, 1.0, 4.0, 2.0),
            operators = mutableListOf('+', '*', '/')
        )

        val result = StringCalculator().calculate(setup.numbers, setup.operators)

        assertThat(result).isEqualTo(42.0)
    }

    @ParameterizedTest
    @ValueSource(strings = ["- 20 + 1 * 4 / 2", "2-1+1*2/2 -"])
    fun `연산 기호 또는 연산 숫자가 맞지 않는 경우`(input: String) {
        assertThrows<IllegalArgumentException> {
            StringCalculator().run(input)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["20 ^ 1 * 4 / 2", "2~1+1*2/2"])
    fun `연산 기호의 맞지 않는 기호가 존재하는 경우`(input: String) {
        assertThrows<IllegalArgumentException> {
            StringCalculator().run(input)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [" ", ""])
    fun `입력 값이 null이거나 빈 공백 문자인 경우`(input: String) {
        assertThrows<IllegalArgumentException> {
            StringCalculator().run(input)
        }
    }

    @Test
    fun `정규식을 이용한 분리`() {
        val input = "10-3/2*123-1"
        val result = input.replace(Regex("(\\d+)"), " $1 ")
            .replace(Regex("([-+*/])"), " $1 ")
            .trim()
            .split("\\s+".toRegex())

        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        result.forEach {
            when {
                it.matches(Regex("\\d+")) -> numbers.add(it.toDouble())
                else -> operators.add(it.first())
            }
        }

        println("$numbers, $operators")
    }
}