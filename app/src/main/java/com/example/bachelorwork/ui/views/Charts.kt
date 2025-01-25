package com.example.bachelorwork.ui.views

import android.graphics.Color
import android.text.Layout
import com.patrykandpatrick.vico.core.cartesian.CartesianChart
import com.patrykandpatrick.vico.core.cartesian.CartesianLayerPadding
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.HorizontalLegend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.VerticalPosition
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape

object Charts {

    val LegendLabelKey = ExtraStore.Key<Set<String>>()


    fun createColumnChart(): CartesianChart {
        val chart = CartesianChart(
            ColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    listOf(
                        LineComponent(
                            fill = Fill(Color.WHITE),
                            thicknessDp = 20f,
                            shape = CorneredShape.rounded(4f, 4f, 0f, 0f),
                        ),
                        LineComponent(
                            fill = Fill(Color.GREEN),
                            thicknessDp = 20f,
                            shape = CorneredShape.rounded(4f, 4f, 0f, 0f),
                        )
                    )
                ),
            ),
            legend = HorizontalLegend(
                items = { extraStore ->
                    extraStore[LegendLabelKey].forEachIndexed { _, label ->
                        add(
                            LegendItem(
                                icon = ShapeComponent(
                                    fill = Fill(Color.GREEN),
                                    shape = CorneredShape.Pill
                                ),
                                TextComponent(
                                    color = Color.WHITE,
                                    textAlignment = Layout.Alignment.ALIGN_CENTER,
                                ),
                                label,
                            )
                        )
                    }
                },
                padding = Dimensions(8f, 4f),
            ),
            marker = defaultMarker,
            startAxis = defaultStartAxis,
            bottomAxis = defaultBottomAxis,
            layerPadding = CartesianLayerPadding(
                scalableStartDp = 6f,
                scalableEndDp = 6f,
            ),
            decorations = listOf(
                HorizontalLine(
                    y = { 500.00 },
                    line = LineComponent(fill = Fill(Color.RED)),
                    label = { "Min Level" },
                    labelComponent = TextComponent(
                        color = Color.WHITE,
                        margins = Dimensions(6f, 0f),
                        padding = Dimensions(8f, 1f),
                        background = ShapeComponent(shape = CorneredShape.rounded(4f,4f,0f,0f))
                    ),
                    verticalLabelPosition = VerticalPosition.Top,
                )
            )
        )

        return chart
    }

    fun createLineChart(): CartesianChart {
        val chart = CartesianChart(
            LineCartesianLayer(
                lineProvider = { _, _ ->
                    LineCartesianLayer.Line(
                        fill = LineCartesianLayer.LineFill.single(Fill(Color.WHITE)),
                        pointProvider = LineCartesianLayer.PointProvider.single(
                            LineCartesianLayer.Point(
                                ShapeComponent(
                                    Fill(Color.WHITE),
                                    shape = CorneredShape.rounded(30f),
                                    shadow = Shadow(10f, color = Color.BLACK)
                                ),
                                sizeDp = 8f
                            )
                        )
                    )
                },
            ),
            marker = defaultMarker,
            startAxis = defaultStartAxis,
            bottomAxis = defaultBottomAxis,
        )

        return chart
    }

    private val defaultMarker = DefaultCartesianMarker(
        label = TextComponent(Color.WHITE, padding = Dimensions(0f, 6f)),
        labelPosition = DefaultCartesianMarker.LabelPosition.Top,
        guideline = LineComponent(Fill(Color.GRAY), shape = DashedShape())
    )

    private val defaultStartAxis = VerticalAxis.start(
        line = LineComponent(Fill(Color.WHITE)),
        label = TextComponent(Color.WHITE, padding = Dimensions(6f)),
        titleComponent = TextComponent(Color.WHITE),
        itemPlacer = VerticalAxis.ItemPlacer.step({100.00}),
        guideline = LineComponent(Fill(Color.GRAY), shape = DashedShape())
    )

    private val defaultBottomAxis = HorizontalAxis.bottom(
        line = LineComponent(Fill(Color.WHITE)),
        label = TextComponent(Color.WHITE, padding = Dimensions(6f)),
    )
}