package xyz.kandrac.assignment.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * [BasicInputView] - base for all inputs
 * [InputView] - standard input
 * [PasswordInput] - password input
 */
private interface Commentary

// region styles
val colorSurfaceDanger = Color(0xFFDC2828)
val colorContentOnNeutralDanger = Color(0xFFDC2828)
val colorSurfaceXHigh = Color(0xFF8C8C9A)
val colorContentOnNeutralXXHigh = Color(0xFF2C2C31)
val colorContentOnNeutralLow = Color(0xFF7D7D8A)

val fontLabelM = TextStyle(fontWeight = FontWeight.W500, fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.16.sp)
val fontLabelS = TextStyle(fontWeight = FontWeight.W500, fontSize = 14.sp, lineHeight = 17.sp, letterSpacing = 0.16.sp)
val fontBodyM = TextStyle(fontWeight = FontWeight.W400, fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.01.sp)
// endregion

// region base
private data class BasicInputViewColors(
    val valueDefault: Color = colorContentOnNeutralXXHigh,
    val valueError: Color = colorContentOnNeutralDanger,
    val borderDefault: Color = colorSurfaceXHigh,
    val borderError: Color = colorSurfaceDanger
) {
    companion object {
        val default = BasicInputViewColors()
    }
}

@Composable
private fun BasicInputView(
    value: String,
    onValueChange: (String) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    headingInnerPadding: Dp = 8.dp,
    message: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    errorText: @Composable (() -> Unit)? = null,
    error: Boolean = false,
    colors: BasicInputViewColors = BasicInputViewColors.default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val density = LocalDensity.current
        val innerPaddingPx = remember(headingInnerPadding) { with(density) { headingInnerPadding.roundToPx() } }

        Layout(content = { title(); message?.invoke() }) { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val totalWidth = placeables.sumOf { it.width } + innerPaddingPx
            if (totalWidth <= constraints.maxWidth) {
                val height = placeables.maxOf { it.height }
                layout(width = totalWidth, height = height) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(
                            x = placeables.take(index).sumOf { it.width } + innerPaddingPx * index,
                            y = (height - placeable.height) / 2)
                    }
                }
            } else {
                layout(
                    width = placeables.maxOf { it.width },
                    height = placeables.sumOf { it.height } + innerPaddingPx * (placeables.size - 1)) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(
                            x = 0,
                            y = placeables.take(index).sumOf { it.height } + innerPaddingPx * index)
                    }
                }
            }
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(color = if (error) colors.valueError else colors.valueDefault),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = {
                Row(
                    modifier = Modifier
                        .border(1.dp, if (error) colors.borderError else colors.borderDefault, RoundedCornerShape(12.dp))
                        .padding(
                            if (trailingIcon == null) PaddingValues(horizontal = 16.dp, vertical = 13.dp)
                            else PaddingValues(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        it()
                        if (value.isEmpty()) { placeholder?.invoke() }
                    }
                    trailingIcon?.invoke()
                }
            }
        )
        if (error && errorText != null) { errorText() }
    }
}
// endregion

data class InputViewColors(
    val titleDefault: Color = colorContentOnNeutralXXHigh,
    val titleError: Color = colorContentOnNeutralDanger,
    val valueDefault: Color = colorContentOnNeutralXXHigh,
    val valueError: Color = colorContentOnNeutralDanger,
    val messageDefault: Color = colorContentOnNeutralLow,
    val messageError: Color = messageDefault,
    val borderDefault: Color = colorSurfaceXHigh,
    val borderError: Color = colorSurfaceDanger,
    val placeholder: Color = colorContentOnNeutralLow,
    val errorText: Color = colorContentOnNeutralDanger
) {
    companion object {
        val default = InputViewColors()
    }
}

private fun InputViewColors.toBasic() = BasicInputViewColors(valueDefault, valueError, borderDefault, borderError)

