package com.aowen.monolith.ui.tooling.previews

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Nexus 5",
    group = "Device Sizes",
    device = Devices.NEXUS_5
)
@Preview(
    name = "Pixel 5",
    group = "Device Sizes",
    device = Devices.PIXEL_5

)
@Preview(
    name = "Pixel 7 Pro",
    group = "Device Sizes",
    device = Devices.PIXEL_7_PRO

)
@Preview(
    name = "Pixel Fold",
    group = "Device Sizes",
    apiLevel = 34,
    device = Devices.FOLDABLE

)
@Preview(
    name = "Pixel C",
    group = "Device Sizes",
    device = Devices.PIXEL_C

)
annotation class TargetDevicesPreview