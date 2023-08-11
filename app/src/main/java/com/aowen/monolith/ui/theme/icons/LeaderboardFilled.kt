package com.aowen.monolith.ui.theme.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes

val Icons.Filled.Leaderboard: ImageVector
    get() {
        if (_leaderboard != null) {
            return _leaderboard!!
        }
        _leaderboard = materialIcon(name = "Filled.Leaderboard") {
            materialPath {
                addPathNodes("M80,840L80,360L280,360L280,840L80,840ZM380,840L380,120L580,120L580,840L380,840ZM680,840L680,440L880,440L880,840L680,840Z")
            }
        }
        return _leaderboard!!
    }

private var _leaderboard: ImageVector? = null