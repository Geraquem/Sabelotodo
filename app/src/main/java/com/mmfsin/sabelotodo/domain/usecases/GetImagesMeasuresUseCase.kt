package com.mmfsin.sabelotodo.domain.usecases

import android.content.Context
import android.content.res.Configuration
import androidx.constraintlayout.widget.ConstraintLayout
import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.presentation.models.GamesType
import com.mmfsin.sabelotodo.presentation.models.GamesType.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetImagesMeasuresUseCase @Inject constructor(
    @ApplicationContext val context: Context
) : BaseUseCase<GetImagesMeasuresUseCase.Params, Int>() {

    override suspend fun execute(params: Params): Int {
        return try {
            var isTablet =
                (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
            if (!isTablet) {
                isTablet =
                    (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE
            }

            val height = when (params.gamesType) {
                GUESSER -> if (isTablet) 440 else 220
                TEMPORARY -> if (isTablet) 380 else 180
            }
            dpToPx(height)

        } catch (e: Exception) {
            dpToPx(200)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    data class Params(
        val gamesType: GamesType
    )
}