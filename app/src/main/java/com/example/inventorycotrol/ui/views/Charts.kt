package com.example.inventorycotrol.ui.views

import android.graphics.Color
import com.patrykandpatrick.vico.core.cartesian.CartesianChart
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.CartesianLayerPadding
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape


class Charts {

    fun createColumnChart(
        bottomCartesianValueFormatter: CartesianValueFormatter? = null,
        legend: Legend<CartesianMeasuringContext, CartesianDrawingContext>? = null
    ): CartesianChart {
        val chart = CartesianChart(
            ColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    listOf(
                        LineComponent(
                            fill = Fill(Color.GREEN),
                            thicknessDp = 20f,
                            shape = CorneredShape.rounded(4f, 4f, 0f, 0f),
                        ),
                        LineComponent(
                            fill = Fill(Color.RED),
                            thicknessDp = 20f,
                            shape = CorneredShape.rounded(4f, 4f, 0f, 0f),
                        )
                    )
                ),
            ),
            legend = legend,
            marker = defaultMarker,
            startAxis = VerticalAxis.start(
                line = LineComponent(Fill(Color.WHITE)),
                label = TextComponent(Color.WHITE, padding = Dimensions(6f)),
                titleComponent = TextComponent(Color.WHITE),
                itemPlacer = VerticalAxis.ItemPlacer.step({ 1.00 }),
                guideline = LineComponent(Fill(Color.GRAY), shape = DashedShape())
            ),
            bottomAxis = bottomCartesianValueFormatter?.let { defaultBottomAxis.copy(valueFormatter = it) }
                ?: defaultBottomAxis,
            layerPadding = CartesianLayerPadding(
                scalableStartDp = 6f,
                scalableEndDp = 6f,
            )
        )

        return chart
    }

    fun createLineChart(bottomCartesianValueFormatter: CartesianValueFormatter? = null): CartesianChart {
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
            layerPadding = CartesianLayerPadding(0f, 0f, 0f, 0f),
            marker = defaultMarker,
            startAxis = defaultStartAxis,
            bottomAxis = bottomCartesianValueFormatter?.let { defaultBottomAxis.copy(valueFormatter = it) }
                ?: defaultBottomAxis,
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
        itemPlacer = VerticalAxis.ItemPlacer.step({ 5.00 }),
        guideline = LineComponent(Fill(Color.GRAY), shape = DashedShape())
    )

    private val defaultBottomAxis = HorizontalAxis.bottom(
        line = LineComponent(Fill(Color.WHITE)),
        label = TextComponent(Color.WHITE, padding = Dimensions(6f))
    )
}