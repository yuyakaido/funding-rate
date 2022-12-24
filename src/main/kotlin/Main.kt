import java.io.File
import java.math.BigDecimal

fun main() {
    var results = emptyList<Result>()

    val directory = File("${System.getProperty("user.dir")}/src/main/resources")
    directory.listFiles()?.forEach { file ->
        val lines = file.readLines()
        val headers = lines.first().replace("\"", "").split(",")
        val fundingRateIndex = headers.indexOf("Funding Rate")
        val records = lines.subList(1, 183)

        var plusCount = 0
        var minusCount = 0
        var bigMinusCount = 0
        var sumOfFundingRate = BigDecimal.ZERO
        var minOfFundingRate = BigDecimal.ZERO
        var maxOfFundingRate = BigDecimal.ZERO
        records.forEach { record ->
            val values = record.replace("\"", "").replace("%", "").split(",")
            val fundingRateValue = BigDecimal(values[fundingRateIndex])
            sumOfFundingRate += fundingRateValue
            if (fundingRateValue < BigDecimal.ZERO) {
                minusCount++
            } else {
                plusCount++
            }
            if (fundingRateValue < BigDecimal(-0.1)) {
                bigMinusCount++
            }
            if (fundingRateValue < minOfFundingRate) {
                minOfFundingRate = fundingRateValue
            }
            if (maxOfFundingRate < fundingRateValue) {
                maxOfFundingRate = fundingRateValue
            }
        }
        results = results.plus(
            Result(
                name = file.name,
                allCount = records.size,
                plusCount = plusCount,
                minusCount = minusCount,
                bigMinusCount = bigMinusCount,
                winingRate = (plusCount.toDouble() / records.size.toDouble() * 100).toInt(),
                sum = sumOfFundingRate,
                min = minOfFundingRate,
                max = maxOfFundingRate
            )
        )
    }

    results.filter { it.sum >= BigDecimal.ZERO }.sortedBy { it.sum }.forEach { println(it) }
}

data class Result(
    val name: String,
    val allCount: Int,
    val plusCount: Int,
    val minusCount: Int,
    val bigMinusCount: Int,
    val winingRate: Int,
    val sum: BigDecimal,
    val min: BigDecimal,
    val max: BigDecimal
)
