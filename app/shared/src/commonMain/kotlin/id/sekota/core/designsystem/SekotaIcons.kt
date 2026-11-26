package id.sekota.core.designsystem

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Line icons traced from the Sekota design (Lucide-style, 1.75 stroke).
 * Keeping them as path data avoids pulling an icon dependency into every target.
 */
object SekotaIcons {
    val Check = stroke("check", "M20 6 9 17l-5-5")
    val ArrowRight = stroke("arrowRight", "M5 12h14", "m12 5 7 7-7 7")
    val ArrowUpRight = stroke("arrowUpRight", "M7 7h10v10", "M7 17 17 7")
    val ChevronLeft = stroke("chevronLeft", "m12 19-7-7 7-7", "M19 12H5")
    val Plus = stroke("plus", "M5 12h14", "M12 5v14")
    val Globe = stroke(
        "globe",
        "M12 2a14.5 14.5 0 0 0 0 20 14.5 14.5 0 0 0 0-20",
        "M2 12h20",
        "M12 2a10 10 0 1 0 0 20 10 10 0 1 0 0-20",
    )
    val Sparkle = stroke(
        "sparkle",
        "M9.937 15.5A2 2 0 0 0 8.5 14.063l-6.135-1.582a.5.5 0 0 1 0-.962L8.5 9.936A2 2 0 0 0 9.937 8.5l1.582-6.135a.5.5 0 0 1 .963 0L14.063 8.5A2 2 0 0 0 15.5 9.937l6.135 1.581a.5.5 0 0 1 0 .964L15.5 14.063a2 2 0 0 0-1.437 1.437l-1.582 6.135a.5.5 0 0 1-.963 0z",
    )
    val Radar = stroke(
        "radar",
        "M19.07 4.93A10 10 0 0 0 6.99 3.34",
        "M4 6h.01",
        "M2.29 9.62A10 10 0 1 0 21.31 8.35",
        "M16.24 7.76A6 6 0 1 0 8.23 16.67",
        "M12 18h.01",
        "M17.99 11.66A6 6 0 0 1 15.77 16.67",
        "M13.41 10.59l5.66-5.66",
    )
    val Users = stroke(
        "users",
        "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
        "M9 3a4 4 0 1 0 0 8 4 4 0 0 0 0-8",
        "M22 21v-2a4 4 0 0 0-3-3.87",
        "M16 3.13a4 4 0 0 1 0 7.75",
    )
    val Sprout = stroke(
        "sprout",
        "M7 20h10",
        "M10 20c5.5-2.5.8-6.4 3-10",
        "M9.5 9.4c1.1.8 1.8 2.2 2.3 3.7-2 .4-3.5.4-4.8-.3-1.2-.6-2.3-1.9-3-4.2 2.8-.5 4.4 0 5.5.8z",
        "M14.1 6a7 7 0 0 0-1.1 4c1.9-.1 3.3-.6 4.3-1.4 1-1 1.6-2.3 1.7-4.6-2.7.1-4 1-4.9 2z",
    )
    val Leaf = stroke(
        "leaf",
        "M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10Z",
        "M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12",
    )
    val Search = stroke("search", "M11 3a8 8 0 1 0 0 16 8 8 0 0 0 0-16", "m21 21-4.3-4.3")
    val Layers = stroke(
        "layers",
        "M12.83 2.18a2 2 0 0 0-1.66 0L2.6 6.08a1 1 0 0 0 0 1.83l8.58 3.91a2 2 0 0 0 1.66 0l8.58-3.9a1 1 0 0 0 0-1.83z",
        "m22 17.65-9.17 4.16a2 2 0 0 1-1.66 0L2 17.65",
        "m22 12.65-9.17 4.16a2 2 0 0 1-1.66 0L2 12.65",
    )
    val Rocket = stroke(
        "rocket",
        "M4.5 16.5c-1.5 1.26-2 5-2 5s3.74-.5 5-2c.71-.84.7-2.13-.09-2.91a2.18 2.18 0 0 0-2.91-.09z",
        "m12 15-3-3a22 22 0 0 1 2-3.95A12.88 12.88 0 0 1 22 2c0 2.72-.78 7.5-6 11a22.35 22.35 0 0 1-4 2z",
        "M9 12H4s.55-3.03 2-4c1.62-1.08 5 0 5 0",
        "M12 15v5s3.03-.55 4-2c1.08-1.62 0-5 0-5",
    )
    val Refresh = stroke(
        "refresh",
        "M3 12a9 9 0 0 1 9-9 9.75 9.75 0 0 1 6.74 2.74L21 8",
        "M21 3v5h-5",
        "M21 12a9 9 0 0 1-9 9 9.75 9.75 0 0 1-6.74-2.74L3 16",
        "M8 16H3v5",
    )
    val Mail = stroke(
        "mail",
        "M2 6a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2z",
        "m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7",
    )
    val BookOpen = stroke(
        "book",
        "M12 7v14",
        "M3 18a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h5a4 4 0 0 1 4 4 4 4 0 0 1 4-4h5a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1h-6a3 3 0 0 0-3 3 3 3 0 0 0-3-3z",
    )
    val Cpu = stroke(
        "cpu",
        "M6 4h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z",
        "M10 9h4a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1h-4a1 1 0 0 1-1-1v-4a1 1 0 0 1 1-1z",
        "M15 2v2M15 20v2M2 15h2M2 9h2M20 15h2M20 9h2M9 2v2M9 20v2",
    )
    val Trend = stroke("trend", "M22 7 13.5 15.5 8.5 10.5 2 17", "M16 7h6v6")
    val Link = stroke(
        "link",
        "M6 16a3 3 0 1 0 0 6 3 3 0 0 0 0-6",
        "M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15",
        "M18 2a3 3 0 1 0 0 6 3 3 0 0 0 0-6",
    )
    val FileText = stroke(
        "file",
        "M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z",
        "M14 2v4a2 2 0 0 0 2 2h4",
        "M10 9H8M16 13H8M16 17H8",
    )
    val Database = stroke(
        "database",
        "M12 2a9 3 0 1 0 0 6 9 3 0 0 0 0-6",
        "M3 5v14a9 3 0 0 0 18 0V5",
        "M3 12a9 3 0 0 0 18 0",
    )
    val Target = stroke(
        "target",
        "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20",
        "M12 6a6 6 0 1 0 0 12 6 6 0 0 0 0-12",
        "M12 10a2 2 0 1 0 0 4 2 2 0 0 0 0-4",
    )
    val Shield = stroke(
        "shield",
        "M20 13c0 5-3.5 7.5-7.66 8.95a1 1 0 0 1-.67-.01C7.5 20.5 4 18 4 13V6a1 1 0 0 1 1-1c2 0 4.5-1.2 6.24-2.72a1.17 1.17 0 0 1 1.52 0C14.51 3.81 17 5 19 5a1 1 0 0 1 1 1z",
        "m9 12 2 2 4-4",
    )
    val BarChart = stroke("barChart", "M3 3v18h18", "M18 17V9", "M13 17V5", "M8 17v-3")
    val Message = stroke(
        "message",
        "M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z",
    )
    val Clipboard = stroke(
        "clipboard",
        "M9 2h6a1 1 0 0 1 1 1v2H8V3a1 1 0 0 1 1-1z",
        "M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2",
        "M9 12h6M9 16h6",
    )
    val Folder = stroke(
        "folder",
        "M20 20a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2h-7.9a2 2 0 0 1-1.69-.9L9.6 3.9A2 2 0 0 0 7.93 3H4a2 2 0 0 0-2 2v13a2 2 0 0 0 2 2Z",
    )
    val Activity = stroke("activity", "M22 12h-4l-3 9L9 3l-3 9H2")
    val Building = stroke(
        "building",
        "M3 21h18",
        "M5 21V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v16",
        "M9 7h1M14 7h1M9 11h1M14 11h1M9 15h1M14 15h1",
    )
    val Factory = stroke(
        "factory",
        "M2 20a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V8l-7 5V8l-7 5V4a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2Z",
        "M17 18h1M12 18h1M7 18h1",
    )
    val Coins = stroke(
        "coins",
        "M8 2a6 6 0 1 0 0 12A6 6 0 0 0 8 2",
        "M18.09 10.37A6 6 0 1 1 10.34 18",
        "M7 6h1v4",
        "m16.71 13.88.7.71-2.82 2.82",
    )
    val Landmark = stroke(
        "landmark",
        "M3 22h18",
        "M6 18v-7M10 18v-7M14 18v-7M18 18v-7",
        "M12 2 20 7H4z",
    )
    val Play = stroke("play", "M6 3l14 9-14 9z")
    val Copy = stroke(
        "copy",
        "M8 8h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V10a2 2 0 0 1 2-2Z",
        "M4 16a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2",
    )

