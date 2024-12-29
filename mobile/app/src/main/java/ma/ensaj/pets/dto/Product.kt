package ma.ensaj.pets.dto

import java.math.BigDecimal
import org.threeten.bp.LocalDateTime

data class Product(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val price: BigDecimal,
    val stockQuantity: Int? = null,
    val category: ProductCategory,
    val brand: String? = null,
    val imageUrl: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
