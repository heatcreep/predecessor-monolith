package com.aowen.predcompanion.glance

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import com.aowen.predcompanion.glance.worker.UpdatePlayerStatsWorker

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        PlayerStatsAppWidget().update(context, glanceId)

        GlanceAppWidgetManager(context).getAppWidgetSizes(glanceId).let {
            UpdatePlayerStatsWorker.enqueue(context, glanceId, force = true)
        }
    }
}