@Composable
fun InputView(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    headingInnerPadding: Dp = 8.dp,
    message: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    error: Boolean = false,
    colors: InputViewColors = InputViewColors.default,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = fontBodyM,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicInputView(
        value = value,
        onValueChange = onValueChange,
        title = { Text(title, style = fontLabelM, color = if (error) colors.titleError else colors.titleDefault) },
        modifier = modifier,
        headingInnerPadding = headingInnerPadding,
        message = message?.let { { Text(it, style = fontLabelS, color = if (error) colors.messageError else colors.messageDefault) } },
        trailingIcon = trailingIcon,
        placeholder = placeholder?.let { { Text(it, style = fontBodyM, color = colors.placeholder) } },
        errorText = errorText?.let { { Text(it, style = fontLabelS, fontWeight = FontWeight.W400, color = colors.errorText) } },
        error = error,
        colors = colors.toBasic(),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    )
}

@Composable
fun PasswordInput(
    value: String,
    validate: MutableState<Boolean>,
    onValueChange: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    validator: (String) -> String = { password ->
        val bullets =
            listOfNotNull(
                "• at least 8 characters".takeIf { password.length < 8 },
                "• at least 1 uppercase character".takeIf { password.none { it.isUpperCase() } },
                "• at least 1 digit".takeIf { password.none { it.isDigit() } },
                "• at least 1 special character ? = # / %".takeIf { password.none { it in "?=#/%" } }
            )
        (bullets.joinToString("\n")).takeIf { bullets.isNotEmpty() } ?: ""
    },
    headingInnerPadding: Dp = 8.dp,
    message: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    colors: InputViewColors = InputViewColors.default,
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black)
) {

    val errorText = validator(value)
    val error = validate.value && errorText.isNotEmpty()

    BasicInputView(
        value = value,
        onValueChange = { validate.value = false; onValueChange(it) },
        title = { Text(title, style = fontLabelM, color = if (error) colors.titleError else colors.titleDefault) },
        modifier = modifier,
        headingInnerPadding = headingInnerPadding,
        message = message?.let { { Text(it, style = fontLabelS, color = if (error) colors.messageError else colors.messageDefault) } },
        trailingIcon = trailingIcon,
        placeholder = placeholder?.let { { Text(it, style = fontBodyM, color = colors.placeholder) } },
        errorText = errorText.takeIf { it.isNotEmpty() }?.let { { Text(it, style = fontLabelS, fontWeight = FontWeight.W400, color = colors.errorText) } },
        error = error,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    )
}

@Composable
@Preview(showBackground = true)
fun InputViewPreviewDefault() {
    val value = remember { mutableStateOf("") }
    InputView(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = Modifier.padding(16.dp),
        title = "Input",
        message = "Optional",
        placeholder = "Placeholder"
    )
}

@Composable
@Preview(showBackground = true)
fun InputViewPreviewTrailingIcon() {
    val value = remember { mutableStateOf("Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.") }
    InputView(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = Modifier.padding(16.dp),
        title = "Input",
        message = "Optional",
        placeholder = "Placeholder",
        trailingIcon = { Box(modifier = Modifier.size(32.dp).background(Color.Red)) })
}


@Composable
@Preview(showBackground = true)
fun InputViewPreviewErrorWithText() {
    val value = remember { mutableStateOf("Test value") }
    InputView(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = Modifier.padding(16.dp),
        title = "Input",
        message = "Optional",
        placeholder = "Placeholder",
        error = true,
        errorText = "Server busy"
    )
}

@Composable
@Preview(showBackground = true)
fun PasswordInputPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        val value = remember { mutableStateOf("") }
        val check = remember { mutableStateOf(false) }

        PasswordInput(
            value = value.value,
            validate = check,
            onValueChange = { value.value = it },
            title = "Password",
            message = "Insert password",
            placeholder = "Password"
        )

        Button(onClick = {
            check.value = true
        }) { Text("Check") }
    }
}