    // Sector glyphs are filled 256x256 icons from the design's marquee.
    val SectorEnergy = filled(
        "sectorEnergy",
        "M116,176a8,8,0,0,1-8,8H80a8,8,0,0,1,0-16h28A8,8,0,0,1,116,176Zm60-8H148a8,8,0,0,0,0,16h28a8,8,0,0,0,0-16Zm64,48a8,8,0,0,1-8,8H24a8,8,0,0,1,0-16h8V88a8,8,0,0,1,12.8-6.4L96,120V88a8,8,0,0,1,12.8-6.4l38.74,29.05L159.1,29.74A16.08,16.08,0,0,1,174.94,16h18.12A16.08,16.08,0,0,1,208.9,29.74l15,105.13s.08.78.08,1.13v72h8A8,8,0,0,1,240,216Zm-77.86-94.4,8.53,6.4h36.11L193.06,32H174.94ZM48,208H208V144H168a8,8,0,0,1-4.8-1.6l-14.4-10.8,0,0L112,104v32a8,8,0,0,1-12.8,6.4L48,104Z",
    )
    val SectorSecurity = filled(
        "sectorSecurity",
        "M208,40H48A16,16,0,0,0,32,56v56c0,52.72,25.52,84.67,46.93,102.19,23.06,18.86,46,25.26,47,25.53a8,8,0,0,0,4.2,0c1-.27,23.91-6.67,47-25.53C198.48,196.67,224,164.72,224,112V56A16,16,0,0,0,208,40Zm0,72c0,37.07-13.66,67.16-40.6,89.42A129.3,129.3,0,0,1,128,223.62a128.25,128.25,0,0,1-38.92-21.81C61.82,179.51,48,149.3,48,112l0-56,160,0ZM82.34,141.66a8,8,0,0,1,11.32-11.32L112,148.69l50.34-50.35a8,8,0,0,1,11.32,11.32l-56,56a8,8,0,0,1-11.32,0Z",
    )
    val SectorCorporate = filled(
        "sectorCorporate",
        "M240,208H224V96a16,16,0,0,0-16-16H144V32a16,16,0,0,0-24.88-13.32L39.12,72A16,16,0,0,0,32,85.34V208H16a8,8,0,0,0,0,16H240a8,8,0,0,0,0-16ZM208,96V208H144V96ZM48,85.34,128,32V208H48ZM112,112v16a8,8,0,0,1-16,0V112a8,8,0,1,1,16,0Zm-32,0v16a8,8,0,0,1-16,0V112a8,8,0,1,1,16,0Zm0,56v16a8,8,0,0,1-16,0V168a8,8,0,0,1,16,0Zm32,0v16a8,8,0,0,1-16,0V168a8,8,0,0,1,16,0Z",
    )
    val SectorGovernment = filled(
        "sectorGovernment",
        "M24,104H48v64H32a8,8,0,0,0,0,16H224a8,8,0,0,0,0-16H208V104h24a8,8,0,0,0,4.19-14.81l-104-64a8,8,0,0,0-8.38,0l-104,64A8,8,0,0,0,24,104Zm40,0H96v64H64Zm80,0v64H112V104Zm48,64H160V104h32ZM128,41.39,203.74,88H52.26ZM248,208a8,8,0,0,1-8,8H16a8,8,0,0,1,0-16H240A8,8,0,0,1,248,208Z",
    )
    val SectorAgri = filled(
        "sectorAgri",
        "M247.63,47.89a8,8,0,0,0-7.52-7.52c-51.76-3-93.32,12.74-111.18,42.22-11.8,19.49-11.78,43.16-.16,65.74a71.34,71.34,0,0,0-14.17,27L98.33,159c7.82-16.33,7.52-33.35-1-47.49-13.2-21.79-43.67-33.47-81.5-31.25a8,8,0,0,0-7.52,7.52c-2.23,37.83,9.46,68.3,31.25,81.5A45.82,45.82,0,0,0,63.44,176,54.58,54.58,0,0,0,87,170.33l25,25V224a8,8,0,0,0,16,0V194.51a55.61,55.61,0,0,1,12.27-35,73.91,73.91,0,0,0,33.31,8.4,60.9,60.9,0,0,0,31.83-8.86C234.89,141.21,250.67,99.65,247.63,47.89ZM47.81,155.6C32.47,146.31,23.79,124.32,24,96c28.32-.24,50.31,8.47,59.6,23.81,4.85,8,5.64,17.33,2.46,26.94L61.65,122.34a8,8,0,0,0-11.31,11.31l24.41,24.41C65.14,161.24,55.82,160.45,47.81,155.6Zm149.31-10.22c-13.4,8.11-29.15,8.73-45.15,2l53.69-53.7a8,8,0,0,0-11.31-11.31L140.65,136c-6.76-16-6.15-31.76,2-45.15,13.94-23,47-35.82,89.33-34.83C232.94,98.34,220.14,131.44,197.12,145.38Z",
    )
    val SectorBanking = filled(
        "sectorBanking",
        "M184,89.57V84c0-25.08-37.83-44-88-44S8,58.92,8,84v40c0,20.89,26.25,37.49,64,42.46V172c0,25.08,37.83,44,88,44s88-18.92,88-44V132C248,111.3,222.58,94.68,184,89.57ZM232,132c0,13.22-30.79,28-72,28-3.73,0-7.43-.13-11.08-.37C170.49,151.77,184,139,184,124V105.74C213.87,110.19,232,122.27,232,132ZM72,150.25V126.46A183.74,183.74,0,0,0,96,128a183.74,183.74,0,0,0,24-1.54v23.79A163,163,0,0,1,96,152,163,163,0,0,1,72,150.25Zm96-40.32V124c0,8.39-12.41,17.4-32,22.87V123.5C148.91,120.37,159.84,115.71,168,109.93ZM96,56c41.21,0,72,14.78,72,28s-30.79,28-72,28S24,97.22,24,84,54.79,56,96,56ZM24,124V109.93c8.16,5.78,19.09,10.44,32,13.57v23.37C36.41,141.4,24,132.39,24,124Zm64,48v-4.17c2.63.1,5.29.17,8,.17,3.88,0,7.67-.13,11.39-.35A121.92,121.92,0,0,0,120,171.41v23.46C100.41,189.4,88,180.39,88,172Zm48,26.25V174.4a179.48,179.48,0,0,0,24,1.6,183.74,183.74,0,0,0,24-1.54v23.79a165.45,165.45,0,0,1-48,0Zm64-3.38V171.5c12.91-3.13,23.84-7.79,32-13.57V172C232,180.39,219.59,189.4,200,194.87Z",
    )
}

private fun stroke(name: String, vararg paths: String): ImageVector {
    val builder = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    )
    paths.forEach { data ->
        builder.addPath(
            pathData = addPathNodes(data),
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
    return builder.build()
}

private fun filled(name: String, path: String): ImageVector {
    val builder = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 256f,
        viewportHeight = 256f,
    )
    builder.addPath(
        pathData = addPathNodes(path),
        fill = Brush.linearGradient(
            colors = listOf(SekotaColors.Green, SekotaColors.Cyan),
            start = Offset(0f, 256f),
            end = Offset(256f, 0f),
        ),
    )
    return builder.build()
}
