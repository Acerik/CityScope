package cz.matejvana.cityscope.mapper

object NumberMapper {

    fun formatWithSpaces(number: Number): String {
        val parts = number.toString().split(".")
        val integerPart = parts[0].reversed().chunked(3).joinToString(" ").reversed()
        val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""
        return integerPart + decimalPart
    }
}