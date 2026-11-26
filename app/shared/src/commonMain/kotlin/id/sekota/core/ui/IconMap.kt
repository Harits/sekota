package id.sekota.core.ui

import androidx.compose.ui.graphics.vector.ImageVector
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.model.IconKey

fun IconKey.vector(): ImageVector = when (this) {
    IconKey.Radar -> SekotaIcons.Radar
    IconKey.Users -> SekotaIcons.Users
    IconKey.Sprout -> SekotaIcons.Sprout
    IconKey.Leaf -> SekotaIcons.Leaf
    IconKey.Search -> SekotaIcons.Search
    IconKey.Layers -> SekotaIcons.Layers
    IconKey.Rocket -> SekotaIcons.Rocket
    IconKey.Refresh -> SekotaIcons.Refresh
    IconKey.Globe -> SekotaIcons.Globe
    IconKey.Sparkle -> SekotaIcons.Sparkle
    IconKey.Link -> SekotaIcons.Link
    IconKey.FileText -> SekotaIcons.FileText
    IconKey.Database -> SekotaIcons.Database
    IconKey.Target -> SekotaIcons.Target
    IconKey.Shield -> SekotaIcons.Shield
    IconKey.BarChart -> SekotaIcons.BarChart
    IconKey.Message -> SekotaIcons.Message
    IconKey.Clipboard -> SekotaIcons.Clipboard
    IconKey.Folder -> SekotaIcons.Folder
    IconKey.Activity -> SekotaIcons.Activity
    IconKey.Building -> SekotaIcons.Building
    IconKey.Factory -> SekotaIcons.Factory
    IconKey.Coins -> SekotaIcons.Coins
    IconKey.Landmark -> SekotaIcons.Landmark
    IconKey.BookOpen -> SekotaIcons.BookOpen
    IconKey.Cpu -> SekotaIcons.Cpu
    IconKey.Trend -> SekotaIcons.Trend
    IconKey.Check -> SekotaIcons.Check
    // Sector icons (filled 256x256 from design)
    IconKey.SectorEnergy -> SekotaIcons.SectorEnergy
    IconKey.SectorSecurity -> SekotaIcons.SectorSecurity
    IconKey.SectorCorporate -> SekotaIcons.SectorCorporate
    IconKey.SectorGovernment -> SekotaIcons.SectorGovernment
    IconKey.SectorAgri -> SekotaIcons.SectorAgri
    IconKey.SectorBanking -> SekotaIcons.SectorBanking
}
