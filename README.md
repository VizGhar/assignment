# Assignment

Find implementation at [input.kt](app/src/main/java/xyz/kandrac/assignment/widget/input.kt) and
commentary/todos below

## Material Design
Material Design is partially handled in Figma, but there are some undefined states.
Can't setup Material Theme based purely on assignment.

- adjust MaterialTheme based on overall design
- apply MaterialTheme instead of directly using colors
- extract colors and text styles to separate files

## Other TODOs

- enabled/disabled colors unspecified and not set
- PasswordInput hardcoded error messages
- currently fill max width is forced - handle measurements = layout phase or using BoxWithConstraints

## NOTEs:

- unspecified behavior Input + Optional text length exceeds line width
  - prefer Row like behavior if possible. Column like behavior otherwise 
- design secretly suggests trailing icon - added
- assignment states - clear "invalid password" communication
  - visual not specified
  - added below input
  - multiline possible therefore start aligned
- styling is fully customizable using base Composable namely BasicInputView
