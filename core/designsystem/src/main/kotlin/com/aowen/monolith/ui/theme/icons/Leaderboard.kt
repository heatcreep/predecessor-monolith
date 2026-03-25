package com.aowen.monolith.ui.theme.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes

val Icons.Outlined.Leaderboard: ImageVector
    get() {
        if (_leaderboard != null) {
            return _leaderboard!!
        }
        _leaderboard = materialIcon(name = "Outlined.Leaderboard") {
            materialPath {
                addPathNodes("M150,770L330,770L330,430L150,430L150,770ZM390,770L570,770L570,190L390,190L390,770ZM630,770L810,770L810,510L630,510L630,770ZM90,830L90,370L330,370L330,130L630,130L630,450L870,450L870,830L90,830Z")
            }
        }
        return _leaderboard!!
    }

private var _leaderboard: ImageVector? = null