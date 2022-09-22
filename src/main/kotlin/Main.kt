import java.io.File
import java.math.BigDecimal

fun main() {
    var results = emptyList<Result>()

    val directory = File("${System.getProperty("user.dir")}/src/main/resources")
    directory.listFiles()?.forEach { file ->
        val lines = file.readLines()
        val headers = lines.first().replace("\"", "").split(",")
        val fundingRateIndex = headers.indexOf("Funding Rate")
        val records = lines.subList(1, 100)

        var sumOfFundingRate = BigDecimal.ZERO
        var minOfFundingRate = BigDecimal.ZERO
        var maxOfFundingRate = BigDecimal.ZERO
        records.forEach { record ->
            val values = record.replace("\"", "").replace("%", "").split(",")
            val fundingRateValue = BigDecimal(values[fundingRateIndex])
            sumOfFundingRate += fundingRateValue
            if (fundingRateValue < minOfFundingRate) {
                minOfFundingRate = fundingRateValue
            }
            if (maxOfFundingRate < fundingRateValue) {
                maxOfFundingRate = fundingRateValue
            }
        }
        results = results.plus(
            Result(
                file = file,
                sum = sumOfFundingRate,
                min = minOfFundingRate,
                max = maxOfFundingRate
            )
        )
    }

    results.filter { it.sum >= BigDecimal.ZERO }.sortedBy { it.sum }.forEach { println(it) }
}

data class Result(
    val file: File,
    val sum: BigDecimal,
    val min: BigDecimal,
    val max: BigDecimal
)
