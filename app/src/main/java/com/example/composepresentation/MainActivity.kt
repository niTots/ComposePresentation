package com.example.composepresentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.animate
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.core.tag
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.selection.Toggleable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.composepresentation.ui.ComposePresentationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePresentationTheme {
                CounterPreview()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    val count = state { 0 }
    val isDefaultColorState = state { true }
    val (selected, onSelected) = state { false }
    val color = animate(target = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.65f) else MaterialTheme.colors.secondary)
    val radius = animate(target = if (selected) 48.dp else 0.dp)

    Surface(
        color = color,
        shape = RoundedCornerShape(radius),
        modifier = Modifier.padding(20.dp)
    ) {
        Toggleable(value = selected, onValueChange = onSelected) {
            ConstraintLayout(
                constraintSet = ConstraintSet {
                    val decreaseButtonConstraint = tag(DECREASE_BUTTON_TAG).apply {
                        left constrainTo parent.left
                        top constrainTo parent.top
                        left.margin = 10.dp
                        centerVertically()
                    }

                    val counterTextConstraint = tag(COUNTER_TEXT_TAG).apply {
                        left constrainTo decreaseButtonConstraint.right
                        top constrainTo parent.top

                        left.margin = 16.dp

                        centerVertically()
                    }

                    val increaseButtonConstraint = tag(INCREASE_BUTTON_TAG).apply {
                        left constrainTo counterTextConstraint.right
                        top constrainTo parent.top

                        left.margin = 16.dp

                        centerVertically()
                    }

                    val colorSwitcherConstraint = tag(COLOR_SWITCHER_TAG).apply {
                        left constrainTo increaseButtonConstraint.right
                        right constrainTo parent.right
                        top constrainTo parent.top

                        left.margin = 10.dp
                    }
                },
                modifier = Modifier.padding(10.dp)
            ) {
                CounterButton(
                    count = count.value,
                    modifier = Modifier.tag(DECREASE_BUTTON_TAG),
                    backGroundColor = if (isDefaultColorState.value) Color.Blue else Color.Red,
                    text = "-", change = { countValue ->
                        count.value = if (countValue == 0) countValue else countValue - 1
                    })

                Text(text = "${count.value}", modifier = Modifier.tag(COUNTER_TEXT_TAG))

                CounterButton(
                    count = count.value,
                    modifier = Modifier.tag(INCREASE_BUTTON_TAG),
                    backGroundColor = if (isDefaultColorState.value) Color.Blue else Color.Red,
                    text = "+", change = { countValue ->
                        count.value = countValue + 1
                    })

                ColorSwitcher(
                    isDefaultColor = isDefaultColorState.value,
                    modifier = Modifier.tag(COLOR_SWITCHER_TAG)
                ) { isDefault ->
                    isDefaultColorState.value = isDefault
                }
            }
        }
    }
}


@Composable
fun CounterButton(
    count: Int,
    text: String,
    change: (Int) -> Unit,
    backGroundColor: Color,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = Modifier.size(40.dp, 40.dp) + modifier,
        onClick = { change(count) },
        padding = InnerPadding(0.dp),
        backgroundColor = backGroundColor,
        contentColor = Color.White

    ) {
        Text(text = text)
    }
}

@Composable
fun ColorSwitcher(
    isDefaultColor: Boolean,
    modifier: Modifier = Modifier,
    onChangeColor: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.preferredSize(12.dp, 12.dp) + Modifier.clickable(
            onClick = { onChangeColor(!isDefaultColor) }
        ) + modifier,
        shape = RoundedCornerShape(8.dp),
        border = Border(0.5.dp, if (isDefaultColor) Color.Blue else Color.Red),
        backgroundColor = if (isDefaultColor) Color.Red else Color.Blue
    )
}

const val DECREASE_BUTTON_TAG = "DECREASE_BUTTON_TAG"
const val INCREASE_BUTTON_TAG = "INCREASE_BUTTON_TAG"
const val COUNTER_TEXT_TAG = "COUNTER_TEXT_TAG"
const val COLOR_SWITCHER_TAG = "COLOR_SWITCHER_TAG"
