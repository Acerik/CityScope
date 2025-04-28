package cz.matejvana.cityscope.mapper

object NumberMapper {

    fun formatWithSpaces(number: Number): String {
        return formatWithSpaces(number.toString())
    }

    fun formatWithSpaces(string: String): String {
        val parts = string.split(".")
        val integerPart = parts[0].reversed().chunked(3).joinToString(" ").reversed()
        val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""
        return integerPart + decimalPart
    }
}